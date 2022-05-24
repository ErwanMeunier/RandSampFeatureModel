/**
 * 
 */
package randsampFM.featureModel;

import de.neominik.uvl.ast.*;

import randsampFM.types.ConfSet;
import randsampFM.types.Feature;
import randsampFM.types.Conf;

import java.util.List;
import java.util.Arrays;
import java.util.Collections;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.math.BigInteger;
import java.util.Random;

public abstract class FeatureDiagram {
	
	protected final static int precision = 100;
	
	protected Feature label;
	protected BigInteger nbConfigurations;
	protected Random generator;
	
	public FeatureDiagram(String label, final Random generator) {
		this.label = new Feature(label);
		this.nbConfigurations = null;
		this.generator = generator;
	}
	
	public static FeatureDiagram parseFeatureDiagram(final de.neominik.uvl.ast.Feature feature, final Random generator) {
		List<Group> groups = Arrays.asList(feature.getGroups()); // retrieves all the groups under the feature
		
		/* 
		 * 0 -> OR
		 * 1 -> XOR
		 * 2 -> MAND/OPT
		 * 3 -> Cardinality
		 * */
		
		ArrayList<Integer> nbTypes = new ArrayList<Integer>(Arrays.asList(new Integer[4])); // counts how many times a type has been encountered below
		Collections.fill(nbTypes, 0); // nbTypes is now full of zero
		
		for(Group group : groups) { 
			switch(group.getType()) { // got naming conventions from neominik/uvl-parser/resources/uvl.bnf 
			case "or":
				nbTypes.set(0, nbTypes.get(0)+1);
				break;
			case "alternative":
				nbTypes.set(1, nbTypes.get(1)+1);
				break;
			case "mandatory":
				nbTypes.set(2, nbTypes.get(2)+1); // same box than mandatory
				break;
			case "optional":
				nbTypes.set(2, nbTypes.get(2)+1); // same box than optional
				break;
			case "cardinality":
				nbTypes.set(3, nbTypes.get(3)+1);
				break;
			default:
				System.out.println(group.getType());
				throw new  UnsupportedOperationException("Type not handled.");
			}
		}
		
		int typeIndex = -1;
		
		long filteredTypes = nbTypes.stream().filter(y -> y > 0).count(); // how many different types ?
		
		if(filteredTypes > 1) { // Checks type consistency
			throw new  UnsupportedOperationException("Types are not consistent -> Feature Model building aborted.");
		}
		else {
			for(int i=0; i<4; i++) {
				if(nbTypes.get(i)>0) {
					typeIndex = i;
				}
			}
		}
		
		FeatureDiagram result;
		
		List<de.neominik.uvl.ast.Feature> children;
		
		Group currentGroup;
		
		switch(typeIndex){
		
		case -1: // LEAF
			result = new FMLeaf(feature.getName());
			break;
			
		case 0: //OR
			currentGroup = groups.stream().filter(g -> g.getType().equals("or")).findFirst().get();
			children = Arrays.asList(currentGroup.getChildren());
			result = new FMOr(feature.getName(),children, generator);
			break;
			
		case 1://XOR
			currentGroup = groups.stream().filter(g -> g.getType().equals("alternative")).findFirst().get();
			children = Arrays.asList(currentGroup.getChildren());
			result = new FMXor(feature.getName(),children, generator);
			break;
			
		case 2://MANDOPT
			// One of mandGroup or optGroup might be empty
			
			boolean bothEmpty = true;
			
			List<de.neominik.uvl.ast.Feature> rawMandChilds; //= List.of();
			List<de.neominik.uvl.ast.Feature> rawOptChilds; //= List.of();
			
			try {
				Group mandGroup = groups.stream().filter(g -> g.getType().equals("mandatory")).findFirst().get();
				rawMandChilds = Arrays.asList(mandGroup.getChildren());
				bothEmpty = false;
			}catch(NoSuchElementException e) { // no element in mandGroup match the predicate
				rawMandChilds = List.of();
			}
			
			try {
				Group optGroup = groups.stream().filter(g -> g.getType().equals("optional")).findFirst().get();
				rawOptChilds = Arrays.asList(optGroup.getChildren());
				bothEmpty = false;
			}catch(NoSuchElementException e) { // no element in optGroup match the predicate
				rawOptChilds = List.of();
			}
			
			if(bothEmpty) {
				throw new NoSuchElementException("Both Mandatory and optional groups are empty");
			} else {
				result = new FMMandOpt(feature.getName(), rawMandChilds, rawOptChilds, generator);
			}
			
			break;
			
		case 3://CARD - WRONG 
			currentGroup = groups.stream().filter(g -> g.getType().equals("cardinality")).toList().get(0);
			
			int lb = currentGroup.getLower();
			int ub = currentGroup.getUpper();
			
			children = Arrays.asList(currentGroup.getChildren());
			result = new FMCard(feature.getName(),children,lb,ub,generator);
			break;
			
		default:
			throw new  UnsupportedOperationException("FilteredTypes not consistent with typeIndex"); // cannot happen 
		}
		
		return result; // return both type and feature model
	}	
	 
	@Override
	public String toString(){
		if(this.label == null) {
			return "null";
		} else {
			return label.toString();
		}
	}
	
	
	/**
	 * nbConfigurations is set to count() the first time count() is called
	 * @return
	 */
	
	public abstract BigInteger count();
	
	public abstract ConfSet enumerate();
		
	public abstract Conf sample(); // TODO : Factorizing sub-class source code

	//TODO public enumerate
	public static List<ConfSet> enumerate(List<FeatureDiagram> fmList, boolean isOptional) {
		LinkedList<ConfSet> result = new LinkedList<ConfSet>();
		
		if(isOptional) {
			for(FeatureDiagram fm : fmList) {
				result.addLast(fm.enumerate());
			}
		}else {
			for(FeatureDiagram fm : fmList) {
				result.addLast(fm.enumerate().union(new ConfSet()));
			}
		}

		return List.copyOf(result);
	}
	
}

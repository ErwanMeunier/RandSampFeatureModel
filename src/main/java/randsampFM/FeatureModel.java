/**
 * 
 */
package randsampFM;

//import java.util.ArrayList;
import de.neominik.uvl.ast.*;

import java.util.List;
import java.util.Arrays;
import java.util.Collections;
import java.util.ArrayList;
import java.util.stream.Collectors;
//import java.util.stream.Stream;
import java.util.NoSuchElementException;

/**
 * @author stagiaire-tasc
 *
 */
public abstract class FeatureModel {
	
	protected Feature label;
	//protected boolean leaf;
	//protected List<FeatureModel> children;
	
	//Global variables
	protected final static String MAND = "MAND";
	protected final static String OPT = "OPT";
	
	
	/*private static final String OR = "or";
	private static final String XOR = "xor";
	private static final String MANDOPT = "mandopt";
	private static final String CARD = "card";
	
	private static final String LEAF = "leaf";*/
	//private static final String NONE = "NONE";
		
	/*public FeatureModel(UVLModel uvlModel){ // Uses UVLParser to get processed-data from raw-data (uvl model)
		this.label = new Feature(uvlModel.toString());
		//TODO
	}*/
	
	/*
	 * public static List<de.neominik.uvl.ast.Feature> getRootFeature(UVLModel uvlmodel){
		return Arrays.asList(uvlmodel.getRootFeatures());
	}
	 * */
	
	/*public FeatureModel(final de.neominik.uvl.ast.UVLModel uvlModel) {
		de.neominik.uvl.ast.Feature rootFeature;
		rootFeature =  Arrays.asList(uvlModel.getRootFeatures()).get(0); // TODO : Exception handling ?
		this.label = new
	}*/
	
	/*public FeatureModel(final de.neominik.uvl.ast.Feature feature) {
		
		this.label = new Feature(feature.getName()); // retrieves the name attached to the current feature
		System.out.println(label.getName());
		this.children = parseFeatureModel(feature); // missing try/catch ?
		System.out.println("Enfant de " + label.getName() + " : " + children.toString());
		this.optional = false;
	}*/
	
	public FeatureModel(String label) {
		this.label = new Feature(label);
	}
	
	/*public static List<FeatureModel> parseFeatureModel(UVLModel uvlModel) {
		return parseFeatureModel(uvlModel.getRootFeatures());
	}*/
	
	
	public static FeatureModel parseFeatureModel(final de.neominik.uvl.ast.Feature feature) {
		List<Group> groups = Arrays.asList(feature.getGroups()); // retrieves all the groups under the feature
		/* 
		 * 0 -> OR
		 * 1 -> XOR
		 * 2 -> MAND/OPT
		 * 3 -> Cardinality
		 * */
		
		ArrayList<Integer> nbTypes = new ArrayList<Integer>(Arrays.asList(new Integer[4])); // counts how many times a type has been encountered below
		Collections.fill(nbTypes, 0); // nbTypes is now full of zero
		
		System.out.println("testFM");
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
				System.out.println("optional");
				break;
			case "cardinalities":
				nbTypes.set(3, nbTypes.get(3)+1);
				break;
			default:
				throw new  UnsupportedOperationException("Type not handled.");
			}
		}
		
		//System.out.println(nbTypes.stream().map(x -> x.toString()).reduce((a,b)->a+b));//test
		
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
		
		FeatureModel result;
		
		List<de.neominik.uvl.ast.Feature> children;
		
		Group currentGroup;
		
		switch(typeIndex){
		
		case -1: // LEAF
			result = new FMLeaf(feature.getName());
			break;
			
		case 0: //OR
			currentGroup = groups.stream().filter(g -> g.getType().equals("or")).findFirst().get();
			children = Arrays.asList(currentGroup.getChildren());
			result = new FMOr(feature.getName(),children);
			break;
			
		case 1://XOR
			currentGroup = groups.stream().filter(g -> g.getType().equals("alternative")).findFirst().get();
			children = Arrays.asList(currentGroup.getChildren());
			result = new FMXor(feature.getName(),children);
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
				result = new FMMandOpt(feature.getName(), rawMandChilds, rawOptChilds);
			}
			
			break;
			
		case 3://CARD - WRONG
			currentGroup = groups.stream().filter(g -> g.getType().equals("cardinalities")).collect(Collectors.toList()).get(0);
			
			int lb = currentGroup.getLower();
			int ub = currentGroup.getUpper();
			
			children = Arrays.asList(currentGroup.getChildren());
			result = new FMCard(feature.getName(),children,lb,ub);
			break;
			
		default:
			throw new  UnsupportedOperationException("FilteredTypes not consistent with typeIndex"); // cannot happen 
		}
		
		System.out.println(result.toString());
		return result; // return both type and feature model
	}	
	 
	@Override
	public String toString(){
		if(this.label == null) {
			return "null";
		} else {
			return label.getName();
		}
	}
	
	public abstract long count();
	
	public abstract ConfSet enumerate();
	
	
	//TODO public enumerate
	
}

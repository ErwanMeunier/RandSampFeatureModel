/**
 * 
 */
package randsampFM;

//import java.util.ArrayList;
import de.neominik.uvl.ast.*;
//import de.neominik.uvl.UVLParser;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * @author stagiaire-tasc
 *
 */
public abstract class FeatureModel {
	
	protected Feature label;
	protected boolean optional; // useful in FMMandOpt -> false by default. On
	protected List<FeatureModel> children;
	
	protected final static String MAND = "MAND";
	protected final static String OPT = "OPT";
	
	
	/*private static final String OR = "or";
	private static final String XOR = "xor";
	private static final String MANDOPT = "mandopt";
	private static final String CARD = "card";
	
	private static final String LEAF = "leaf";*/
	//private static final String NONE = "NONE";
		
	public FeatureModel(UVLModel uvlModel){ // Uses UVLParser to get processed-data from raw-data (uvl model)
		this.label = new Feature(uvlModel.toString());
		//TODO
	}
	
	public FeatureModel(final de.neominik.uvl.ast.Feature feature) {
		this.label = new Feature(feature.getName()); // retrieves the name attached to the current feature
		this.children = parseFeatureModel(feature); // missing try/catch ?
		this.optional = false;
	}
	
	/*public static List<FeatureModel> parseFeatureModel(UVLModel uvlModel) {
		return parseFeatureModel(uvlModel.getRootFeatures());
	}*/
	
	private static List<FeatureModel> parseFeatureModel(final de.neominik.uvl.ast.Feature feature) {
		List<Group> groups = Arrays.asList(feature.getGroups()); // retrieves all the groups under the feature
		
		/* 
		 * 0 -> OR
		 * 1 -> XOR
		 * 2 -> MAND/OPT
		 * 3 -> Cardinality
		 * */
		
		ArrayList<Integer> nbTypes = new ArrayList<Integer>(4); // counts how many times a type has been encountered below
		
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
			case "cardinalities":
				nbTypes.set(3, nbTypes.get(3)+1);
				break;
			default:
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
		
		List<FeatureModel> newFMs;
		Group currentGroup;
		
		switch(typeIndex){
		
		case -1: // LEAF
			newFMs = new ArrayList<FeatureModel>(new FMLeaf());
			break;
			
		case 0: //OR
			currentGroup = groups.stream().filter(g -> g.getType().equals("or")).findFirst().get();
			newFMs = Arrays.asList(currentGroup.getChildren()).stream().map(f -> new FMOr(f)).collect(Collectors.toList());
			break;
			
		case 1://XOR
			currentGroup = groups.stream().filter(g -> g.getType().equals("alternative")).findFirst().get();
			newFMs = Arrays.asList(currentGroup.getChildren()).stream().map(f -> new FMXor(f)).collect(Collectors.toList());
			break;
			
		case 2://MANDOPT
			Group mandGroup = groups.stream().filter(g -> g.getType().equals("mandatory")).findFirst().get();
			Group optGroup = groups.stream().filter(g -> g.getType().equals("optional")).findFirst().get();
			var newFMsMand = Arrays.asList(mandGroup.getChildren()).stream().map(f -> new FMMandOpt(f,false));
			var newFMsOpt = Arrays.asList(optGroup.getChildren()).stream().map(f -> new FMMandOpt(f,true));
			
			break;
			
		case 3://CARD
			currentGroup = groups.stream().filter(g -> g.getType().equals("cardinalities")).collect(Collectors.toList()).get(0);
			
			int lb = currentGroup.getLower();
			int ub = currentGroup.getUpper();
			
			newFMs = Arrays.asList(currentGroup.getChildren()).stream().map(f -> new FMXor(f,lb,ub)).collect(Collectors.toList());;
			break;
			
		default:
			throw new  UnsupportedOperationException("FilteredTypes not consistent with typeIndex"); // cannot happen 
		}
		
		return newFMs; // return both type and feature model
	}	
	 
	protected boolean isOptional() {
		return optional;
	}
	
	public abstract long count();
	
	//public abstract ConfSet enumerate();
	
	
	//TODO public enumerate
	
}

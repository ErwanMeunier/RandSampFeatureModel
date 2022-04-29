/**
 * 
 */
package randsampFM;

//import java.util.ArrayList;
import java.util.List;
import de.neominik.uvl.ast.*;
//import de.neominik.uvl.UVLParser;

/**
 * @author stagiaire-tasc
 *
 */
public abstract class FeatureModel {
	
	protected Feature label;
		
	public FeatureModel(UVLModel uvlModel){ // Uses UVLParser to get processed-data from raw-data (uvl model)
		this.label = new Feature(uvlModel.toString());
	}
	
	/*public static List<FeatureModel> parseFeatureModel(UVLModel uvlModel) {
		return parseFeatureModel(uvlModel.getRootFeatures());
	}*/
	
	public static List<FeatureModel> parseFeatureModel(de.neominik.uvl.ast.Feature feature) {
		
	}
	
	private String checkType(final Group[] groupes) { // checks groups consistency 
		
	}
	
	 /*private void postTreeConstraints(final Model model, final Feature rootFeature, final Map<String,BoolVar> map) {
		    BoolVar mainVar = map.get(rootFeature.getName());
		    BoolVar[] childrenVars;
		    for (Group group : rootFeature.getGroups()) {
		      switch (group.getType()) {
		      case "optional":
		        for (Feature feature : group.getChildren()) {
		          map.get(feature.getName()).le(mainVar).post(); // f <= mainVar
		          postTreeConstraints(model, feature, map);
		        }
		        break;
		      case "mandatory":
		        for (Feature feature : group.getChildren()) {
		          map.get(feature.getName()).eq(mainVar).post(); // f == mainVar
		          postTreeConstraints(model, feature, map);
		        }
		        break;
		      case "alternative":
		        childrenVars = Arrays.stream(group.getChildren()).map(feature -> {postTreeConstraints(model, feature, map);return map.get(feature.getName());}).toArray(BoolVar[]::new);
		        model.sum(childrenVars, "=", mainVar).post();
		        break;
		      case "cardinalities":
		        IntVar lb = model.intVar(new int[]{0, group.getLower()});
		        lb.eq(mainVar.mul(group.getLower())).post();
		        IntVar ub = model.intVar(new int[]{0, group.getUpper()});
		        ub.eq(mainVar.mul(group.getUpper())).post();
		        childrenVars = Arrays.stream(group.getChildren()).map(feature -> {postTreeConstraints(model, feature, map);return map.get(feature.getName());}).toArray(BoolVar[]::new);
		        model.sum(childrenVars, "<=", ub).post();
		        model.sum(childrenVars, ">=", lb).post();
		        break;
		      default:
		        throw new IllegalStateException("The group type " + group.getType() + " is not allowed");
		      }
		    }
		  }*/
	 
	public abstract long count();
	
	
	//TODO public enumerate
	
}

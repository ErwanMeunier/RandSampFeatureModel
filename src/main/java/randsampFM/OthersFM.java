/**
 * 
 */
package randsampFM;

import java.util.ArrayList;

import de.neominik.uvl.ast.UVLModel;
import de.neominik.uvl.ast.*;

/**
 * @author erwan
 *
 */
public abstract sealed class OthersFM extends FeatureModel permits FmXor, FmOR{

	/**
	 * @param uvlModel
	 */
	protected ArrayList<FeatureModel> children; // Will be just iterated over
	
	public OthersFM(de.neominik.uvl.ast.Feature feature) {

	    for (Group group : feature.getGroups()) {
	    	
	    	for (de.neominik.uvl.ast.Feature childFeature : group.getChildren()) {
	    		
	    	}
	    }
	      
		// TODO Auto-generated constructor stub
	}
	
	// Does not realize count() -> Done in inherited class
	
}

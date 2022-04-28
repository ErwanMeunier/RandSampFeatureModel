/**
 * 
 */
package randsampFM;

import java.util.ArrayList;
import de.neominik.uvl.ast.*;
//import de.neominik.uvl.UVLParser;

/**
 * @author stagiaire-tasc
 *
 */
public abstract class FeatureModel {
	
	protected Feature label;
	protected ArrayList<FeatureModel> children; // Will be just iterated over
		
	public FeatureModel(UVLModel uvlModel){ // Uses UVLParser to get processed-data from raw-data (uvl model)
		
	}
	
	//public FeatureModel() there are several constructors...
	
	public abstract int count();
	
	//TODO public enumerate
	
}

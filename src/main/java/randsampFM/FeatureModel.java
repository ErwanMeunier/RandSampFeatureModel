/**
 * 
 */
package randsampFM;

//import java.util.ArrayList;
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
	
	public FeatureModel() {
		// We call matching constructors
	}
	
	public abstract long count();
	
	
	//TODO public enumerate
	
}

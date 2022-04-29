/**
 * 
 */
package randsampFM;

import java.util.ArrayList;

import de.neominik.uvl.ast.UVLModel;

/**
 * @author erwan
 *
 */
public abstract sealed class OthersFM extends FeatureModel permits FmXor, FmOR{

	/**
	 * @param uvlModel
	 */
	protected ArrayList<FeatureModel> children; // Will be just iterated over
	
	public OthersFM(UVLModel uvlModel) {
		super(uvlModel);
		// TODO Auto-generated constructor stub
	}
	
	// Does not realize count() -> Done in inherited class
	
}

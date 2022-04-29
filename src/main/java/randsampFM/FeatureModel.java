/**
 * 
 */
package randsampFM;

//import java.util.ArrayList;
import de.neominik.uvl.ast.*;
import java.util.Set;
//import de.neominik.uvl.UVLParser;

/**
 * @author stagiaire-tasc
 *
 */
public abstract class FeatureModel {
	
	/**
		 * @author erwan
		 *
		 */

	protected Feature label;
		
	public FeatureModel(UVLModel uvlModel){ // Uses UVLParser to get processed-data from raw-data (uvl model)
		//TODO
	}
	
	public FeatureModel() {
		// We call matching constructors
	}
	
	public abstract int count();
	
	@Override
	public int hashCode() {
		return label.getName().hashCode(); // We assume that each Feature is unique 
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true; // Same references
		
		if(obj == null || obj.getClass()!=this.getClass()) return false; // Different types
		
		FeatureModel tmpFM = (FeatureModel) obj; // type casting (ClassCastException cannot happen because types have already been checked above)
		
		return (tmpFM.hashCode()==this.hashCode());
	}
	
	//TODO public enumerate
	
}

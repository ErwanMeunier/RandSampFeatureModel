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
	
	protected Feature label;
	protected Set<FeatureModel> children; // Will be just iterated over
		
	public FeatureModel(UVLModel uvlModel){ // Uses UVLParser to get processed-data from raw-data (uvl model)
		
	}
	
	//public FeatureModel() there are several constructors...
	
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

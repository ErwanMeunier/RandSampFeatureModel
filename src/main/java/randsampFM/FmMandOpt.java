/**
 * 
 */
package randsampFM;

import de.neominik.uvl.ast.UVLModel;
import java.util.Set;
import java.io.IOException;

public class FmMandOpt extends FeatureModel {

	Set<FeatureModel> mand;
	Set<FeatureModel> opt;
	
	public FmMandOpt(UVLModel uvlModel) {
		
		// TODO Auto-generated constructor stub
	}
	
	public FmMandOpt() {} // input : rootfeature

	public int count() {
		int countMand = children.stream().mapToInt(x->x.count()).reduce() + 1;
		int countOpt = ;
		return countOpt*coutMand;
	}
	
}

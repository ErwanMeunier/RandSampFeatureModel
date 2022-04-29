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
		super()
		// TODO Auto-generated constructor stub
	}
	
	public FmMandOpt() {
		//TODO
	} // input : rootfeature

	public int count() {
		int countMand = mand.stream().mapToInt(x->x.count()).reduce(1, (a,b)-> a*b);
		int countOpt = opt.stream().mapToInt(x->x.count()+1).reduce(1, (a,b)-> a*b);
		return countOpt*countMand;
	}
	
}

/**
 * 
 */
package randsampFM;

import de.neominik.uvl.ast.UVLModel;

public class FmMandOpt extends FeatureModel {

	public FmMandOpt(UVLModel uvlModel) {
		super(uvlModel);
		// TODO Auto-generated constructor stub
	}

	public int count() {
		Set<FeatureModel> mand = children.stream().filter(x -> ); // TODO would it be better to discriminate mandatory and optional during the Feature Diagram building ?
	}
	
	public boolean isMand(FeatureModel fm) {} // TODO true if mandatory
	
	public boolean isOpt(FeatureModel fm) {} // TODO true if optional
}

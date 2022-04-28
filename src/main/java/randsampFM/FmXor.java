package randsampFM;

import de.neominik.uvl.ast.UVLModel;

public class FmXor extends FeatureModel {

	public FmXor(UVLModel uvlModel) {
		super(uvlModel);
		// TODO Auto-generated constructor stub
	}
	
	public int count() { // not tail recursive regarding the "+1"...
		return children.stream().mapToInt(x->x.count()).sum() + 1;
	}
}

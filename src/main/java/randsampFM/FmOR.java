package randsampFM;

import de.neominik.uvl.ast.UVLModel;

public class FmOR extends FeatureModel {

	public FmOR(UVLModel uvlModel) {
		super(uvlModel);
		// TODO Auto-generated constructor stub
	}

	public int count() {
		return children.stream().map(x->x.count()).reduce(0, (a,b)-> a+b);
	}
}

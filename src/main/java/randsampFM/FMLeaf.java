package randsampFM;

import de.neominik.uvl.ast.UVLModel;

public final class FMLeaf extends FeatureModel {

	public FMLeaf(UVLModel uvlModel) {
		super(uvlModel);
		// TODO Auto-generated constructor stub
	}

	public long count() {
		return 1;
	}
	
	public ConfSet enumerate() {
		return ConfSet.singletonCS(this.label);
	}
}

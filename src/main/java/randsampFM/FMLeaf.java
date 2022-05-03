package randsampFM;

public final class FMLeaf extends FeatureModel {
	
	public FMLeaf(String label) {
		super(label);
	}

	public long count() {
		return 1;
	}
	
	public ConfSet enumerate() {
		return ConfSet.singletonCS(this.label); // empty confSet
	}
}

package randsampFM.featureModel;

import randsampFM.types.ConfSet;

import java.math.BigInteger;

public final class FMLeaf extends FeatureModel {
	
	public FMLeaf(String label) {
		super(label);
	}

	public BigInteger count() {
		return BigInteger.ONE;
	}
	
	public ConfSet enumerate() {
		return ConfSet.singletonCS(this.label); // empty confSet
	}
}

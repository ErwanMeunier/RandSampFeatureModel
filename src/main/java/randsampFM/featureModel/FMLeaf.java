package randsampFM.featureModel;

import randsampFM.types.ConfSet;
import randsampFM.types.Conf;

import java.math.BigInteger;
import java.util.Set;

public final class FMLeaf extends FeatureModel {
	
	public FMLeaf(String label) {
		super(label);
	}

	public BigInteger count() {
		if(this.nbConfigurations == null) {
			this.nbConfigurations = BigInteger.ONE;
		} 
		return nbConfigurations;
	}
	
	public ConfSet enumerate() {
		return ConfSet.singletonCS(this.label); // empty confSet
	}
	
	public Conf sample() {
		return new Conf(Set.of(label));
	}
}

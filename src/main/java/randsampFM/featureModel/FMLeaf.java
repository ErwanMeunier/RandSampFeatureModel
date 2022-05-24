package randsampFM.featureModel;

import randsampFM.types.ConfSet;
import randsampFM.types.Conf;

import java.math.BigInteger;
import java.util.Set;

public final class FMLeaf extends FeatureDiagram {
	
	public FMLeaf(String label) {
		super(label, null); // no need to have a random number generator here
	}

	public BigInteger count() {
		if(this.nbConfigurations == null) {
			this.nbConfigurations = BigInteger.ONE;
		} 
		return nbConfigurations;
	}
	
	public ConfSet enumerate() {
		return ConfSet.singletonCS(this.label);
	}
	
	public Conf sample() {
		return new Conf(Set.of(label));
	}
}

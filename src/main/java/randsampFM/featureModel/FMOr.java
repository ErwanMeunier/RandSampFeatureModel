package randsampFM.featureModel;

import java.util.stream.Collectors;

import randsampFM.types.Conf;
import randsampFM.types.ConfSet;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.List;
import java.util.Set;
import java.util.Random;

public class FMOr extends FeatureDiagram {

	List<FeatureDiagram> children;
	
	public FMOr(String label, List<de.neominik.uvl.ast.Feature> rawChildren, final Random generator) {
		super(label, generator);
		this.children = rawChildren.stream().map(x -> parseFeatureDiagram(x,generator)).collect(Collectors.toList());
	}

	public BigInteger count() {
		if(this.nbConfigurations == null) {
			nbConfigurations = children.stream().map(x->x.count().add(BigInteger.ONE)).reduce(BigInteger.ONE, (a,b)-> a.multiply(b)).subtract(BigInteger.ONE);
		}
		return nbConfigurations;
	}
	
	public ConfSet enumerate() {
		Conf rootConf = new Conf(Set.of(this.label)); 
		ConfSet root = new ConfSet(Set.of(rootConf));
		ConfSet result = root.expansion(children.stream().map(x -> x.enumerate().union(ConfSet.emptyCS())).reduce(ConfSet.emptyCS(),(a,b) -> a.expansion(b)));
		return result.without(rootConf);
		// TODO : Exceptions handling
	}
	
	public Conf sample() {
		double draw; 
		double bound;
		Conf result = new Conf();

		while(result.isEmpty()) { // TODO : Very unlikely but the loop might not end...
			for(FeatureDiagram fm : children) {
				bound = (BigDecimal.ONE).divide(new BigDecimal(fm.count().add(BigInteger.ONE)),precision,RoundingMode.HALF_EVEN).doubleValue();
				draw = generator.nextDouble(); // between 0.0 and 1.0

				if(bound <= draw) { // Complemented probability
					result = result.union(fm.sample());
				}
			}
		}

		return result.union(new Conf(Set.of(this.label)));
	}
}

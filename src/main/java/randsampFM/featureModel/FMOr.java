package randsampFM.featureModel;

import java.util.stream.Collectors;

import randsampFM.types.Conf;
import randsampFM.types.ConfSet;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.List;
import java.util.Set;

public class FMOr extends FeatureModel {

	List<FeatureModel> children;
	
	public FMOr(String label, List<de.neominik.uvl.ast.Feature> rawChildren) {
		super(label);
		this.children = rawChildren.stream().map(x -> parseFeatureModel(x)).collect(Collectors.toList());
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
			BigDecimal draw; 
			BigDecimal bound;
			BigDecimal nbc = new BigDecimal(this.count()); // converts a BigInt into a BigDec
			Conf result = new Conf(Set.of(this.label));
			
			while(result.isEmpty()) { // TODO : Very unlikely but the loop might not end...
				for(FeatureModel fm : children) {
					bound = (new BigDecimal(fm.count())).divide(nbc,10,RoundingMode.HALF_EVEN);
					draw = BigDecimal.valueOf(Math.random());
					int comparison = draw.compareTo(bound);
					if(comparison == -1 || comparison == -0) {
						result = result.union(fm.sample());
					}
				}
			}
			
			return result;
		}
}

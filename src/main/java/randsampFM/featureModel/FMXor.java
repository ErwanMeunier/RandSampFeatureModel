package randsampFM.featureModel;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.math.BigInteger;
import java.math.BigDecimal;
import java.lang.Math;
import java.math.RoundingMode;

import randsampFM.types.Conf;
import randsampFM.types.ConfSet;

public final class FMXor extends FeatureModel{

	List<FeatureModel> children;
	
	public FMXor(String label, List<de.neominik.uvl.ast.Feature> rawChildren) {
		super(label);
		this.children = rawChildren.stream().map(x -> parseFeatureModel(x)).collect(Collectors.toList());
		// TODO Auto-generated constructor stub
	}
	
	public BigInteger count() {
		if(this.nbConfigurations == null) {
			nbConfigurations = children.stream().map(x->x.count()).reduce(BigInteger.ZERO, (a,b)-> a.add(b));
		}
		return nbConfigurations;
	}
	
	public ConfSet enumerate() {
		ConfSet root = ConfSet.singletonCS(this.label);
		//root.expansion(children.stream().map(x -> x.enumerate()).reduce(ConfSet.emptyCS(),(a,b) -> a.union(b)));
		return root.expansion(children.stream().map(x -> x.enumerate()).reduce(new ConfSet(),(a,b) -> a.union(b)));
		// TODO : Exceptions handling
	}
	
	/**
	 * @param draw 
	 * @param bound
	 * @param nbc
	 */
	public Conf sample() {
		BigDecimal draw; 
		BigDecimal bound;
		BigDecimal nbc = new BigDecimal(this.count()); // converts a BigInt into a BigDec
		Conf result = new Conf(Set.of(this.label));
		
		/* a, b BigDecimal
		 * a.divide(b, scale, rounding method)
		 * */
		for(FeatureModel fm : children) {
			bound = (new BigDecimal(fm.count())).divide(nbc,10,RoundingMode.HALF_EVEN);
			draw = BigDecimal.valueOf(Math.random());
			int comparison = draw.compareTo(bound);
			if(comparison == -1 || comparison == -0) {
				result = result.union(fm.sample());
			}
		}
		return result;
	}
}

package randsampFM.featureModel;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.math.BigInteger;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

import randsampFM.types.Conf;
import randsampFM.types.ConfSet;

public final class FMXor extends FeatureDiagram{

	List<FeatureDiagram> children;
	
	public FMXor(String label, List<de.neominik.uvl.ast.Feature> rawChildren, final Random generator) {
		super(label, generator);
		this.children = rawChildren.stream().map(x -> parseFeatureDiagram(x,generator)).collect(Collectors.toList());
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
		Conf result = new Conf(Set.of(this.label));
		
		/* a, b BigDecimal
		 * a.divide(b, scale, rounding method)
		 * */
		result = result.union(this.choose().sample());
		
		return result;
	}
	
	private FeatureDiagram choose(){
		double r = generator.nextDouble();
		BigDecimal nbConf = new BigDecimal(this.count());
		

		Object[] childs = children.stream().map(x -> new BigDecimal(x.count())).toArray();
		
		double p;
		int i = 0;
		BigDecimal child;
		
		while(r >= 0) {
			child = (BigDecimal) childs[i];
			p = child.divide(nbConf,precision,RoundingMode.HALF_EVEN).doubleValue();
			r = r - p;
			i++;
		}
		
		return children.get(i-1);
	}
}

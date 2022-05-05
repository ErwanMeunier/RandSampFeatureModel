package randsampFM.featureModel;

import java.util.stream.Collectors;

import randsampFM.types.Conf;
import randsampFM.types.ConfSet;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;

public class FMOr extends FeatureModel {

	List<FeatureModel> children;
	
	public FMOr(String label, List<de.neominik.uvl.ast.Feature> rawChildren) {
		super(label);
		this.children = rawChildren.stream().map(x -> parseFeatureModel(x)).collect(Collectors.toList());
	}

	public BigInteger count() {
		return children.stream().map(x->x.count().add(BigInteger.ONE)).reduce(BigInteger.ONE, (a,b)-> a.multiply(b)).subtract(BigInteger.ONE);
	}
	
	public ConfSet enumerate() {
		Conf rootConf = new Conf(Set.of(this.label)); 
		ConfSet root = new ConfSet(Set.of(rootConf));
		ConfSet result = root.expansion(children.stream().map(x -> x.enumerate().union(ConfSet.emptyCS())).reduce(ConfSet.emptyCS(),(a,b) -> a.expansion(b)));
		return result.without(rootConf);
		// TODO : Exceptions handling
	}
}

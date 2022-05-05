package randsampFM.featureModel;

import java.util.List;
import java.util.stream.Collectors;
import java.math.BigInteger;

import randsampFM.types.ConfSet;

public final class FMXor extends FeatureModel{

	List<FeatureModel> children;
	
	public FMXor(String label, List<de.neominik.uvl.ast.Feature> rawChildren) {
		super(label);
		this.children = rawChildren.stream().map(x -> parseFeatureModel(x)).collect(Collectors.toList());
		// TODO Auto-generated constructor stub
	}
	
	public BigInteger count() {
		return children.stream().map(x->x.count()).reduce(BigInteger.ZERO, (a,b)-> a.add(b));
	}
	
	public ConfSet enumerate() {
		ConfSet root = ConfSet.singletonCS(this.label);
		//root.expansion(children.stream().map(x -> x.enumerate()).reduce(ConfSet.emptyCS(),(a,b) -> a.union(b)));
		return root.expansion(children.stream().map(x -> x.enumerate()).reduce(new ConfSet(),(a,b) -> a.union(b)));
		// TODO : Exceptions handling
	}
}

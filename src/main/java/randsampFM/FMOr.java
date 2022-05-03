package randsampFM;

import java.util.stream.Collectors;
import java.util.List;

public class FMOr extends FeatureModel {

	List<FeatureModel> children;
	
	public FMOr(String label, List<de.neominik.uvl.ast.Feature> rawChildren) {
		super(label);
		this.children = rawChildren.stream().map(x -> parseFeatureModel(x)).collect(Collectors.toList());
	}

	public long count() {
		return children.stream().mapToLong(x->x.count()+1).reduce(1, (a,b)-> a*b)-1;
	}
	
	public ConfSet enumerate() {
		ConfSet root = ConfSet.singletonCS(this.label);
		return root.expansion(children.stream().map(x -> x.enumerate().union(new ConfSet())).reduce((a,b) -> a.expansion(b)).get());
		// TODO : Exceptions handling
	}
}

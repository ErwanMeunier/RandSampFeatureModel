package randsampFM;

import java.util.List;
import java.util.stream.Collectors;

public final class FMXor extends FeatureModel{

	List<FeatureModel> children;
	
	public FMXor(String label, List<de.neominik.uvl.ast.Feature> rawChildren) {
		super(label);
		this.children = rawChildren.stream().map(x -> parseFeatureModel(x)).collect(Collectors.toList());
		// TODO Auto-generated constructor stub
	}
	
	public long count() {
		return children.stream().mapToLong(x->x.count()).sum() + 1;
	}
	
	public ConfSet enumerate() {
		ConfSet root = ConfSet.singletonCS(this.label);
		return root.expansion(children.stream().map(x -> x.enumerate()).reduce((a,b) -> a.union(b)).get());
		// TODO : Exceptions handling
	}
}

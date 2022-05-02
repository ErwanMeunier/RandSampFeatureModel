package randsampFM;

public class FMOr extends FeatureModel {

	public FMOr(de.neominik.uvl.ast.Feature feature) {
		super(feature);
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

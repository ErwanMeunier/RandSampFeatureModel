package randsampFM;

public class FMOr extends FeatureModel {

	public FMOr(de.neominik.uvl.ast.Feature feature) {
		super(feature);
		
		
		
	}

	public long count() {
		return children.stream().mapToLong(x->x.count()+1).reduce(1, (a,b)-> a*b)-1;
	}
}

package randsampFM;

public final class FMXor extends FeatureModel{

	public FMXor(final de.neominik.uvl.ast.Feature feature) {
		super(feature);
		// TODO Auto-generated constructor stub
	}
	
	public long count() {
		return children.stream().mapToLong(x->x.count()).sum() + 1;
	}
}

package randsampFM;

public class FMCard extends FMOr{

	private int lowerBound;
	private int upperBound; 
	
	public FMCard(de.neominik.uvl.ast.Feature feature, int lb, int ub) {
		super(feature);
		lowerBound = lb;
		upperBound = ub;
	}
	
	public long count() {
		return 1;
	}

	public ConfSet enumerate() {
		return ConfSet.singletonCS(new Feature("TODO"));
	}
	
	
}

package randsampFM;

import java.util.List;
import java.util.stream.Collectors;

public class FMCard extends FeatureModel{

	// TEMPORARY CLASS : TODO TOTALLY WRONG
	private int lowerBound;
	private int upperBound; 
	private List<FeatureModel> children;
	
	
	public FMCard(String label, List<de.neominik.uvl.ast.Feature> rawChildren, int lb, int ub) {
		super(label);
		lowerBound = lb;
		upperBound = ub;
		this.children = rawChildren.stream().map(x -> parseFeatureModel(x)).collect(Collectors.toList());
	}
	
	public long count() {
		return 1;
	}

	public ConfSet enumerate() {
		return ConfSet.singletonCS(new Feature("TODO"));
	}
	
	
}

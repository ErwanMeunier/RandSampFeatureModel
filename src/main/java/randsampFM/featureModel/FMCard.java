package randsampFM.featureModel;

import java.util.List;
//import java.util.stream.Collectors;
import java.math.BigInteger;

import randsampFM.types.Conf;
import randsampFM.types.ConfSet;
import randsampFM.types.Feature;
import java.util.Random;

public class FMCard extends FeatureModel{

	// TEMPORARY CLASS : TODO TOTALLY WRONG
	//private int lowerBound;
	//private int upperBound; 
	//private List<FeatureModel> children;
	
	
	public FMCard(String label, List<de.neominik.uvl.ast.Feature> rawChildren, int lb, int ub, Random generator) {
		super(label,generator);
		//lowerBound = lb;
		//upperBound = ub;
		//this.children = rawChildren.stream().map(x -> parseFeatureModel(x,generator)).collect(Collectors.toList());
	}
	
	public BigInteger count() {
		return BigInteger.ONE;
	}

	public ConfSet enumerate() {
		return ConfSet.singletonCS(new Feature("TODO"));
	}
	
	public Conf sample() {
		return new Conf();
	}
	
}

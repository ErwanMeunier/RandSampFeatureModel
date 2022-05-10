package randsampFM.featureModel;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Random;

import randsampFM.types.ConfSet;
import randsampFM.types.Conf;

public final class FMMandOpt extends FeatureModel {
			
	List<FeatureModel> mandChilds;
	List<FeatureModel> optChilds;
	
	public FMMandOpt(String label, List<de.neominik.uvl.ast.Feature> rawMandChilds , List<de.neominik.uvl.ast.Feature> rawOptChilds, Random generator) {
		super(label, generator);
		mandChilds = rawMandChilds.stream().map(x -> parseFeatureModel(x, generator)).collect(Collectors.toList());
		optChilds = rawOptChilds.stream().map(x -> parseFeatureModel(x, generator)).collect(Collectors.toList());
	}

	@Override
	public BigInteger count() {
		if(this.nbConfigurations == null) {
			BigInteger optCount;
			BigInteger mandCount;
			
			if(optChilds.isEmpty()) {
				optCount = BigInteger.ONE;
			} 
			else 
			{
				optCount = optChilds.stream().map(x -> x.count().add(BigInteger.ONE)).reduce((a,b)->a.multiply(b)).get();
			}
			
			if(mandChilds.isEmpty()) {
				mandCount = BigInteger.ONE;
			} 
			else {
				mandCount = mandChilds.stream().map(x -> x.count()).reduce((a,b)->a.multiply(b)).get();
			}
			this.nbConfigurations = mandCount.multiply(optCount);
		}
		return nbConfigurations;
	}
	
	public ConfSet enumerate() {
		ConfSet root = ConfSet.singletonCS(label);
		
		Stream<ConfSet> mandStream = Stream.empty(); 
		Stream<ConfSet> optStream = Stream.empty();	
		ConfSet result = new ConfSet();
		short nbEmpty = 0;
		
		// mandChilds and optChilds cannot be simultaneously empty
		
		if(mandChilds.isEmpty()) {
			result = ConfSet.expansion(optChilds.stream().map(x -> x.enumerate().union(ConfSet.emptyCS())).collect(Collectors.toList()));
			nbEmpty++;
		} else {
			mandStream = mandChilds.stream().map(x -> x.enumerate());
			
		}

		if(optChilds.isEmpty()) {
			result = ConfSet.expansion(mandChilds.stream().map(x -> x.enumerate()).collect(Collectors.toList()));
			nbEmpty++;
		} else {
			optStream = optChilds.stream().map(x -> x.enumerate().union(ConfSet.emptyCS()));
		}
		
		switch(nbEmpty) {
		
		case 0:
			ConfSet tempMand = mandStream.reduce(ConfSet.emptyCS(), (a,b)->a.expansion(b));
			ConfSet tempOpt = optStream.reduce(ConfSet.emptyCS(), (a,b)->a.expansion(b));
			result = tempMand.expansion(tempOpt);
			break;
		
		case 1: 
			break;
			
		default: // ~ case 0
			throw new NoSuchElementException("Both mandStream and optStrem cannot be empty");
		}

		return root.expansion(result); // TODO : Exceptions handling ?
	}
	
	public Conf sample() {
		double draw; 
		double bound; // between 0.0 and 1.0
		Conf result = new Conf(Set.of(this.label));
		
		for(FeatureModel fm : mandChilds) {
			result = result.union(fm.sample());
		}
		
		for(FeatureModel fm : optChilds) {
			bound = (BigDecimal.ONE).divide(new BigDecimal(fm.count().add(BigInteger.ONE)),precision,RoundingMode.HALF_EVEN).doubleValue();
			draw = generator.nextDouble(); // between 0.0 and 1.0

			if(bound <= draw) { // Complemented probability
				result = result.union(fm.sample());
			}
		}
		
		return result;
	}

}

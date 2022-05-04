package randsampFM;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class FMMandOpt extends FeatureModel {
			
	List<FeatureModel> mandChilds;
	List<FeatureModel> optChilds;
	
	public FMMandOpt(String label, List<de.neominik.uvl.ast.Feature> rawMandChilds , List<de.neominik.uvl.ast.Feature> rawOptChilds) {
		super(label);
		mandChilds = rawMandChilds.stream().map(x -> parseFeatureModel(x)).collect(Collectors.toList());
		optChilds = rawOptChilds.stream().map(x -> parseFeatureModel(x)).collect(Collectors.toList());
	}

	@Override
	public long count() {
		long optCount;
		long mandCount;
		
		if(optChilds.isEmpty()) {
			optCount = 1;
		} 
		else 
		{
			optCount = optChilds.stream().mapToLong(x -> x.count()+1).reduce((a,b)->a*b).getAsLong();
		}
		
		if(mandChilds.isEmpty()) {
			mandCount = 1;
		} 
		else {
			mandCount = mandChilds.stream().mapToLong(x -> x.count()+1).reduce((a,b)->a*b).getAsLong();
		}
		
		return mandCount*optCount;
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
			ConfSet tempMand = ConfSet.expansion(mandStream.collect(Collectors.toList()));
			ConfSet tempOpt = ConfSet.expansion(optStream.collect(Collectors.toList()));
			result = tempMand.expansion(tempOpt);
			break;
		
		case 1: 
			break;
			
		default: // ~ case 0
			throw new NoSuchElementException("Both mandStream and optStrem cannot be empty");
		}

		return root.expansion(result); // TODO : Exceptions handling 
	}

}

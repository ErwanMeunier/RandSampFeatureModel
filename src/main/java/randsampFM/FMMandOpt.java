/**
 * 
 */
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
		ConfSet result;
		boolean bothNotEmpty = true;
		
		System.out.println("enumerateMANDOPT");
		
		// mandChilds and optChilds cannot be simultaneously empty
		
		if(mandChilds.isEmpty()) {
			result = ConfSet.expansion(optChilds.stream().map(x -> x.enumerate().union(new ConfSet())).collect(Collectors.toList()));
			bothNotEmpty = false;
		} else {
			mandStream = mandChilds.stream().map(x -> x.enumerate());
			
		}
		System.out.println("enumerateMANDOPT");
		if(optChilds.isEmpty()) {
			result = ConfSet.expansion(mandChilds.stream().map(x -> x.enumerate()).collect(Collectors.toList()));
			bothNotEmpty = true;
		} else {
			optStream = optChilds.stream().map(x -> x.enumerate().union(new ConfSet()));
		}
		
		if(bothNotEmpty) {
			result = ConfSet.expansion(Stream.concat(mandStream, optStream).collect(Collectors.toList()));
		}else {
			throw new NoSuchElementException("Something wrong happened");//TODO
		}
		
		System.out.println("enumerateMANDOPT");
		System.out.println(optChilds.toString());
		System.out.println(mandChilds.toString());
		
		
		return root.expansion(result); // TODO : Exceptions handling 
	}
	
}

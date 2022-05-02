/**
 * 
 */
package randsampFM;

import java.util.NoSuchElementException;
import java.util.stream.Stream;

public final class FMMandOpt extends FeatureModel {
			
	public FMMandOpt(final de.neominik.uvl.ast.Feature feature, final boolean isOptional) {
		super(feature);
		this.optional = isOptional;
	}

	@Override
	public long count() {
		long optCount;
		long mandCount;
		try{
			optCount = children.stream().filter(fm -> fm.isOptional()).mapToLong(fm -> fm.count()+1).reduce((a,b)->a*b).getAsLong();
		} catch(NoSuchElementException e) {
			optCount = 1;
		}
		try {
			mandCount = children.stream().filter(fm -> fm.isOptional()).mapToLong(fm -> fm.count()).reduce((a,b)->a*b).getAsLong();
		} catch(NoSuchElementException e) {
			mandCount = 1;
		}
		
		return mandCount*optCount;
	}
	
	public ConfSet enumerate() {
		ConfSet root = ConfSet.singletonCS(this.label);
		
		return root.expansion(
				Stream.concat(
				 children.stream().filter(x -> !x.isOptional()).map(x -> x.enumerate()),
				 children.stream().filter(x -> x.isOptional()).map(x -> x.enumerate().union(new ConfSet()))
				 ).reduce((a,b)->a.expansion(b)).get()
				);
		 // TODO : Exceptions handling 
	}
	
}

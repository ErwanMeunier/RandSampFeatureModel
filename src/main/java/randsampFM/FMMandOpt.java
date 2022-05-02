/**
 * 
 */
package randsampFM;

import java.util.NoSuchElementException;

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
	
}

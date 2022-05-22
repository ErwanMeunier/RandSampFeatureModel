/**
 * 
 */
package randsampFM.types;

public class Feature {
	
	private final String name;
	;
	public Feature(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	@Override 
	public boolean equals(Object obj) {
		
		if(this == obj) return true; // Same references
		
		if(obj == null || obj.getClass()!=this.getClass()) return false; // Different types
		
		Feature tempFeature = (Feature) obj; // type casting (ClassCastException cannot happen because types have already been checked above)
		
		return (tempFeature.getName().equals(this.name));
	}
	
	@Override
	public int hashCode(){
		return name.hashCode();
	}
}

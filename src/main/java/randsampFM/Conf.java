package randsampFM;

import java.util.Set;
import java.util.HashSet;

public class Conf {
	
	private Set<Feature> innerSet;
	private String signature; // Useful in ConfSet.class
	
	/*Creates a new empty Conf*/
	public Conf() {
		innerSet = new HashSet<>();
		signature = "[]";
	}
	
	public Conf(Set<Feature> newSet){
		innerSet = Set.copyOf(newSet); // immutable set
		Object[] signatureList = innerSet.stream().map(ft -> ft.toString()).sorted().toArray(); // Sorts and concatenates the elements of the set -> to be hashed right after.  
		
		// Efficient concatenation
		
		StringBuilder tmpSig = new StringBuilder("");
		
		for(Object c : signatureList) {
			tmpSig = tmpSig.append(((String)c) + ",");
		}
		
		signature = tmpSig.toString();
	}

	public Set<Feature> getInnerSet() {
		return Set.copyOf(innerSet);
	}
	
	public String getSignature() {
		return signature;
	}
	
	@Override
	public String toString() {
		return "[" + signature +"]"; // TODO -> Clean + and use StringBuilder
	}

	public Conf copy(){ // TODO We could make another constructor in order to avoid to compute the hash twice
		return new Conf(innerSet);
	}
	
	@Override
	public int hashCode() {
		return signature.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if(this == obj) return true; // Same references
		
		if(obj == null || obj.getClass()!=this.getClass()) return false; // Different types
		
		Conf tempConf = (Conf) obj; // type casting (ClassCastException cannot happen because types have already been checked above)
		return tempConf.getSignature().equals(this.signature); // strings comparison
	}
	
	public Conf union(Conf addedConf) { // "immutable" union

		Set<Feature> newSet = new HashSet<>();
		newSet.addAll(this.getInnerSet());
		newSet.addAll(addedConf.getInnerSet());
		return new Conf(newSet);
	}	
}

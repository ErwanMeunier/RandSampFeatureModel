/**
 * 
 */
package randsampFM;

import java.util.Set;
import java.util.Collections;

/**
 * @author stagiaire-tasc
 *
 */
public class Conf {
	
	private Set<Feature> innerSet;
	private int hash;
	private String signature; // Useful in ConfSet.class
	
	/*Creates a new empty Conf*/
	public Conf() {
		innerSet = Collections.emptySet(); // new empty set
		hash = "".hashCode();
	}
	
	/*Creates a new filled Conf*/
	public Conf(Set<Feature> newSet){
		innerSet = Set.copyOf(newSet); // immutable set
		signature= innerSet.stream().map(ft -> ft.getName()).sorted().reduce("",(a,b)->a+b); // Sorts and concatenates the elements of the set -> to be hashed right after.  
		hash = signature.hashCode();
	}

	public Set<Feature> getInnerSet() {
		return Set.copyOf(innerSet);
	}
	
	public String getSignature() {
		return signature;
	}

	public Conf copy(){ // TODO We could make another constructor in order to avoid to compute the hash twice
		return new Conf(innerSet);
	}
	
	@Override
	public int hashCode() {
		return hash;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if(this == obj) return true; // Same references
		
		if(obj == null || obj.getClass()!=this.getClass()) return false; // Different types
		
		Conf tempConf = (Conf) obj; // type casting (ClassCastException cannot happen because types have already been checked above)
		
		return (tempConf.hashCode()==this.hash); // no need to compare innerSets
	}
	
	public Conf union(Conf addedConf) { // "immutable" union
		Set<Feature> newSet = Collections.emptySet(); // new empty set
		newSet.addAll(this.innerSet);
		newSet.addAll(addedConf.getInnerSet());
		return new Conf(newSet);
	}
	
	/*public Conf remove(Set<Feature> set){
		Really useful ?
	}*/
	
	
}

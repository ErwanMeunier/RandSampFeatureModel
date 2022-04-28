/**
 * 
 */
package randsampFM;

import java.util.Set;
import java.util.Collections;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * @author stagiaire-tasc
 *
 */
public class ConfSet {
	
	private Set<Conf> innerSet;
	private int hash;
	
	public ConfSet() {
		innerSet = new HashSet<Conf> ();
		hash = "".hashCode();
	}
	
	public ConfSet(Set<Conf> newSet) {
		innerSet = Set.copyOf(newSet);
		String toBeHashed = innerSet.stream().map(x->x.getSignature()).sorted().reduce("",(a,b)->a+b);
		hash = toBeHashed.hashCode();
	}
	
	public ConfSet createConfSetfromRaw(Set<Set<Feature>> newSet){ // otherwise, we can't have both ConfSet(Set<Set<Feature>> newSet) and ConfSet(Set<Conf> newSet) in the same time
		Set<Set<Feature>> tmpSet = Set.copyOf(newSet); // immutable set
		Set<Conf> tmpSet2 = Collections.unmodifiableSet(tmpSet.stream().map(x -> new Conf(x)).collect(Collectors.toSet())); // Almost the same principle as in Conf.class
		return new ConfSet(tmpSet2);
	}
	
	public Set<Conf> getInnerSet(){
		return Set.copyOf(innerSet);
	}
	
	
	@Override // not necessary as an "HashCode" but useful in equals
	public int hashCode() {
		return hash;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true; // Same references
		
		if(obj == null || obj.getClass()!=this.getClass()) return false; // Different types
		
		ConfSet tempCS = (ConfSet) obj; // type casting (ClassCastException cannot happen because types have already been checked above)
		
		return (tempCS.hashCode()==this.hash); // no need to compare innerSets
	}
	
	/*public ConfSet expansion() {
		
	}*/
	
	public ConfSet union(ConfSet addedConf) {
		Set<Conf> newSet = Collections.emptySet(); // new empty set : Set<Conf> 
		newSet.addAll(this.innerSet);
		newSet.addAll(addedConf.getInnerSet());
		return new ConfSet(newSet);
	}
}

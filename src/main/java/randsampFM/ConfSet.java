/**
 * 
 */
package randsampFM;

import java.util.Set;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class ConfSet {
	
	private Set<Conf> innerSet;
	private String signature;
	
	public ConfSet() {
		innerSet = new HashSet<Conf> ();
		signature = "";
	}
	
	public ConfSet(Set<Conf> newSet) {
		innerSet = Set.copyOf(newSet);
		Object[] signatureList = innerSet.stream().map(x->x.getSignature()).sorted().toArray();
		
		// In order to concatenate efficiently
		StringBuilder tmpSig = new StringBuilder("");
		
		for(Object c : signatureList) {
			tmpSig = tmpSig.append("[" + (String) c).append("],");
		}
		
		signature = tmpSig.toString();
	}
	
	// Unused for now
	private ConfSet(Set<Conf> newSet, String signature){
		innerSet = Set.copyOf(newSet);
		this.signature = signature;
	}
	
	public ConfSet createConfSetfromRaw(final Set<Set<Feature>> newSet){ // otherwise, we can't have both ConfSet(Set<Set<Feature>> newSet) and ConfSet(Set<Conf> newSet) in the same time
		Set<Set<Feature>> tmpSet = Set.copyOf(newSet); // immutable set
		Set<Conf> tmpSet2 = Collections.unmodifiableSet(tmpSet.stream().map(x -> new Conf(x)).collect(Collectors.toSet())); // Almost the same principle as in Conf.class
		return new ConfSet(tmpSet2);
	}
	
	public static ConfSet singletonCS(final Feature feature) { // for the rootFeatures
		return new ConfSet(Set.of(new Conf(Set.of(feature))));
	}
	
	public static ConfSet emptyCS() {
		return new ConfSet(Set.of(new Conf()));
	}
	
	private Set<Conf> getInnerSet(){
		return Set.copyOf(innerSet);
	}
	
	public ConfSet union(final ConfSet addedConf) {
		Set<Conf> newSet = new HashSet<>(); // new empty set : Set<Conf> 
		newSet.addAll(this.innerSet);
		newSet.addAll(addedConf.getInnerSet());
		return new ConfSet(newSet);
	}
	
	public int size() {
		return innerSet.size();
	}
	
	public ConfSet expansion(ConfSet cs2) {
		
		Set<Conf> set2 = cs2.getInnerSet();
		Set<Conf> tmpNewSet = new HashSet<>(); // empty set
		ConfSet newConfSet = new ConfSet();
		
			for(Conf c1 : innerSet) {
				for(Conf c2: set2) {
					tmpNewSet = Set.of(c1.union(c2));
					newConfSet = newConfSet.union((new ConfSet(tmpNewSet)));
				}
			}
		
		return newConfSet;
	}
	
	public static ConfSet expansion(List<ConfSet> listCS) {
		ConfSet result = new ConfSet(Set.of(new Conf()));
		
		for(ConfSet cs : listCS) {
			result = result.expansion(cs);
		}
		return result;
	}
	// TODO: EXPANSION BY CARDINALITY
	
	public ConfSet without(final Conf c) {
		if (!innerSet.contains(c)) {
			return new ConfSet(innerSet, signature);
		}else {
			Set<Conf> result = new HashSet<>();
			result.addAll(innerSet);
			result.remove(c);
			return new ConfSet(result);
		}
	}
	
	@Override
	public String toString() {
		return signature;
	}
}

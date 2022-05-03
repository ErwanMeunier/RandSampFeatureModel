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
			tmpSig = tmpSig.append((String) c);
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
	
	private Set<Conf> getInnerSet(){
		return Set.copyOf(innerSet);
	}
	
	public String getContent() {
		return signature;
	}
	
	private static Set<Conf> union(final Set<Conf> set1, final Set<Conf> set2){ // immutable union
		Set<Conf> result = new HashSet<>(set1);
		result.addAll(set2);
		return result;
	}
	
	public ConfSet union(final ConfSet addedConf) {
		Set<Conf> newSet = Collections.emptySet(); // new empty set : Set<Conf> 
		newSet.addAll(this.innerSet);
		newSet.addAll(addedConf.getInnerSet());
		return new ConfSet(newSet);
	}
	
	public ConfSet expansion(ConfSet cs2) {
		
		Set<Conf> result = cs2.getInnerSet();
		Set<Conf> tmpNewSet;
		
		for(Conf c1 : innerSet) {
			tmpNewSet = result.stream().map(x -> x.union(c1)).collect(Collectors.toSet()); // "Distributes" type: Set<Conf>
			result = union(result,tmpNewSet);
		}
		
		return new ConfSet(result);
	}
	
	// TODO: EXPANSION BY CARDINALITY
}

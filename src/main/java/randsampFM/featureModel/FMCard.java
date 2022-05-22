package randsampFM.featureModel;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
//import java.util.stream.Collectors;
import java.math.BigInteger;

import randsampFM.types.Conf;
import randsampFM.types.ConfSet;
import randsampFM.types.Feature;
import java.util.Random;
import java.util.stream.Collectors;

public class FMCard extends FeatureModel{

	// TEMPORARY CLASS : TODO TOTALLY WRONG
	private int lb; // lower bound 
	private int ub; // upper bound
	private int n;
	private ArrayList<FeatureModel> children;
	private LinkedList<LinkedList<FeatureModel>> localEnumeration; 
	
	public FMCard(String label, List<de.neominik.uvl.ast.Feature> rawChildren, int lb, int ub, Random generator) {
		
		super(label,generator);
		this.lb = lb;
		this.ub = ub;
		this.children = rawChildren.stream()
				.map(x -> parseFeatureModel(x,generator))
				.collect(Collectors.toCollection(ArrayList::new));
		this.n = children.size();
		this.localEnumeration = generate();
		//throw new UnsupportedOperationException();
	}
	
	public BigInteger count() {
		BigInteger result = BigInteger.ZERO;
		BigInteger temp;
		for(LinkedList<FeatureModel> fmList : localEnumeration) {
			temp = fmList.stream().map(fm -> fm.count()).reduce(BigInteger.ONE, (a,b)->a.multiply(b));
			result = result.add(temp);
		}
		return result;
	}

	public ConfSet enumerate() {
		ConfSet result = new ConfSet();
		ConfSet temp;
		for(LinkedList<FeatureModel> fmList : localEnumeration) {
			temp = fmList.stream().map(fm -> fm.enumerate()).reduce(ConfSet.emptyCS(), (a,b)->a.expansion(b));
			result = result.union(temp);
		}
		return result;
	}
	
	public Conf sample() {
		return new Conf(); //TODO
	}
	
	// Initialisation
	private LinkedList<LinkedList<FeatureModel>> generate(){
		LinkedList<LinkedList<FeatureModel>> left = generateRec(false, 0, new LinkedList<FeatureModel>());
		LinkedList<LinkedList<FeatureModel>> right = generateRec(true, 0, new LinkedList<FeatureModel>());
		left.addAll(right);
		return left;
	}
	
	private LinkedList<LinkedList<FeatureModel>> generateRec(boolean add, int card, LinkedList<FeatureModel> currentList) {
		LinkedList<FeatureModel> temp = currentList.stream().collect(Collectors.toCollection(LinkedList::new)); 
		LinkedList<LinkedList<FeatureModel>> result = new LinkedList<LinkedList<FeatureModel>>(); 
		
		if(add) {
			if(card + 1 <= ub) {
				temp.add(children.get(card));
				result.add(temp);
			} else {
				result.add(currentList);
				return result;
			}
		} else {
			if(card < n - lb - 1 ) {
				result.add(currentList);
				return result;
			}
		}
		
		result.addAll(generateRec(false, card+1, temp)); // left
		result.addAll(generateRec(true, card+1, temp)); // right
		return result;//TODO
	}
	
}

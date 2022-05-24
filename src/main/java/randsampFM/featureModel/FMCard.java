package randsampFM.featureModel;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.math.BigDecimal;
//import java.util.stream.Collectors;
import java.math.BigInteger;
import java.math.RoundingMode;

import randsampFM.types.Conf;
import randsampFM.types.ConfSet;

//import randsampFM.types.Feature;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class FMCard extends FeatureDiagram{

	// TEMPORARY CLASS : TODO TOTALLY WRONG
	private int lb; // lower bound 
	private int ub; // upper bound
	private int n;
	private BigInteger nbConfigurations;
	private ArrayList<FeatureDiagram> children;
	private LinkedList<List<FeatureDiagram>> localEnumeration; 
	private ArrayList<BigInteger> subsetsCards;
	
	public FMCard(String label, List<de.neominik.uvl.ast.Feature> rawChildren, int lb, int ub, Random generator) {
		
		super(label,generator);
		this.nbConfigurations = null;
		this.subsetsCards = null;
		this.lb = lb;
		System.out.println(lb);
		this.ub = ub;
		System.out.println(ub);
		this.children = rawChildren.stream()
				.map(x -> parseFeatureDiagram(x,generator))
				.collect(Collectors.toCollection(ArrayList::new));
		this.n = children.size();
		System.out.println(children);
		this.localEnumeration = new LinkedList<List<FeatureDiagram>>();
		generate();
		System.out.println(localEnumeration);
		//throw new UnsupportedOperationException();
	}
	
	public BigInteger count() {
		if(nbConfigurations == null) {
			nbConfigurations = BigInteger.ZERO;
			BigInteger temp;
			for(List<FeatureDiagram> fmList : localEnumeration) {
				temp = fmList.stream().map(fm -> fm.count()).reduce(BigInteger.ONE, (a,b)->a.multiply(b));
				nbConfigurations = nbConfigurations.add(temp);
			}
		}
		return nbConfigurations;
	}

	public ConfSet enumerate() {
		ConfSet result = new ConfSet();
		ConfSet temp;
		for(List<FeatureDiagram> fmList : localEnumeration) {
			temp = fmList.stream().map(fm -> fm.enumerate()).reduce(ConfSet.emptyCS(), (a,b)->a.expansion(b));
			result = result.union(temp);
		}
		return result.expansion(ConfSet.singletonCS(this.label));
	}
	
	// Card can be seen as a void XOR
	public Conf sample() {
		
		if(subsetsCards == null) {
			subsetsCards = new ArrayList<BigInteger>(n);
			BigInteger c;
			
			for(List<FeatureDiagram> fmList : localEnumeration) {
				c = fmList.stream().map(fm -> fm.count()).reduce(BigInteger.ONE, (a,b)->a.multiply(b));
				subsetsCards.add(c);
			}	
		}
		
		List<FeatureDiagram> subset = choose();
		Conf result = new Conf();
		
		for(FeatureDiagram fm : subset) {
			result = result.union(fm.sample());
		}
		
		return result.union(new Conf(Set.of(this.label)));
	}
	
	private List<FeatureDiagram> choose(){
		/**
		 * Chooses a subset of possible combinations.
		 * */
		
		double r = generator.nextDouble();
		BigDecimal nbConf = new BigDecimal(this.count());
		

		Object[] tmpChildren = subsetsCards.stream().map(x -> new BigDecimal(x)).toArray();
		
		double p;
		int i = 0;
		BigDecimal child;
		
		while(r >= 0) {
			child = (BigDecimal) tmpChildren[i];
			p = child.divide(nbConf,precision,RoundingMode.HALF_EVEN).doubleValue();
			r = r - p;
			i++;
		}
		
		return localEnumeration.get(i-1); // TODO : Changing the type from linkedList to ArrayList ?
	}
	
	// Initialization
	private void generate(){
		generateRec(1, 1, new LinkedList<Integer>());
	}
	
	// TODO ; add SIZE variable
	private void generateRec(int depth, int card, LinkedList<Integer> currentList) {
		@SuppressWarnings("unchecked")
		LinkedList<Integer> temp = (LinkedList<Integer>) currentList.clone(); 
		
		int threshold = card + n - depth;
		System.out.println("-----------");
		System.out.println("threshold : " + threshold);
		System.out.println("depth : " + depth);
		System.out.println("card : " + card);
		System.out.println("currentList : " + currentList);
		
		
		if(depth <= n) {
			if(lb <= threshold || threshold <= ub) {
				@SuppressWarnings("unchecked")
				LinkedList<Integer> left = (LinkedList<Integer>) temp.clone();
				@SuppressWarnings("unchecked")
				LinkedList<Integer> right = (LinkedList<Integer>) temp.clone();
				System.out.println("right" + right);
				right.add(depth);
				System.out.println("right" + right);
				generateRec(depth+1, card, left);
				generateRec(depth+1, card+1, right);
			} else {
				System.out.println("STOP");
			}
			
		} else {
			if(lb <= threshold && threshold <= ub) {
			System.out.println("AU BOUT");
			localEnumeration.add(currentList.stream().map(i -> children.get(i-1)).toList());
			}
		}
	}
	
}

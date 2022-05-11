package randsampFM;

import de.neominik.uvl.ast.*;
import de.neominik.uvl.UVLParser;
import randsampFM.featureModel.FeatureModel;
import randsampFM.types.Conf;
import randsampFM.types.ConfSet;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
//import picocli.CommandLine.Parameters;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
//import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Random;
//import java.util.Set;
import java.util.stream.Collectors;
//import java.util.stream.IntStream;
import java.util.Map;
import java.util.ArrayList;

/*
 * @author Erwan Meunier
 * since 04/08/2022
 * @version 0.1
 * */

@Command( // TODO
		name = "Randsampfm", 
		mixinStandardHelpOptions = true, 
		version = "RandSampFeatureModel 0.1",
		description = "Provides Feature Models enumeration, counting and sampling"
		)

public final class Main implements Runnable {
	
	@Deprecated
	public final static String wd = System.getProperty("user.dir"); // working directory
	
	public final static long seed = 877; // prime number
	public final static Random generator = new Random(seed);
	
	/* USELESS @Deprecated
	 * public final static int nbActions = 3; 
	public static ArrayList<Boolean> actions = new ArrayList<Boolean>(nbActions);
	/*0 -> enumerate
	 *1 -> count
	 *2 -> sample
	 * ... <to fill>
	 * */
	
	@Option(names = {"-e", "--enumerate"}, description = "Enumerates all the configurations from the given feature model.")
	private boolean enumeration;
	
	@Option(names = {"-c", "--count"}, description = "Returns the number of total configurations from the given feature model.")
	private boolean counting;
	
	@Option(names = {"-s", "--sample"}, description = "Returns random configuration uniformly.")
	private long nbSamples = 1; // default
	// If nbSamples is zero there is no sampling. Otherwise, we sample as many configurations as requested.
	
	@Option(names = {"-P", "--path"}, required = true, description = ".ulv file from which the feature model will be parsed.")
	private String path;
	
	@Option(names = {"-b"}, description = "Benchmark mode.")
	private long samplesize = 0;
	/* IDEAS
	 * TODO : 
	 * output(s) into a file
	 * timelimit
	 * verbose mode
	 * */
	
	@Override
	public void run() {
		//System.out.println(path);
		
		FeatureModel fm = parseAndConvert(path);
		
		if(samplesize > 0) {
			benchmark(fm, samplesize);
		} else {
			if(counting) {
				System.out.println("COUNTING");
				System.out.println("Number of configurations: " + fm.count());
				System.out.println("Done");
			}
			
			if(nbSamples>0) {
				System.out.println("SAMPLING");
				for(long i=1; i<=nbSamples; i++) {
					System.out.println("[" + i + "]:" + fm.sample());
				}
				System.out.println("Done");
			}
			
			if(enumeration) {
				System.out.println("ENUMERATION");
				ConfSet result = fm.enumerate();
				System.out.println(result);
				System.out.println("Done");
			}
			
			System.out.print("END");
		}
		
	}
	
	public static void main(String[] args) {
		int exitCode = new CommandLine(new Main()).execute(args);
		System.exit(exitCode);
	}
	
	private UVLModel loadModel(final String filename) {
		try {
			return (UVLModel) UVLParser.parse(Files.readString(Path.of(filename)));
	    } catch (IOException e) {
	    	e.printStackTrace();
	    	System.exit(0);
	    	return null;
	    }
	}

	private FeatureModel uvlModeltoFM(final UVLModel uvlmodel){
		de.neominik.uvl.ast.Feature rootFeature = Arrays.asList(uvlmodel.getRootFeatures()).stream().findFirst().get();
		return FeatureModel.parseFeatureModel(rootFeature, generator);
	}
	
	private FeatureModel parseAndConvert(final String filename) { // mainly intended to the benchmark
		return uvlModeltoFM(loadModel(filename));
	}
	
	private void benchmark(final FeatureModel fm, final long nbOfSamples){
		BigInteger nbConf = fm.count();
		final long threshold = 5000;
		//final long nbOfSamples = 10000;
		final BigInteger bigThreshold = BigInteger.valueOf(threshold);
		int comparison = nbConf.compareTo(bigThreshold);

		if(comparison == 1) {
			System.out.println("WARNING : Benchmark may not end in a reasonable time. Benchmark aborted.");
		}else {
			ConfSet enumerationOfFM = fm.enumerate();
			
			int smallNbConf = nbConf.intValueExact();
			
			ArrayList<Conf> configs = new ArrayList<Conf>(enumerationOfFM.getInnerSet().stream().toList()); // add .sorted() ?
			
			Map<Conf, Integer> intToconf = fm.enumerate().getInnerSet().stream().map(x -> new Conf[] {x,x}).collect(Collectors.toMap(e -> e[0], e -> configs.indexOf(e[0])));
			//associate configs index and conf
			
			ArrayList<Integer> rawData = new ArrayList<Integer>(Collections.nCopies(smallNbConf, 0));
			// ArrayList full of zeros
			
			for(int i = 0; i < nbOfSamples; i++) {
				//System.out.print(i);
				Conf tmp = fm.sample();
				//System.out.println(tmp);
				int index = intToconf.get(tmp);
				rawData.set(index, rawData.get(index)+1);
			}
		
			List<Double> data = rawData.stream().map(x -> (double) x/nbOfSamples).toList();
					
			System.out.println(data);			
		}

	}
	
}

package randsampFM;

import de.neominik.uvl.ast.*;
import de.neominik.uvl.UVLParser;
import randsampFM.featureModel.FeatureModel;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.util.Arrays;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;

/*
 * @author Erwan Meunier
 * since 04/08/2022
 * @version 0.1
 * */

/*@Command( // TODO
		name = "randsampfm", 
		mixinStandardHelpOptions = true, 
		version = "RandSampFeatureModel 0.1",
		description = "Provides Feature Models enumeration, counting and sampling"
		)
*/
public final class Main {

	final static String wd = System.getProperty("user.dir"); // working directory
	
	public static void main(String[] args) {
		FeatureModel testFM = parseAndConvert("/src/test/resources/Feature_Models/Database/BerkeleyDB/berkeleydb.uvl");
		
		System.out.println(testFM.count());
		
		//System.out.println(testFM.enumerate());
		//System.out.println(testFM.enumerate().size());
		/*for(int i = 0; i<100; i++) {
			System.out.println(testFM.sample());
		}*/
	}
	
	public static UVLModel loadModel(final String filename) {
		try {
			return (UVLModel) UVLParser.parse(Files.readString(Path.of(filename)));
	    } catch (IOException e) {
	    	e.printStackTrace();
	    	System.exit(0);
	    	return null;
	    }
	}

	public static FeatureModel uvlModeltoFM(final UVLModel uvlmodel){
		de.neominik.uvl.ast.Feature rootFeature = Arrays.asList(uvlmodel.getRootFeatures()).stream().findFirst().get();
		return FeatureModel.parseFeatureModel(rootFeature);
	}
	
	public static FeatureModel parseAndConvert(final String filename) { // intended to the benchmark
		return uvlModeltoFM(loadModel(wd+filename)); // wd can be removed
	}
	
}

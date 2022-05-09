package randsampFM;

import de.neominik.uvl.ast.*;
import randsampFM.featureModel.FeatureModel;
import de.neominik.uvl.UVLParser;

import java.util.Arrays;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;

/*
 * @author Erwan Meunier
 * since 04/08/2022
 * */

public final class Main {

	final static String wd = System.getProperty("user.dir"); // working directory
	
	public static void main(String[] args) {
		FeatureModel testFM = parseAndConvert("/src/test/resources/test1.uvl");
		System.out.println(testFM.enumerate());
		System.out.println(testFM.enumerate().size());
		System.out.println(testFM.count());
		
		for(int i = 0; i<100; i++) {
			System.out.println(testFM.sample());
		}
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

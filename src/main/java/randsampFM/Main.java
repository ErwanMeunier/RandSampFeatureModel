package randsampFM;

import de.neominik.uvl.ast.*;
import de.neominik.uvl.UVLParser;

import java.util.Arrays;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.util.NoSuchElementException;

/*
 * @author Erwan Meunier
 * 
 * 
 * */

public final class Main {

	public static void main(String[] args) {
		String wd = System.getProperty("user.dir");// get the current working directory
		
		UVLModel model = loadModel(wd + "/src/test/resources/test1.uvl");
		
		ConfSet result;
		
		try {
			de.neominik.uvl.ast.Feature rootFeature = Arrays.asList(model.getRootFeatures()).stream().findFirst().get();
			result = (FeatureModel.parseFeatureModel(rootFeature)).stream().findFirst().get().enumerate();
		} catch(NoSuchElementException e){
			System.out.println("Empty Feature Model");
			result = new ConfSet(); // empty ConfSet
		}
		
		System.out.println(result.getContent());
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
	
	public static List<de.neominik.uvl.ast.Feature> getRootFeature(UVLModel uvlmodel){
		return Arrays.asList(uvlmodel.getRootFeatures());
	}
}

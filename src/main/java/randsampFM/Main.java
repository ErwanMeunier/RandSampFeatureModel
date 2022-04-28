package randsampFM;

import de.neominik.uvl.ast.*;
import de.neominik.uvl.UVLParser;

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;

public final class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

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
}

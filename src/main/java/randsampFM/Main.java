package randsampFM;

import de.neominik.uvl.ast.*;
import de.neominik.uvl.UVLParser;

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;

/*
 * @author Erwan Meunier
 * 
 * 
 * */

public final class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Affichage pour voir si c'est bien compilé");
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

package astFileProcessor;

import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import astFileProcessor.processors.ASTComponentProcessor;
import astFileProcessor.processors.ASTServiceProcessor;
import astFileProcessor.processors.ImportProcessor;


public class ASTLoader {

	private JSONObject astTreeRoot = null;
	private ImportProcessor importProcessor = null;
	private ASTServiceProcessor astServiceProcessor = null;
	private ASTComponentProcessor astComponentProcessor = null;
	
	public ASTLoader(String fileName) {
		this.importProcessor = new ImportProcessor();
		this.astTreeRoot = ASTLoader.loadAST(fileName);
		this.astServiceProcessor = new ASTServiceProcessor();
		this.astComponentProcessor = new ASTComponentProcessor();
	}
	
	public static JSONObject loadAST(String astPath) {
		try {
	        JSONParser parser = new JSONParser();
	        JSONObject configurationObject = (JSONObject) parser.parse(new FileReader(astPath));
	        return configurationObject;
	    } catch (IOException | ParseException e) {
	        e.printStackTrace();
	    }
		return null;
	}
	
	public static JSONObject loadASTFromString(String astString) {
		try {
	        JSONParser parser = new JSONParser();
	        JSONObject configurationObject = (JSONObject) parser.parse(astString);
	        return configurationObject;
	    } catch (ParseException e) {
	        e.printStackTrace();
	    }
		return null;
	}
	
	public void parse(ASTParseConfiguration astParseConfiguration) {
		if (astParseConfiguration.enabledImports()) {
			this.importProcessor.getImports(this.astTreeRoot);
			this.importProcessor.printImports();
		}
		if (astParseConfiguration.enabledServices()) {
			this.astServiceProcessor.getClasses(this.astTreeRoot);
			this.astServiceProcessor.print();
		}
		if (astParseConfiguration.enabledComponents()) {
			this.astComponentProcessor.getClasses(this.astTreeRoot);
			this.astComponentProcessor.print();
		}
	}
}

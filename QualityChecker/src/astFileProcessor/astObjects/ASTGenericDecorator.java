package astFileProcessor.astObjects;

import java.util.List;
import java.io.IOException;
import java.util.ArrayList;
import org.json.simple.JSONObject;

import astFileProcessor.annotationManagment.AnnotationInstance;
import codeConstructsEvaluation.transformation.ASTConverterClient;


public class ASTGenericDecorator extends AnnotationInstance {

	public enum DecoratorAssociatedWith {CLASS, METHOD, FILE_ONLY, UNKNOWN};
	private JSONObject astRepresentation;
	private String codeRepresentation = null;
	private List<JSONObject> coveredCodes;
	private List<String> coveredCodesString = null;
	private DecoratorAssociatedWith associationWith = DecoratorAssociatedWith.UNKNOWN;
	
	
	public ASTGenericDecorator(String name, JSONObject astRepresentation, JSONObject coveredCode) {
		super(name);
		this.astRepresentation = astRepresentation;
		this.coveredCodes = new ArrayList<JSONObject>();
		this.coveredCodes.add(coveredCode);
	}
	
	public void setAssociatedWith(DecoratorAssociatedWith associationWith) { this.associationWith = associationWith; }
	
	public boolean isAssociatedWith(DecoratorAssociatedWith associationWith) {
		return this.associationWith == associationWith;
	}

	public void putAlternativeCode(JSONObject alternativeCode) { this.coveredCodes.add(alternativeCode); }
	
	public String getName() { return this.getAnnotationName(); }
	
	public JSONObject getASTRepresentation() { return this.astRepresentation; }
	
	public JSONObject getCoveredCode(int index) { return this.coveredCodes.get(index); }

	public String getCodeRepresentation() throws IOException, InterruptedException { 
		if (this.codeRepresentation == null) {
			this.codeRepresentation = ASTConverterClient.convertFromASTToCode(this.astRepresentation.toString());
		}
		return this.codeRepresentation; 
	}
	
	public void convertToCodeString() throws IOException, InterruptedException {
		if (this.coveredCodesString == null) {
			this.coveredCodesString = new ArrayList<String>();
		} else {
			this.coveredCodesString.clear();
		}
		
		String convertedFromASTtoCode;
		for(JSONObject coveredCode: coveredCodes) {
			convertedFromASTtoCode = ASTConverterClient.convertFromASTToCode(coveredCode.toString());
			this.coveredCodesString.add(convertedFromASTtoCode);
		}
		
		this.codeRepresentation = ASTConverterClient.convertFromASTToCode(this.astRepresentation.toString());
	}

}

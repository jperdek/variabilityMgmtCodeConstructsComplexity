package astFileProcessor.astObjects;

import org.json.simple.JSONObject;


public class ASTIllegalDecorator extends ASTGenericDecorator {
	
	private JSONObject redundantCode = null;
	private String redundantCodeString = null;
	
	
	public ASTIllegalDecorator(String name, JSONObject astRepresentation, 
			JSONObject coveredCode, JSONObject redundantCode) {
		super(name, astRepresentation, coveredCode);
		this.redundantCode = redundantCode;
	}
	
	public ASTIllegalDecorator(String name, JSONObject astRepresentation, 
			JSONObject coveredCode, JSONObject redundantCode, String redundantCodeString) {
		this(name, astRepresentation, coveredCode, redundantCode);
		this.redundantCodeString = redundantCodeString;
	}
	
	public JSONObject getRedundantCode() { return this.redundantCode; }
	
	public String getRedundantCodeString() { return this.redundantCodeString; }
}

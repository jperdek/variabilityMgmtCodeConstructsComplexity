package astFileProcessor.processors;

import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import astFileProcessor.astObjects.ASTAnyObjectType;
import astFileProcessor.astObjects.ASTClass;
import astFileProcessor.astObjects.ASTService;


public class ASTServiceProcessor extends ASTClassProcessor {

	protected String type = "Service";
	
	public ASTServiceProcessor() {
	}

	protected void getDecorators(JSONArray modifiers, ASTClass processedClass) {
		Iterator<JSONObject> modifiersIterator = modifiers.iterator();
		JSONObject processedObject, expressionObject;
		ASTAnyObjectType decoratorObject;
		
		while (modifiersIterator.hasNext()) {
			processedObject = (JSONObject) modifiersIterator.next();
	
			expressionObject = (JSONObject) processedObject.get("expression");
			if (expressionObject == null) {
				System.out.println("Expression not found. Probably object is not decorator! PROBABLY EMPTY RECORD, SKIPPING!");
				continue;
			}
			decoratorObject = ASTClassDecoratorProcessor.getDecorators(expressionObject, processedClass);
		}
	}
	
	protected String getType() { return this.type; }
	
	protected ASTClass createClass(String name) { return new ASTService(name); } 
	
	public static String getGeneralType() { return "Service"; }
}

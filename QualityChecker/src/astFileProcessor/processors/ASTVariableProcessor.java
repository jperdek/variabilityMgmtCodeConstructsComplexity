package astFileProcessor.processors;

import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import astFileProcessor.astObjects.ASTAnyObjectType;
import astFileProcessor.astObjects.ASTArrayDataType;
import astFileProcessor.astObjects.ASTDataType;
import astFileProcessor.astObjects.ASTGenericDataType;

public class ASTVariableProcessor {

	public ASTVariableProcessor() {}
	
	public static ASTGenericDataType processVariable(JSONObject processedObject) {
		return ASTVariableProcessor.processVariable(processedObject, null);
	}
	
	public static ASTGenericDataType processVariable(JSONObject processedObject, String name) {
		Object initialization = processedObject.get("initializer");
		boolean isArray, isAnyObject;
		JSONObject initializedObject, nameObject;
		Object specificNameObject;
		ASTGenericDataType createdVariable = null;

		String type = "any";
		String value;
		
		if (name == null) {
			nameObject =((JSONObject) processedObject.get("name"));
			specificNameObject = nameObject.get("escapedText");
			if (specificNameObject == null) {
				name = (String) nameObject.get("text");
			} else {
				name = (String) specificNameObject;
			}
		}

		if (initialization != null) {
			initializedObject = (JSONObject) initialization;
			isArray = (processedObject.get("elements") != null); //to process ARRAYs
			isAnyObject = (initializedObject.get("properties") != null); //to process JSONs
			if (isArray) { //PROCESSING JSON
				createdVariable = ASTVariableProcessor.processArrayVariable(processedObject, name);
				type = ((ASTArrayDataType) createdVariable).getType();
			} else if (isAnyObject) { // PROCESSING JSON
				createdVariable = ASTVariableProcessor.processAnyVariable(initializedObject, name);
				// type is any all the time
			}	else {
			
				if (type.equals("any")) {
					type = ASTVariableProcessor.evaluateType(initializedObject);
				}
				value = ASTVariableProcessor.getVariableValue(initializedObject);
				createdVariable = new ASTDataType(name, value, type);
			}
		} else {
			createdVariable = new ASTDataType(name, type);
		}
		return createdVariable;
	}

	private static ASTGenericDataType processArrayVariable(JSONObject processedObject, String arrayObjectName) {
		ASTGenericDataType createdVariable = new ASTArrayDataType(arrayObjectName);
		Iterator<JSONObject> variablesIterator = ((JSONArray) processedObject.get("elements")).iterator();
		JSONObject processedVariableIdentifier;
		ASTGenericDataType subVariable;
		String type = "any";
		
		while(variablesIterator.hasNext()) {
			processedVariableIdentifier = variablesIterator.next();
			if (type.equals("any")) {
				type = ASTVariableProcessor.evaluateType(processedVariableIdentifier);
			}
			subVariable = ASTVariableProcessor.processVariable(processedObject);
			((ASTArrayDataType) createdVariable).addElement(subVariable);
		}
		((ASTArrayDataType) createdVariable).setType(type);
		return createdVariable;
	}

	private static ASTGenericDataType processAnyVariable(JSONObject processedObject, String arrayObjectName) {
		ASTAnyObjectType createdVariable = new ASTAnyObjectType(arrayObjectName);
		Iterator<JSONObject> objectPropertiesIterator = ((JSONArray) processedObject.get("properties")).iterator();
		JSONObject processedVariableIdentifier;
		ASTGenericDataType subVariable;
		String type = "any";
		
		while(objectPropertiesIterator.hasNext()) {
			processedVariableIdentifier = objectPropertiesIterator.next();
			if (type.equals("any")) {
				type = ASTVariableProcessor.evaluateType(processedVariableIdentifier);
			}

			subVariable = ASTVariableProcessor.processVariable(processedVariableIdentifier, null);
			((ASTAnyObjectType) createdVariable).addObject(subVariable);
			((ASTAnyObjectType) createdVariable).setType(type);
		}
		return createdVariable;
	}
	
	private static String getVariableValue(JSONObject processedVariableIdentifier) {
		if (processedVariableIdentifier == null) { return null; }
		Object valueElement = processedVariableIdentifier.get("text");
		if (valueElement != null) {
			return (String) valueElement;
		} else {
			return (String) processedVariableIdentifier.get("escapedText");
		}
	}

	private static String evaluateType(JSONObject processedObject) {
		if (processedObject.get("expression") == null) {
			if (processedObject.get("numericLiteralFlags") != null) {
				return "number";
			} else if (processedObject.get("hasExtendedUnicodeEscape") != null) {
				return "string";
			}
		}
		return "any";
	}
}

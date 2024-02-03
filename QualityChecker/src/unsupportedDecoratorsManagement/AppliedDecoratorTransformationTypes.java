package unsupportedDecoratorsManagement;

import java.io.IOException;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import astFileProcessor.annotationManagment.astConstructs.NotFoundBlockElementToWrap;
import astFileProcessor.astObjects.ASTGenericDecorator;
import astFileProcessor.processors.DecoratorManipulationSettings;
import unsupportedDecoratorsManagement.entities.IllegalImportNameSpecifiedException;


public interface AppliedDecoratorTransformationTypes {

	public static final String OPTIONAL_IMPORT = "IMPORT";
	public static final String ALTERNATIVE_CODE = "NOT";
	public static final String INCLUDE_FILE_DO_NOTHING = "NOTHING";
	public static final String OPTIONAL_ARGUMENT = "ARG";
	
	public static enum DecoratorTransformationType {
		OPTIONAL_IMPORT,
		ALTERNATIVE_CODE,
		INCLUDE_FILE_DO_NOTHING,
		OPTIONAL_ARGUMENT,
		COMMENTED_LINE,
		LEGACY
	};
	
	public ASTGenericDecorator processSelf(JSONObject treeRoot, JSONObject processedDecorator, 
			Map<JSONObject, JSONObject> parentMap, String extractedString, 
			DecoratorManipulationSettings decoratorManipulationSettings) throws IOException, InterruptedException, IllegalImportNameSpecifiedException, NotFoundBlockElementToWrap;
	
	public static String getFrameworkDecoratorName(JSONObject processedDecorator) {
		JSONObject decoratorInnerPart = ((JSONObject) ((JSONObject) 
				processedDecorator.get("expression")).get("expression"));
		JSONObject decoratorNamePart = (JSONObject) decoratorInnerPart.get("name");
		String frameworkDecoratorName = ((String) decoratorInnerPart.get("escapedText"));
		
		if (frameworkDecoratorName == null) {
			frameworkDecoratorName = ((String) decoratorNamePart.get("escapedText"));
		}
		//System.out.println("FOUND DECORATOR: " + frameworkDecoratorName);
		return frameworkDecoratorName;
	}
	
	public static JSONObject getAffectedCode(JSONObject decoratorParent, boolean isIllegalDecorator) {
		if (isIllegalDecorator) {
			return AppliedDecoratorTransformationTypes.getRedundantCodeByDecorator(decoratorParent);
		}
		return AppliedDecoratorTransformationTypes.getCoveredCodeByDecorator(decoratorParent);
	}

	public static JSONObject getRedundantCodeByDecorator(JSONObject decoratorParent) {
		JSONParser parser = new JSONParser();
		JSONObject redundantCode;
		try {
			redundantCode = (JSONObject) parser.parse(decoratorParent.toString());
			if (redundantCode.get("illegalDecorators") != null) {
				redundantCode.remove("illegalDecorators");
			}
			// REMOVE ONLY CORRECT ONE - TO-DO
			//int foundOnIndex = -1;
			//JSONArray constructorMembers = (JSONArray) constructorObject.get("members");
			//for(int objectIndex = 0; objectIndex < constructorMembers.size(); objectIndex++) {
			//	if (constructorMembers.get(objectIndex) == annotatedParentByDecorator) {
			//		foundOnIndex = objectIndex;
			//		break;
			//	}
			//}
			//
			//
		} catch (ParseException e) {
			e.printStackTrace();
			redundantCode = null;
		}
		return redundantCode;
	}
	
	public static JSONObject getAndReplaceAffectedClassMember(JSONObject decoratorParent, JSONObject classObject, JSONObject objectToPlace) {
		JSONParser parser = new JSONParser();
		JSONObject redundantCode;
		
		try {
			redundantCode = (JSONObject) parser.parse(decoratorParent.toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			redundantCode = null;
		}
		
		
		JSONArray classMembers = (JSONArray) classObject.get("members");
		//try {
		for (int index = 0; index < classMembers.size(); index++) {
			if (((JSONObject) classMembers.get(index)) == decoratorParent) {
				classMembers.add(objectToPlace);
				classMembers.remove(decoratorParent);
				break;
			}
		}
		return redundantCode;
	}
	
	public static JSONObject getAndReplaceAffectedClassMembers(JSONObject decoratorParent, JSONObject classObject, JSONArray objectsToPlace) {
		JSONParser parser = new JSONParser();
		JSONObject redundantCode;
		
		try {
			redundantCode = (JSONObject) parser.parse(decoratorParent.toString());
		} catch (ParseException e) {
			e.printStackTrace();
			redundantCode = null;
		}
		
		JSONArray classMembers = (JSONArray) classObject.get("members");

		for (int index = 0; index < classMembers.size(); index++) {
			if (((JSONObject) classMembers.get(index)).toString().equals(decoratorParent.toString())) {
				classMembers.remove(index);
				classMembers.addAll(index, objectsToPlace);
				break;
			}
		}
		return redundantCode;
	}
	
	public static JSONObject getCoveredCodeByDecorator(JSONObject decoratorParent) {
		JSONParser parser = new JSONParser();
		JSONObject redundantCode;
		try {
			redundantCode = (JSONObject) parser.parse(decoratorParent.toString());
			if (redundantCode.get("illegalDecorators") != null) {
				redundantCode.remove("illegalDecorators");
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			redundantCode = null;
		}
		return redundantCode;
	}
}

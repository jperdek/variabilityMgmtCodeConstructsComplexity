package astFileProcessor.annotationManagment.astConstructs;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class NearestAnnotatedVariablesMerger {

	private static int index = 1;
	
	/**
	 * Prepares alternative declaration which can be compiled and evaluated
	 * 
	 * @param alternativeCodeAST
	 */
	public static void transformAlternativeNonConflictingDeclaration(JSONObject alternativeCodeAST, JSONObject decoratorParent) {
		JSONObject declarationListAlternative = (JSONObject) alternativeCodeAST.get("declarationList");
		JSONObject declarationListNativeCode = (JSONObject) decoratorParent.get("declarationList");
		// CODE IS DECLARATION
		if (declarationListAlternative != null && declarationListNativeCode != null) {
			NearestAnnotatedVariablesMerger.prepareAndInsertAlternativeNonConflictingDeclaration(
					declarationListAlternative, declarationListNativeCode);
		}
	}

	public static void transformAlternativeNonConflictingDeclarations(JSONObject alternativeCode, JSONObject decoratorParent) {
		JSONArray alternativeStatements = (JSONArray) alternativeCode.get("statements");
		for (Object alternativeStatement: alternativeStatements) {	
			NearestAnnotatedVariablesMerger.transformAlternativeNonConflictingDeclaration((JSONObject) alternativeStatement, decoratorParent);
		}
	}
	
	private static void appendDeclarationToAssociatedCodePart(JSONObject declarationAlternative, JSONObject declarationListNativeCode) {
		((JSONArray) declarationListNativeCode.get("declarations")).add(declarationAlternative);
	}
	
	private static void prepareAndInsertAlternativeNonConflictingDeclaration(JSONObject declarationList, JSONObject declarationListNativeCode) {
		JSONArray declarationsArray;
		JSONObject declaration;
		String declarationName;
	
		declarationsArray = (JSONArray) declarationList.get("declarations");
		for(Object declarationObject: declarationsArray) {
			declaration = (JSONObject) declarationObject;
			declarationName = (String) ((JSONObject) declaration.get("name")).get("escapedText");
			declarationName = declarationName + "Alter" + NearestAnnotatedVariablesMerger.index;
			NearestAnnotatedVariablesMerger.index = NearestAnnotatedVariablesMerger.index + 1;
			//System.out.println(NearestAnnotatedVariablesMerger.index);
			//((JSONObject) ((JSONObject) declaration).get("name")).put("escapedText", declarationName);
			//NearestAnnotatedVariablesMerger.appendDeclarationToAssociatedCodePart(declaration, declarationListNativeCode);
			((JSONObject) ((JSONObject) declaration).get("name")).put("escapedText", declarationName);
		}
	}
	
	private static void prepareAlternativeNonConflictingDeclaration(JSONObject declarationList, JSONObject declarationListNativeCode) {
		JSONArray declarationsArray;
		JSONObject declaration;
		String declarationName;
	
		declarationsArray = (JSONArray) declarationList.get("declarations");
		for(Object declarationObject: declarationsArray) {
			declaration = (JSONObject) declarationObject;
			declarationName = (String) ((JSONObject) declaration.get("name")).get("escapedText");
			declarationName = declarationName + "Alter" + NearestAnnotatedVariablesMerger.index;
			NearestAnnotatedVariablesMerger.index = NearestAnnotatedVariablesMerger.index + 1;
			((JSONObject) ((JSONObject) declaration).get("name")).put("escapedText", declarationName);
		}
	}
}

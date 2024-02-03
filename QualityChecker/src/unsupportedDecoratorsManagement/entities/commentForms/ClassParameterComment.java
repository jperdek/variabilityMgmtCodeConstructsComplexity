package unsupportedDecoratorsManagement.entities.commentForms;

import java.io.IOException;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import astFileProcessor.astObjects.ASTGenericDecorator;
import astFileProcessor.processors.DecoratorManipulationSettings;
import codeConstructsEvaluation.transformation.ASTConverterClient;
import unsupportedDecoratorsManagement.AppliedDecoratorTransformationTypes;


public class ClassParameterComment {

	public ClassParameterComment() {
	}
	
	public static ASTGenericDecorator processParameterDecoratorWithReplacementFunctionality(String decoratorName, JSONObject treeRoot,
			JSONObject processedDecorator, JSONObject decoratorParent, JSONObject potentialClass, Map<JSONObject, JSONObject> parentMap, 
			String replacementParameter, DecoratorManipulationSettings  decoratorManipulationSettings) throws IOException, InterruptedException {
		if (replacementParameter.endsWith(",")) {
			replacementParameter = replacementParameter.substring(0, replacementParameter.length() - 1); 
		}
		JSONObject potentialClassReal = (JSONObject) parentMap.get(potentialClass);
		if (potentialClassReal == null) {
			ClassParameterComment.replaceParameterValueAndType(decoratorParent, replacementParameter);
			if (decoratorManipulationSettings.shouldAllBeRemoved() || decoratorManipulationSettings.shouldOnlyIllegalBeRemoved()) {
				((JSONArray) decoratorParent.get("modifiers")).remove(processedDecorator);
			} else if (decoratorManipulationSettings.shouldRemoveConfigurationExpressions()) {
				//expressionAST = storedExpressionAST; //duplicate code
				((JSONArray) ((JSONObject) processedDecorator.get("expression")).get("arguments")).remove(0);
			}
			return null;
		}
		//this is usually not reached
		return ClassParameterComment.processClassAttributeWithComment(decoratorName, replacementParameter, treeRoot, 
				processedDecorator, potentialClass, potentialClassReal, decoratorManipulationSettings);
	}
	
	
	public static void replaceParameterValueAndType(JSONObject potentialClassMember, String extractedString) {
		extractedString = extractedString.replace("private", "").replace("protected", "").replace("public", "").replace(",", "").strip();
		String parameterParts[] = extractedString.split(":");
		String parameterName = parameterParts[0];
		((JSONObject) potentialClassMember.get("name")).put("escapedText", parameterName);
		
		JSONObject parameterType;
		JSONObject typeName;
		String typeNameString;
		if (parameterParts.length > 1) {
			typeNameString = parameterParts[1];
			if (!potentialClassMember.containsKey("type")) {
				parameterType = new JSONObject();
				typeName = new JSONObject();
				parameterType.put("typeName", typeName);
				potentialClassMember.put("type", parameterType);
			} else {
				parameterType = (JSONObject) potentialClassMember.get("type");
				typeName = (JSONObject) parameterType.get("typeName");
			}
			typeName.put("escapedText", typeNameString);
		}
	}
	
	public static ASTGenericDecorator processClassAttributeWithComment(
			String decoratorName, String extractedString, JSONObject treeRoot, 
			JSONObject processedDecorator, JSONObject decoratorParent, JSONObject potentialClass, 
			DecoratorManipulationSettings decoratorManipulationSettings) throws IOException, InterruptedException {
		String affectedClassName;
		JSONObject coveredCodeAST, classRecord;
		JSONArray coveredCodeMembers, coveredCodeModifiers;
		JSONObject coveredCode, redundantCode;
		JSONObject codeParameter;

		if (!decoratorManipulationSettings.shouldOnlyIllegalBeRemoved()) {
			extractedString = extractedString.replaceFirst("private", "");
		}
		extractedString = "class AAAA { constructor(" + extractedString + ") {} }"; //TO DO RECREATE FROM AST AND MERGE CORRECTLY!!!!
		coveredCodeAST = ASTConverterClient.convertFromCodeToASTJSON(extractedString);
		affectedClassName = (String) ((JSONObject) potentialClass.get("name")).get("escapedText");
	
		coveredCodeMembers = ASTConverterClient.getMembersFromASTFile(coveredCodeAST, "AAAA");
		coveredCode = (JSONObject) coveredCodeMembers.get(0);
		if (!decoratorManipulationSettings.shouldOnlyIllegalBeRemoved()) {
			codeParameter = (JSONObject) ((JSONArray) coveredCode.get("parameters")).get(0);
			coveredCodeModifiers = (JSONArray) codeParameter.get("modifiers");
			
			if (coveredCodeModifiers == null) {
				codeParameter.put("modifiers", new JSONArray());
			}
			((JSONArray) codeParameter.get("modifiers")).add(processedDecorator);
		}
	
		classRecord = ClassFinderHelper.getClassRecord(treeRoot, affectedClassName);
		potentialClass = classRecord; //should be same, but as objects are not
		redundantCode = AppliedDecoratorTransformationTypes.getAndReplaceAffectedClassMembers(
				decoratorParent, classRecord, coveredCodeMembers);
		
		return new ASTGenericDecorator(decoratorName, processedDecorator, coveredCode);
	}
}

package unsupportedDecoratorsManagement.entities.commentForms;

import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import astFileProcessor.annotationManagment.astConstructs.AnnotationWrapperIntoBlock;
import astFileProcessor.annotationManagment.astConstructs.NotFoundBlockElementToWrap;
import astFileProcessor.astObjects.ASTGenericDecorator;
import astFileProcessor.processors.DecoratorManipulationSettings;
import codeConstructsEvaluation.transformation.ASTConverterClient;
import unsupportedDecoratorsManagement.AppliedDecoratorTransformationTypes;


public class ClassAttributeComment {

	public ClassAttributeComment() {
	}
	
	public static ASTGenericDecorator processAndSelectTransformationFormForClassAttributeWithComment(
			String decoratorName, String extractedString, JSONObject treeRoot, JSONObject expressionAST,
			JSONObject processedDecorator, JSONObject decoratorParent, JSONObject potentialClass, 
			DecoratorManipulationSettings decoratorManipulationSettings) throws IOException, InterruptedException, NotFoundBlockElementToWrap {
		if (decoratorManipulationSettings.areWrappersMorePreferable()) {
			return ClassAttributeComment.wrapClassAttributeWithComment(decoratorName, extractedString, treeRoot,
					expressionAST, processedDecorator, decoratorParent, potentialClass, decoratorManipulationSettings);
		} 
		return ClassAttributeComment.processClassAttributeWithComment(decoratorName, extractedString, treeRoot,
				processedDecorator, decoratorParent, potentialClass, decoratorManipulationSettings);
	}

	private static JSONObject makeDecoratorDuplicate(JSONObject processedDecorator) {
		JSONParser parser = new JSONParser();
		try {
			return  (JSONObject) parser.parse(processedDecorator.toString());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static void optionallyReturnDecoratorUnderCommentedCode(String illegalDecoratorName, 
			JSONObject processedDecorator, JSONObject coveredCode, DecoratorManipulationSettings decoratorManipulationSettings) {
		JSONObject decoratorDuplicate;
		if (decoratorManipulationSettings.canBeProcessed(illegalDecoratorName, true, false) && 
				(!decoratorManipulationSettings.shouldAllBeRemoved() && !decoratorManipulationSettings.shouldOnlyIllegalBeRemoved())) {
			if (!coveredCode.containsKey("modifiers")) {
				coveredCode.put("modifiers", new JSONArray());
			}
			decoratorDuplicate = ClassAttributeComment.makeDecoratorDuplicate(processedDecorator);
			((JSONArray) coveredCode.get("modifiers")).add(0, decoratorDuplicate);
			if (decoratorManipulationSettings.shouldRemoveConfigurationExpressions()) {
				((JSONArray) ((JSONObject) ((JSONObject) ((JSONArray) coveredCode.get(
						"modifiers")).get(0)).get("expression")).get("arguments")).remove(0);
			}
		}
	}
	
	public static ASTGenericDecorator processClassAttributeWithComment(
			String decoratorName, String extractedString, JSONObject treeRoot, 
			JSONObject processedDecorator, JSONObject decoratorParent, JSONObject potentialClass, 
			DecoratorManipulationSettings decoratorManipulationSettings) throws IOException, InterruptedException {
		String affectedClassName;
		JSONObject coveredCodeAST, classRecord;
		JSONArray coveredCodeStatements;
		JSONObject coveredCode, redundantCode;
	
		extractedString = "class AAAA { " + extractedString + " }"; //TO DO RECREATE FROM AST AND MERGE CORRECTLY!!!!
		coveredCodeAST = ASTConverterClient.convertFromCodeToASTJSON(extractedString);
		affectedClassName = (String) ((JSONObject) potentialClass.get("name")).get("escapedText");
		coveredCodeStatements = ASTConverterClient.getMembersFromASTFile(coveredCodeAST, "AAAA");
		coveredCode = (JSONObject) coveredCodeStatements.get(0);
		
		ClassAttributeComment.optionallyReturnDecoratorUnderCommentedCode(
				decoratorName, processedDecorator, coveredCode, decoratorManipulationSettings);
		
		classRecord = ClassFinderHelper.getClassRecord(treeRoot, affectedClassName);
		potentialClass = classRecord; //should be same, but as objects are not
		redundantCode = AppliedDecoratorTransformationTypes.getAndReplaceAffectedClassMembers(
				decoratorParent, classRecord, coveredCodeStatements);
	
		return new ASTGenericDecorator(decoratorName, processedDecorator, coveredCode);
	}
	
	public static ASTGenericDecorator wrapClassAttributeWithComment(
			String decoratorName, String extractedString, JSONObject treeRoot, JSONObject expressionAST, 
			JSONObject processedDecorator, JSONObject decoratorParent, 
			JSONObject potentialClass, DecoratorManipulationSettings decoratorManipulationSettings) throws IOException, InterruptedException, NotFoundBlockElementToWrap {
		boolean removePrevious = decoratorManipulationSettings.getRemoveHelperUnwantedCode();
		String affectedClassName;
		JSONObject coveredCodeAST, classRecord;
		JSONArray coveredCodeStatements;
		JSONObject coveredCode, redundantCode;

		extractedString = "class AAAA { " + extractedString + " }"; //TO DO RECREATE FROM AST AND MERGE CORRECTLY!!!!
		coveredCodeAST = ASTConverterClient.convertFromCodeToASTJSON(extractedString);
		affectedClassName = (String) ((JSONObject) potentialClass.get("name")).get("escapedText");
		coveredCodeStatements = ASTConverterClient.getMembersFromASTFile(coveredCodeAST, "AAAA");
		coveredCode = (JSONObject) coveredCodeStatements.get(0);
		
		classRecord = ClassFinderHelper.getClassRecord(treeRoot, affectedClassName);
		potentialClass = classRecord; //should be same, but as objects are not

		ClassAttributeComment.optionallyReturnDecoratorUnderCommentedCode(
				decoratorName, processedDecorator, coveredCode, decoratorManipulationSettings);
		//JSONObject expressionWrapper = new JSONObject();
		//JSONArray codeArray = new JSONArray();
		//codeArray.add(expressionAST);
		//expressionWrapper.put("statements", codeArray);
		//expressionAST = ASTConverterClient.convertFromASTToCodeJSON(expressionWrapper.toString());
		
		if (decoratorManipulationSettings.canBeProcessed(decoratorName, false, false) && 
				(decoratorManipulationSettings.shouldAllBeRemoved() || decoratorManipulationSettings.shouldOnlyIllegalBeRemoved())) {
			if (potentialClass != null && decoratorManipulationSettings.getRemoveHelperUnwantedCode()) {
				((JSONArray) potentialClass.get("statements")).remove(decoratorParent);
			}
			return new ASTGenericDecorator(decoratorName, processedDecorator, coveredCode);
		}
		
		AnnotationWrapperIntoBlock.constructAnnotationsWrapperIntoBlockNewCodeStringSearch(expressionAST, coveredCodeStatements, 
					decoratorParent, classRecord, removePrevious, 
					AnnotationWrapperIntoBlock.AstPlaces.MEMBERS, 
					AnnotationWrapperIntoBlock.DeclarationTypes.WITHOUT, decoratorManipulationSettings); //ONLY STATIC and WITHOUT ARE ALLOWED (CONST NOT WORK)

		return new ASTGenericDecorator(decoratorName, processedDecorator, coveredCode);
	}
}

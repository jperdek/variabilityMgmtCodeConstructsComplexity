package unsupportedDecoratorsManagement.entities;

import java.io.IOException;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import astFileProcessor.annotationManagment.astConstructs.AnnotationWrapperIntoBlock;
import astFileProcessor.annotationManagment.astConstructs.NotFoundBlockElementToWrap;
import astFileProcessor.astObjects.ASTGenericDecorator;
import astFileProcessor.astObjects.ASTIllegalDecorator;
import astFileProcessor.processors.DecoratorManipulationSettings;
import codeConstructsEvaluation.transformation.ASTConverterClient;
import unsupportedDecoratorsManagement.AppliedDecoratorTransformationTypes;
import unsupportedDecoratorsManagement.entities.commentForms.ClassAttributeComment;
import unsupportedDecoratorsManagement.entities.commentForms.ClassParameterComment;


public class DecoratorTransformationComment  implements AppliedDecoratorTransformationTypes{
	
	private ASTGenericDecorator processFullAnnotationsRemoval(String illegalDecoratorName, JSONObject processedDecorator, 
			JSONObject coveredCode, JSONObject redundantCode, 
			JSONObject decoratorParent, JSONObject potentialClass, 
			JSONArray coveredCodeStatements, DecoratorManipulationSettings decoratorManipulationSettings) {
		// inserts commented code statements into code
		if (decoratorManipulationSettings.shouldIncludePositiveVariabilityCommentedCode()) {
			JSONArray codeSequence = (JSONArray) potentialClass.get("statements");
			int indexOfElementInCodeSequence = codeSequence.indexOf(decoratorParent);
			codeSequence.addAll(indexOfElementInCodeSequence + 1, coveredCodeStatements);
		}
		// removes configuration expression if configured
		if (potentialClass != null && decoratorManipulationSettings.getRemoveHelperUnwantedCode()) {
			((JSONArray) potentialClass.get("statements")).remove(decoratorParent);
		}
		return new ASTIllegalDecorator(illegalDecoratorName, processedDecorator, coveredCode, redundantCode);
	}
	
	@Override
	public ASTGenericDecorator processSelf(JSONObject treeRoot, JSONObject processedDecorator, 
			Map<JSONObject, JSONObject> parentMap, String extractedString, DecoratorManipulationSettings decoratorManipulationSettings) 
					throws IOException, InterruptedException, NotFoundBlockElementToWrap {
		String illegalDecoratorName = AppliedDecoratorTransformationTypes.getFrameworkDecoratorName(processedDecorator);
		JSONObject coveredCode;
		JSONArray coveredCodeStatements;
		JSONObject decoratorParent = parentMap.get(processedDecorator);
		JSONObject potentialClass = (JSONObject) parentMap.get(decoratorParent);
		JSONObject redundantCode;
		JSONObject expressionAST = (JSONObject) ((JSONArray) ((JSONObject)
				processedDecorator.get("expression")).get("arguments")).get(0);
		
		if (extractedString.endsWith(",") || (potentialClass != null && potentialClass.get("parameters") != null)) { 
			return ClassParameterComment.processParameterDecoratorWithReplacementFunctionality(illegalDecoratorName, treeRoot, 
					processedDecorator, decoratorParent, potentialClass, parentMap, extractedString, decoratorManipulationSettings);
		}
		
		if (potentialClass != null && potentialClass.get("members") != null) {
			return ClassAttributeComment.processAndSelectTransformationFormForClassAttributeWithComment(illegalDecoratorName, extractedString,
					treeRoot, expressionAST, processedDecorator, decoratorParent, potentialClass, decoratorManipulationSettings);
		} 
	
		coveredCode = ASTConverterClient.convertFromCodeToASTJSON(extractedString);
		coveredCodeStatements = ASTConverterClient.getStatementsFromASTFile(coveredCode);
		redundantCode = AppliedDecoratorTransformationTypes.getAffectedCode(decoratorParent, true);
		
		if (decoratorManipulationSettings.canBeProcessed(illegalDecoratorName, false, false) && 
				(decoratorManipulationSettings.shouldAllBeRemoved() || decoratorManipulationSettings.shouldOnlyIllegalBeRemoved())) {
			return this.processFullAnnotationsRemoval(illegalDecoratorName, processedDecorator, coveredCode, redundantCode, decoratorParent, 
					potentialClass, coveredCodeStatements, decoratorManipulationSettings);
		}
		
		boolean removePrevious = decoratorManipulationSettings.getRemoveHelperUnwantedCode();
		AnnotationWrapperIntoBlock.constructAnnotationsWrapperIntoBlockNewCode(expressionAST, coveredCodeStatements, 
					decoratorParent, (JSONObject) parentMap.get(decoratorParent), removePrevious, 
					AnnotationWrapperIntoBlock.AstPlaces.STATEMENTS, AnnotationWrapperIntoBlock.DeclarationTypes.VAR, decoratorManipulationSettings);
		
		return new ASTIllegalDecorator(illegalDecoratorName, processedDecorator, coveredCode, redundantCode);
	}
}

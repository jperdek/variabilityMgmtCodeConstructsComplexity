package unsupportedDecoratorsManagement.entities;

import java.io.IOException;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import astFileProcessor.annotationManagment.astConstructs.AnnotationAlternativeWrapperIntoBlock;
import astFileProcessor.annotationManagment.astConstructs.AnnotationWrapperIntoBlock;
import astFileProcessor.annotationManagment.astConstructs.NearestAnnotatedVariablesMerger;
import astFileProcessor.annotationManagment.astConstructs.NotFoundBlockElementToWrap;
import astFileProcessor.astObjects.ASTGenericDecorator;
import astFileProcessor.astObjects.ASTIllegalDecorator;
import astFileProcessor.processors.DecoratorManipulationSettings;
import codeConstructsEvaluation.transformation.ASTConverterClient;
import unsupportedDecoratorsManagement.AppliedDecoratorTransformationTypes;


/**
 * Extracts alternative line
 * 
 * @author perde
 *
 */
public class DecoratorTransformationAlternative implements AppliedDecoratorTransformationTypes {

	
	private JSONObject constructExpressionInAnnotatedManner() {
		return null;
	}
	
	private ASTGenericDecorator processFullAnnotationsRemoval(String illegalDecoratorName, JSONObject processedDecorator, 
			JSONObject coveredCode, JSONObject parentRoot, JSONObject decoratorParent, JSONObject alternativeCode, String alternativeCodeString) {
		NearestAnnotatedVariablesMerger.transformAlternativeNonConflictingDeclarations(alternativeCode, decoratorParent);
		JSONArray codeSequence = (JSONArray) parentRoot.get("statements");
		int indexOfElementInCodeSequence = codeSequence.indexOf(decoratorParent);
		codeSequence.add(indexOfElementInCodeSequence + 1, alternativeCode); 
		return new ASTIllegalDecorator(illegalDecoratorName, processedDecorator, 
				coveredCode, alternativeCode, alternativeCodeString);
	}
	
	@Override
	public ASTGenericDecorator processSelf(JSONObject treeRoot, JSONObject processedDecorator,
			Map<JSONObject, JSONObject> parentMap, String alternativeCodeString, 
			DecoratorManipulationSettings decoratorManipulationSettings) throws IOException, InterruptedException, NotFoundBlockElementToWrap {
		String illegalDecoratorName = AppliedDecoratorTransformationTypes.getFrameworkDecoratorName(processedDecorator);
		JSONObject decoratorParent = (JSONObject) parentMap.get(processedDecorator);
		JSONObject coveredCode = AppliedDecoratorTransformationTypes.getAffectedCode(decoratorParent, true);
		JSONObject alternativeCode = ASTConverterClient.convertFromCodeToASTJSON(alternativeCodeString);
		JSONObject expressionAST = (JSONObject) ((JSONArray) ((JSONObject)
				processedDecorator.get("expression")).get("arguments")).get(0);
		alternativeCode = (JSONObject) alternativeCode.get("ast");
	
		if (false) {
			NearestAnnotatedVariablesMerger.transformAlternativeNonConflictingDeclarations(alternativeCode, decoratorParent);
		} else {
			JSONObject parentRoot = (JSONObject) parentMap.get(decoratorParent);
			if (parentRoot == null) { parentRoot = treeRoot; }

			if (decoratorManipulationSettings.canBeProcessed(illegalDecoratorName, true, false) && 
					(decoratorManipulationSettings.shouldAllBeRemoved() || decoratorManipulationSettings.shouldOnlyIllegalBeRemoved())) {
				return this.processFullAnnotationsRemoval(illegalDecoratorName, processedDecorator, coveredCode, parentRoot,
						decoratorParent, alternativeCode, alternativeCodeString);
			}
			
			AnnotationAlternativeWrapperIntoBlock.constructAnnotationsWrapperIntoAlternativeBlock(
					expressionAST, alternativeCode, decoratorParent, parentRoot, 
					AnnotationWrapperIntoBlock.DeclarationTypes.VAR, decoratorManipulationSettings);
	
		}
		
		if (parentMap.get(decoratorParent) != null && decoratorManipulationSettings.getRemoveHelperUnwantedCode()) {
			//((JSONArray) parentMap.get(decoratorParent).get("statements")).remove(decoratorParent);
		}
		return new ASTIllegalDecorator(illegalDecoratorName, processedDecorator, 
				coveredCode, alternativeCode, alternativeCodeString);
	}
}

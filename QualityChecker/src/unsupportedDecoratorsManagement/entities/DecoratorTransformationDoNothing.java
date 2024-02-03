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
import unsupportedDecoratorsManagement.AppliedDecoratorTransformationTypes;


/**
 * Only to let parser to copy annotated file if other annotations fail - NOTHING FUNCTIONAL IS COVERED BY THIS DECORATOR
 * 
 * @author perde
 *
 */
public class DecoratorTransformationDoNothing implements AppliedDecoratorTransformationTypes {

	@Override
	public ASTGenericDecorator processSelf(JSONObject treeRoot, JSONObject processedDecorator,
			Map<JSONObject, JSONObject> parentMap, String extractedString, DecoratorManipulationSettings decoratorManipulationSettings) throws NotFoundBlockElementToWrap, IOException, InterruptedException {
		boolean removePrevious = decoratorManipulationSettings.getRemoveHelperUnwantedCode();
		String illegalDecoratorName = AppliedDecoratorTransformationTypes.getFrameworkDecoratorName(processedDecorator);
		JSONObject decoratorParent = (JSONObject) parentMap.get(processedDecorator);
		JSONObject parentWithStatements = parentMap.get(decoratorParent);
		JSONObject redundantCode = AppliedDecoratorTransformationTypes.getAffectedCode(decoratorParent, true);
		JSONObject expressionAST = (JSONObject) ((JSONArray) ((JSONObject)
				processedDecorator.get("expression")).get("arguments")).get(0);

		if (decoratorManipulationSettings.canBeProcessed(illegalDecoratorName, true, false) && 
				(decoratorManipulationSettings.shouldAllBeRemoved() || decoratorManipulationSettings.shouldOnlyIllegalBeRemoved())) {
			if (parentWithStatements != null && decoratorManipulationSettings.getRemoveHelperUnwantedCode()) {
				((JSONArray) parentWithStatements.get("statements")).remove(decoratorParent);
			}
			return new ASTIllegalDecorator(illegalDecoratorName, processedDecorator, null, redundantCode);
		}
	
		AnnotationWrapperIntoBlock.constructAnnotationsWrapperIntoBlockNotRealNewCode(expressionAST,
				decoratorParent, parentWithStatements, removePrevious, 
				AnnotationWrapperIntoBlock.AstPlaces.STATEMENTS, 
				AnnotationWrapperIntoBlock.DeclarationTypes.VAR, decoratorManipulationSettings);
		if (parentWithStatements != null && decoratorManipulationSettings.getRemoveHelperUnwantedCode()) {
			((JSONArray) parentWithStatements.get("statements")).remove(decoratorParent);
		}
		return new ASTIllegalDecorator(illegalDecoratorName, processedDecorator, null, redundantCode);
	}
}

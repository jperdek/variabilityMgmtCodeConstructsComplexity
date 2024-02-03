package unsupportedDecoratorsManagement.entities;

import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import astFileProcessor.astObjects.ASTGenericDecorator;
import astFileProcessor.processors.DecoratorManipulationSettings;
import unsupportedDecoratorsManagement.AppliedDecoratorTransformationTypes;


/**
 * Extracts annotated argument - optionally should delete argument
 * 
 * @author perde
 *
 */
public class DecoratorTransformationArgument implements AppliedDecoratorTransformationTypes {

	@Override
	public ASTGenericDecorator processSelf(JSONObject treeRoot, JSONObject processedDecorator,
			Map<JSONObject, JSONObject> parentMap, String extractedString, DecoratorManipulationSettings decoratorManipulationSettings) {
		String frameworkDecoratorName = AppliedDecoratorTransformationTypes.getFrameworkDecoratorName(processedDecorator);
		JSONObject annotatedParentByDecorator = (JSONObject) parentMap.get(processedDecorator);
		JSONObject coveredCode = AppliedDecoratorTransformationTypes.getAffectedCode(annotatedParentByDecorator, false);
		JSONObject storedExpressionAST = (JSONObject) ((JSONArray) ((JSONObject)
				processedDecorator.get("expression")).get("arguments")).get(0);
		if (decoratorManipulationSettings.shouldRemoveConfigurationExpressions()) {
			//expressionAST = storedExpressionAST; //duplicate code
			((JSONArray) ((JSONObject) processedDecorator.get("expression")).get("arguments")).remove(0);
		}
		return new ASTGenericDecorator(frameworkDecoratorName, processedDecorator, coveredCode);
	}
}

package unsupportedDecoratorsManagement;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import astFileProcessor.annotationManagment.astConstructs.NotFoundBlockElementToWrap;
import astFileProcessor.astObjects.ASTGenericDecorator;
import astFileProcessor.processors.DecoratorManipulationSettings;
import unsupportedDecoratorsManagement.entities.IllegalImportNameSpecifiedException;


public class DecoratorsTransformationTypesProcessor {

	private int searchDecoratorsMethodArgumentIndex = 1;
	private Map<JSONObject, JSONObject> objectParent;
	
	public DecoratorsTransformationTypesProcessor() {
		this.objectParent = new HashMap<JSONObject, JSONObject>();
	}
	
	public DecoratorsTransformationTypesProcessor( Map<JSONObject, JSONObject> objectParent) {
		this.objectParent = objectParent;
	}
	
	public void setSearchDecoratorMethodArgumentIndex(int searchDecoratorsMethodArgumentIndex) {
		this.searchDecoratorsMethodArgumentIndex = searchDecoratorsMethodArgumentIndex;
	}

	public void getPositions(JSONObject astPart, JSONObject astParent) {
		String key;
		this.objectParent.put(astPart, astParent);

		Object entryValue;
		JSONObject entryJSONObject;
		for(Object entryKey: astPart.keySet()) {
			key = (String) entryKey;
			entryValue = astPart.get(key);
			if (entryValue instanceof JSONObject) {
				entryJSONObject = (JSONObject) entryValue;
				this.getPositions(entryJSONObject, astPart);
			} else if(entryValue instanceof JSONArray) {
				for (Object arrayPart: ((JSONArray) entryValue)) {
					entryJSONObject = (JSONObject) arrayPart;
					this.getPositions(entryJSONObject, astPart);
				}
			}
		}
	}

	public ASTGenericDecorator processDecoratorTransformation(JSONObject astRoot, JSONObject astDecoratorRoot, DecoratorManipulationSettings decoratorManipulationSettings) 
			  throws NonExistingDecoratorTransformationType, IOException, InterruptedException, IllegalImportNameSpecifiedException, NotFoundBlockElementToWrap {
		JSONObject decoratorExpression = (JSONObject) astDecoratorRoot.get("expression");
		if (decoratorExpression == null) {
			return null;
		}
		JSONArray usedMethodArguments;
		
		JSONObject annotationContentObject;
		ASTGenericDecorator transformedDecoratorEntity;
		String annotationContent;
		DecoratorTransformation decoratorTransformationObject;
		usedMethodArguments =(JSONArray) decoratorExpression.get("arguments");
		if (usedMethodArguments != null) {
			if(usedMethodArguments.size() > this.searchDecoratorsMethodArgumentIndex) {
				annotationContentObject = (JSONObject) usedMethodArguments.get(this.searchDecoratorsMethodArgumentIndex);
				annotationContent = (String) annotationContentObject.get("text");
				if (annotationContent == null) {
					decoratorTransformationObject = new DecoratorTransformation();
					transformedDecoratorEntity = decoratorTransformationObject.applyTransformation(
							astRoot, astDecoratorRoot, this.objectParent, decoratorManipulationSettings);
					
					return transformedDecoratorEntity;
				}
				decoratorTransformationObject = new DecoratorTransformation(annotationContent);
				transformedDecoratorEntity = decoratorTransformationObject.applyTransformation(
						astRoot, astDecoratorRoot, this.objectParent, decoratorManipulationSettings);
				
				return transformedDecoratorEntity;
			} else {
				decoratorTransformationObject = new DecoratorTransformation();
				transformedDecoratorEntity = decoratorTransformationObject.applyTransformation(
						astRoot, astDecoratorRoot, this.objectParent, decoratorManipulationSettings);
				
				return transformedDecoratorEntity;
			}
		} 
		return null;
	}
}

package astFileProcessor.processors;

import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import astFileProcessor.annotationManagment.AnnotationExtractor;
import astFileProcessor.astObjects.ASTAnyObjectType;
import astFileProcessor.astObjects.ASTClass;
import astFileProcessor.astObjects.ASTDecorator;
import astFileProcessor.astObjects.ASTGenericDataType;


//https://babeljs.io/docs/babel-plugin-proposal-decorators
public class ASTClassDecoratorProcessor extends ASTClassProcessor {

	protected String type = "Decorator";
	private DecoratorManipulationSettings decoratorsManipulationSettings;
	

	public ASTClassDecoratorProcessor() {
		super();
	}
	
	public void getClasses(JSONObject astRoot,
			DecoratorManipulationSettings decoratorsManipulationSettings) {
		this.decoratorsManipulationSettings = decoratorsManipulationSettings;
		super.getClasses(astRoot);
	}
	
	protected boolean isAllowedFor(String className) { 
		className = className.toLowerCase();
		if(className.contains(ASTComponentProcessor.getGeneralType().toLowerCase()) || 
				className.contains(ASTServiceProcessor.getGeneralType().toLowerCase())) {
			return true;
		}
		return false;
	}
	
	protected void getDecorators(JSONArray modifiers, ASTClass processedClass) {
		JSONObject processedObject, expressionObject;
		
		for(int index=0; index < modifiers.size(); index++) {
			processedObject = (JSONObject) modifiers.get(index);
			expressionObject = (JSONObject) processedObject.get("expression");
			if (expressionObject == null) {
				System.out.println("Expression not found. Probably object is not decorator! PROBABLY EMPTY RECORD, SKIPPING!");
				continue;
			}
			if (this.processDecorators(expressionObject, processedClass)) {
				if (this.decoratorsManipulationSettings.shouldRemoveProcessedDecoratorsOnly()) {
					modifiers.remove(processedObject);
					index--;
				}
			} else {
				if (this.decoratorsManipulationSettings.shouldRemoveUnprocessedDecoratorsOnly()) {
					modifiers.remove(processedObject);
					index--;
				}
			}
		}
	
	}
	
	public boolean processDecorators(JSONObject decoratorObject, ASTClass processedClass) {
		ASTAnyObjectType decoratorRoot = null;
		String decoratorName = (String) ((JSONObject) decoratorObject.get("expression")).get("escapedText");
		JSONObject decoratorNameObject;
		if (decoratorName == null) {
			decoratorNameObject = ((JSONObject) ((JSONObject) decoratorObject.get("expression")).get("name"));
			if (decoratorNameObject != null) {
				decoratorName = (String) decoratorNameObject.get("escapedText");
			}
		}
		//System.out.println("FOUND DECORATOR: " + decoratorName);
		if (decoratorObject.get("arguments") == null) {
			decoratorRoot = new ASTAnyObjectType(decoratorName);
			decoratorRoot.setType("DECORATOR");
		} else {
		
			Iterator<JSONObject> decoratorParameterObjectIterator = ((JSONArray) decoratorObject.get("arguments")).iterator();
			Iterator<JSONObject> decoratorPropertiesIterator;
			decoratorRoot = new ASTAnyObjectType(decoratorName);
			decoratorRoot.setType("DECORATOR");
			ASTGenericDataType newObject;
			JSONObject processedObject, processedDecoratorContent;
			JSONArray properties;
			String objectBaseName;
	
			while (decoratorParameterObjectIterator.hasNext()) {
				processedDecoratorContent = (JSONObject) decoratorParameterObjectIterator.next();
				properties = (JSONArray) processedDecoratorContent.get("properties");
				if (properties == null) {
					continue;
				}
				decoratorPropertiesIterator = properties.iterator();
				while (decoratorPropertiesIterator.hasNext()) {
					processedObject = decoratorPropertiesIterator.next();
	
					objectBaseName = (String) ((JSONObject) processedObject.get("name")).get("text");
	
					newObject = (ASTGenericDataType) ASTVariableProcessor.processVariable(processedObject, objectBaseName);
					decoratorRoot.addObject(newObject);
				}
			}
			processedClass.addDecorator(decoratorName, decoratorRoot);
		}
		return this.decoratorsManipulationSettings.canBeProcessed(decoratorName, false, true);
	}
	
	public static ASTAnyObjectType extractSelectedDecorators(JSONObject decoratorObject, 
			ASTClass processedClass, AnnotationExtractor annotationExtractor) {
		String decoratorName = (String) ((JSONObject) decoratorObject.get("expression")).get("escapedText");
		Iterator<JSONObject> decoratorParameterObjectIterator = ((JSONArray) decoratorObject.get("arguments")).iterator();
		Iterator<JSONObject> decoratorPropertiesIterator;
		ASTAnyObjectType decoratorRoot = new ASTAnyObjectType(decoratorName);
		decoratorRoot.setType("DECORATOR");
		ASTGenericDataType newObject;
		JSONObject processedObject, processedDecoratorContent;
		String objectBaseName;
		while (decoratorParameterObjectIterator.hasNext()) {
			processedDecoratorContent = (JSONObject) decoratorParameterObjectIterator.next();
			decoratorPropertiesIterator = ((JSONArray) processedDecoratorContent.get("properties")).iterator();
			while (decoratorPropertiesIterator.hasNext()) {
				processedObject = decoratorPropertiesIterator.next();
				
				objectBaseName = (String) ((JSONObject) processedObject.get("name")).get("text");
				newObject = (ASTGenericDataType) ASTVariableProcessor.processVariable(processedObject, objectBaseName);
				decoratorRoot.addObject(newObject);
			}
		}
		processedClass.addDecorator(decoratorName, decoratorRoot);
		return decoratorRoot;
	}
	
	/**
	 * Natively used for search only
	 * 
	 * @param decoratorObject
	 * @param processedClass
	 * @return
	 */
	public static ASTAnyObjectType getDecorators(JSONObject decoratorObject, ASTClass processedClass) {
		String decoratorName = (String) ((JSONObject) decoratorObject.get("expression")).get("escapedText");
		Iterator<JSONObject> decoratorParameterObjectIterator = ((JSONArray) decoratorObject.get("arguments")).iterator();
		Iterator<JSONObject> decoratorPropertiesIterator;
		ASTAnyObjectType decoratorRoot = new ASTAnyObjectType(decoratorName);
		decoratorRoot.setType("DECORATOR");
		ASTGenericDataType newObject;
		JSONObject processedObject, processedDecoratorContent;
		String objectBaseName;

		while (decoratorParameterObjectIterator.hasNext()) {
			processedDecoratorContent = (JSONObject) decoratorParameterObjectIterator.next();
			decoratorPropertiesIterator = ((JSONArray) processedDecoratorContent.get("properties")).iterator();
			while (decoratorPropertiesIterator.hasNext()) {
				processedObject = decoratorPropertiesIterator.next();

				objectBaseName = (String) ((JSONObject) processedObject.get("name")).get("text");
				newObject = (ASTGenericDataType) ASTVariableProcessor.processVariable(processedObject, objectBaseName);
				decoratorRoot.addObject(newObject);
			}
		}
		processedClass.addDecorator(decoratorName, decoratorRoot);
		return decoratorRoot;
	}
	
	protected String getType() { return this.type; }
	
	protected ASTClass createClass(String name) { return new ASTDecorator(name); } 
}

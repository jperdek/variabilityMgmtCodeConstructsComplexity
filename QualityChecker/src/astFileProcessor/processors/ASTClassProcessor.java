package astFileProcessor.processors;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import astFileProcessor.astObjects.ASTClass;
import astFileProcessor.astObjects.ASTGenericDataType;
import astFileProcessor.astObjects.ASTMethod;


public class ASTClassProcessor {
	
	protected String type = "Class";
	protected List<ASTClass> processedClasses = null;
	
	public ASTClassProcessor() {
		this.processedClasses = new ArrayList<ASTClass>();
	}
	
	protected String getType() { return this.type; }
	
	public void getClasses(JSONObject jsonObject) {
		Set<Map.Entry<String, Object>> entrySet = jsonObject.entrySet();
		Iterator<Map.Entry<String, Object>> iterator = entrySet.iterator();

		String keyName;
		ASTClass processedClass;
		Object entryValue;
		JSONObject dataObject,  arrayObject;
		JSONArray classMembers, classModifiers, dataArray;
		String className;
		Iterator<Object> dataArrayIterator;
		
		while(iterator.hasNext()) {
			Entry<String, Object> entry = iterator.next();
			keyName = entry.getKey().strip();
			entryValue = entry.getValue();
			if (!keyName.equals("members")) {
				if (entryValue instanceof JSONObject) {
					dataObject = (JSONObject) entryValue;
				} else if (entryValue instanceof JSONArray) {
					dataArray = (JSONArray) entryValue;
					dataArrayIterator = dataArray.iterator();
					while(dataArrayIterator.hasNext()) {
						arrayObject = (JSONObject) dataArrayIterator.next();
						this.getClasses(arrayObject);
					}
					continue;
				} else {
					continue;
				}
				this.getClasses(dataObject);
			} else {
				dataObject = jsonObject;
				className = (String) ((JSONObject) dataObject.get("name")).get("escapedText");
				if (className.toLowerCase().contains(this.getType().toLowerCase()) || (this.isAllowedFor(className))) {
					processedClass = this.createClass(className);
					classMembers = (JSONArray) dataObject.get("members");
					classModifiers = (JSONArray) dataObject.get("modifiers");
					this.getMembers(classMembers, processedClass);
					this.getModifiers(classModifiers, processedClass);
					
					this.processedClasses.add(processedClass);
				}
			}
		}
	}
	
	protected boolean isAllowedFor(String className) { return false; }
	
	protected ASTClass createClass(String name) { return new ASTClass(name); } 
	
	protected ASTMethod processMethod(JSONObject processedObject, String methodName) {
		ASTMethod astMethod = new ASTMethod(methodName);
		Object parameters = processedObject.get("parameters");
		Iterator<JSONObject> methodParameters;
		JSONObject processedVariable;
		ASTGenericDataType parameterObject;
		String parameterName;
		
		if (parameters != null) {
			methodParameters = ((JSONArray) processedObject.get("parameters")).iterator();
			while(methodParameters.hasNext()) {
				processedVariable = methodParameters.next();
				parameterName = (String) ((JSONObject) processedVariable.get("name")).get("escapedText");
				parameterObject = ASTVariableProcessor.processVariable(processedVariable, parameterName);
				astMethod.addParameter(parameterObject);
			}
		}
		
		return astMethod;
	}
	
	protected void getMembers(JSONArray members, ASTClass processedClass) {
		Iterator<JSONObject> memberIterator = members.iterator();
		JSONObject processedObject;
		String memberName;
		ASTGenericDataType newVariable;
		ASTMethod newMethod;
		Object isMethod, objectName;
		
		
		while (memberIterator.hasNext()) {
			processedObject = (JSONObject) memberIterator.next();

			objectName = processedObject.get("name");
			if (objectName == null) {
				memberName = "None or constructor";
			} else {
				memberName = (String) ((JSONObject) objectName).get("escapedText");
			}
			isMethod = processedObject.get("parameters");
			if (isMethod == null) {
				newVariable = ASTVariableProcessor.processVariable(processedObject, memberName);
				processedClass.addAttribute(memberName, newVariable);
			} else {
				newMethod = this.processMethod(processedObject, memberName);
				processedClass.addMethod(memberName, newMethod);
			}
		}
	}
	
	protected void getModifiers(JSONArray modifiers, ASTClass processedClass) {
		this.getDecorators(modifiers, processedClass);
	}
	
	protected void getDecorators(JSONArray modifiers, ASTClass processedClass) {
	}
	
	public void print() {
		Iterator<ASTClass> processedClassesIterator = this.processedClasses.iterator();
		ASTClass processedClass;
		while(processedClassesIterator.hasNext()) {
			processedClass = processedClassesIterator.next();
			processedClass.print();
		}
	}
	
}

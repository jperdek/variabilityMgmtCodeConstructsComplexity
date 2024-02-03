package astFileProcessor.astObjects;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;



public class ASTClass {
	private Map<String, ASTGenericDataType> classAttributes;
	private Map<String, ASTMethod> classMethods;
	private Map<String, ASTAnyObjectType> classDecorators;
	private String name;
	
	public ASTClass(String name) {
		this.name = name;
		this.classAttributes = new HashMap<String, ASTGenericDataType>();
		this.classMethods = new HashMap<String, ASTMethod>();
		this.classDecorators = new HashMap<String, ASTAnyObjectType>();
	}
	
	public String getName() { return this.name; }
	
	public void addMethod(String methodName, ASTMethod ASTMethod) {
		this.classMethods.put(methodName, ASTMethod);
	}
	
	public void addAttribute(String attributeName, ASTGenericDataType datType) {
		this.classAttributes.put(attributeName, datType);
	}
	
	public void addDecorator(String decoratorName, ASTAnyObjectType datType) {
		this.classDecorators.put(decoratorName, datType);
	}
	
	public void printAttributes() {
		System.out.println("Attributes: ");
		Iterator<Entry<String, ASTGenericDataType>> attributeIterator = this.classAttributes.entrySet().iterator();
		Entry<String, ASTGenericDataType> attributeEntry;
		ASTGenericDataType attribute;
		while(attributeIterator.hasNext()) {
			attributeEntry = attributeIterator.next();
			attribute = attributeEntry.getValue();
			attribute.print();
		}
	}
	
	public void printMethods() {
		System.out.println("Methods: ");
		Iterator<Entry<String, ASTMethod>> methodsIterator = this.classMethods.entrySet().iterator();
		Entry<String, ASTMethod> methodEntry;
		ASTMethod method;
		while(methodsIterator.hasNext()) {
			methodEntry = methodsIterator.next();
			method = methodEntry.getValue();
			method.print();
		}
	}
	
	public void printDecorators() {
		System.out.println("Decorators: ");
		Iterator<Entry<String, ASTAnyObjectType>> decoratorsIterator = this.classDecorators.entrySet().iterator();
		Entry<String, ASTAnyObjectType> decoratorEntry;
		ASTAnyObjectType decorator;
		while(decoratorsIterator.hasNext()) {
			decoratorEntry = decoratorsIterator.next();
			decorator = decoratorEntry.getValue();
			decorator.print();
		}
	}
	
	public void print() {
		System.out.println("Class name: " + name);
		this.printAttributes();
		this.printMethods();
		this.printDecorators();
	}
}

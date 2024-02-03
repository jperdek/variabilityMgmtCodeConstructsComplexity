package astFileProcessor.astObjects;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;


public class ASTMethod {

	private String methodName;
	private List<ASTGenericDataType> parameters;
	
	public ASTMethod(String name) {
		this.methodName = name;
		this.parameters = new ArrayList<ASTGenericDataType>();
	}
	
	public void addParameter(ASTGenericDataType parameter) { this.parameters.add(parameter); }
	public ASTGenericDataType getParameter(int position) { return this.parameters.get(position); }
	
	public void print() {
		System.out.println("METHOD: " + this.methodName);
		Iterator<ASTGenericDataType> iterator = this.parameters.iterator();
		ASTGenericDataType processedDataType;
		while(iterator.hasNext()) {
			processedDataType = iterator.next();
			processedDataType.print();
		}
	}
}

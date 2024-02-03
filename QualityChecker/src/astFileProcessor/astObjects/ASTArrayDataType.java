package astFileProcessor.astObjects;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;


public class ASTArrayDataType extends ASTGenericDataType {

	private String type = "array";
	private List<ASTGenericDataType> array = null;
	
	public ASTArrayDataType(String name) {
		super(name);
		this.array = new ArrayList<ASTGenericDataType>();
	}
	
	public void addElement(ASTGenericDataType subVariable) {
		this.array.add(subVariable);
	}

	public void addElement(int position, ASTDataType element) {
		this.array.add(position, element);
	}
	
	public ASTGenericDataType getElement(int position) { return this.array.get(position); }
	
	public void print() {
		System.out.println("Array of type: " + this.getClass());
		Iterator<ASTGenericDataType> iterator = this.array.iterator();
		ASTGenericDataType element;
		while(iterator.hasNext()) {
			element = iterator.next();
			System.out.print(element.getName());
			System.out.print(", ");
		}
		System.out.println();
	}
	
	public void setType(String type) { this.type = type; } 
	public String getType() { return this.type; }
}

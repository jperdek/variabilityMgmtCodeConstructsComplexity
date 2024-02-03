package astFileProcessor.astObjects;
import java.util.Map;
import java.util.Map.Entry;
import java.util.HashMap;
import java.util.Iterator;


public class ASTAnyObjectType extends ASTGenericDataType {

	private String type = "Object";
	private Map<String, ASTGenericDataType> objects;
	
	public ASTAnyObjectType(String name) {
		super(name);
		objects = new HashMap<String, ASTGenericDataType>();
	}
	
	public void addObject(ASTGenericDataType object) {
		String name = object.getName();
		this.objects.put(name, object); 
	}

	public void addObject(String objectName, ASTGenericDataType object) { this.objects.put(name, object); }
	public ASTGenericDataType getObject(String objectName) { return this.objects.get(objectName); }
	
	public void setType(String type) { this.type = type; }

	public void print() {
		System.out.println(this.type + ": " + this.name);
		Iterator<Entry<String, ASTGenericDataType>> objectVariablesIterator = this.objects.entrySet().iterator();
		Entry<String, ASTGenericDataType> genericDataTypeEntry;
		ASTGenericDataType genericDataType;
		while(objectVariablesIterator.hasNext()) {
			genericDataTypeEntry = objectVariablesIterator.next();
			genericDataType = genericDataTypeEntry.getValue();
			genericDataType.print();
		}
	}
}

package astFileProcessor.astObjects;


public class ASTDataType extends ASTGenericDataType{
	private String value;
	private String type;
	
	public ASTDataType(String name, String type) {
		super(name);
		this.type = type;
	}
	
	public ASTDataType(String name, String value, String type) {
		super(name);
		this.value = value;
		this.type = type;
	}
	
	public String getValue() { return this.value; }
	public String getType() { return this.type; }
	
	public void print() {
		System.out.println(this.name + "[" + this.value + ": " + this.type + "]");
	}
}

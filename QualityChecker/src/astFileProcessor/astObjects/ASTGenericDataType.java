package astFileProcessor.astObjects;


public class ASTGenericDataType {

	protected String name;
	
	public ASTGenericDataType(String name) {
		this.name = name;
	}
	
	public String getName() { return this.name; }
	
	public void print() {
		System.out.println("Type: " + this.getClass());
	}
}

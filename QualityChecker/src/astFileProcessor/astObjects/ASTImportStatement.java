package astFileProcessor.astObjects;

public class ASTImportStatement {

	private String importName = null;
	private String importedFrom = null;
	
	public ASTImportStatement(String importName, String importedFrom) {
		this.importName = importName;
		this.importedFrom = importedFrom;
	}
	
	public String getImportName() { return this.importName; }
	public String getImportFrom() { return this.importedFrom; }
}

package astFileProcessor;

public class ASTParseConfiguration {
	
	public final static String[] RESERVED_ANNOTATIONS = new String[] {"wholeEntity", 
			"wholeInitlialization", "variableDeclaration", "wholeBlock", "wholeBlock2", "skipLine"};
	public final static String[] RESERVED_ANGULAR_ANNOTATIONS = new String[] {
			"wholeBlockFile", "wholeBlockMethod", "skipLineParameter", "skipLineVariableDeclaration", 
			"skipLineClassVariableDeclaration", "skipLineFile"};
	private boolean parseImports = true;
	private boolean parseServices = true;
	private boolean parseComponents = true;
	private boolean parseServiceAttributes = true;
	private boolean parseServiceMethods = true;
	
	
	public ASTParseConfiguration() {
	}
	
	public boolean enabledImports() { return this.parseComponents; }
	public boolean enabledServices() { return this.parseServices; }
	public boolean enabledComponents() { return this.parseComponents; }
	
	public boolean enabledServiceAttributes() { return this.parseServiceAttributes; }
	public boolean enabledServiceMethods() { return this.parseServiceMethods; }
	
	public final String[] getReservedAnnotations() { return ASTParseConfiguration.RESERVED_ANNOTATIONS; }
}

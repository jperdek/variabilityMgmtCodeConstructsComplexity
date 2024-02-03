package codeConstructsEvaluation;

public interface ComplexityConstructEvaluationConfiguration {

	public final static String PROJECT_PATH = "";
	public final static String EXPRESSION_CONSTRUCT_TYPE = "var_variable";
	public final static String SERVER_URL = "http://localhost:5001/";
	
	public static enum AnalyzedEntityType {
		  SERVICE,
		  COMPONENT,
		  DIRECTIVE
	};
	public final static String TYPHON_SERVICE_URL = "analyzeTyphonJS";
	public final static String JAVASCRIPT_SERVICE_URL = "analyzeESLintCC";
	public final static String ECMASCRIPT_SERVICE_URL = "analyzeEScomplex";
	
	public final static String FROM_CODE_TO_AST_SERVICE_URL = "convert";
	public final static String FROM_AST_TO_CODE_SERVICE_URL = "convertBack";
}

package codeConstructsEvaluation.analysis;

import java.util.List;
import astFileProcessor.astObjects.ASTGenericDecorator;


public class TransformationOutput {
	private String transformedCode;
	private List<ASTGenericDecorator> parsedGenericDecorators;
	
	public TransformationOutput(String transformedCode, List<ASTGenericDecorator> parsedGenericDecorators) {
		this.transformedCode = transformedCode;
		this.parsedGenericDecorators = parsedGenericDecorators;
	}
	
	public String getTransformedCode() { return transformedCode; }
	
	public List<ASTGenericDecorator> getParsedGenericDecorators() { return parsedGenericDecorators; }
}

package codeConstructsEvaluation.transformation;

import java.io.IOException;

import codeConstructsEvaluation.ComplexityMeasurement;

public interface ComplexityService {
	
	String getName();
	String doCleaning(String content);
	ComplexityMeasurement prepareComplexityMeasurement(String fileName, 
			String fileContent, boolean storeFileContent) throws IOException, InterruptedException;
}

package codeConstructsEvaluation.transformation.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;
import codeConstructsEvaluation.transformation.PostRequester;
import codeConstructsEvaluation.transformation.TyphonTypeScriptComplexityAnalysis;

class TyphoneTypeScriptComplexityAnalysis {

	@Test
	void test() throws IOException, InterruptedException {
		String fileName = "MyService";
		String filePath = "E://aspects/spaProductLine/QualityChecker/src/astFileProcessor/testFiles/serviceCode.txt";
		String fileType = "service";
		String fileContent = PostRequester.loadFileContent(filePath);
		TyphonTypeScriptComplexityAnalysis typhoneTypeScriptComplexityAnalysis =  new TyphonTypeScriptComplexityAnalysis();
		
		JSONObject result = typhoneTypeScriptComplexityAnalysis.getJSONComplexityReport(fileName, fileContent);
		assertEquals(4, (Long) ((JSONObject) result.get("aggregate")).get("cyclomatic"));
		assertEquals(0.062, (Double) ((JSONObject) ((JSONObject) result.get("aggregate")).get("halstead")).get("bugs"));
	}

}

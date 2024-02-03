package astFileProcessor;

public class TestAST {

	public TestAST() {	
	}
	
	private void processFile(String filePath) {
		ASTLoader astLoader = new ASTLoader(filePath);
		ASTParseConfiguration configuration = new ASTParseConfiguration();
		astLoader.parse(configuration);
		System.out.println("------------        NEXT        ----->");
	}

	public static void main(String args[]) {
		TestAST testAST = new TestAST();
		String filePath1 = "E://aspects/spaProductLine/QualityChecker/src/astFileProcessor/testFiles/serviceAST.txt";
		String filePath2 = "E://aspects/spaProductLine/QualityChecker/src/astFileProcessor/testFiles/componentAST.txt";
		String filePath3 = "E://aspects/spaProductLine/QualityChecker/src/astFileProcessor/testFiles/extendedComponentAST.txt";
		testAST.processFile(filePath1);
		testAST.processFile(filePath2);
		testAST.processFile(filePath3);
	}
}

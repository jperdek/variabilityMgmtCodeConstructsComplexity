package astFileProcessor.annotationManagment;

import org.json.simple.JSONObject;

import astFileProcessor.ASTLoader;
import astFileProcessor.processors.ASTClassDecoratorProcessor;
import astFileProcessor.processors.DecoratorManipulationSettings;


public class TestAnnotationManagement {

	public TestAnnotationManagement() {	
	}
	
	private void processFile(String filePath, DecoratorManipulationSettings decoratorsManipulationSettings) {
		JSONObject astRoot = ASTLoader.loadAST(filePath);;
		ASTClassDecoratorProcessor decoratorProcessor = new ASTClassDecoratorProcessor();
		decoratorProcessor.getClasses(astRoot, decoratorsManipulationSettings);
	}

	public static void main(String args[]) {
		TestAnnotationManagement testAST = new TestAnnotationManagement();

		DecoratorManipulationSettings decoratorsManipulationSettings1 = new DecoratorManipulationSettings();
		decoratorsManipulationSettings1.removeAllAnnotations();
		DecoratorManipulationSettings decoratorsManipulationSettings2 = new DecoratorManipulationSettings();
		String filePath1 = "E://aspects/spaProductLine/QualityChecker/src/astFileProcessor/testFiles/serviceAST.txt";
		// String filePath2 = "E://aspects/spaProductLine/QualityChecker/src/astFileProcessor/testFiles/componentAST.txt";
		// String filePath3 = "E://aspects/spaProductLine/QualityChecker/src/astFileProcessor/testFiles/extendedComponentAST.txt";
		decoratorsManipulationSettings1.allowOnlyDefaultOnes();
		decoratorsManipulationSettings1.allowOnlyUsedAngularOnes();
		
		testAST.processFile(filePath1, decoratorsManipulationSettings1);
		
		decoratorsManipulationSettings2.allowOnlyDefaultOnes();
		decoratorsManipulationSettings2.allowOnlyUsedAngularOnes();
		testAST.processFile(filePath1, decoratorsManipulationSettings2);
	}
}

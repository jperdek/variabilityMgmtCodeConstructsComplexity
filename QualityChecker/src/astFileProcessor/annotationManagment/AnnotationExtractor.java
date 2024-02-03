package astFileProcessor.annotationManagment;

import org.json.simple.JSONObject;
import astFileProcessor.ASTLoader;
import astFileProcessor.processors.ASTClassDecoratorProcessor;
import astFileProcessor.processors.DecoratorManipulationSettings;


public class AnnotationExtractor {

	public AnnotationExtractor() {
		
	}
	
	public void extractAnnotations(String filePath, DecoratorManipulationSettings decoratorsManipulationSettings) {
		JSONObject astRoot = ASTLoader.loadAST(filePath);
		ASTClassDecoratorProcessor decoratorProcessor = new ASTClassDecoratorProcessor();
		decoratorProcessor.getClasses(astRoot, decoratorsManipulationSettings);
	}
	
	public static void main(String[] args) {
		AnnotationExtractor ae = new AnnotationExtractor();
		DecoratorManipulationSettings decoratorsManipulationSettings = new DecoratorManipulationSettings();
		decoratorsManipulationSettings.removeAllAnnotations();
		decoratorsManipulationSettings.allowOnlyDefaultOnes();
		decoratorsManipulationSettings.allowOnlyUsedAngularOnes();
		
		String filePath1 = "E://aspects/spaProductLine/QualityChecker/src/astFileProcessor/testFiles/annotatedComponentAST.txt";
		ae.extractAnnotations(filePath1,decoratorsManipulationSettings);
	}
}

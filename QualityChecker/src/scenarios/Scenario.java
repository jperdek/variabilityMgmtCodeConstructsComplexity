package scenarios;

import java.io.IOException;

import SPLComplexityEvaluation.SPLDecoratorComplexityComparator;
import astFileProcessor.annotationManagment.astConstructs.NotFoundBlockElementToWrap;
import codeConstructsEvaluation.analysis.DecoratorComplexityComparator;
import codeConstructsEvaluation.analysis.DecoratorComplexityMeasuresSettings;
import codeConstructsEvaluation.transformation.ComplexityService;
import codeConstructsEvaluation.transformation.TyphonTypeScriptComplexityAnalysis;
import unsupportedDecoratorsManagement.NonExistingDecoratorTransformationType;
import unsupportedDecoratorsManagement.entities.IllegalImportNameSpecifiedException;


public interface Scenario {

	public void launchScenario() throws NonExistingDecoratorTransformationType, IOException, IllegalImportNameSpecifiedException, NotFoundBlockElementToWrap;
	public void launchScenario(String pathToProjectTree, 
			ComplexityService complexityService) throws NonExistingDecoratorTransformationType, IOException, 
	                                    IllegalImportNameSpecifiedException, NotFoundBlockElementToWrap;
	
	public static SPLDecoratorComplexityComparator getDefaultComplexityComparator() {
		String pathToProjectTree = "file:///E:/aspects/canvasSPLforSPA/canvasSPLforSPA/src";
		ComplexityService defaultComplexityService = new TyphonTypeScriptComplexityAnalysis();
		DecoratorComplexityComparator decoratorComplexityComparator = new DecoratorComplexityComparator();
		DecoratorComplexityMeasuresSettings decoratorComplexitySettings = new DecoratorComplexityMeasuresSettings(
				decoratorComplexityComparator, defaultComplexityService);
		SPLDecoratorComplexityComparator splDecoratorComparator = 
				new SPLDecoratorComplexityComparator(pathToProjectTree, decoratorComplexitySettings);
		return splDecoratorComparator;
	}
	
	public static SPLDecoratorComplexityComparator getDefaultComplexityComparator(String pathToProjectTree, ComplexityService complexityService) {
		DecoratorComplexityComparator decoratorComplexityComparator = new DecoratorComplexityComparator();
		DecoratorComplexityMeasuresSettings decoratorComplexitySettings = new DecoratorComplexityMeasuresSettings(
				decoratorComplexityComparator, complexityService);
		SPLDecoratorComplexityComparator splDecoratorComparator = 
				new SPLDecoratorComplexityComparator(pathToProjectTree, decoratorComplexitySettings);
		return splDecoratorComparator;
	}
}

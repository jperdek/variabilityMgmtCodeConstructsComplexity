
import codeConstructsEvaluation.analysis.DecoratorComplexityComparator;
import codeConstructsEvaluation.analysis.DecoratorComplexityMeasuresSettings;
import codeConstructsEvaluation.transformation.ComplexityService;
import codeConstructsEvaluation.transformation.TyphonTypeScriptComplexityAnalysis;
import unsupportedDecoratorsManagement.NonExistingDecoratorTransformationType;
import unsupportedDecoratorsManagement.entities.IllegalImportNameSpecifiedException;
import SPLComplexityEvaluation.SPLDecoratorComplexityComparator;
import astFileProcessor.annotationManagment.astConstructs.NotFoundBlockElementToWrap;

import java.io.IOException;


public class SPLComplexityEvaluator {
	
	public static void main(String args[]) throws NonExistingDecoratorTransformationType, IOException, 
	                                                 IllegalImportNameSpecifiedException, NotFoundBlockElementToWrap {
		String pathToProjectTree = "file:///E:/aspects/canvasSPLforSPA/canvasSPLforSPA/src";
		ComplexityService defaultComplexityService = new TyphonTypeScriptComplexityAnalysis();
		DecoratorComplexityComparator decoratorComplexityComparator = new DecoratorComplexityComparator();
		DecoratorComplexityMeasuresSettings decoratorComplexitySettings = new DecoratorComplexityMeasuresSettings(
				decoratorComplexityComparator, defaultComplexityService);
		SPLDecoratorComplexityComparator splDecoratorComparator = 
				new SPLDecoratorComplexityComparator(pathToProjectTree, decoratorComplexitySettings);
		
		splDecoratorComparator.compareComplexityWithAndWithout();
	}
}

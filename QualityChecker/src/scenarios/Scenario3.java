package scenarios;

import SPLComplexityEvaluation.SPLDecoratorComplexityComparator;
import astFileProcessor.annotationManagment.astConstructs.NotFoundBlockElementToWrap;
import astFileProcessor.processors.DecoratorManipulationSettings;
import codeConstructsEvaluation.analysis.DecoratorComplexityComparator;
import codeConstructsEvaluation.analysis.DecoratorComplexityMeasuresSettings;
import codeConstructsEvaluation.transformation.ComplexityService;
import codeConstructsEvaluation.transformation.TyphonTypeScriptComplexityAnalysis;
import unsupportedDecoratorsManagement.NonExistingDecoratorTransformationType;
import unsupportedDecoratorsManagement.entities.IllegalImportNameSpecifiedException;

import java.io.IOException;


public class Scenario3 implements Scenario {

	public void launchScenario() throws NonExistingDecoratorTransformationType, IOException, IllegalImportNameSpecifiedException, NotFoundBlockElementToWrap {
		SPLDecoratorComplexityComparator splDecoratorComparator = Scenario.getDefaultComplexityComparator();
		DecoratorManipulationSettings decoratorManipulationSettingsBaseVersion = TransformationForms.getForm5WithPreferableDecoratorsAndAdditionalDeadCode();
		DecoratorManipulationSettings decoratorManipulationSettingsComparedVersion= TransformationForms.getForm1WithPrefereableDecorators();
		splDecoratorComparator.compareComplexityForScenario(
				decoratorManipulationSettingsBaseVersion, decoratorManipulationSettingsComparedVersion);
	}
	
	public void launchScenario(String pathToProjectTree, ComplexityService complexityService) 
			        throws NonExistingDecoratorTransformationType, IOException, IllegalImportNameSpecifiedException, NotFoundBlockElementToWrap {
		SPLDecoratorComplexityComparator splDecoratorComparator = Scenario.getDefaultComplexityComparator(pathToProjectTree, complexityService);
		DecoratorManipulationSettings decoratorManipulationSettingsBaseVersion = TransformationForms.getForm5WithPreferableDecoratorsAndAdditionalDeadCode();
		DecoratorManipulationSettings decoratorManipulationSettingsComparedVersion= TransformationForms.getForm1WithPrefereableDecorators();
		splDecoratorComparator.compareComplexityForScenario(
				decoratorManipulationSettingsBaseVersion, decoratorManipulationSettingsComparedVersion);
	}
	
	public static void main(String args[]) throws NonExistingDecoratorTransformationType, IOException, 
	                                                           IllegalImportNameSpecifiedException, NotFoundBlockElementToWrap {
		Scenario3 scenario3 = new Scenario3();
		scenario3.launchScenario();
	}
}

package scenarios;

import SPLComplexityEvaluation.SPLDecoratorComplexityComparator;
import astFileProcessor.annotationManagment.astConstructs.NotFoundBlockElementToWrap;
import astFileProcessor.processors.DecoratorManipulationSettings;
import codeConstructsEvaluation.transformation.ComplexityService;
import unsupportedDecoratorsManagement.NonExistingDecoratorTransformationType;
import unsupportedDecoratorsManagement.entities.IllegalImportNameSpecifiedException;
import java.io.IOException;


public class Scenario1 implements Scenario {

	public void launchScenario() throws NonExistingDecoratorTransformationType, IOException, IllegalImportNameSpecifiedException, NotFoundBlockElementToWrap {
		SPLDecoratorComplexityComparator splDecoratorComparator = Scenario.getDefaultComplexityComparator();
		DecoratorManipulationSettings decoratorManipulationSettingsBaseVersion = TransformationForms.getForm1WithPrefereableDecorators();
		DecoratorManipulationSettings decoratorManipulationSettingsComparedVersion= TransformationForms.getForm2WithoutConfigurationExpressions();
		splDecoratorComparator.compareComplexityForScenario(
				decoratorManipulationSettingsBaseVersion, decoratorManipulationSettingsComparedVersion);
	}
	
	public void launchScenario(String pathToProjectTree, ComplexityService complexityService)
			   throws NonExistingDecoratorTransformationType, IOException, IllegalImportNameSpecifiedException, NotFoundBlockElementToWrap {
		SPLDecoratorComplexityComparator splDecoratorComparator = Scenario.getDefaultComplexityComparator(pathToProjectTree, complexityService);
		DecoratorManipulationSettings decoratorManipulationSettingsBaseVersion = TransformationForms.getForm1WithPrefereableDecorators();
		DecoratorManipulationSettings decoratorManipulationSettingsComparedVersion= TransformationForms.getForm2WithoutConfigurationExpressions();
		splDecoratorComparator.compareComplexityForScenario(
				decoratorManipulationSettingsBaseVersion, decoratorManipulationSettingsComparedVersion);
	}

	public static void main(String args[]) throws NonExistingDecoratorTransformationType, IOException, IllegalImportNameSpecifiedException, NotFoundBlockElementToWrap {
		Scenario1 scenario1 = new Scenario1();
		scenario1.launchScenario();
	}
}

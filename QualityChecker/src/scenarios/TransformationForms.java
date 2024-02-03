package scenarios;

import java.io.IOException;
import SPLComplexityEvaluation.SPLDecoratorComplexityComparator;
import astFileProcessor.annotationManagment.astConstructs.NotFoundBlockElementToWrap;
import astFileProcessor.processors.DecoratorManipulationSettings;
import unsupportedDecoratorsManagement.NonExistingDecoratorTransformationType;
import unsupportedDecoratorsManagement.entities.IllegalImportNameSpecifiedException;


public class TransformationForms {
	
	public static DecoratorManipulationSettings getForm1WithPrefereableDecorators() {
		DecoratorManipulationSettings decoratorsManipulationSettings = new DecoratorManipulationSettings();
		decoratorsManipulationSettings.allowOnlyUsedAngularOnes();			 		//ONLY PREFERED ANNOTATIONS ARE MANAGED
		decoratorsManipulationSettings.setIfRemoveProcessedDecoratorsOnly(false);
		decoratorsManipulationSettings.setIfRemoveUnprocessedDecoratorsOnly(false);
		decoratorsManipulationSettings.setIfRemoveConfigurationExpressions(false);  // EXPRESSIONS REMAIN AS IS (IF ANNOTATIONS ARE NOT REMOVED)
		decoratorsManipulationSettings.setRemoveHelperUnwantedCode(true); 			// REMOVE OPTIONAL HELPER UNWANTED CODE 
		decoratorsManipulationSettings.setSearchType(DecoratorManipulationSettings.SearchType.START);
		decoratorsManipulationSettings.setIfPositiveVariabilityCommentedCodeShouldBeIncluded(true);
		return decoratorsManipulationSettings;
	}
	
	public static DecoratorManipulationSettings getForm2WithoutConfigurationExpressions() {
		DecoratorManipulationSettings decoratorsManipulationSettings = new DecoratorManipulationSettings();
		decoratorsManipulationSettings.allowOnlyUsedAngularOnes();			 		//ONLY PREFERED ANNOTATIONS ARE MANAGED
		decoratorsManipulationSettings.setIfRemoveProcessedDecoratorsOnly(false);
		decoratorsManipulationSettings.setIfRemoveUnprocessedDecoratorsOnly(false);
		decoratorsManipulationSettings.setIfRemoveConfigurationExpressions(true);	// REMOVES CONFIGURATION EXPRESSIONS
		decoratorsManipulationSettings.setRemoveHelperUnwantedCode(true); 			// REMOVE OPTIONAL HELPER UNWANTED CODE 
		decoratorsManipulationSettings.setSearchType(DecoratorManipulationSettings.SearchType.START);
		decoratorsManipulationSettings.setIfPositiveVariabilityCommentedCodeShouldBeIncluded(true);
		return decoratorsManipulationSettings;
	}
	
	public static DecoratorManipulationSettings getForm3WithPreferedWrappers() {
		DecoratorManipulationSettings decoratorsManipulationSettings = new DecoratorManipulationSettings();
		decoratorsManipulationSettings.allowOnlyUsedAngularOnes();					//ONLY PREFERED ANNOTATIONS ARE MANAGED
		decoratorsManipulationSettings.setIfRemoveProcessedDecoratorsOnly(false);
		decoratorsManipulationSettings.setIfRemoveUnprocessedDecoratorsOnly(false);
		decoratorsManipulationSettings.setSearchType(DecoratorManipulationSettings.SearchType.START);
		decoratorsManipulationSettings.setIfRemoveConfigurationExpressions(false);  // EXPRESSIONS REMAIN AS IS (IF ANNOTATIONS ARE NOT REMOVED)
		decoratorsManipulationSettings.setRemoveHelperUnwantedCode(true); 			// REMOVE OPTIONAL HELPER UNWANTED CODE 
		decoratorsManipulationSettings.setIfWrappersAreMorePreferable(true); 		// WRAPPERS ARE MORE PREFERED
		return decoratorsManipulationSettings;
	}
	
	public static DecoratorManipulationSettings getForm4WithoutVariabilityAnnotations() {
		DecoratorManipulationSettings decoratorsManipulationSettings = new DecoratorManipulationSettings();
		decoratorsManipulationSettings.removeAllAnnotations();
		decoratorsManipulationSettings.allowOnlyUsedAngularOnes();			 		// ONLY PREFERED ANNOTATIONS ARE MANAGED
		decoratorsManipulationSettings.setRemoveHelperUnwantedCode(true); 	 		// REMOVE OPTIONAL HELPER UNWANTED CODE
		decoratorsManipulationSettings.setIfRemoveConfigurationExpressions(true);  // EXPRESSIONS REMAIN AS IS (IF ANNOTATIONS ARE NOT REMOVED)
		decoratorsManipulationSettings.setIfRemoveProcessedDecoratorsOnly(true);    // REMOVES ONLY DECORATORS THAT HAVE BEEN PROCESSED - THUS FRAMEWORK ONES
		decoratorsManipulationSettings.setIfRemoveUnprocessedDecoratorsOnly(false); // DISBLAES TO REMOVE DECORATORS THAT HAVE NOT BEEN PROCESSED - THUS FRAMEWORK ONES
		decoratorsManipulationSettings.setIfPositiveVariabilityCommentedCodeShouldBeIncluded(true);
		decoratorsManipulationSettings.setSearchType(DecoratorManipulationSettings.SearchType.START);
		return decoratorsManipulationSettings;
	}
	
	public static DecoratorManipulationSettings getForm5WithPreferableDecoratorsAndAdditionalDeadCode() {
		DecoratorManipulationSettings decoratorsManipulationSettings = new DecoratorManipulationSettings();
		decoratorsManipulationSettings.allowOnlyUsedAngularOnes();			 		// ONLY PREFERED ANNOTATIONS ARE MANAGED
		decoratorsManipulationSettings.setIfRemoveProcessedDecoratorsOnly(false);
		decoratorsManipulationSettings.setIfRemoveUnprocessedDecoratorsOnly(false);
		decoratorsManipulationSettings.setSearchType(DecoratorManipulationSettings.SearchType.START);
		decoratorsManipulationSettings.setIfRemoveConfigurationExpressions(false);  // EXPRESSIONS REMAIN AS IS (IF ANNOTATIONS ARE NOT REMOVED)
		decoratorsManipulationSettings.setRemoveHelperUnwantedCode(false); 			// PRESERVE HELPER UNWANTED CODE 
		decoratorsManipulationSettings.setIfPositiveVariabilityCommentedCodeShouldBeIncluded(true);
		return decoratorsManipulationSettings;
	}
	
	public static void evaluateForm1() throws NonExistingDecoratorTransformationType, IOException, IllegalImportNameSpecifiedException, NotFoundBlockElementToWrap {
		SPLDecoratorComplexityComparator splDecoratorComparator = Scenario.getDefaultComplexityComparator();
		DecoratorManipulationSettings decoratorManipulationSettings = TransformationForms.getForm1WithPrefereableDecorators();
		splDecoratorComparator.evaluateComplexitiesOfAllForms("FORM1", decoratorManipulationSettings);
	}
	
	public static void evaluateForm2() throws NonExistingDecoratorTransformationType, IOException, IllegalImportNameSpecifiedException, NotFoundBlockElementToWrap {
		SPLDecoratorComplexityComparator splDecoratorComparator = Scenario.getDefaultComplexityComparator();
		DecoratorManipulationSettings decoratorManipulationSettings = TransformationForms.getForm2WithoutConfigurationExpressions();
		splDecoratorComparator.evaluateComplexitiesOfAllForms("FORM2", decoratorManipulationSettings);
	}
	
	public static void evaluateForm3() throws NonExistingDecoratorTransformationType, IOException, IllegalImportNameSpecifiedException, NotFoundBlockElementToWrap {
		SPLDecoratorComplexityComparator splDecoratorComparator = Scenario.getDefaultComplexityComparator();
		DecoratorManipulationSettings decoratorManipulationSettings = TransformationForms.getForm3WithPreferedWrappers();
		splDecoratorComparator.evaluateComplexitiesOfAllForms("FORM3", decoratorManipulationSettings);
	}
	
	public static void evaluateForm4() throws NonExistingDecoratorTransformationType, IOException, IllegalImportNameSpecifiedException, NotFoundBlockElementToWrap {
		SPLDecoratorComplexityComparator splDecoratorComparator = Scenario.getDefaultComplexityComparator();
		DecoratorManipulationSettings decoratorManipulationSettings = TransformationForms.getForm4WithoutVariabilityAnnotations();
		splDecoratorComparator.evaluateComplexitiesOfAllForms("FORM4", decoratorManipulationSettings);
	}
	
	public static void evaluateForm5() throws NonExistingDecoratorTransformationType, IOException, IllegalImportNameSpecifiedException, NotFoundBlockElementToWrap {
		SPLDecoratorComplexityComparator splDecoratorComparator = Scenario.getDefaultComplexityComparator();
		DecoratorManipulationSettings decoratorManipulationSettings = TransformationForms.getForm5WithPreferableDecoratorsAndAdditionalDeadCode();
		splDecoratorComparator.evaluateComplexitiesOfAllForms("FORM5", decoratorManipulationSettings);
	}
	
	public static void evaluateAllForms() throws NonExistingDecoratorTransformationType, IOException, 
														IllegalImportNameSpecifiedException, NotFoundBlockElementToWrap {
		TransformationForms.evaluateForm1();
		TransformationForms.evaluateForm2();
		TransformationForms.evaluateForm3();
		TransformationForms.evaluateForm4();
		TransformationForms.evaluateForm5();
	}
	
	public static void main(String args[]) throws NonExistingDecoratorTransformationType, IOException, 
														IllegalImportNameSpecifiedException, NotFoundBlockElementToWrap {
		TransformationForms.evaluateAllForms();
	}
}

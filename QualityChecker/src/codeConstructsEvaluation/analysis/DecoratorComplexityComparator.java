package codeConstructsEvaluation.analysis;

import java.io.IOException;

import org.json.simple.JSONObject;

import astFileProcessor.annotationManagment.astConstructs.NotFoundBlockElementToWrap;
import astFileProcessor.processors.ASTClassDecoratorProcessor;
import astFileProcessor.processors.ASTDecoratorProcessor;
import astFileProcessor.processors.DecoratorManipulationSettings;
import codeConstructsEvaluation.ComplexityMeasurement;
import codeConstructsEvaluation.transformation.ASTConverterClient;
import codeConstructsEvaluation.transformation.ComplexityService;
import codeConstructsEvaluation.transformation.PostRequester;
import codeConstructsEvaluation.transformation.TyphonTypeScriptComplexityAnalysis;
import unsupportedDecoratorsManagement.NonExistingDecoratorTransformationType;
import unsupportedDecoratorsManagement.entities.IllegalImportNameSpecifiedException;


public class DecoratorComplexityComparator {

	public DecoratorComplexityComparator() {	
	}
	
	private TransformationOutput removeOrTransformDecorators(String fileContent, DecoratorManipulationSettings decoratorsManipulationSettings) 
			throws IOException, InterruptedException, NonExistingDecoratorTransformationType, IllegalImportNameSpecifiedException, NotFoundBlockElementToWrap {
		JSONObject astRoot = ASTConverterClient.convertFromCodeToASTJSON(fileContent);
		ASTDecoratorProcessor decoratorProcessor = new ASTDecoratorProcessor();
		decoratorProcessor.parseDecorators(astRoot, decoratorsManipulationSettings);
		String removedDecoratorsContent = ASTConverterClient.convertFromASTToCode(astRoot.get("ast").toString());
		//System.out.println(astRoot.get("ast").toString());
		return new TransformationOutput(removedDecoratorsContent, decoratorProcessor.getAssociatedDecorators());
	}
	
	private String removeDecoratorsClassEntity(String fileContent, DecoratorManipulationSettings 
			decoratorsManipulationSettings) throws IOException, InterruptedException, NonExistingDecoratorTransformationType {
		JSONObject astRoot = ASTConverterClient.convertFromCodeToASTJSON(fileContent);
		ASTClassDecoratorProcessor decoratorProcessor = new ASTClassDecoratorProcessor();
		decoratorProcessor.getClasses(astRoot, decoratorsManipulationSettings);
		String removedDecoratorsContent = ASTConverterClient.convertFromASTToCode(astRoot.get("ast").toString());
		return removedDecoratorsContent;
	}
	
	public ComplexityMeasurement evaluateDecoratorComplexity(
			String fileWithDecoratorsPath, ComplexityService complexityService, boolean useRestrictedClassEntity, 
			DecoratorManipulationSettings decoratorsManipulationSettings1, DecoratorManipulationSettings decoratorsManipulationSettings2) 
					throws IOException, InterruptedException, NonExistingDecoratorTransformationType, IllegalImportNameSpecifiedException, NotFoundBlockElementToWrap {
		String fileWithoutDecoratorsContent;
		TransformationOutput transformationOutput1 = null, transformationOutput2 = null;
		String fileWithDecoratorsContent = PostRequester.loadFileContent(fileWithDecoratorsPath);
		fileWithoutDecoratorsContent = fileWithDecoratorsContent;
		if (useRestrictedClassEntity) {
			fileWithDecoratorsContent = this.removeDecoratorsClassEntity(
					fileWithDecoratorsContent, decoratorsManipulationSettings1);
		} else {
			transformationOutput1 = this.removeOrTransformDecorators(
					fileWithDecoratorsContent, decoratorsManipulationSettings1);
			fileWithDecoratorsContent = transformationOutput1.getTransformedCode();
		}
		ComplexityMeasurement fileWithDecoratorComplexityMeasurement = complexityService.prepareComplexityMeasurement(
				fileWithDecoratorsPath,  fileWithDecoratorsContent, decoratorsManipulationSettings1.shouldWholeFileContentBeStored());
		if (transformationOutput1 != null) { 
			fileWithDecoratorComplexityMeasurement.associateDecoratorsWithFileUnit(
				transformationOutput1.getParsedGenericDecorators()); 
			fileWithDecoratorComplexityMeasurement.associateDecoratorsWithEverything(
				transformationOutput1.getParsedGenericDecorators());
		}
	
		if (useRestrictedClassEntity) {
			fileWithoutDecoratorsContent = this.removeDecoratorsClassEntity(
					fileWithDecoratorsContent, decoratorsManipulationSettings2);
		} else {
			transformationOutput2 = this.removeOrTransformDecorators(
					fileWithDecoratorsContent, decoratorsManipulationSettings2);
			fileWithoutDecoratorsContent = transformationOutput2.getTransformedCode();
		}
		ComplexityMeasurement fileWithoutDecoratorComplexityMeasurement = complexityService.prepareComplexityMeasurement(
				fileWithDecoratorsPath,  fileWithoutDecoratorsContent, decoratorsManipulationSettings2.shouldWholeFileContentBeStored());
		if (transformationOutput2 != null) { 
			fileWithoutDecoratorComplexityMeasurement.associateDecoratorsWithFileUnit(
				transformationOutput2.getParsedGenericDecorators()); 
			fileWithoutDecoratorComplexityMeasurement.associateDecoratorsWithEverything(
					transformationOutput2.getParsedGenericDecorators());
		}
		return (ComplexityMeasurement) fileWithDecoratorComplexityMeasurement.makeDifference(fileWithoutDecoratorComplexityMeasurement);
	}

	public ComplexityMeasurement evaluateAndAssociateDecoratorComplexities(String fileWithDecoratorsPath, 
			ComplexityService complexityService, DecoratorManipulationSettings decoratorsManipulationSettings1, 
			DecoratorManipulationSettings decoratorsManipulationSettings2) 
					throws IOException, InterruptedException, NonExistingDecoratorTransformationType, IllegalImportNameSpecifiedException, NotFoundBlockElementToWrap {
		String fileWithDecoratorsContent = PostRequester.loadFileContent(fileWithDecoratorsPath);
		String fileWithoutDecoratorsContent;
		// clone String data twice to make transformation from both independent
		fileWithDecoratorsContent = fileWithoutDecoratorsContent = complexityService.doCleaning(fileWithDecoratorsContent);
		// System.out.println(fileWithDecoratorsContent);
		TransformationOutput transformationOutput1 = this.removeOrTransformDecorators(fileWithDecoratorsContent, decoratorsManipulationSettings1);
		fileWithDecoratorsContent = transformationOutput1.getTransformedCode();
		ComplexityMeasurement fileWithDecoratorComplexityMeasurement = complexityService.prepareComplexityMeasurement(
				fileWithDecoratorsPath,  fileWithDecoratorsContent, decoratorsManipulationSettings1.shouldWholeFileContentBeStored());
		
		TransformationOutput transformationOutput2 = this.removeOrTransformDecorators(fileWithoutDecoratorsContent, decoratorsManipulationSettings2);
		fileWithoutDecoratorsContent =  transformationOutput2.getTransformedCode();
		ComplexityMeasurement fileWithoutDecoratorComplexityMeasurement = complexityService.prepareComplexityMeasurement(
				fileWithDecoratorsPath,  fileWithoutDecoratorsContent, decoratorsManipulationSettings2.shouldWholeFileContentBeStored());
		fileWithoutDecoratorComplexityMeasurement.associateDecoratorsWithFileUnit(transformationOutput1.getParsedGenericDecorators()); 
		fileWithoutDecoratorComplexityMeasurement.associateDecoratorsWithEverything(transformationOutput1.getParsedGenericDecorators());
		fileWithDecoratorComplexityMeasurement.associateDecoratorsWithFileUnit(transformationOutput2.getParsedGenericDecorators()); 
		fileWithDecoratorComplexityMeasurement.associateDecoratorsWithEverything(transformationOutput2.getParsedGenericDecorators());
		ComplexityMeasurement finalMeasurement = (ComplexityMeasurement) fileWithDecoratorComplexityMeasurement.makeDifference(
				fileWithoutDecoratorComplexityMeasurement);
		finalMeasurement.putAssociatedMeasurement("withDecorators", fileWithDecoratorComplexityMeasurement);
		finalMeasurement.putAssociatedMeasurement("withoutDecorators", fileWithoutDecoratorComplexityMeasurement);
		return finalMeasurement;
	}
	
	public ComplexityMeasurement evaluateAndAssociateDecoratorComplexities(String fileWithDecoratorsPath, 
			ComplexityService complexityService, DecoratorManipulationSettings decoratorsManipulationSettings) 
					throws IOException, InterruptedException, NonExistingDecoratorTransformationType,
														IllegalImportNameSpecifiedException, NotFoundBlockElementToWrap {
		String fileWithDecoratorsContent = PostRequester.loadFileContent(fileWithDecoratorsPath);
		// clone String data twice to make transformation from both independent
		fileWithDecoratorsContent = complexityService.doCleaning(fileWithDecoratorsContent);
		// System.out.println(fileWithDecoratorsContent);
		TransformationOutput transformationOutput = this.removeOrTransformDecorators(fileWithDecoratorsContent, decoratorsManipulationSettings);
		fileWithDecoratorsContent = transformationOutput.getTransformedCode();
		ComplexityMeasurement complexityMeasurement = complexityService.prepareComplexityMeasurement(
				fileWithDecoratorsPath,  fileWithDecoratorsContent, decoratorsManipulationSettings.shouldWholeFileContentBeStored());
		complexityMeasurement.associateDecoratorsWithFileUnit(transformationOutput.getParsedGenericDecorators());
		complexityMeasurement.associateDecoratorsWithEverything(transformationOutput.getParsedGenericDecorators());
		return complexityMeasurement;
	}
	
	private static void test(boolean useRestrictedClassEntity, DecoratorManipulationSettings decoratorsManipulationSettings) 
					throws IOException, InterruptedException, NonExistingDecoratorTransformationType, IllegalImportNameSpecifiedException, NotFoundBlockElementToWrap {
		//String fileWithDecoratorsPath = "E://aspects/spaProductLine/QualityChecker/src/astFileProcessor/testFiles/serviceCode.txt";
		//String fileWithDecoratorsPath = "E://aspects/spaProductLine/QualityChecker/src/astFileProcessor/testFiles/serviceCodeMix.txt";
		//String fileWithDecoratorsPath = "E://aspects/spaProductLine/QualityChecker/src/astFileProcessor/testFiles/angularAnnotations/alternativeTest.txt";
		//String fileWithDecoratorsPath = "E://aspects/spaProductLine/QualityChecker/src/astFileProcessor/testFiles/angularAnnotations/commentedTest.txt";
		//String fileWithDecoratorsPath = "E://aspects/spaProductLine/QualityChecker/src/astFileProcessor/testFiles/angularAnnotations/nothingTest.txt";
		//String fileWithDecoratorsPath = "E://aspects/spaProductLine/QualityChecker/src/astFileProcessor/testFiles/angularAnnotations/importTest.txt";
		//String fileWithDecoratorsPath = "E://aspects/spaProductLine/QualityChecker/src/astFileProcessor/testFiles/angularAnnotations/argumentTest.txt";
		//String fileWithDecoratorsPath = "E://aspects/spaProductLine/QualityChecker/src/astFileProcessor/testFiles/angularAnnotations/classVariableTest.txt";
		String fileWithDecoratorsPath = "E://aspects/spaProductLine/QualityChecker/src/astFileProcessor/testFiles/angularAnnotations/classParameterTest.txt";
		DecoratorManipulationSettings basicTransforamtionIntoConmpilableCode = DecoratorManipulationSettings.getSettingsTransformedIntoComprehensionBasis(); 
		basicTransforamtionIntoConmpilableCode.setWholeFileContentToBeStoredOption(true);
		decoratorsManipulationSettings.setWholeFileContentToBeStoredOption(true);

		DecoratorComplexityComparator decoratorComplexityComparator = new DecoratorComplexityComparator();
		ComplexityService typhonComplexityService = new TyphonTypeScriptComplexityAnalysis();
		ComplexityMeasurement complexityDifference = decoratorComplexityComparator.evaluateDecoratorComplexity(
				fileWithDecoratorsPath, typhonComplexityService, useRestrictedClassEntity, 
				basicTransforamtionIntoConmpilableCode, decoratorsManipulationSettings);
		complexityDifference.print();
	}
	
	private static void useFirstClassEntityRestrictedMechanism() 
			throws IOException, InterruptedException, NonExistingDecoratorTransformationType, IllegalImportNameSpecifiedException, NotFoundBlockElementToWrap {
		DecoratorManipulationSettings decoratorsManipulationSettings = new DecoratorManipulationSettings();
		decoratorsManipulationSettings.removeAllAnnotations();
		decoratorsManipulationSettings.allowOnlyUsedAngularOnes();
		decoratorsManipulationSettings.setSearchType(DecoratorManipulationSettings.SearchType.MATCH);
		DecoratorComplexityComparator.test(true, decoratorsManipulationSettings);
	}
	
	private static void useSecondGeneralDecoratorParsingMechanism() 
			throws IOException, InterruptedException, NonExistingDecoratorTransformationType, IllegalImportNameSpecifiedException, NotFoundBlockElementToWrap {
		DecoratorManipulationSettings decoratorsManipulationSettings = new DecoratorManipulationSettings();
		decoratorsManipulationSettings.removeAllAnnotations();
		decoratorsManipulationSettings.setIfRemoveProcessedDecoratorsOnly(true);
		decoratorsManipulationSettings.allowOnlyUsedAngularOnes();
		decoratorsManipulationSettings.setIfWrappersAreMorePreferable(true);
		decoratorsManipulationSettings.setSearchType(DecoratorManipulationSettings.SearchType.MATCH);
		DecoratorComplexityComparator.test(false, decoratorsManipulationSettings);
	}
	
	public static void main(String[] args) throws IOException, InterruptedException, 
	                               NonExistingDecoratorTransformationType, IllegalImportNameSpecifiedException, NotFoundBlockElementToWrap {
		//DecoratorComplexityComparator.useFirstClassEntityRestrictedMechanism();
		DecoratorComplexityComparator.useSecondGeneralDecoratorParsingMechanism();
	}
}

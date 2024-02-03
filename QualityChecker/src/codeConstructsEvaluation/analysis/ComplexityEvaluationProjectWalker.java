package codeConstructsEvaluation.analysis;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.stream.Stream;

import astFileProcessor.annotationManagment.astConstructs.NotFoundBlockElementToWrap;
import astFileProcessor.processors.DecoratorManipulationSettings;
import codeConstructsEvaluation.ComplexityMeasurement;
import codeConstructsEvaluation.ComplexityRecordsCollector;
import codeConstructsEvaluation.transformation.ComplexityService;
import unsupportedDecoratorsManagement.NonExistingDecoratorTransformationType;
import unsupportedDecoratorsManagement.entities.IllegalImportNameSpecifiedException;


public class ComplexityEvaluationProjectWalker {
	private DecoratorComplexityComparator decoratorComplexityComparator;

	public ComplexityEvaluationProjectWalker(DecoratorComplexityMeasuresSettings complexitySettings) {
		this.decoratorComplexityComparator = complexitySettings.getDecoratorComplexityComparator();
	}

	public void evaluateOnExistingProject(ComplexityRecordsCollector complexityRecordsCollector, 
			ComplexityService usedComplexityService, String pathToProjectTree, 
			DecoratorManipulationSettings decoratorsManipulationSettings1, 
			DecoratorManipulationSettings decoratorsManipulationSettings2) 
					throws NonExistingDecoratorTransformationType, IllegalImportNameSpecifiedException, NotFoundBlockElementToWrap {
		Stream<Path> s = null;
		ComplexityMeasurement complexityMeasurement;
		
		try {
			Path inputPath1 = Path.of(URI.create(pathToProjectTree));
			s = Files.walk(inputPath1);
			Iterator<Path> paths = s.iterator();
			while(paths.hasNext()) {
				Path actualFilePath = paths.next();
				if(!Files.isDirectory(actualFilePath) && actualFilePath.toString().endsWith(".ts")) {
					System.out.print("Processing: " + actualFilePath);
					complexityMeasurement = this.decoratorComplexityComparator.evaluateAndAssociateDecoratorComplexities(
							actualFilePath.toString(), usedComplexityService, decoratorsManipulationSettings1, decoratorsManipulationSettings2);
					complexityRecordsCollector.addMeasurement(complexityMeasurement);
					System.out.println("...done");
				} else {
					System.out.println("Skipping: " + actualFilePath);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			if(s != null) { s.close(); }
		}
	}
	
	public void evaluateOnExistingProject(ComplexityRecordsCollector complexityRecordsCollector, 
			ComplexityService usedComplexityService, String pathToProjectTree, 
			DecoratorManipulationSettings decoratorsManipulationSettings) 
					throws NonExistingDecoratorTransformationType, IllegalImportNameSpecifiedException, NotFoundBlockElementToWrap {
		Stream<Path> s = null;
		ComplexityMeasurement complexityMeasurement;
		
		try {
			Path inputPath1 = Path.of(URI.create(pathToProjectTree));
			s = Files.walk(inputPath1);
			Iterator<Path> paths = s.iterator();
			while(paths.hasNext()) {
				Path actualFilePath = paths.next();
				if(!Files.isDirectory(actualFilePath) && actualFilePath.toString().endsWith(".ts")) {
					System.out.print("Processing: " + actualFilePath);
					complexityMeasurement = this.decoratorComplexityComparator.evaluateAndAssociateDecoratorComplexities(
							actualFilePath.toString(), usedComplexityService, decoratorsManipulationSettings);
					complexityRecordsCollector.addMeasurement(complexityMeasurement);
					System.out.println("...done");
				} else {
					System.out.println("Skipping: " + actualFilePath);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			if(s != null) { s.close(); }
		}
	}
}

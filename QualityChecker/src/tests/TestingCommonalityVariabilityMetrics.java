package tests;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import entities.ProjectEntity;
import entities.VariableEntitiesMarker;
import metrics.CommonalityVariabilityMetrics;
import metrics.MetricsEvaluator;


class TestingCommonalityVariabilityMetrics {

	@Test
	void test() {
		String featureManagementDirectory = "None";
		ExampleTree3 tree = new ExampleTree3();
		Map<String, ProjectEntity> functionalEntities = tree.createTree();
		ProjectEntity[] featureManagementArrayEntities = tree.getFeatureManagementArrayEntities();
		List<ProjectEntity> featureManagementEntities = Arrays.asList(featureManagementArrayEntities);
		
		VariableEntitiesMarker variableEntitiesMarker = new VariableEntitiesMarker();
		variableEntitiesMarker.markVariableEntities(functionalEntities, featureManagementEntities, featureManagementDirectory);
		MetricsEvaluator metricsEvaluator = new MetricsEvaluator();
		CommonalityVariabilityMetrics commonalityVariabilityMetrics = metricsEvaluator.evaluateCommonalityVariability(
				functionalEntities, featureManagementDirectory);
		
		//commonalityVariabilityMetrics.printAll();
		assertEquals(commonalityVariabilityMetrics.getStructureSimilarityCoefficient(), 3.0 / (3.0 + 6.0));
		assertEquals(commonalityVariabilityMetrics.getStructureVariabilityCoefficient(), 6.0 / (3.0 + 6.0));
	}

	@Test
	void test2() {
		String featureManagementDirectory = "None";
		ExampleTree3 tree = new ExampleTree3();
		Map<String, ProjectEntity> functionalEntities = tree.createTree();
		ProjectEntity[] featureManagementArrayEntities = tree.getFeatureManagementArrayEntities2();
		List<ProjectEntity> featureManagementEntities = Arrays.asList(featureManagementArrayEntities);
		
		VariableEntitiesMarker variableEntitiesMarker = new VariableEntitiesMarker();
		variableEntitiesMarker.markVariableEntities(functionalEntities, featureManagementEntities, featureManagementDirectory);
		MetricsEvaluator metricsEvaluator = new MetricsEvaluator();
		CommonalityVariabilityMetrics commonalityVariabilityMetrics = metricsEvaluator.evaluateCommonalityVariability(
				functionalEntities, featureManagementDirectory);
		
		//commonalityVariabilityMetrics.printAll();
		assertEquals(commonalityVariabilityMetrics.getStructureSimilarityCoefficient(), 4.0 / (4.0 + 5.0));
		assertEquals(commonalityVariabilityMetrics.getStructureVariabilityCoefficient(), 5.0 / (4.0 + 5.0));
	}
}

package metrics;

import java.util.Iterator;
import java.util.Map;
import entities.ProjectEntity;


public class MetricsEvaluator {

	private CommonalityVariabilityMetrics commonalityVariabilityMetrics;
	
	public MetricsEvaluator() {
		this.commonalityVariabilityMetrics = new CommonalityVariabilityMetrics();
	}
	
	public CommonalityVariabilityMetrics evaluateCommonalityVariability(
			Map<String, ProjectEntity> projectEntityMapping, String featureManagementDirectory) {
		Iterator<ProjectEntity> projectEntities = projectEntityMapping.values().iterator();
		
		while(projectEntities.hasNext()) {
			ProjectEntity processedEntity = projectEntities.next();
			String pathInProjectOfEntity = processedEntity.getPathInProject();
			if (pathInProjectOfEntity.indexOf(featureManagementDirectory) != -1) {
				continue;
			}
			if (processedEntity.isCommon()) {
				this.commonalityVariabilityMetrics.addCommonality(pathInProjectOfEntity);
			} else {
				this.commonalityVariabilityMetrics.addVariability(pathInProjectOfEntity);
			}
		}
		return this.commonalityVariabilityMetrics;
	}
}

package visualization;

import java.util.Iterator;
import java.util.Map;
import entities.ProjectEntity;
import metrics.MemberQuality;


public class EntityAttributePrinter {

	public EntityAttributePrinter() {	
	}
	
	public void printEntityAttributes(Map<String, ProjectEntity> entitiesMapping, String featureManagementDirectory) {
		Iterator<ProjectEntity> projectEntities = entitiesMapping.values().iterator();
		
		double wholeWeight = MemberQuality.getWholeWeight(entitiesMapping, featureManagementDirectory);
		while(projectEntities.hasNext()) {
			ProjectEntity processingEntity = projectEntities.next();
			String entityPath = processingEntity.getPathInProject();
			if (entityPath.indexOf(featureManagementDirectory) != -1) {
				continue;
			}
			double weight = processingEntity.getMemberQuality().getComponentStandaloneProposedReuseValue(wholeWeight);
			System.out.println(processingEntity.isCommon() + " " + processingEntity.getPathInProject() + " " + weight);
		}
	}
}

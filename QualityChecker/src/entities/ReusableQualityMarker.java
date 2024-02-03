package entities;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import metrics.CommonalityVariabilityMetrics;
import metrics.MemberQuality;


public class ReusableQualityMarker {

	public ReusableQualityMarker() {
	}

	public void markSharingEntities(
			Map<String, ProjectEntity> functionalEntities, 
			List<ProjectEntity> featureManagementEntities, String featureManagementDirectory) {
		Iterator<ProjectEntity> featureManagementIterator = featureManagementEntities.iterator();
		while(featureManagementIterator.hasNext()) {
			ProjectEntity managementEntity = featureManagementIterator.next();
			
			Iterator<String> usedImports = managementEntity.getImports().iterator();
			while(usedImports.hasNext()) {
				String usedImport = usedImports.next().replace("file:///", "");
				if (usedImport.indexOf(featureManagementDirectory) != -1) {
					continue;
				}
				if (functionalEntities.containsKey(usedImport)) {
					ProjectEntity foundEntity = functionalEntities.get(usedImport);
					MemberQuality qualityMemberOfFoundEntity = foundEntity.getMemberQuality();

					Set<String> usedEntities = new HashSet<String>();
					// imports are connected also when no hierarchy dependency is made - many false positives!
					this.setChildrenAsSharingComponent(foundEntity, usedEntities, functionalEntities, false, 0, featureManagementDirectory);
					Iterator<String> usedEntityIterator = usedEntities.iterator();
					while(usedEntityIterator.hasNext()) {
						qualityMemberOfFoundEntity.addReusingComponent(usedEntityIterator.next());
					}
				}
			}
		}
	}
	
	public void setChildrenAsSharingComponent(ProjectEntity variableEntity, Set<String> usedSet,
			Map<String, ProjectEntity> functionalEntities, boolean allowAllImports, int depth, String featureManagementDirectory) {
		String pathOfEntityInProject = variableEntity.getPathInProject();
		
		if (!usedSet.contains(pathOfEntityInProject)) {
			usedSet.add(pathOfEntityInProject);


			Iterator<String> usedImports = variableEntity.getImports().iterator();
			while(usedImports.hasNext()) {
				String usedImport = usedImports.next().replace("file:///", "");
				if (usedImport.indexOf(featureManagementDirectory) != -1) {
					continue;
				}
				if (functionalEntities.containsKey(usedImport)) {
					ProjectEntity foundEntity = functionalEntities.get(usedImport);
					String pathOfFoundEntity = foundEntity.getPathInProject();
					if (pathOfFoundEntity.endsWith(".component.ts") || 
							//pathOfFoundEntity.endsWith(".service.ts") ||
							allowAllImports || 
							(pathOfFoundEntity.endsWith(".service.ts") && depth < 1)	// services which appear directly provide main functionality, are not utils
							) {
	
						this.setChildrenAsSharingComponent(foundEntity, usedSet, functionalEntities, allowAllImports, depth + 1, featureManagementDirectory);
					}
				} else {
					//System.out.println("---NOT FOUND---->" + usedImport);
				}
			}
		}
	}
}

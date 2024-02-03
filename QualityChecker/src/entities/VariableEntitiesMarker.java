package entities;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class VariableEntitiesMarker {

	public VariableEntitiesMarker() {
	}
	
	public void markVariableEntities(Map<String, ProjectEntity> functionalEntities, 
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
					foundEntity.setAsVariable();
					// imports are connected also when no hierarchy dependency is made - many false positives!
					
					this.setChildrenAsVariable(foundEntity, new HashSet<String>(), functionalEntities, false, 0, featureManagementDirectory);
				}
			}
		}
	}
	
	public void setChildrenAsVariable(ProjectEntity variableEntity, Set<String> usedSet,
			Map<String, ProjectEntity> functionalEntities, boolean allowAllImports, 
			int depth, String featureManagementDirectory) {
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
						
						foundEntity.setAsVariable();
						String fileContent = FileLoader.loadWholeFile(pathOfFoundEntity);
						//ClassTypeScriptParser.analyzeTypescriptFile("class Greeter { greeting: string; }");
						this.setChildrenAsVariable(foundEntity, usedSet, functionalEntities, allowAllImports, depth + 1, featureManagementDirectory);
					}
				} else {
					//System.out.println("---NOT FOUND---->" + usedImport);
				}
			}
		}
	}
	
}

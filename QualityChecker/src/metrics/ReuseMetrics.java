package metrics;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import entities.ProjectEntity;
import entities.ReusableQualityMarker;

public class ReuseMetrics {

	private ReusableQualityMarker reusableQualityMarker;
	
	public ReuseMetrics() {
		this.reusableQualityMarker = new ReusableQualityMarker();
	}
	
	public void evaluateReusableQualityWithProcessing(Map<String, ProjectEntity> functionalEntities, 
			List<ProjectEntity> featureManagementEntities, String featureManagementDirectory) {
		this.reusableQualityMarker.markSharingEntities(functionalEntities, featureManagementEntities, featureManagementDirectory);
		this.countComponentValueForAllMappedComponets(functionalEntities, featureManagementDirectory);
		this.evaluateReusableQuality(functionalEntities, featureManagementDirectory);
	}
	
	public void evaluateReusableQuality(Map<String, ProjectEntity> projectEntityMapping, String featureManagementDirectory) {
		Iterator<ProjectEntity> projectEntities = projectEntityMapping.values().iterator();
		
		double wholeWeightValue = MemberQuality.getWholeWeight(projectEntityMapping, featureManagementDirectory);
		while(projectEntities.hasNext()) {
			ProjectEntity processedEntity = projectEntities.next();
			String pathOfEntityInProject = processedEntity.getPathInProject();
			if (pathOfEntityInProject.indexOf(featureManagementDirectory) != -1) {
				continue;
			}
			MemberQuality qualityMetrics = processedEntity.getMemberQuality();
			
			double wholeCompoundValue = qualityMetrics.getWholeCompoundValue();
			double wholeCompoundReusableValue = qualityMetrics.getWholeCompoundReusableValue(wholeWeightValue);
			double componentOwnValuabilityMultipliedByItsUsage = qualityMetrics.getValuabilityByReusedComponents();
			double componentOwnValuability = qualityMetrics.getValuability();
			double componentOwnValuabilityMultipliedByItsUsageInProductLine = qualityMetrics.getComponentStandaloneProposedReuseValueByReusedComponents(wholeWeightValue);
			double componentOwnValuabilityInProductLine = qualityMetrics.getComponentStandaloneProposedReuseValue(wholeWeightValue);
			System.out.println("Component identifier: " + pathOfEntityInProject);
			System.out.println("Whole compound value of component: " + wholeCompoundValue);
			System.out.println("Whole compound reusable value of component: " + wholeCompoundReusableValue);
			System.out.println("Component own/standalone valuability: " + componentOwnValuability);
			System.out.println("Component own/standalone valuability / whole product line value: " + componentOwnValuabilityInProductLine);
			System.out.println("Component own/standalone valuability * times its usage: " + 
			  componentOwnValuabilityMultipliedByItsUsage);
			System.out.println("WHOLE PRODUCT LINE VALUE: " + wholeWeightValue);

			System.out.println();
		}
	}
	
	public void countComponentValueForAllMappedComponets(
			Map<String, ProjectEntity> projectEntityMapping, String featureManagementDirectory) {
		Iterator<ProjectEntity> projectEntities = projectEntityMapping.values().iterator();
		
		while(projectEntities.hasNext()) {
			ProjectEntity processedEntity = projectEntities.next();
			String entityPath = processedEntity.getPathInProject();
			if (entityPath.indexOf(featureManagementDirectory) != -1) {
				continue;
			}
			MemberQuality memberQuality = processedEntity.getMemberQuality();
			double wholeComponentValue = ReuseMetrics.calculateWholeComponentValue(
					memberQuality.getReusingComponents(), projectEntityMapping, featureManagementDirectory);
			memberQuality.setWholeComponentValue(wholeComponentValue);
		}
	}
	
	public static double calculateWholeComponentValue(Set<String> includedComponents, 
			Map<String, ProjectEntity> projectEntityMapping, String featureManagementDirectory) {
		Iterator<String> includedComponentsIterator = includedComponents.iterator();
		//MemberQuality memberQuality = componentItself.getMemberQuality(); // should be in mapping
		
		double wholeComponentValue = 0.0; //memberQuality.getValuability();
		while(includedComponentsIterator.hasNext()) {
			String includedComponentID = includedComponentsIterator.next();
			ProjectEntity innerComponent = projectEntityMapping.get(includedComponentID);
			String entityPath = innerComponent.getPathInProject();
			if (entityPath.indexOf(featureManagementDirectory) != -1) {
				continue;
			}
			MemberQuality memberQuality = innerComponent.getMemberQuality();
			wholeComponentValue = wholeComponentValue + memberQuality.getValuability();
		}
		return wholeComponentValue;
	}
	
	public boolean decideIfIsVariablePoint(String entityImportPath, List<ProjectEntity> featureManagementEntities) {
		Iterator<ProjectEntity> featuresIterator = featureManagementEntities.iterator();
		
		while(featuresIterator.hasNext()) {
			ProjectEntity feature = featuresIterator.next();
			Iterator<String> usedImports = feature.getImports().iterator();
	
			while(usedImports.hasNext()) {
				String usedImport = usedImports.next().replace("file:///", "");
				
				if (entityImportPath.equals(usedImport)) {
					return true;
				}
			}			
		}
		
		return false;
	}

	public void evaluateReusableQualityToCSV(Map<String, ProjectEntity> projectEntityMapping, 
			String featureManagementDirectory, List<ProjectEntity> featureManagementEntities, 
			String csvFilePath, boolean onlyVariablePoints) throws IOException {
		File csvFile = new File(csvFilePath);
		FileWriter fileWriter = new FileWriter(csvFile);
		
		Iterator<ProjectEntity> projectEntities = projectEntityMapping.values().iterator();
		
		double wholeWeightValue = MemberQuality.getWholeWeight(projectEntityMapping, featureManagementDirectory);
		fileWriter.write("Name;Type;Component partial value (from all SPL);Whole value with its subcomponents;Component valuability (in LOC);Is common;Is variability point;Whole SPL weight\n");
		
		
		while(projectEntities.hasNext()) {
			ProjectEntity processedEntity = projectEntities.next();
			String pathOfEntityInProject = processedEntity.getPathInProject();
			if (pathOfEntityInProject.indexOf(featureManagementDirectory) != -1) {
				continue;
			}
			boolean isVariablePoint = this.decideIfIsVariablePoint(pathOfEntityInProject, featureManagementEntities);
			if (!onlyVariablePoints || isVariablePoint) {
				MemberQuality qualityMetrics = processedEntity.getMemberQuality();
				boolean isCommon = processedEntity.isCommon();
				double wholeCompoundValue = qualityMetrics.getWholeCompoundValue();
				double wholeCompoundReusableValue = qualityMetrics.getWholeCompoundReusableValue(wholeWeightValue);
				double componentOwnValuability = qualityMetrics.getValuability();
		
				StringBuilder line = new StringBuilder();
				pathOfEntityInProject = pathOfEntityInProject.substring(pathOfEntityInProject.lastIndexOf("/") + 1, pathOfEntityInProject.length());
				pathOfEntityInProject = pathOfEntityInProject.replace(".ts", "");
	
		
				String type = "Unknown";
				if (pathOfEntityInProject.indexOf(".component") != -1) {
					type = "component";
					pathOfEntityInProject = pathOfEntityInProject.replace(".component", "");
				} else if (pathOfEntityInProject.indexOf(".service") != -1) {
					type = "service";
					pathOfEntityInProject = pathOfEntityInProject.replace(".service", "");
				} else if (pathOfEntityInProject.indexOf(".directive") != -1) {
					type = "directive";
					pathOfEntityInProject = pathOfEntityInProject.replace(".directive", "");
				}  else if (pathOfEntityInProject.indexOf(".mock") != -1) {
					type = "mock";
					pathOfEntityInProject = pathOfEntityInProject.replace(".mock", "");
				} else {
					type = "interface";
				}
			
				line.append(pathOfEntityInProject);
				line.append(';');
				line.append(type);
				line.append(';');
				line.append(String.valueOf(wholeCompoundReusableValue).replace(".", ","));
				line.append(';');
				line.append(String.valueOf(wholeCompoundValue).replace(".", ","));
				line.append(';');
				line.append(String.valueOf(componentOwnValuability).replace(".", ","));
				line.append(';');
				line.append(isCommon);
				line.append(';');
				line.append(isVariablePoint);
				line.append(';');
				line.append(String.valueOf(wholeWeightValue).replace(".", ","));
				
				line.append("\n");
	            fileWriter.write(line.toString());
			}
		}
		fileWriter.close();
	}
}

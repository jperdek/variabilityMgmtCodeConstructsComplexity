package metrics;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import entities.ProjectEntity;

public class MemberQuality {

	private double wholeComponentValue = 0.0;
	
	private long numberLinesOfCode = 0;
	private long numberLinesOfTemplateCode = 0;
	private long numberLinesOfStyles = 0;
	
	private double linesOfCodeValuability = 3;
	private double linesOfTemplateCodeValuability = 2;
	private double linesOfStylesValuability = 0.25;
	
	private Set<String> reusingComponents = new HashSet<String>(); 


	public MemberQuality(String componentIdentifier) {
		this.reusingComponents.add(componentIdentifier);
	}

	public MemberQuality(String componentIdentifier, long numberLinesOfCode) {
		this.numberLinesOfCode = numberLinesOfCode;
		this.reusingComponents.add(componentIdentifier);
	}
	
	public MemberQuality(String componentIdentifier, 
			long numberLinesOfCode, long numberLinesOfTemplateCode, long numberLinesOfStyles) {
		this.numberLinesOfCode = numberLinesOfCode;
		this.numberLinesOfTemplateCode = numberLinesOfTemplateCode;
		this.numberLinesOfStyles = numberLinesOfStyles;
		this.reusingComponents.add(componentIdentifier);
	}
	

	public void setNumberLinesOfCode(long numberLinesOfCode) {
		this.numberLinesOfCode = numberLinesOfCode;
	}
	
	public void setNumberLinesOfTemplateCode(long numberLinesOfTemplateCode) {
		this.numberLinesOfTemplateCode = numberLinesOfTemplateCode;
	}
	
	public void setNumberLinesOfStyles(long numberLinesOfStyles) {
		this.numberLinesOfStyles = numberLinesOfStyles;
	}
	
	public void addNumberLinesOfStyles(long numberLinesOfStyles) {
		this.numberLinesOfStyles = this.numberLinesOfStyles + numberLinesOfStyles;
	}
	
	public void addReusingComponent(String reusingComponentPart) {
		this.reusingComponents.add(reusingComponentPart);
	}
	
	public Set<String> getReusingComponents() {
		return this.reusingComponents;
	}
	
	public void setWeights(double codeValuability, double templateValuability, double stylesValuability) {
		this.linesOfCodeValuability = codeValuability;
		this.linesOfTemplateCodeValuability = templateValuability;
		this.linesOfStylesValuability = stylesValuability;
	}

	public double getValuability() {
		return 0.0 + this.numberLinesOfCode * this.linesOfCodeValuability + 
				this.numberLinesOfTemplateCode * this.linesOfTemplateCodeValuability + 
				this.numberLinesOfStyles * this.linesOfStylesValuability;
	}
	
	/**
	 * Returns valuability of component multiplied by its usage in all imported relevant components
	 * 
	 * @return
	 */
	public double getValuabilityByReusedComponents() {
		int numberReusingComponents = this.reusingComponents.size();
		return (0.0 + this.numberLinesOfCode * this.linesOfCodeValuability + 
				this.numberLinesOfTemplateCode * this.linesOfTemplateCodeValuability + 
				this.numberLinesOfStyles + this.linesOfStylesValuability) * (0.0 + numberReusingComponents);
	}
	
	// for whole result of subcomponents recursion is necessary
	public double getComponentStandaloneProposedReuseValue(double valueOfWholeProductLine) {
		return (0.0 + this.getValuability()) / valueOfWholeProductLine;
	}
	
	public double getComponentStandaloneProposedReuseValueByReusedComponents(double valueOfWholeProductLine) {
		int numberReusingComponents = this.reusingComponents.size();
		return ((this.getValuability() + 0.0) * numberReusingComponents) / valueOfWholeProductLine;
	}
	

	public static double getWholeWeight(
			Map<String, ProjectEntity> entitiesMapping, String featureManagementDirectory) {
		double wholeWeight = 0.0;
		Iterator<ProjectEntity> projectEntities = entitiesMapping.values().iterator();
		
		while(projectEntities.hasNext()) {
			ProjectEntity processingEntity = projectEntities.next();
			String entityPath = processingEntity.getPathInProject();
			if (entityPath.indexOf(featureManagementDirectory) != -1) {
				continue;
			}
			wholeWeight = wholeWeight + processingEntity.getMemberQuality().getValuability(); 
		}

		return wholeWeight;
	}
	
	public void setWholeComponentValue(double wholeComponentValue) {
		this.wholeComponentValue = wholeComponentValue;
	}
	
	public double getWholeCompoundValue() { return this.wholeComponentValue; }
	
	public double getWholeCompoundReusableValue(double valueOfWholeProductLine) { return (this.wholeComponentValue + 0.0) / valueOfWholeProductLine; }
}

package codeConstructsEvaluation.analysis;

import codeConstructsEvaluation.transformation.ComplexityService;
import java.util.HashSet;
import java.util.Set;


public class DecoratorComplexityMeasuresSettings {
	
	private Set<String> decoratorsToRemove;
	private DecoratorComplexityComparator decoratorComplexityComparator;
	private Set<ComplexityService> complexityServices;
	private Set<String> enabledFileTypesToProcess;
	

	public DecoratorComplexityMeasuresSettings(DecoratorComplexityComparator decoratorComplexityComparator, 
			ComplexityService defaultComplexityService) {
		this.decoratorsToRemove = new HashSet<String>();
		this.complexityServices = new HashSet<ComplexityService>();
		this.enabledFileTypesToProcess = new HashSet<String>();
		this.enabledFileTypesToProcess.add(".ts");
		this.complexityServices.add(defaultComplexityService);
		this.decoratorComplexityComparator = decoratorComplexityComparator;
	}

	public void addDecoratorToRemove(String decoratorToRemoveName) {
		this.decoratorsToRemove.add(decoratorToRemoveName);
	}
	
	public void addComplexityService(ComplexityService newComplexityService) {
		this.complexityServices.add(newComplexityService);
	}

	public boolean canBeProcessedType(String fileName) { return this.enabledFileTypesToProcess.contains(
			fileName.substring(fileName.lastIndexOf("."))); }
	
	public Set<String> getDecoratorsToRemove() { return this.decoratorsToRemove; }

	public Set<ComplexityService> getComplexityServices() { return this.complexityServices; }
	
	public DecoratorComplexityComparator getDecoratorComplexityComparator() { return this.decoratorComplexityComparator; }
}

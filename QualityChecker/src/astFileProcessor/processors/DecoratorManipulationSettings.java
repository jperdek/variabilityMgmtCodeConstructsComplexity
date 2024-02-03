package astFileProcessor.processors;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import astFileProcessor.ASTParseConfiguration;


public class DecoratorManipulationSettings {
	
	private boolean removeDecorators = false;
	private boolean removeIllegalDecoratorsOnly = false;
	private boolean analyzeIllegalDecorators = true;
	public static enum SearchType { START, MATCH, END, CONTAINS};
	private Set<String> importedDecoratorNames = null;
	private Map<String, Integer> allowedDecoratorNames = null;
	private Map<String, Integer> notAllowedDecoratorNames = null;
	private SearchType chosenSearchType = SearchType.MATCH;
	private boolean removeUnprocessedDecoratorsOnly = false;
	private boolean removeProcessedDecoratorsOnly = true;
	private boolean removeHelperUnwantedCode = true;
	private boolean areWrappersMorePreferable = false;
	private boolean formatCreatedAnnotationInLine = true;
	private boolean shouldWholeFileContentBeStored = true;
	private boolean removeConfigurationExpressions = false;
	private boolean includePositiveVariabilityCommentedCode = true;
	public String name = "unknown";
	

	public DecoratorManipulationSettings() {
		this.allowedDecoratorNames = new HashMap<String, Integer>();
		this.notAllowedDecoratorNames = new HashMap<String, Integer>();
		this.importedDecoratorNames = new HashSet<String>();
	}

	public Set<String> getDecoratorImportToRemove() { return this.importedDecoratorNames; }
	
	public void setImportedDecoratorNameToOptionallyRemove(String importedDecoratorNames) { this.importedDecoratorNames.add(importedDecoratorNames); }
	
	public boolean shouldIncludePositiveVariabilityCommentedCode() { return this.includePositiveVariabilityCommentedCode; }
	
	public void setIfPositiveVariabilityCommentedCodeShouldBeIncluded(boolean includePositiveVariabilityCode) { 
		this.includePositiveVariabilityCommentedCode = includePositiveVariabilityCode; 
	}

	public void setIfRemoveConfigurationExpressions(boolean removeConfigurationExpressions) { 
		this.removeConfigurationExpressions = removeConfigurationExpressions; }
	
	public boolean shouldRemoveConfigurationExpressions() { return this.removeConfigurationExpressions; }
	
	public boolean shouldWholeFileContentBeStored() { return this.shouldWholeFileContentBeStored; }
	
	public void setWholeFileContentToBeStoredOption(boolean shouldWholeFileContentBeStored) {
		this.shouldWholeFileContentBeStored = shouldWholeFileContentBeStored;
	}
	
	public void setFormatAnnotationInLine(boolean formatCreatedAnnotationInLine) { this.formatCreatedAnnotationInLine = formatCreatedAnnotationInLine; }
	
	public boolean formatAnnotationInLine() { return this.formatCreatedAnnotationInLine; }
	
	public boolean areWrappersMorePreferable() { return this.areWrappersMorePreferable; }
	
	public void setIfWrappersAreMorePreferable(boolean areWrappersMorePreferable) { this.areWrappersMorePreferable = areWrappersMorePreferable; }
	
	public boolean getRemoveHelperUnwantedCode() { return this.removeHelperUnwantedCode; }
	
	public void setRemoveHelperUnwantedCode(boolean removeHelperUnwantedCode) { this.removeHelperUnwantedCode = removeHelperUnwantedCode; }
	
	public boolean shouldRemoveUnprocessedDecoratorsOnly() { return this.removeUnprocessedDecoratorsOnly; }
	
	public boolean shouldRemoveProcessedDecoratorsOnly() { return this.removeProcessedDecoratorsOnly; }
	
	public void setIfRemoveUnprocessedDecoratorsOnly(boolean value) { this.removeUnprocessedDecoratorsOnly = value; }
	
	public void setIfRemoveProcessedDecoratorsOnly(boolean value) { this.removeProcessedDecoratorsOnly = value; }
	
	public void removeAllAnnotations() { this.removeDecorators = true; }
	
	public void removeIllegalAnnotationsOnly() { this.removeDecorators = false; this.removeIllegalDecoratorsOnly = true; }
	
	public boolean shouldOnlyIllegalBeRemoved() { return this.removeIllegalDecoratorsOnly; }
	
	public boolean shouldAllBeRemoved() { return this.removeDecorators; }
	
	public void setSearchType(SearchType searchType) { this.chosenSearchType = searchType; }
	
	public SearchType getSearchType() { return this.chosenSearchType; }
	
	public boolean canBeProcessed(String decoratorName, boolean isIllegalDecorator, boolean shouldCount) {
		if (isIllegalDecorator && !this.analyzeIllegalDecorators) {
			return false;
		}
		
		if (this.allowedDecoratorNames.isEmpty() && this.notAllowedDecoratorNames.isEmpty()) {
			return true;
		}
		
		if (!this.allowedDecoratorNames.isEmpty()) {
			return this.isDecoratorAllowed(decoratorName, this.chosenSearchType, shouldCount);
		}
		if (!this.notAllowedDecoratorNames.isEmpty()) {
			return this.isDecoratorNotAllowed(decoratorName, this.chosenSearchType, shouldCount);
		}
		return true;
	}
	
	public boolean isDecoratorAllowed(String decoratorName, SearchType type, boolean shouldCount) {
		int count;
		if (type == SearchType.MATCH) {
			boolean result =  this.allowedDecoratorNames.containsKey(decoratorName);
			if (result) {
				count = this.allowedDecoratorNames.get(decoratorName);
				if (shouldCount) { count = count + 1; }
				this.allowedDecoratorNames.put(decoratorName, count);
			}
			return result;
		} else if (type == SearchType.START) {
			for (String allowedName: this.allowedDecoratorNames.keySet()) {
				if (decoratorName.startsWith(allowedName)) {
					count = this.allowedDecoratorNames.get(allowedName);
					if (shouldCount) { count = count + 1; }
					this.allowedDecoratorNames.put(allowedName, count);

					return true;
				}
			}
		} else if (type == SearchType.END){
			for (String allowedName: this.allowedDecoratorNames.keySet()) {
				if (decoratorName.endsWith(allowedName)) {
					count = this.allowedDecoratorNames.get(allowedName);
					if (shouldCount) { count = count + 1; }
					this.allowedDecoratorNames.put(allowedName, count);
					return true;
				}
			}
		} else {
			for (String allowedName: this.allowedDecoratorNames.keySet()) {
				if (decoratorName.contains(allowedName)) {
					count = this.allowedDecoratorNames.get(allowedName);
					if (shouldCount) { count = count + 1; }
					this.allowedDecoratorNames.put(allowedName, count);
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean isDecoratorNotAllowed(String decoratorName, SearchType type, boolean shouldCount) {
		int count;
		if (type == SearchType.MATCH) {
			
			boolean result =  this.notAllowedDecoratorNames.containsKey(decoratorName);
			if (result) {
				count = this.notAllowedDecoratorNames.get(decoratorName);
				if (shouldCount) { count = count + 1; }
				this.notAllowedDecoratorNames.put(decoratorName, count);
			}
			return result;
		} else if (type == SearchType.START) {
			for (String notAllowedName: this.notAllowedDecoratorNames.keySet()) {
				if (decoratorName.startsWith(notAllowedName)) {
					count = this.notAllowedDecoratorNames.get(notAllowedName);
					if (shouldCount) { count = count + 1; }
					this.notAllowedDecoratorNames.put(notAllowedName, count);
					return true;
				}
			}
		} else if (type == SearchType.END){
			for (String notAllowedName: this.notAllowedDecoratorNames.keySet()) {
				if (decoratorName.endsWith(notAllowedName)) {
					count = this.notAllowedDecoratorNames.get(notAllowedName);
					if (shouldCount) { count = count + 1; }
					this.notAllowedDecoratorNames.put(notAllowedName, count);
					return true;
				}
			}
		} else {
			for (String notAllowedName: this.notAllowedDecoratorNames.keySet()) {
				if (decoratorName.contains(notAllowedName)) {
					count = this.notAllowedDecoratorNames.get(notAllowedName);
					if (shouldCount) { count = count + 1; }
					this.notAllowedDecoratorNames.put(notAllowedName, count);
					return true;
				}
			}
		}
		return false;
	}
	
	public void allowOnlyDefaultOnes() {
		this.notAllowedDecoratorNames.clear();
		for(String decoratorName: ASTParseConfiguration.RESERVED_ANNOTATIONS) {
			this.allowedDecoratorNames.put(decoratorName, 0);
		}
	}
	
	public void allowOnlyUsedAngularOnes() {
		this.setImportedDecoratorNameToOptionallyRemove("DecoratorTypesService");
		this.notAllowedDecoratorNames.clear();
		for(String decoratorName: ASTParseConfiguration.RESERVED_ANGULAR_ANNOTATIONS) {
			this.allowedDecoratorNames.put(decoratorName, 0);
		}
	}
	
	public void notAllowOnlyDefaultOnes() {
		this.allowedDecoratorNames.clear();
		for(String decoratorName: ASTParseConfiguration.RESERVED_ANNOTATIONS) {
			this.notAllowedDecoratorNames.put(decoratorName, 0);
		}
	}
	
	public void notAllowOnlyUsedAngularOnes() {
		this.allowedDecoratorNames.clear();
		for(String decoratorName: ASTParseConfiguration.RESERVED_ANGULAR_ANNOTATIONS) {
			this.notAllowedDecoratorNames.put(decoratorName, 0);
		}
	}

	public boolean isDecoratorIllegal(String decoratorName) {
		return false;
	}
	
	public static DecoratorManipulationSettings getSettingsWithoutCustomDecorators() {
		DecoratorManipulationSettings decoratorsManipulationSettings = new DecoratorManipulationSettings();
		decoratorsManipulationSettings.removeAllAnnotations();
		decoratorsManipulationSettings.allowOnlyUsedAngularOnes();
		decoratorsManipulationSettings.setSearchType(DecoratorManipulationSettings.SearchType.START);
		return decoratorsManipulationSettings;
	}
	
	public static DecoratorManipulationSettings getSettingsTransformedIntoComprehensionBasis() {
		DecoratorManipulationSettings decoratorsManipulationSettings = new DecoratorManipulationSettings();
		decoratorsManipulationSettings.allowOnlyUsedAngularOnes();
		decoratorsManipulationSettings.setIfRemoveProcessedDecoratorsOnly(false);
		decoratorsManipulationSettings.setIfRemoveUnprocessedDecoratorsOnly(false);
		decoratorsManipulationSettings.setSearchType(DecoratorManipulationSettings.SearchType.START);
		return decoratorsManipulationSettings;
	}
	
	public static DecoratorManipulationSettings getSettingsForDecoratorsComplexityComparisons() {
		DecoratorManipulationSettings decoratorsManipulationSettings = new DecoratorManipulationSettings();
		decoratorsManipulationSettings.removeAllAnnotations();
		decoratorsManipulationSettings.setIfRemoveProcessedDecoratorsOnly(true);
		decoratorsManipulationSettings.allowOnlyUsedAngularOnes();
		decoratorsManipulationSettings.setSearchType(DecoratorManipulationSettings.SearchType.START);
		return decoratorsManipulationSettings;
	}
}

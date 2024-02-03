package astFileProcessor.processors;

import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import astFileProcessor.annotationManagment.astConstructs.NotFoundBlockElementToWrap;
import astFileProcessor.astObjects.ASTGenericDecorator;
import unsupportedDecoratorsManagement.DecoratorsTransformationTypesProcessor;
import unsupportedDecoratorsManagement.NonExistingDecoratorTransformationType;
import unsupportedDecoratorsManagement.entities.IllegalImportNameSpecifiedException;


public class ASTDecoratorProcessor {

	private List<ASTGenericDecorator> frameworkDecorators;
	private DecoratorsTransformationTypesProcessor decoratorsTransformationTypesProcessor;
	private DecoratorManipulationSettings decoratorsManipulationSettings;
	private int numberDecoratorsBefore, numberDecoratorsAfter;
	

	public ASTDecoratorProcessor() {
		this.frameworkDecorators = new ArrayList<ASTGenericDecorator>();
		this.decoratorsTransformationTypesProcessor = new DecoratorsTransformationTypesProcessor();
	}
	
	public void parseDecorators(JSONObject astRoot, DecoratorManipulationSettings decoratorsManipulationSettings) 
			throws NonExistingDecoratorTransformationType, IOException, InterruptedException, 
			IllegalImportNameSpecifiedException, NotFoundBlockElementToWrap {
		this.decoratorsManipulationSettings = decoratorsManipulationSettings;
		this.getSelectedDecorators(astRoot);
	}
	
	public void getSelectedDecorators(JSONObject astRoot) throws NonExistingDecoratorTransformationType, 
	               IOException, InterruptedException, IllegalImportNameSpecifiedException, NotFoundBlockElementToWrap {
		this.decoratorsTransformationTypesProcessor.getPositions(astRoot, astRoot);
		this.numberDecoratorsBefore = this.frameworkDecorators.size();
		this.getSelectedDecoratorsRecursive(astRoot, astRoot, astRoot);
		this.numberDecoratorsAfter = this.frameworkDecorators.size();
		
		if (this.decoratorsManipulationSettings.shouldAllBeRemoved()) {
			ImportProcessor.searchAndRemoveImports(astRoot, this.decoratorsManipulationSettings.getDecoratorImportToRemove());
		}
	}
	
	public List<ASTGenericDecorator> getAssociatedDecorators() {
		return new ArrayList<ASTGenericDecorator>(this.frameworkDecorators.subList(numberDecoratorsBefore, numberDecoratorsAfter));
	}
	
	private void getSelectedDecoratorsRecursive(JSONObject astRoot, 
			JSONObject astPart, JSONObject astParent) throws NonExistingDecoratorTransformationType, IOException, InterruptedException, 
	               IllegalImportNameSpecifiedException, NotFoundBlockElementToWrap {
		JSONArray frameworkDecorators1 = (JSONArray) astPart.get("illegalDecorators");
		JSONArray frameworkDecorators2 = (JSONArray) astPart.get("modifiers");
		
		JSONObject frameworkDecoratorAST;
		ASTGenericDecorator frameworkDecorator;
		
		String decoratorName;
		Object entryValue;
		JSONObject entryJSONObject;
		JSONArray entryArray;
		int jumpBefore;
		
		for(String key: new HashSet<String>(astPart.keySet())) {
			entryValue = astPart.get(key);
			if (entryValue instanceof JSONObject) {
				entryJSONObject = (JSONObject) entryValue;
				this.getSelectedDecoratorsRecursive(astRoot, entryJSONObject, astPart);
			} else if(entryValue instanceof JSONArray) {
				entryArray = (JSONArray) entryValue;
				jumpBefore = entryArray.size();
				for (int index=0; index < entryArray.size(); index++) {
					entryJSONObject = (JSONObject) entryArray.get(index);
					this.getSelectedDecoratorsRecursive(astRoot, entryJSONObject, astPart);
					if (jumpBefore != entryArray.size()) {
						index = index + entryArray.size() - jumpBefore;;
						jumpBefore = entryArray.size();
					}
				}
			}
		}
	
		if (frameworkDecorators1 != null || frameworkDecorators2 != null) {
			// PROCESS ILLEGAL DECORATORS
			if (frameworkDecorators1 != null) {
				for (int index = 0; index < frameworkDecorators1.size(); index++) {
					frameworkDecoratorAST = (JSONObject) frameworkDecorators1.get(index);
					frameworkDecorator = this.decoratorsTransformationTypesProcessor.processDecoratorTransformation(
							astRoot, frameworkDecoratorAST, this.decoratorsManipulationSettings);
					if (frameworkDecorator != null) {
						decoratorName = frameworkDecorator.getName();
						if (this.decoratorsManipulationSettings.canBeProcessed(decoratorName, true, true)) {
							this.frameworkDecorators.add(frameworkDecorator);
							if (this.decoratorsManipulationSettings.shouldRemoveProcessedDecoratorsOnly()) {
								frameworkDecorators1.remove(frameworkDecoratorAST);
								index--;
							}
						} else if (this.decoratorsManipulationSettings.shouldRemoveUnprocessedDecoratorsOnly()) {
							frameworkDecorators1.remove(frameworkDecoratorAST);
							index--;
						}
					}
				}
				if (this.decoratorsManipulationSettings.shouldOnlyIllegalBeRemoved() 
						|| this.decoratorsManipulationSettings.shouldAllBeRemoved()) {
					if (!this.decoratorsManipulationSettings.shouldRemoveUnprocessedDecoratorsOnly() 
							&& !this.decoratorsManipulationSettings.shouldRemoveProcessedDecoratorsOnly()) {
						frameworkDecorators1.clear();
					}
				}
			}
			
			// PROCESS VALID DECORATORS
			if (frameworkDecorators2 != null) {
				for (int index = 0; index < frameworkDecorators2.size(); index++) {
					frameworkDecoratorAST = (JSONObject) frameworkDecorators2.get(index);
					frameworkDecorator = this.decoratorsTransformationTypesProcessor.processDecoratorTransformation(
							astRoot, frameworkDecoratorAST, this.decoratorsManipulationSettings);
					if (frameworkDecorator != null) {
						decoratorName = frameworkDecorator.getName();
						if (this.decoratorsManipulationSettings.canBeProcessed(decoratorName, false, true)) {
							this.frameworkDecorators.add(frameworkDecorator);
							if (this.decoratorsManipulationSettings.shouldRemoveProcessedDecoratorsOnly()) {
								frameworkDecorators2.remove(frameworkDecoratorAST);
								index--;
							}
						} else if (this.decoratorsManipulationSettings.shouldRemoveUnprocessedDecoratorsOnly()) {
							frameworkDecorators2.remove(frameworkDecoratorAST);
							index--;
						}
					}
				}
				if (this.decoratorsManipulationSettings.shouldAllBeRemoved()) {
					if (!this.decoratorsManipulationSettings.shouldRemoveUnprocessedDecoratorsOnly() 
							&& !this.decoratorsManipulationSettings.shouldRemoveProcessedDecoratorsOnly()) {
						frameworkDecorators2.clear();
					}
				}
			}
		}
	}
}

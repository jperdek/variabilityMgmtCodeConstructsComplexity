package unsupportedDecoratorsManagement;

import java.io.IOException;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import astFileProcessor.annotationManagment.astConstructs.AnnotationWrapperIntoBlock;
import astFileProcessor.annotationManagment.astConstructs.NotFoundBlockElementToWrap;
import astFileProcessor.astObjects.ASTGenericDecorator;
import astFileProcessor.processors.DecoratorManipulationSettings;
import unsupportedDecoratorsManagement.entities.DecoratorTransformationAlternative;
import unsupportedDecoratorsManagement.entities.DecoratorTransformationArgument;
import unsupportedDecoratorsManagement.entities.DecoratorTransformationComment;
import unsupportedDecoratorsManagement.entities.DecoratorTransformationDoNothing;
import unsupportedDecoratorsManagement.entities.DecoratorTransformationImport;
import unsupportedDecoratorsManagement.entities.IllegalImportNameSpecifiedException;


public class DecoratorTransformation implements AppliedDecoratorTransformationTypes {

	private String extractedString = null;
	private AppliedDecoratorTransformationTypes observedDecoratorType;

	
	public DecoratorTransformation() {
	}
	
	public DecoratorTransformation(String annotationContent) throws NonExistingDecoratorTransformationType {
		this.observedDecoratorType = this.processTransformationInformation(annotationContent);
	}
	
	private AppliedDecoratorTransformationTypes processTransformationInformation(String annotationContent) throws NonExistingDecoratorTransformationType {
		annotationContent = annotationContent.strip();
		if (annotationContent.startsWith("[") && annotationContent.endsWith("]")) {
			annotationContent = annotationContent.substring(1, annotationContent.length() - 1);
			if (annotationContent.startsWith(AppliedDecoratorTransformationTypes.INCLUDE_FILE_DO_NOTHING)) {
				this.extractedString = annotationContent.replaceFirst(
						AppliedDecoratorTransformationTypes.INCLUDE_FILE_DO_NOTHING, "").replaceFirst("=", "");
				return new DecoratorTransformationDoNothing();
			} else if (annotationContent.startsWith(AppliedDecoratorTransformationTypes.ALTERNATIVE_CODE)) {
				this.extractedString = annotationContent.replaceFirst(
						AppliedDecoratorTransformationTypes.ALTERNATIVE_CODE, "").replaceFirst("=", "");
				return new DecoratorTransformationAlternative();
			} else if (annotationContent.startsWith(AppliedDecoratorTransformationTypes.OPTIONAL_ARGUMENT)) {
				this.extractedString = annotationContent.replaceFirst(
						AppliedDecoratorTransformationTypes.OPTIONAL_ARGUMENT, "").replaceFirst("=", "");
				return new DecoratorTransformationArgument();
			} else if (annotationContent.startsWith(AppliedDecoratorTransformationTypes.OPTIONAL_IMPORT)) {
				this.extractedString = annotationContent.replaceFirst(
						AppliedDecoratorTransformationTypes.OPTIONAL_IMPORT, "").replaceFirst("=", "");
				return new DecoratorTransformationImport();
			} else {
				throw new NonExistingDecoratorTransformationType("Non recognized type: " + annotationContent);
			}
		} else {
			annotationContent = annotationContent.replaceFirst("//", "");
			this.extractedString = annotationContent;
			return new DecoratorTransformationComment();
		}
	}
	
	public String getExtractedString() { return this.extractedString; }
	
	private void optionallyWrapAssociatedClassDecoratorsAndRemovePreviousForm(JSONObject treeRoot, JSONObject processedDecorator, 
			Map<JSONObject, JSONObject> parentMap, DecoratorManipulationSettings decoratorManipulationSettings) throws NotFoundBlockElementToWrap, IOException, InterruptedException {
		if (decoratorManipulationSettings.areWrappersMorePreferable()) {
			JSONObject decoratorParent = parentMap.get(processedDecorator);
			JSONObject upperStatementWithImports = (JSONObject) parentMap.get(decoratorParent);
			JSONObject expressionAST = (JSONObject) ((JSONArray) ((JSONObject)
					processedDecorator.get("expression")).get("arguments")).get(0);
			AnnotationWrapperIntoBlock.AstPlaces usedPlaces;
			AnnotationWrapperIntoBlock.DeclarationTypes usedTypes;
			if (upperStatementWithImports == null) {
				upperStatementWithImports = (JSONObject) treeRoot.get("ast");
				usedPlaces = AnnotationWrapperIntoBlock.AstPlaces.STATEMENTS;
				usedTypes = AnnotationWrapperIntoBlock.DeclarationTypes.VAR;
			} else if (upperStatementWithImports.containsKey("parameters")) {
				usedPlaces = null;
				usedTypes = null;
			} else if (upperStatementWithImports.containsKey("members")) {
				usedPlaces = AnnotationWrapperIntoBlock.AstPlaces.MEMBERS;
				usedTypes = AnnotationWrapperIntoBlock.DeclarationTypes.WITHOUT;
			}  else {
				usedPlaces = AnnotationWrapperIntoBlock.AstPlaces.STATEMENTS;
				usedTypes = AnnotationWrapperIntoBlock.DeclarationTypes.VAR;
			}
		
			if (usedPlaces != null  && usedTypes != null) {
				AnnotationWrapperIntoBlock.constructAnnotationsWrapperIntoBlock(expressionAST, 
						decoratorParent, upperStatementWithImports, false, usedPlaces, usedTypes, decoratorManipulationSettings);
				((JSONArray) decoratorParent.get("modifiers")).remove(processedDecorator);
			}
		}
	}
	
	public ASTGenericDecorator applyTransformation(JSONObject treeRoot, JSONObject processedDecorator, 
			Map<JSONObject, JSONObject> parentMap, DecoratorManipulationSettings decoratorManipulationSettings) 
					throws IOException, InterruptedException, IllegalImportNameSpecifiedException, NotFoundBlockElementToWrap {
		if (this.extractedString == null) {
			String frameworkDecoratorName = AppliedDecoratorTransformationTypes.getFrameworkDecoratorName(processedDecorator);
			JSONObject annotatedParentByDecorator = (JSONObject) parentMap.get(processedDecorator);
			JSONObject coveredCode = AppliedDecoratorTransformationTypes.getAffectedCode(annotatedParentByDecorator, false);

			if (decoratorManipulationSettings.canBeProcessed(frameworkDecoratorName, false, false)) {
				JSONObject storedExpressionAST = (JSONObject) ((JSONArray) ((JSONObject)
						processedDecorator.get("expression")).get("arguments")).get(0);
				if (decoratorManipulationSettings.shouldRemoveConfigurationExpressions()) {
					((JSONArray) ((JSONObject) processedDecorator.get("expression")).get("arguments")).remove(0);
				}
				
				this.optionallyWrapAssociatedClassDecoratorsAndRemovePreviousForm(
						treeRoot, processedDecorator, parentMap, decoratorManipulationSettings);
			} 
			return new ASTGenericDecorator(frameworkDecoratorName, processedDecorator, coveredCode);
		}
	
		return this.observedDecoratorType.processSelf(treeRoot, processedDecorator, 
				parentMap, this.extractedString, decoratorManipulationSettings);
	}

	@Override
	public ASTGenericDecorator processSelf(JSONObject treeRoot, JSONObject processedDecorator, 
			Map<JSONObject, JSONObject> parentMap, String extractedString, DecoratorManipulationSettings decoratorManipulationSettings) throws IOException, InterruptedException, IllegalImportNameSpecifiedException, NotFoundBlockElementToWrap {
		ASTGenericDecorator resultingDecorator = this.observedDecoratorType.processSelf(treeRoot, processedDecorator, 
				parentMap, extractedString, decoratorManipulationSettings);
		if (decoratorManipulationSettings.shouldRemoveConfigurationExpressions()) {
			//expressionAST = storedExpressionAST; //duplicate code
			((JSONArray) ((JSONObject) processedDecorator.get("expression")).get("arguments")).remove(0);
		}
		return resultingDecorator;
	}
}

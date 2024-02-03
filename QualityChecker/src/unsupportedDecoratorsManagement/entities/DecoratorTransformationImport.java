package unsupportedDecoratorsManagement.entities;

import java.io.IOException;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import astFileProcessor.annotationManagment.astConstructs.AnnotationWrapperIntoBlock;
import astFileProcessor.annotationManagment.astConstructs.NotFoundBlockElementToWrap;
import astFileProcessor.astObjects.ASTGenericDecorator;
import astFileProcessor.astObjects.ASTIllegalDecorator;
import astFileProcessor.processors.DecoratorManipulationSettings;
import unsupportedDecoratorsManagement.AppliedDecoratorTransformationTypes;


/**
 * Extracts import - optionally should delete imports according IMPORT statement, if import is not in AST, then it should be inserted
 * 
 * @author perde
 *
 */
public class DecoratorTransformationImport implements AppliedDecoratorTransformationTypes {

	@Override
	public ASTGenericDecorator processSelf(JSONObject treeRoot, JSONObject processedDecorator, 
			Map<JSONObject, JSONObject> parentMap, String extractedString, DecoratorManipulationSettings decoratorManipulationSettings) throws IllegalImportNameSpecifiedException, NotFoundBlockElementToWrap, IOException, InterruptedException {
		String illegalDecoratorName = AppliedDecoratorTransformationTypes.getFrameworkDecoratorName(processedDecorator);
		JSONObject coveredCode = this.searchForImport(treeRoot, extractedString);
		if (coveredCode == null) {
			throw new IllegalImportNameSpecifiedException("Import with name: " + extractedString + " does not exists!");
		}
		JSONObject decoratorParent = (JSONObject) parentMap.get(processedDecorator);
		JSONObject parentWithStatements = parentMap.get(decoratorParent);
		JSONObject redundantCode = AppliedDecoratorTransformationTypes.getAffectedCode(decoratorParent, true);
		
		JSONObject expressionAST = (JSONObject) ((JSONArray) ((JSONObject)
				processedDecorator.get("expression")).get("arguments")).get(0);

		JSONObject upperStatementWithImports = (JSONObject) parentMap.get(decoratorParent);
		if (upperStatementWithImports == null) { upperStatementWithImports = treeRoot; } // imports are always on top	
		
		if (decoratorManipulationSettings.canBeProcessed(illegalDecoratorName, true, false) && 
				(decoratorManipulationSettings.shouldAllBeRemoved() || decoratorManipulationSettings.shouldOnlyIllegalBeRemoved())) {
			if (parentWithStatements != null && decoratorManipulationSettings.getRemoveHelperUnwantedCode()) {
				((JSONArray) parentWithStatements.get("statements")).remove(decoratorParent);
			}
			return new ASTIllegalDecorator(illegalDecoratorName, processedDecorator, coveredCode, redundantCode);
		}
		
		AnnotationWrapperIntoBlock.constructAnnotationsWrapperIntoBlock(expressionAST, 
					decoratorParent, upperStatementWithImports, true,
					AnnotationWrapperIntoBlock.AstPlaces.STATEMENTS, 
					AnnotationWrapperIntoBlock.DeclarationTypes.VAR, decoratorManipulationSettings);
		
		if (parentWithStatements != null && decoratorManipulationSettings.getRemoveHelperUnwantedCode()) {
			((JSONArray) parentWithStatements.get("statements")).remove(decoratorParent);
		}
		return new ASTIllegalDecorator(illegalDecoratorName, processedDecorator, coveredCode, redundantCode);
	}
	
	private JSONObject searchForImport(JSONObject astPart, String importNames) {
		JSONObject importPart = (JSONObject) astPart.get("importClause");
		JSONArray importObjects;
		JSONObject foundImport;
		
		if (importPart != null) {
			importObjects = ((JSONArray) ((JSONObject) importPart.get("namedBindings")).get("elements"));
			for (Object processedObject: importObjects) {
				String importName = (String) ((JSONObject) ((JSONObject) processedObject).get("name")).get("escapedText");
				if (importNames.equals(importName)) { //must match!!!!importNames.contains(importName)
					return astPart;
				}
			}
		}
		
		String key;
		Object entryValue;
		JSONObject entryJSONObject;
		JSONArray statementsArray;
		for(Object entryKey: astPart.keySet()) {
			key = (String) entryKey;
			entryValue = astPart.get(key);
			if (entryValue instanceof JSONObject) {
				entryJSONObject = (JSONObject) entryValue;
				foundImport = this.searchForImport(entryJSONObject, importNames);
				if (foundImport != null) {
					return foundImport;
				}
			} else if(entryValue instanceof JSONArray) {
				statementsArray = (JSONArray) entryValue;
				for (int index = 0; index < statementsArray.size(); index++) {
					entryJSONObject = (JSONObject) statementsArray.get(index);
					foundImport = this.searchForImport(entryJSONObject, importNames);
					if (foundImport != null) {
						return foundImport;
					}
				}
			}
		}
		return null;
	}
	
	//									[1. object]																						[2. object]
	//@DecoratorTypesService.skipLineVariableDeclaration({"puzzleAlgorithmType": "['JIGSAW', 'JIGSAW2']"}, "[IMPORT=DrawBordersService]") var newA;
	//[3. object] -> import { DrawBordersService } from './puzzleGenerator/puzzleRenderingAlgorithm/algorithm1/draw-borders.service';
	//
}

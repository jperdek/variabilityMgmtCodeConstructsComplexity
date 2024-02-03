package astFileProcessor.processors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import astFileProcessor.astObjects.ASTImportStatement;


public class ImportProcessor {

	private List<ASTImportStatement> allImports = new ArrayList<ASTImportStatement>();
	
	
	public ImportProcessor() {
	}
	
	public void getImports(JSONObject jsonObject) {
		Set<Map.Entry<String, Object>> entrySet = jsonObject.entrySet();
		Iterator<Map.Entry<String, Object>> iterator = entrySet.iterator();
		ASTImportStatement importStatement;
		String importName, importedFrom;
		String keyName;
		Object entryValue;
		JSONObject dataObject, importElement, arrayObject;
		JSONArray importElements, dataArray;
		Iterator<Object> importElementsIterator, dataArrayIterator;
	
		while(iterator.hasNext()) {
			Entry<String, Object> entry = iterator.next();
			keyName = entry.getKey().strip();
			entryValue = entry.getValue();
			if (entryValue instanceof JSONObject) {
				dataObject = (JSONObject) entryValue;
			} else if (entryValue instanceof JSONArray) {
				dataArray = (JSONArray) entryValue;
				dataArrayIterator = dataArray.iterator();
				while(dataArrayIterator.hasNext()) {
					arrayObject = (JSONObject) dataArrayIterator.next();
					this.getImports(arrayObject);
				}
				continue;
			} else {
				continue;
			}
			
			if (!keyName.equals("importClause")) {
				this.getImports(dataObject);
			} else {
				importElements = (JSONArray) ((JSONObject) dataObject.get("namedBindings")).get("elements");
				importElementsIterator = importElements.iterator();
				while(importElementsIterator.hasNext()) {
					importElement = (JSONObject) importElementsIterator.next();
					importName = (String) ((JSONObject) importElement.get("name")).get("escapedText");
					importedFrom = (String) ((JSONObject) jsonObject.get("moduleSpecifier")).get("text");
					importStatement = new ASTImportStatement(importName, importedFrom);
					this.allImports.add(importStatement);
				}
			}
		}
		
	}
	
	public void printImports() {
		Iterator<ASTImportStatement> importIterator = this.allImports.iterator();
		ASTImportStatement importStatement;
		
		while(importIterator.hasNext()) {
			importStatement = importIterator.next();
			System.out.println(importStatement.getImportName() + " from " + importStatement.getImportFrom());
		}
	}
	
	public static JSONObject searchAndRemoveImports(JSONObject astPart, Set<String> importNamesToRemove) {
		JSONObject importPart = (JSONObject) astPart.get("importClause");
		JSONArray importObjects;
		JSONObject foundImport, nameBindings ;
		
		if (importPart != null) {
			nameBindings = (JSONObject) importPart.get("namedBindings");
			if (nameBindings == null) { return null; }
			importObjects = ((JSONArray) nameBindings.get("elements"));

			if (importObjects == null) { return null; }
			for (Object processedObject: importObjects) {
				String importName = (String) ((JSONObject) ((JSONObject) processedObject).get("name")).get("escapedText");
				for(String importNameToRemove: importNamesToRemove) {
					if (importNameToRemove.equals(importName)) { //must match!!!!importNames.contains(importName)
						return astPart;
					}
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
				foundImport = ImportProcessor.searchAndRemoveImports(entryJSONObject, importNamesToRemove);
			} else if(entryValue instanceof JSONArray) {
				statementsArray = (JSONArray) entryValue;
				int index = 0;
				while (index < statementsArray.size()) {
					entryJSONObject = (JSONObject) statementsArray.get(index);
					foundImport = ImportProcessor.searchAndRemoveImports(entryJSONObject, importNamesToRemove);
					if (foundImport != null) {
						statementsArray.remove(foundImport);
					} else {
						index++;
					}
				}
			}
		}
		return null;
	}
}

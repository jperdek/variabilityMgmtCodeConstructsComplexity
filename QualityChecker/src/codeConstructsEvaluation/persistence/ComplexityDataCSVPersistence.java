package codeConstructsEvaluation.persistence;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import codeConstructsEvaluation.ComplexityMeasurement;
import codeConstructsEvaluation.ComplexityRecord;

import java.util.Set;


public class ComplexityDataCSVPersistence {

	private String filesFileName = "files";
	private String classesFileName = "classes";
	private String methodsFileName = "methods";
	private String generalInformation = "general";
	private String defaultPath = "./";
	private String additionalNamePart = "";
	

	public ComplexityDataCSVPersistence(String defaultPath) {
		this.defaultPath = defaultPath;
	}
	
	public void evaluateReusableQualityToCSV(List<ComplexityMeasurement> complexityMeasurements, 
			String csvFilePathDictionary) throws IOException {
		Map<String, List<String>> columnNamesToTypeMap = new HashMap<String, List<String>>();
		Map<String, List<StringBuilder>> contentToTypeMap = new HashMap<String, List<StringBuilder>>();
		for (ComplexityMeasurement complexityMeasurement: complexityMeasurements) {
			parseRecordObjects(complexityMeasurement.getComplexityRecordsEntrySet(), columnNamesToTypeMap, contentToTypeMap);
		}
		this.saveRecords(csvFilePathDictionary, columnNamesToTypeMap, contentToTypeMap);
	}
	
	public void setAdditionalNamePart(String additionalNamePart) {
		this.additionalNamePart = additionalNamePart;
	}
	
	private String getMappedName(String className) {
		if (className.contains("File")) {
			return this.filesFileName;
		} else if (className.contains("Method")) {
			return this.methodsFileName;
		} else if (className.contains("Class")) {
			return this.classesFileName;
		} else {
			return this.generalInformation + className.substring(0, className.indexOf("|"));
		}
	}
	
	private boolean testOnGeneralContent(String className) {
		if (className.contains("File")) {
			return false;
		} else if (className.contains("Method")) {
			return false;
		} else if (className.contains("Class")) {
			return false;
		} else {
			return true;
		}
	}
	
	private void saveRecords(String csvFilePathDictionary, Map<String, List<String>> columnNamesToTypeMap, 
			Map<String, List<StringBuilder>> contentToTypeMap) throws IOException {
		File csvFile;
		Map<String, FileWriter> createdWriters = new HashMap<String, FileWriter>();
		FileWriter fileWriter;
		String className, mappedName;
		List<StringBuilder> lines;
		for(Entry<String, List<StringBuilder>> analyzedEntry: contentToTypeMap.entrySet()) {
			lines = analyzedEntry.getValue();
			className = analyzedEntry.getKey();
			
			fileWriter = createdWriters.get(className);
			if (fileWriter == null) {
				 mappedName = this.getMappedName(className);
				 csvFile = new File(this.defaultPath + csvFilePathDictionary + "/" + mappedName + this.additionalNamePart + ".csv");
				 fileWriter = new FileWriter(csvFile);
				 createdWriters.put(className, fileWriter);
				 fileWriter.append(String.join(";", columnNamesToTypeMap.get(className).toArray(new String[0])));
			}
			for (StringBuilder line: lines) {
				fileWriter.write(line.toString());
			}
		}
		
		for(Entry<String, FileWriter> fileWriterInstance: createdWriters.entrySet()) {
			fileWriter = fileWriterInstance.getValue();
			fileWriter.close();
		}
	}
	
	private void parseRecordObjects(Set<Entry<String, ? extends ComplexityRecord>> analyzedEntries, Map<String, List<String>> columnNamesToTypeMap,
			Map<String, List<StringBuilder>> contentToTypeMap) {
		String complexityRecordName;
		String objectClass;
		List<String> columnNames;
		ComplexityRecord complexityRecord;
		List<StringBuilder> fileContents;
		Set<Entry<String, ? extends ComplexityRecord>> analyzedEntriesOfRecord;
		for(Entry<String, ? extends ComplexityRecord> analyzedEntry: analyzedEntries) {
			StringBuilder line = new StringBuilder();
			complexityRecord = analyzedEntry.getValue();
			complexityRecordName = analyzedEntry.getKey();
			objectClass = complexityRecord.getClass().getTypeName();
			if (this.testOnGeneralContent(objectClass)) {
				objectClass = complexityRecordName.split(" ")[0] + "|" + objectClass;
			}
			fileContents = contentToTypeMap.get(objectClass);
			if (fileContents == null) {
				fileContents = new ArrayList<StringBuilder>();
				contentToTypeMap.put(objectClass, fileContents);
			}
			columnNames = columnNamesToTypeMap.get(objectClass);
			if (columnNames == null) {
				columnNames = new ArrayList<String>();
				complexityRecord.putColumnName(columnNames, true);
				
				columnNames.add("\n");
				columnNamesToTypeMap.put(objectClass, columnNames);
			}
			complexityRecord.writeToCSV(line, true);
			line.append("\n");

			fileContents.add(line);
			analyzedEntriesOfRecord = complexityRecord.getComplexityRecordsEntrySet();
			if (analyzedEntriesOfRecord != null) {
				parseRecordObjects(analyzedEntriesOfRecord, columnNamesToTypeMap, contentToTypeMap);
			}
		}
	}
}

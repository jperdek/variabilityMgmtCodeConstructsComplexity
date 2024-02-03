package codeConstructsEvaluation;

import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import astFileProcessor.astObjects.ASTGenericDecorator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import codeConstructsEvaluation.entities.FileDependency;


public class FileComplexityRecord extends ComplexityRecord implements EntityComplexityDifference  {
	
	private Map<String, MethodComplexityRecord> methodsMap; //also functions in JavaScript
	private Map<String, ClassComplexityRecord> classesMap;
	private Map<String, FileDependency> imports;
	private String fileName;

	public static enum AnalyzedFileContentType { AFTER, BEFORE, STANDALONE };
	private String analyzedFileContent = null;
	private String analyzedFileContentBefore = null;
	private String analyzedFileContentAfter = null;
	

	public FileComplexityRecord(String fileName) {
		super(ComplexityRecord.SourceType.FILE);
		this.fileName = fileName;
		this.methodsMap = new HashMap<String, MethodComplexityRecord>();
		this.classesMap = new HashMap<String, ClassComplexityRecord>();
		this.imports = new HashMap<String, FileDependency>();
	}
	
	public void putAnalyzedFileContent(String analyzedFileContent, AnalyzedFileContentType contentType) {
		this.analyzedFileContent = analyzedFileContent;
		if (contentType == AnalyzedFileContentType.AFTER) {
			this.analyzedFileContentAfter = analyzedFileContent;
		} else if (contentType == AnalyzedFileContentType.BEFORE) {
			this.analyzedFileContentBefore = analyzedFileContent;
		}
	}
	
	public String getFileName() { return this.fileName; }
	
	public boolean isComparatoryInstance() { return this.analyzedFileContentBefore != null || this.analyzedFileContentAfter != null; }

	public String getAnalyzedFileContent() { return this.analyzedFileContent; }
	
	public String getAnalyzedFileContent(AnalyzedFileContentType contentType) {
		if (contentType == AnalyzedFileContentType.AFTER) {
			return this.analyzedFileContentAfter;
		} else if (contentType == AnalyzedFileContentType.BEFORE) {
			return this.analyzedFileContentBefore;
		}
		return this.analyzedFileContent; 
	}
	
	public void putAssociatedMethod(String methodName, MethodComplexityRecord methodComplexityRecord) {
		this.methodsMap.put(methodName, methodComplexityRecord);
	}
	
	public MethodComplexityRecord getAssociatedMethod(String methodName) {
		return this.methodsMap.get(methodName);
	}
	
	public Iterator<MethodComplexityRecord> getMethodsRecordsComplexityIterator() {
		return this.methodsMap.values().iterator();
	}
	
	public void putAssociatedClass(String className, ClassComplexityRecord classComplexityRecord) {
		this.classesMap.put(className, classComplexityRecord);
	}
	
	public ClassComplexityRecord getAssociatedClass(String className) {
		return this.classesMap.get(className);
	}
	
	public Iterator<ClassComplexityRecord> getClassRecordsComplexityIterator() {
		return this.classesMap.values().iterator();
	}
	
	public void addOrUpdateImport(String path, String type, int importOccurence) {
		FileDependency fileDependency = new FileDependency(path, type, importOccurence);
		this.imports.put(path, fileDependency);
	}
	
	public void addOrUpdateImport(String path, String type) {
		FileDependency fileDependency = new FileDependency(path, type);
		this.imports.put(path, fileDependency);
	}
	
	public void associateDecorators(List<ASTGenericDecorator> frameworkDecorators) {
		this.associatedDecorators = new ArrayList<ASTGenericDecorator>(frameworkDecorators);
		for (ComplexityRecord complexityRecord: this.methodsMap.values()) {
			complexityRecord.associateDecorators(this.associatedDecorators);
		}
		for (ComplexityRecord complexityRecord: this.classesMap.values()) {
			complexityRecord.associateDecorators(this.associatedDecorators);
		}
	}
	
	@Override
	public EntityComplexityDifference makeDifference(EntityComplexityDifference compareWith) {
		ComplexityRecord baseDifference = (ComplexityRecord) super.makeDifference(compareWith);
		FileComplexityRecord compareWithFileComplexityRecord = (FileComplexityRecord) compareWith;
		FileComplexityRecord complexityFileRecordDifference = new FileComplexityRecord(this.fileName);
		complexityFileRecordDifference.updateAccordingTo(baseDifference);
		
		if (this.associatedDecorators != null) {
			complexityFileRecordDifference.associateDecorators(associatedDecorators);
		}
		
		String recordMethodName;
		MethodComplexityRecord recordMethodDifference, 
		baseComplexityMethodRecord, comparedWithComplexityMethodRecord;
		for(Entry<String, MethodComplexityRecord> complexityMethodRecord: this.methodsMap.entrySet()) {
			recordMethodName = complexityMethodRecord.getKey();
			baseComplexityMethodRecord = complexityMethodRecord.getValue();
			comparedWithComplexityMethodRecord = compareWithFileComplexityRecord.getAssociatedMethod(recordMethodName);
			if (comparedWithComplexityMethodRecord == null) {
				complexityFileRecordDifference.putAssociatedMethod(recordMethodName, baseComplexityMethodRecord);
			} else {
				recordMethodDifference = (MethodComplexityRecord) baseComplexityMethodRecord.makeDifference(
						comparedWithComplexityMethodRecord);
				complexityFileRecordDifference.putAssociatedMethod(recordMethodName, recordMethodDifference);
			}	
		}
		
		String recordClassName;
		ClassComplexityRecord recordClassDifference, 
		baseComplexityClassRecord, comparedWithComplexityClassRecord;
		for(Entry<String, ClassComplexityRecord> complexityClassRecord: this.classesMap.entrySet()) {
			recordClassName = complexityClassRecord.getKey();
			baseComplexityClassRecord = complexityClassRecord.getValue();
			comparedWithComplexityClassRecord = compareWithFileComplexityRecord.getAssociatedClass(recordClassName);
			if (comparedWithComplexityClassRecord == null) {
				complexityFileRecordDifference.putAssociatedClass(recordClassName, baseComplexityClassRecord);
			} else {
				recordClassDifference = (ClassComplexityRecord) baseComplexityClassRecord.makeDifference(
						comparedWithComplexityClassRecord);
				complexityFileRecordDifference.putAssociatedClass(recordClassName, recordClassDifference);
			}	
		}

		complexityFileRecordDifference.putAnalyzedFileContent(this.analyzedFileContent, AnalyzedFileContentType.BEFORE);
		complexityFileRecordDifference.putAnalyzedFileContent(
					compareWithFileComplexityRecord.getAnalyzedFileContent(), AnalyzedFileContentType.AFTER);
		
		return complexityFileRecordDifference;
	}
	
	@Override
	public void print() {
		System.out.println();
		System.out.println("XXXXXXXX| FILE |XXXXXXX");
		super.print();
		String methodName, className;
		System.out.println("XXX| File methods: (" + this.methodsMap.size() + ")");
		for(Entry<String, MethodComplexityRecord> complexityMethodRecord: this.methodsMap.entrySet()) {
			methodName = complexityMethodRecord.getKey();
			complexityMethodRecord.getValue().print();
			System.out.println("XXX __METHOD_END__ " + methodName + " ___XXXX");
		}

		System.out.println("XXX| File classes: (" + this.classesMap.size() + ")");
		for(Entry<String, ClassComplexityRecord> complexityClassRecord: this.classesMap.entrySet()) {
			className = complexityClassRecord.getKey();
			complexityClassRecord.getValue().print();
			System.out.println("XXX __CLASS_END___ " + className + " ___XXXX");
		}
		System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
		System.out.println();
		System.out.println();
	}
	
	@Override
	public void putColumnName(List<String> columnNames, boolean aggregated) {
		super.putColumnName(columnNames, aggregated);
		
		//columnNames.add(suffixName + " File name");
		columnNames.add("File name");
		columnNames.add("File methods");
		columnNames.add("File classes");
		columnNames.add("File framework decorators");
		
		if (!aggregated) {
			for(Entry<String, MethodComplexityRecord> complexityMethodRecord: this.methodsMap.entrySet()) {
				//methodName = complexityMethodRecord.getKey();
				complexityMethodRecord.getValue().putColumnName(columnNames, aggregated);
			}
	
			for(Entry<String, ClassComplexityRecord> complexityClassRecord: this.classesMap.entrySet()) {
				//className = complexityClassRecord.getKey();
				complexityClassRecord.getValue().putColumnName(columnNames, aggregated);
			}
		}
	}
	
	@Override
	public void writeToCSV(StringBuilder content, boolean aggregated) {
		super.writeToCSV(content, aggregated);
		
		content.append("FILE: " + this.fileName);
		content.append(';');
		content.append(String.valueOf(this.methodsMap.size()).replace(".", ","));
		content.append(';');
		content.append(String.valueOf(this.classesMap.size()).replace(".", ","));
		content.append(';');
		content.append(String.valueOf(this.getNumberAssociatedDecorators()).replace(".", ","));
		content.append(';');
		
		if (!aggregated) {
			for(Entry<String, MethodComplexityRecord> complexityMethodRecord: this.methodsMap.entrySet()) {
				complexityMethodRecord.getValue().writeToCSV(content, aggregated);
			}
	
			for(Entry<String, ClassComplexityRecord> complexityClassRecord: this.classesMap.entrySet()) {
				complexityClassRecord.getValue().writeToCSV(content, aggregated);
			}
		}
	}
	
	@Override
	public Set<Entry<String, ? extends ComplexityRecord>> getComplexityRecordsEntrySet() {
		Set<Entry<String, ? extends ComplexityRecord>> usedSet = new HashSet<Entry<String, ? extends ComplexityRecord>>();
		for(Entry<String, MethodComplexityRecord> complexityMethodRecord: this.methodsMap.entrySet()) {
			usedSet.add(complexityMethodRecord);
		}
		for(Entry<String, ClassComplexityRecord> complexityMethodRecord: this.classesMap.entrySet()) {
			usedSet.add(complexityMethodRecord);
		}
		return usedSet;
	}
}

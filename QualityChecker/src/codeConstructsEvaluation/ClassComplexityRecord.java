package codeConstructsEvaluation;

import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import astFileProcessor.astObjects.ASTGenericDecorator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


public class ClassComplexityRecord extends ComplexityRecord implements EntityComplexityDifference  {
	
	private Map<String, ComplexityRecord> associatedRecords;
	private Map<String, MethodComplexityRecord> classMethods;
	private double maintainability;
	private String className;
	
	
	public ClassComplexityRecord(String className, double maintainability) {
		super(ComplexityRecord.SourceType.CLASS);
		this.associatedRecords = new HashMap<String, ComplexityRecord>();
		this.classMethods = new HashMap<String, MethodComplexityRecord>();
		this.className = className;
		this.maintainability = maintainability;
	}
	
	public String getClassName() { return this.className; }
	
	public double getMaintainability() { return this.maintainability; }
	
	public void putClassMethod(String methodName, MethodComplexityRecord methodComplexityRecord) {
		this.classMethods.put(methodName, methodComplexityRecord);
	}
	
	public MethodComplexityRecord getMethodComplexityRecord(String methodName) {
		return this.classMethods.get(methodName);
	}
	
	public void putAssociatedRecord(String recordIdentifier, ComplexityRecord complexityRecord) {
		this.associatedRecords.put(recordIdentifier, complexityRecord);
	}
	
	public ComplexityRecord getAssociatedRecord(String recordIdentifier) {
		return this.associatedRecords.get(recordIdentifier);
	}
	
	private ClassComplexityRecord instantiateClassDifference(ClassComplexityRecord compareWith) {
		String className = this.className;
		if (!className.equals(compareWith.getClassName())) {
			className = className + " != " + compareWith.getClassName();
		}
		double maintainabilityDifference = this.maintainability - compareWith.getMaintainability();
		return new ClassComplexityRecord(className, maintainabilityDifference);
	}
	
	public void associateDecorators(List<ASTGenericDecorator> frameworkDecorators) {
		this.associatedDecorators = new ArrayList<ASTGenericDecorator>(frameworkDecorators);
		for (ComplexityRecord complexityRecord: this.classMethods.values()) {
			complexityRecord.associateDecorators(this.associatedDecorators);
		}
		for (ComplexityRecord complexityRecord: this.associatedRecords.values()) {
			complexityRecord.associateDecorators(this.associatedDecorators);
		}
	}
	
	@Override
	public EntityComplexityDifference makeDifference(EntityComplexityDifference compareWith) {
		ComplexityRecord baseDifference = (ComplexityRecord) super.makeDifference(compareWith);
		ClassComplexityRecord compareWithClassComplexityRecord = (ClassComplexityRecord) compareWith;
		ClassComplexityRecord complexityClassRecordDifference = this.instantiateClassDifference(
				compareWithClassComplexityRecord);
		complexityClassRecordDifference.updateAccordingTo(baseDifference);

		if (this.associatedDecorators != null) {
			complexityClassRecordDifference.associateDecorators(this.associatedDecorators);
		}
	
		String recordMethodName;
		MethodComplexityRecord recordMethodDifference, 
		baseComplexityMethodRecord, comparedWithComplexityMethodRecord;
		for(Entry<String, MethodComplexityRecord> complexityMethodRecord: this.classMethods.entrySet()) {
			recordMethodName = complexityMethodRecord.getKey();
			baseComplexityMethodRecord = complexityMethodRecord.getValue();
			comparedWithComplexityMethodRecord = compareWithClassComplexityRecord.getMethodComplexityRecord(recordMethodName);
			if (comparedWithComplexityMethodRecord == null) {
				complexityClassRecordDifference.putClassMethod(recordMethodName, baseComplexityMethodRecord);
			} else {
				recordMethodDifference = (MethodComplexityRecord) baseComplexityMethodRecord.makeDifference(
						comparedWithComplexityMethodRecord);
				complexityClassRecordDifference.putClassMethod(recordMethodName, recordMethodDifference);
			}	
		}
		
		ComplexityRecord recordDifference,
		baseComplexityAssociatedRecord, comparedWithComplexityAssociatedRecord;
		for(Entry<String, ComplexityRecord> complexityAssociatedRecord: this.associatedRecords.entrySet()) {
			recordMethodName = complexityAssociatedRecord.getKey();
			baseComplexityAssociatedRecord = complexityAssociatedRecord.getValue();
			comparedWithComplexityAssociatedRecord = compareWithClassComplexityRecord.getAssociatedRecord(recordMethodName);
			if (comparedWithComplexityAssociatedRecord == null) {
				complexityClassRecordDifference.putAssociatedRecord(recordMethodName, baseComplexityAssociatedRecord);
			} else {
				recordDifference = (ComplexityRecord) baseComplexityAssociatedRecord.makeDifference(
						comparedWithComplexityAssociatedRecord);
				complexityClassRecordDifference.putAssociatedRecord(recordMethodName, recordDifference);
			}	
		}
		
		return complexityClassRecordDifference;
	}
	
	@Override
	public void print() {	
		System.out.println("+++| CLASS: " + this.className + " (maintainability: " + this.maintainability + ")");
		super.print();
		String methodName, recordName;
		
		System.out.println("+++| File methods: (" + this.classMethods.size() + ")");
		for(Entry<String, MethodComplexityRecord> complexityMethodRecord: this.classMethods.entrySet()) {
			methodName = complexityMethodRecord.getKey();
			complexityMethodRecord.getValue().print();
			System.out.println("+++ __METHOD_END__ " + methodName + " ___+++");
		}

		System.out.println("+++| Associated record: (" + this.associatedRecords.size() + ")");
		for(Entry<String, ComplexityRecord> complexityAssociatedRecord: this.associatedRecords.entrySet()) {
			recordName = complexityAssociatedRecord.getKey();
			System.out.println("+++| Associated record: (" + recordName + ")");
			complexityAssociatedRecord.getValue().print();
		}

		System.out.println("++++++++++++++++++++++++++++++++++++++++++++++");
	}
	
	@Override
	public void putColumnName(List<String> columnNames, boolean aggregated) {
		super.putColumnName(columnNames, aggregated);
		columnNames.add("Class Name");
		columnNames.add("Class Maintainability");
		columnNames.add("Class number methods");
		
		if (!aggregated) {
			for(Entry<String, MethodComplexityRecord> complexityMethodRecord: this.classMethods.entrySet()) {
				complexityMethodRecord.getValue().putColumnName(columnNames, aggregated);
			}
	
			for(Entry<String, ComplexityRecord> complexityAssociatedRecord: this.associatedRecords.entrySet()) {
				complexityAssociatedRecord.getValue().putColumnName(columnNames, aggregated);
			}
		}
	}
	
	@Override
	public void writeToCSV(StringBuilder content, boolean aggregated) {
		if (aggregated) { // WRITES AGGREGATED RESULTS FOR THE CLASS (for class methods, aggregatedAverage remains
			if (this.associatedRecords.size() >= 1 && this.associatedRecords.containsKey("AGGREGATE_CLASS")) {
				this.associatedRecords.get("AGGREGATE_CLASS").writeToCSV(content, aggregated);
			}
		} else {
			super.writeToCSV(content, aggregated);
		}
		content.append(this.className);
		content.append(';');
		content.append(String.valueOf(this.maintainability).replace(".", ","));
		content.append(';');
		content.append(String.valueOf(this.classMethods.size()).replace(".", ","));
		content.append(';');
		
		if (!aggregated) {
			for(Entry<String, MethodComplexityRecord> complexityMethodRecord: this.classMethods.entrySet()) {
				complexityMethodRecord.getValue().writeToCSV(content, aggregated);
			}
	
			for(Entry<String, ComplexityRecord> complexityAssociatedRecord: this.associatedRecords.entrySet()) {
				complexityAssociatedRecord.getValue().writeToCSV(content, aggregated);
			}
		} 
	}
	
	@Override
	public Set<Entry<String, ? extends ComplexityRecord>> getComplexityRecordsEntrySet() {
		Set<Entry<String, ? extends ComplexityRecord>> usedSet = new HashSet<Entry<String, ? extends ComplexityRecord>>();
		for(Entry<String, ComplexityRecord> complexityRecord: this.associatedRecords.entrySet()) {
			usedSet.add(complexityRecord);
		}
		for(Entry<String, MethodComplexityRecord> complexityMethodRecord: this.classMethods.entrySet()) {
			usedSet.add(complexityMethodRecord);
		}
		return usedSet;
	}
}

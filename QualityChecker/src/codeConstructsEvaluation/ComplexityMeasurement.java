package codeConstructsEvaluation;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import astFileProcessor.astObjects.ASTGenericDecorator;
import astFileProcessor.astObjects.ASTGenericDecorator.DecoratorAssociatedWith;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


public class ComplexityMeasurement implements EntityComplexityDifference {

	private String measurementType = "Single";
	private double maintainability = -1;
	private Map<String, ComplexityRecord> complexityMeasurement;
	private Map<String, ComplexityRecord> uniqueComplexityMeasurements = null;
	private Map<String, ComplexityMeasurement> associatedMeasurements = null;

	
	public ComplexityMeasurement() {
		this.complexityMeasurement = new HashMap<String, ComplexityRecord>();
		this.maintainability = -1;
	}
	
	public ComplexityMeasurement(String measurementType, double maintainability) {
		this();
		this.maintainability = maintainability;
		this.measurementType = measurementType;
	}
	
	public ComplexityMeasurement(String measurementType) {
		this();
		this.measurementType = measurementType;
	}
	
	public void associateDecoratorsWithFileUnit(List<ASTGenericDecorator> associatedDecorators) {
		for (ComplexityRecord complexityRecord: this.complexityMeasurement.values()) {
			if (complexityRecord instanceof FileComplexityRecord) {
				complexityRecord.associateDecorators(associatedDecorators);
			}
		}
		if (this.uniqueComplexityMeasurements != null) {
			for (ComplexityRecord complexityRecord: this.uniqueComplexityMeasurements.values()) {
				if (complexityRecord instanceof FileComplexityRecord) {
					complexityRecord.associateDecorators(associatedDecorators);
				}
			}
		}
		if(this.associatedMeasurements != null) {
			for (ComplexityMeasurement associatedMeasurement: this.associatedMeasurements.values()) {
				associatedMeasurement.associateDecoratorsWithFileUnit(associatedDecorators);
			}
		}
	}
	
	public void associateDecoratorsWithEverything(List<ASTGenericDecorator> associatedDecorators) {
		for (ComplexityRecord complexityRecord: this.complexityMeasurement.values()) {
			complexityRecord.associateDecorators(associatedDecorators);
		}
		if(this.associatedMeasurements != null) {
			for (ComplexityMeasurement associatedMeasurement: this.associatedMeasurements.values()) {
				associatedMeasurement.associateDecoratorsWithEverything(associatedDecorators);
			}
		}
	}
	
	public void associateDecorators(List<ASTGenericDecorator> associatedDecorators) {
		 Predicate<ASTGenericDecorator> byClass = associatedDecorator -> associatedDecorator.isAssociatedWith(DecoratorAssociatedWith.CLASS);
		 Predicate<ASTGenericDecorator> byMethod = associatedDecorator -> associatedDecorator.isAssociatedWith(DecoratorAssociatedWith.METHOD);
		for (ComplexityRecord complexityRecord: this.complexityMeasurement.values()) {
			if (complexityRecord instanceof FileComplexityRecord) {
				complexityRecord.associateDecorators(associatedDecorators);
			} else if (complexityRecord instanceof ClassComplexityRecord) {
				complexityRecord.associateDecorators(associatedDecorators.stream().filter(byClass).collect(Collectors.toList()));
			} else if (complexityRecord instanceof MethodComplexityRecord) {
				complexityRecord.associateDecorators(associatedDecorators.stream().filter(byMethod).collect(Collectors.toList()));
			}
		}
	}
	
	public void putAssociatedMeasurement(String measurementType, ComplexityMeasurement parentMeasurement) {
		if (this.associatedMeasurements == null) {
			this.associatedMeasurements = new HashMap<String, ComplexityMeasurement>();
		}
		this.associatedMeasurements.put(measurementType, parentMeasurement);
	}
	
	public void putUniqueComplexityMeasurement(String measurementType, ComplexityRecord uniqueComplexityRecord) {
		if (this.uniqueComplexityMeasurements == null) {
			this.uniqueComplexityMeasurements = new HashMap<String, ComplexityRecord>();
		}
		this.uniqueComplexityMeasurements.put(measurementType, uniqueComplexityRecord);
	}

	public double getMaintainability() { return this.maintainability; }
	
	public ComplexityMeasurement getAssociatedMeasurement(String associatedMeasurementIdentifier) {
		if (this.associatedMeasurements != null) {
			return this.associatedMeasurements.get(associatedMeasurementIdentifier);
		}
		return null;
	}
	public void changeMeasurementType(String measurementType) {
		this.measurementType = measurementType;
	}
	
	public ComplexityRecord getComplexityRecord(String recordName) {
		return this.complexityMeasurement.get(recordName);
	}
	
	public ComplexityRecord getComplexityRecord() {
		return this.complexityMeasurement.get("default");
	}
	
	public void setComplexityRecord(String recordName, ComplexityRecord complexityRecord) {
		this.complexityMeasurement.put(recordName, complexityRecord);
	}
	
	public void setComplexityRecord(ComplexityRecord complexityRecord) {
		this.complexityMeasurement.put("default", complexityRecord);
	}
	
	public String getMeasurementType() { return this.measurementType; }

	
	@Override
	public EntityComplexityDifference makeDifference(EntityComplexityDifference compareWith) {
		ComplexityMeasurement compareWithMeasurement = (ComplexityMeasurement) compareWith;
		ComplexityMeasurement complexityMeasurementDifference = new ComplexityMeasurement();
		String recordName;
		ComplexityRecord recordDifference, baseComplexityRecord, comparedWithComplexityRecord;
		for(Entry<String, ComplexityRecord> complexityRecord: this.complexityMeasurement.entrySet()) {
			recordName = complexityRecord.getKey();
			baseComplexityRecord = complexityRecord.getValue();
			comparedWithComplexityRecord = compareWithMeasurement.getComplexityRecord(recordName);
			if (comparedWithComplexityRecord == null) {
				complexityMeasurementDifference.putUniqueComplexityMeasurement(recordName, baseComplexityRecord);
			} else {
				recordDifference = (ComplexityRecord) baseComplexityRecord.makeDifference(comparedWithComplexityRecord);
				complexityMeasurementDifference.setComplexityRecord(recordName, recordDifference);
			}
			
		}
		return complexityMeasurementDifference;
	}

	@Override
	public void print() {
		String recordName;
		System.out.println("##########| COMPLEXITY MEASUREMENT (maintainability: " + this.maintainability + 
				", measurements: " + this.complexityMeasurement.size() + ") |#########");
		for(Entry<String, ComplexityRecord> complexityRecord: this.complexityMeasurement.entrySet()) {
			recordName = complexityRecord.getKey();
			System.out.println("######## ___ RECORD " + recordName + " ___ ##########");
			complexityRecord.getValue().print();
			System.out.println("########_____RECORD_END___ ##########");
		}
		System.out.println("################################################");
	}
	
	@Override
	public void putColumnName(List<String> columnNames, boolean aggregated) {
		columnNames.add("Maintainability");
		for(Entry<String, ComplexityRecord> complexityRecord: this.complexityMeasurement.entrySet()) {
			//recordName = complexityRecord.getKey();
			complexityRecord.getValue().putColumnName(columnNames, aggregated);
		}
	}
	
	@Override
	public void writeToCSV(StringBuilder content, boolean aggregated) {
		content.append(String.valueOf(this.maintainability).replace(".", ","));
		content.append(';');
		for(Entry<String, ComplexityRecord> complexityRecord: this.complexityMeasurement.entrySet()) {
			complexityRecord.getValue().writeToCSV(content, aggregated);
		}
	}
	
	@Override
	public Set<Entry<String, ? extends ComplexityRecord>> getComplexityRecordsEntrySet() {
		Set<Entry<String, ? extends ComplexityRecord>> usedSet = new HashSet<Entry<String, ? extends ComplexityRecord>>();
		for(Entry<String, ComplexityRecord> complexityMethodRecord: this.complexityMeasurement.entrySet()) {
			usedSet.add(complexityMethodRecord);
		}

		return usedSet;
	}
}


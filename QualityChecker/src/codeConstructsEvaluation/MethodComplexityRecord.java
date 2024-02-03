package codeConstructsEvaluation;

import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import astFileProcessor.astObjects.ASTGenericDecorator;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;


public class MethodComplexityRecord extends ComplexityRecord implements EntityComplexityDifference {

	private Map<String, MethodComplexityRecord> nestedMethods;
	private String methodName;
	private int maxNestedMethodDepth;
	private List<String> parameterNames;
	private String associatedClassName = "Not class";

	
	public MethodComplexityRecord(String methodName, int maxNestedMethodDepth) {
		super(ComplexityRecord.SourceType.CLASS_METHOD);
		this.methodName = methodName;
		this.maxNestedMethodDepth = maxNestedMethodDepth;
		this.parameterNames = new ArrayList<String>();
		this.nestedMethods = new HashMap<String, MethodComplexityRecord>();
	}
	
	public void setAssociatedClassName(String associatedClassName) { this.associatedClassName = associatedClassName; }
	
	public String getAssociatedClassName() { return this.associatedClassName; }
	
	public int getMaxNestedMethodDepth() { return this.maxNestedMethodDepth; }
	
	public void addParameterName(String parameterName) { this.parameterNames.add(parameterName); }
	
	public boolean hasParameter(String parameterName) { return this.parameterNames.contains(parameterName); }
	
	public String[] getParameterNames() { return this.parameterNames.toArray(new String[0]); }

	public void putNestedMethod(String nestedMethodName, MethodComplexityRecord nestedmethodComplexityRecord) {
		this.nestedMethods.put(nestedMethodName, nestedmethodComplexityRecord);
	}
	
	public MethodComplexityRecord getNestedRecord(String nestedMethodName) {
		return this.nestedMethods.get(nestedMethodName);
	}
	
	public String getMethodName() { return this.methodName; }
	
	public void setMethodName(String methodName) { this.methodName = methodName; }
	
	private MethodComplexityRecord instantiateMethodDifference(MethodComplexityRecord comparedWith) {
		String methodName = this.methodName;
		if (!methodName.equals(comparedWith.getMethodName())) {
			methodName = methodName + " != " + comparedWith.getMethodName();
		}
		return new MethodComplexityRecord(methodName, this.maxNestedMethodDepth);
	}
	
	public void associateDecorators(List<ASTGenericDecorator> frameworkDecorators) {
		this.associatedDecorators = new ArrayList<ASTGenericDecorator>(frameworkDecorators);
		for (ComplexityRecord complexityRecord: this.nestedMethods.values()) {
			complexityRecord.associateDecorators(this.associatedDecorators);
		}
	}
	
	@Override
	public EntityComplexityDifference makeDifference(EntityComplexityDifference compareWith) {
		ComplexityRecord baseDifference = (ComplexityRecord) super.makeDifference(compareWith);
		MethodComplexityRecord compareWithMethodComplexityRecord = (MethodComplexityRecord) compareWith;
		MethodComplexityRecord complexityMethodRecordDifference = this.instantiateMethodDifference(
				compareWithMethodComplexityRecord);
		complexityMethodRecordDifference.setAssociatedClassName(this.associatedClassName);
		complexityMethodRecordDifference.updateAccordingTo(baseDifference);

		if (this.associatedDecorators != null) {
			complexityMethodRecordDifference.associateDecorators(this.associatedDecorators);
		}
	
		String recordMethodName;
		MethodComplexityRecord recordMethodDifference, 
		baseComplexityMethodRecord, comparedWithComplexityMethodRecord;
		for(Entry<String, MethodComplexityRecord> complexityMethodRecord: this.nestedMethods.entrySet()) {
			recordMethodName = complexityMethodRecord.getKey();
			baseComplexityMethodRecord = complexityMethodRecord.getValue();
			comparedWithComplexityMethodRecord = compareWithMethodComplexityRecord.getNestedRecord(recordMethodName);
			if (comparedWithComplexityMethodRecord == null) {
				complexityMethodRecordDifference.putNestedMethod(recordMethodName, baseComplexityMethodRecord);
			} else {
				recordMethodDifference = (MethodComplexityRecord) baseComplexityMethodRecord.makeDifference(
						comparedWithComplexityMethodRecord);
				complexityMethodRecordDifference.putNestedMethod(recordMethodName, recordMethodDifference);
			}	
		}
		
		for(String parameterName: this.parameterNames) {
			if (!this.hasParameter(parameterName)) {
				complexityMethodRecordDifference.addParameterName(parameterName);
			}
		}
		
		return complexityMethodRecordDifference;
	}

	@Override
	public void print() {	
		System.out.println("***| METHOD: " + this.methodName + " (max nested depth: " + this.maxNestedMethodDepth + ") |**************");
		super.print();
		String methodName;
		
		if (this.parameterNames.size() > 0) {
			System.out.print  ("***| Method Parameter Names |****");
			String identifier;
			Iterator<String> identifiersIterator = this.parameterNames.iterator();
			while(identifiersIterator.hasNext()) {
				identifier = identifiersIterator.next();
				System.out.print(identifier);
				if (identifiersIterator.hasNext()) {
					System.out.print(", ");
				} else {
					System.out.println();
				}
			}
		}
		
		System.out.println("***| Nested methods: (" + this.nestedMethods.size() + ")");
		for(Entry<String, MethodComplexityRecord> complexityMethodRecord: this.nestedMethods.entrySet()) {
			methodName = complexityMethodRecord.getKey();
			complexityMethodRecord.getValue().print();
			System.out.println("*** __METHOD_END__ " + methodName + " ___***");
		}
		
		System.out.println("************************************************");
	}
	
	@Override
	public void putColumnName(List<String> columnNames, boolean aggregated) {
		super.putColumnName(columnNames, aggregated);
		
		columnNames.add("Method Name");
		columnNames.add("Method Max Depth");
		columnNames.add("Method Number Nested Methods");
		columnNames.add("Method Parameter Names");
		columnNames.add("Associated Class Name");
		if (!aggregated) {
			for(Entry<String, MethodComplexityRecord> complexityMethodRecord: this.nestedMethods.entrySet()) {
				complexityMethodRecord.getValue().putColumnName(columnNames, aggregated);
			}
		}
	}
	
	@Override
	public void writeToCSV(StringBuilder content, boolean aggregated) {
		super.writeToCSV(content, aggregated);
		content.append(this.methodName);
		content.append(';');
		content.append(String.valueOf(this.maxNestedMethodDepth).replace(".", ","));
		content.append(';');
		content.append(String.valueOf(this.nestedMethods.size()).replace(".", ","));
		content.append(';');
		content.append(String.join(",", this.parameterNames.toArray(new String[0])));
		content.append(';');
		content.append(this.associatedClassName);
		content.append(';');
		if (!aggregated) {
			for(Entry<String, MethodComplexityRecord> complexityMethodRecord: this.nestedMethods.entrySet()) {
				complexityMethodRecord.getValue().writeToCSV(content, aggregated);
			}
		}
	}
	
	@Override
	public Set<Entry<String, ? extends ComplexityRecord>> getComplexityRecordsEntrySet() {
		Set<Entry<String, ? extends ComplexityRecord>> usedSet = new HashSet<Entry<String, ? extends ComplexityRecord>>();
		for(Entry<String, MethodComplexityRecord> complexityMethodRecord: this.nestedMethods.entrySet()) {
			usedSet.add(complexityMethodRecord);
		}
		return usedSet;
	}

}

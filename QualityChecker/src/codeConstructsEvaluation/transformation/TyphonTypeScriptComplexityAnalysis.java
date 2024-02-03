package codeConstructsEvaluation.transformation;


import java.io.IOException;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import codeConstructsEvaluation.ClassComplexityRecord;
import codeConstructsEvaluation.ComplexityConstructEvaluationConfiguration;
import codeConstructsEvaluation.ComplexityMeasurement;
import codeConstructsEvaluation.ComplexityRecord;
import codeConstructsEvaluation.FileComplexityRecord;
import codeConstructsEvaluation.MethodComplexityRecord;
import codeConstructsEvaluation.entities.CyclomaticComplexity;
import codeConstructsEvaluation.entities.HalsteadMeasures;
import codeConstructsEvaluation.entities.IdentifiersRecord;
import codeConstructsEvaluation.entities.SourceLinesOfCodeMeasure;


public class TyphonTypeScriptComplexityAnalysis implements ComplexityService{

	public TyphonTypeScriptComplexityAnalysis() { }
	public static final String TYPE = "TYPHONE";
	public static final String METHOD_AVERAGE = "METHOD_AVERAGE";
	public static final String AGGREGATE = "AGGREGATE";
	public static final String OVERALL_AVERAGE = "OVERALL_AVERAGE";
	public static final String METHOD_AVERAGE_CLASS = "METHOD_AVERAGE_CLASS";
	public static final String AGGREGATE_CLASS = "AGGREGATE_CLASS";
	public static final String OVERALL_AVERAGE_CLASS = "OVERALL_AVERAGE_CLASS";
	
	
	private static String privateKeywordHandling(String fileContent) {
		return fileContent.replace("private", "");
	}
	
	/**
	 * Works for simple types without more than one <> (such as <any>, <type1>,... and not <T<a>>)
	 * @param fileContent
	 * @return
	 */
	private static String manageSimpleTypeHandling(String fileContent) {
		return fileContent.replaceAll("\\(\s*<([^>]+)>\s*([^/);,(-+*%^]+)\\)", "($1 as $2)");
	}
	
	private static String manageExtensiveComments(String fileContent) {
		String newFileContent = fileContent;
		String substringNative;
		String substringChanged;
		int lastPosition;
		for (int index = fileContent.indexOf("/**"); index >= 0; index = fileContent.indexOf("/**", index + 1)) {
			lastPosition = fileContent.substring(index).indexOf("*/") + 2;
			substringNative = fileContent.substring(index, index + lastPosition);
			substringChanged = "//" + substringNative.replaceAll("\n", "\n//");
			newFileContent = newFileContent.replace(substringNative, substringChanged);
		}
		return newFileContent;
	}
	
	public ComplexityMeasurement prepareComplexityMeasurement(String fileName, 
			String fileContent, boolean storeFileContent) throws IOException, InterruptedException {
		JSONObject configRoot = this.getJSONComplexityReport(fileName, fileContent);
		return this.createComplexityMeasurement(fileName, configRoot, fileContent, storeFileContent);	
	}
	
	public JSONObject getJSONComplexityReport(String fileName, String fileContent) throws IOException, InterruptedException {
		fileContent = this.doCleaning(fileContent);
		String typhonUrl = ComplexityConstructEvaluationConfiguration.SERVER_URL + ComplexityConstructEvaluationConfiguration.TYPHON_SERVICE_URL;
		JSONObject configRoot = JSONResponseReader.loadJSONConfig(PostRequester.doPost(typhonUrl, fileContent));
		return configRoot;
	}
	
	private SourceLinesOfCodeMeasure getSourceLinesOfCodeMeasure(JSONObject recordPart) {
		double physicalLOC = loadDobleOrLongAsDouble(recordPart.get("physical"));
		double logicalLOC = loadDobleOrLongAsDouble(recordPart.get("logical"));
		return  new SourceLinesOfCodeMeasure(physicalLOC, logicalLOC);
	}
	
	private IdentifiersRecord loadIdentifiersRecord(JSONObject recordPart) {
		double distinctIdentifiers = loadDobleOrLongAsDouble(recordPart.get("distinct"));
		double totalIdentifiers = loadDobleOrLongAsDouble(recordPart.get("total"));
		IdentifiersRecord newIdentifiersRecord = new IdentifiersRecord(distinctIdentifiers, totalIdentifiers);
		if (recordPart.containsKey("identifiers")) {
			JSONArray identifiers = (JSONArray) recordPart.get("identifiers");
			Iterator<Object> identifiersIterator =  identifiers.iterator();
			String identifier;
			while (identifiersIterator.hasNext()) {
				identifier = (String) identifiersIterator.next();
				newIdentifiersRecord.addIdentifier(identifier);
			}
		} else {
			newIdentifiersRecord.setNoAssociatedIdentifiers();
		}
		return newIdentifiersRecord;
	}
	
	private HalsteadMeasures loadHalsteadMeasures(JSONObject recordPart) {
		double bugs = loadDobleOrLongAsDouble(recordPart.get("bugs"));
		double difficulty = loadDobleOrLongAsDouble(recordPart.get("difficulty"));
		double effort = loadDobleOrLongAsDouble(recordPart.get("effort"));
		double length = loadDobleOrLongAsDouble(recordPart.get("length"));
		double time = loadDobleOrLongAsDouble(recordPart.get("time"));
		double vocabulary = loadDobleOrLongAsDouble(recordPart.get("vocabulary"));
		double volume = loadDobleOrLongAsDouble(recordPart.get("volume"));
		IdentifiersRecord operators = this.loadIdentifiersRecord((JSONObject) recordPart.get("operators"));
		IdentifiersRecord operands = this.loadIdentifiersRecord((JSONObject) recordPart.get("operands"));
		return new HalsteadMeasures(bugs, difficulty, effort, length, time, vocabulary, volume, operators, operands);
	}
	
	private double loadDobleOrLongAsDouble(Object recordPart) {
		if (recordPart instanceof Long) {
			return ((Long) recordPart).doubleValue();
		} else {
			return (double) recordPart;
		}
	}
	
	private CyclomaticComplexity loadCyclomaticComplexity(JSONObject recordPart) {
		double cyclomatic = loadDobleOrLongAsDouble(recordPart.get("cyclomatic"));
		double cyclomaticDensity = loadDobleOrLongAsDouble(recordPart.get("cyclomaticDensity"));
		return new CyclomaticComplexity(cyclomatic, cyclomaticDensity);
	}
	
	private ComplexityRecord createComplexityRecord(JSONObject complexityJSONRecord) {	
		ComplexityRecord complexityRecord = new ComplexityRecord();
		CyclomaticComplexity cyclomaticComplexity = this.loadCyclomaticComplexity(complexityJSONRecord);
		HalsteadMeasures halsteadMeasures = this.loadHalsteadMeasures((JSONObject) complexityJSONRecord.get("halstead"));
		SourceLinesOfCodeMeasure slocMeasure = this.getSourceLinesOfCodeMeasure((JSONObject) complexityJSONRecord.get("sloc"));
		
		complexityRecord.setCyclomaticComplexity(cyclomaticComplexity);
		complexityRecord.setHalsteadMeasure(halsteadMeasures);
		complexityRecord.setSourceLinesOfCodeMeasure(slocMeasure);
		return complexityRecord;
	}
	
	private void addComplexityRecordsToComplexityRecord(JSONObject complexityJSONRecord, ComplexityRecord complexityRecord) {	
		CyclomaticComplexity cyclomaticComplexity = this.loadCyclomaticComplexity(complexityJSONRecord);
		HalsteadMeasures halsteadMeasures = this.loadHalsteadMeasures((JSONObject) complexityJSONRecord.get("halstead"));
		SourceLinesOfCodeMeasure slocMeasure = this.getSourceLinesOfCodeMeasure((JSONObject) complexityJSONRecord.get("sloc"));
		
		complexityRecord.setCyclomaticComplexity(cyclomaticComplexity);
		complexityRecord.setHalsteadMeasure(halsteadMeasures);
		complexityRecord.setSourceLinesOfCodeMeasure(slocMeasure);
	}
	 
	private ClassComplexityRecord createClassComplexityRecord(JSONObject complexityJSONRecord) {
		String className = (String) complexityJSONRecord.get("name");
		double classMaintainability = loadDobleOrLongAsDouble(complexityJSONRecord.get("maintainability")); 
		ClassComplexityRecord classComplexityRecord = new ClassComplexityRecord(className, classMaintainability);
		ComplexityRecord complexityAggregateRecord = this.createComplexityRecord((JSONObject) complexityJSONRecord.get("aggregate"));
		ComplexityRecord complexityAverageRecord = this.createComplexityRecord((JSONObject) complexityJSONRecord.get("aggregateAverage"));
		ComplexityRecord complexityMethodAverageRecord = this.createComplexityRecord((JSONObject) complexityJSONRecord.get("methodAverage"));
		
		complexityAggregateRecord.setAssociatedWith("CLASS: " + className);
		complexityAverageRecord.setAssociatedWith("CLASS: " + className);
		complexityMethodAverageRecord.setAssociatedWith("CLASS: " + className);
		classComplexityRecord.putAssociatedRecord(TyphonTypeScriptComplexityAnalysis.AGGREGATE_CLASS, complexityAggregateRecord);
		classComplexityRecord.putAssociatedRecord(TyphonTypeScriptComplexityAnalysis.OVERALL_AVERAGE_CLASS, complexityAverageRecord);
		classComplexityRecord.putAssociatedRecord(TyphonTypeScriptComplexityAnalysis.METHOD_AVERAGE_CLASS, complexityMethodAverageRecord);
		
		JSONObject classMethod;
		String recordClassMethodName;
		MethodComplexityRecord classMethodComplexityRecord;
		Iterator classMethods = ((JSONArray) complexityJSONRecord.get("methods")).iterator();
		while(classMethods.hasNext()) {
			classMethod = (JSONObject) classMethods.next();
			classMethodComplexityRecord = this.createMethodComplexityRecord("", classMethod);
			classMethodComplexityRecord.setAssociatedClassName(className);
			classMethodComplexityRecord.setAssociatedWith(className);
			recordClassMethodName = classMethodComplexityRecord.getMethodName();
			classComplexityRecord.putClassMethod(recordClassMethodName, classMethodComplexityRecord);
		}
		return classComplexityRecord;
	}

	private void loadAllClassesComplexities(JSONObject complexityJSONRecord, FileComplexityRecord fileComplexityRecord, String fileName) {
		Iterator<Object> classesIterator = ((JSONArray) complexityJSONRecord.get("classes")).iterator();
		JSONObject classComplexity;
		String className;
		ClassComplexityRecord evaluatedClassComplexityRecord;
		while (classesIterator.hasNext()) {
			classComplexity = (JSONObject) classesIterator.next();
			evaluatedClassComplexityRecord = this.createClassComplexityRecord(classComplexity);
			evaluatedClassComplexityRecord.setAssociatedWith(fileName);
			className = evaluatedClassComplexityRecord.getClassName();
			fileComplexityRecord.putAssociatedClass(className, evaluatedClassComplexityRecord);
		}
	}
	
	private MethodComplexityRecord createMethodComplexityRecord(String additionalMethodName, JSONObject complexityJSONRecord) {
		String methodName;
		int maxNestedMethodDepth;
		if (complexityJSONRecord.containsKey("name")) {
			methodName = (String) complexityJSONRecord.get("name");
		} else {
			methodName = additionalMethodName;
		}
		if (complexityJSONRecord.containsKey("maxNestedMethodDepth")) {
			maxNestedMethodDepth = ((Long) complexityJSONRecord.get("maxNestedMethodDepth")).intValue();
		} else {
			maxNestedMethodDepth = 0;
		}
		MethodComplexityRecord methodComplexityRecord = new MethodComplexityRecord(methodName, maxNestedMethodDepth);
		this.addComplexityRecordsToComplexityRecord(complexityJSONRecord, methodComplexityRecord);
		
		String methodParameterName;
		if (complexityJSONRecord.containsKey("paramNames")) {
			Iterator<Object> methodParamsIterator = ((JSONArray) complexityJSONRecord.get("paramNames")).iterator();
			while( methodParamsIterator.hasNext()) {
				methodParameterName = (String) methodParamsIterator.next();
				methodComplexityRecord.addParameterName(methodParameterName);
			}
		}
		return methodComplexityRecord;
	}
	
	private void loadAllMethodComplexities(JSONObject complexityJSONRecord, ClassComplexityRecord classComplexityRecord) {
		Iterator<Object> methodsIterator = ((JSONArray) complexityJSONRecord.get("methods")).iterator();
		JSONObject methodComplexity;
		String methodName;
		MethodComplexityRecord evaluatedMethodComplexityRecord;
		JSONObject nestedMethod;
		Iterator<Object> nestedMethodsIterator;
		while (methodsIterator.hasNext()) {
			methodComplexity = (JSONObject) methodsIterator.next();
			evaluatedMethodComplexityRecord = this.createMethodComplexityRecord(null, methodComplexity);
			evaluatedMethodComplexityRecord.setAssociatedClassName(classComplexityRecord.getClassName());

			methodName = evaluatedMethodComplexityRecord.getMethodName();
			classComplexityRecord.putClassMethod(methodName, evaluatedMethodComplexityRecord);
			
			nestedMethodsIterator = ((JSONArray) methodComplexity.get("nestedMethods")).iterator();
			while(nestedMethodsIterator.hasNext()) {
				nestedMethod = (JSONObject) nestedMethodsIterator.next();
				this.loadNestedMethodComplexities(nestedMethod, evaluatedMethodComplexityRecord);
			}
		}
	}
	
	private void loadNestedMethodComplexities(JSONObject nestedMethodComplexity, MethodComplexityRecord methodComplexityRecord) {
		String methodName;
		MethodComplexityRecord evaluatedMethodComplexityRecord;
		JSONObject nestedMethod;
		Iterator<Object> nestedMethodsIterator;
		evaluatedMethodComplexityRecord = this.createMethodComplexityRecord(null, nestedMethodComplexity);
		methodName = evaluatedMethodComplexityRecord.getMethodName();
		methodComplexityRecord.putNestedMethod(methodName, evaluatedMethodComplexityRecord);
		
		nestedMethodsIterator = ((JSONArray) nestedMethodComplexity.get("nestedMethods")).iterator();
		while(nestedMethodsIterator.hasNext()) {
			nestedMethod = (JSONObject) nestedMethodsIterator.next();
			this.loadNestedMethodComplexities(nestedMethod, evaluatedMethodComplexityRecord);
		}
	}
	
	private void loadAllMethodComplexities(JSONObject complexityJSONRecord, FileComplexityRecord fileComplexityRecord, String fileName) {
		Iterator<Object> methodsIterator = ((JSONArray) complexityJSONRecord.get("methods")).iterator();
		JSONObject methodComplexity;
		String methodName;
		MethodComplexityRecord evaluatedMethodComplexityRecord;
		JSONObject nestedMethod;
		Iterator<Object> nestedMethodsIterator;
		while (methodsIterator.hasNext()) {
			methodComplexity = (JSONObject) methodsIterator.next();
			evaluatedMethodComplexityRecord = this.createMethodComplexityRecord(null, methodComplexity);
			methodName = evaluatedMethodComplexityRecord.getMethodName();
			evaluatedMethodComplexityRecord.setAssociatedWith(fileName);
			fileComplexityRecord.putAssociatedMethod(methodName, evaluatedMethodComplexityRecord);
			nestedMethodsIterator = ((JSONArray) methodComplexity.get("nestedMethods")).iterator();
			while(nestedMethodsIterator.hasNext()) {
				nestedMethod = (JSONObject) nestedMethodsIterator.next();
				this.loadNestedMethodComplexities(nestedMethod, evaluatedMethodComplexityRecord);
			}
		}
	}
	
	private ComplexityMeasurement createComplexityMeasurement(String fileName, JSONObject complexityJSONRecord, 
			String fileContent, boolean storeFileContent) {
		double maintainability = loadDobleOrLongAsDouble(complexityJSONRecord.get("maintainability"));
		ComplexityMeasurement complexityMeasurement = new ComplexityMeasurement(TyphonTypeScriptComplexityAnalysis.TYPE, maintainability);
		FileComplexityRecord fileComplexityRecord = new FileComplexityRecord(fileName);
		if (storeFileContent) { 
			fileComplexityRecord.putAnalyzedFileContent(fileContent, FileComplexityRecord.AnalyzedFileContentType.STANDALONE); 
		}
		
		this.loadAllClassesComplexities(complexityJSONRecord, fileComplexityRecord, fileName);
		this.loadAllMethodComplexities(complexityJSONRecord, fileComplexityRecord, fileName);
		complexityMeasurement.setComplexityRecord(fileName, fileComplexityRecord);
		
		ComplexityRecord complexityAggregateRecord = this.createComplexityRecord((JSONObject) complexityJSONRecord.get("aggregate"));
		ComplexityRecord complexityAverageRecord = this.createComplexityRecord((JSONObject) complexityJSONRecord.get("aggregateAverage"));
		ComplexityRecord complexityMethodAverageRecord = this.createComplexityRecord((JSONObject) complexityJSONRecord.get("methodAverage"));
		
		complexityAggregateRecord.setAssociatedWith("FILE: " + fileName);
		complexityAverageRecord.setAssociatedWith("FILE: " + fileName);
		complexityMethodAverageRecord.setAssociatedWith("FILE: " + fileName);

		complexityMeasurement.setComplexityRecord(
				TyphonTypeScriptComplexityAnalysis.AGGREGATE, complexityAggregateRecord );
		complexityMeasurement.setComplexityRecord(
				TyphonTypeScriptComplexityAnalysis.OVERALL_AVERAGE, complexityAverageRecord);
		complexityMeasurement.setComplexityRecord(
				TyphonTypeScriptComplexityAnalysis.METHOD_AVERAGE, complexityMethodAverageRecord);
		return complexityMeasurement;
	}

	@Override
	public String getName() { return TyphonTypeScriptComplexityAnalysis.TYPE; }

	@Override
	public String doCleaning(String fileContent) {
		fileContent = TyphonTypeScriptComplexityAnalysis.privateKeywordHandling(fileContent);
		fileContent = TyphonTypeScriptComplexityAnalysis.manageSimpleTypeHandling(fileContent);
		fileContent = TyphonTypeScriptComplexityAnalysis.manageExtensiveComments(fileContent);
		return fileContent;
	}
}

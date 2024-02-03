package codeConstructsEvaluation.entities;

import java.util.List;


public interface ComplexityMeasure {

	public ComplexityMeasure getComplexityDifference(ComplexityMeasure complexityMeasure1, ComplexityMeasure complexityMeasure2);
	public void putColumnName(List<String> columnNames);
	public void writeToCSV(StringBuilder content);
	public void print();
}

package codeConstructsEvaluation.entities;

import java.util.List;

public class CyclomaticComplexity implements ComplexityMeasure {

	private double cyclomaticNumber;
	private double cyclomaticDensity;
	
	public CyclomaticComplexity(double cyclomaticNumber, double cyclomaticDensity) {
		this.cyclomaticNumber = cyclomaticNumber;
		this.cyclomaticDensity = cyclomaticDensity;	
	}
	
	public double getCyclomaticNumber() { return this.cyclomaticNumber; }
	
	public double getCyclomaticDensity() { return this.cyclomaticDensity; }
	
	public static CyclomaticComplexity getComplexityDifference(
			CyclomaticComplexity complexity1, CyclomaticComplexity complexity2) {
		double cyclomaticNumberDifference = complexity1.getCyclomaticNumber() - complexity2.getCyclomaticNumber();
		double cyclomaticDensityDifference = complexity1.getCyclomaticDensity() - complexity2.getCyclomaticDensity();
		return new CyclomaticComplexity(cyclomaticNumberDifference, cyclomaticDensityDifference);
	}

	@Override
	public ComplexityMeasure getComplexityDifference(ComplexityMeasure complexityMeasure1, ComplexityMeasure complexityMeasure2) {
		return CyclomaticComplexity.getComplexityDifference((CyclomaticComplexity) complexityMeasure1, (CyclomaticComplexity) complexityMeasure2);
	}
	
	@Override
	public void print() {
		System.out.println("____| Cyclomatic Complexity |____");
		System.out.println("---| cyclomatic number: " + this.cyclomaticNumber);
		System.out.println("---| cyclomatic density: " + this.cyclomaticDensity);
		System.out.println("-----------------------------------------");
	}

	public static void putColumnNameStatic(List<String> columnNames) {
		columnNames.add("Cycl. Number");
		columnNames.add("Cycl. Density");
	}
	
	public static void writeToCSVStatic(StringBuilder content) {
		content.append("");
		content.append(';');
		content.append("");
		content.append(';');
	}
	
	@Override
	public void putColumnName(List<String> columnNames) {
		columnNames.add("Cycl. Number");
		columnNames.add("Cycl. Density");
	}
	
	@Override
	public void writeToCSV(StringBuilder content) {
		content.append(String.valueOf(this.cyclomaticNumber).replace(".", ","));
		content.append(';');
		content.append(String.valueOf(this.cyclomaticDensity).replace(".", ","));
		content.append(';');
	}
}

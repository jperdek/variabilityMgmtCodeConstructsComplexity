package codeConstructsEvaluation;

import java.util.List;
import java.util.ArrayList;


public class ComplexityRecordsCollector {

	private List<ComplexityMeasurement> complexityMeasurements;
	
	
	public ComplexityRecordsCollector() {
		this.complexityMeasurements = new ArrayList<ComplexityMeasurement>();
	}
	
	public void addMeasurement(ComplexityMeasurement complexityMeasurement) {
		this.complexityMeasurements.add(complexityMeasurement);
	}
	
	public ComplexityMeasurement getMeasurement(int index) {
		return this.complexityMeasurements.get(index);
	}
	
	public List<ComplexityMeasurement> getMeasurements() { return this.complexityMeasurements; }
}

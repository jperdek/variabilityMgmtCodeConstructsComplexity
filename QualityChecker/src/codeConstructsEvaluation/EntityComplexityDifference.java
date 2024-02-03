package codeConstructsEvaluation;

import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

public interface EntityComplexityDifference {

	public EntityComplexityDifference makeDifference(EntityComplexityDifference compareWith);
	public void putColumnName(List<String> columnNames, boolean aggregated);
	public Set<Entry<String,? extends ComplexityRecord>> getComplexityRecordsEntrySet();
	public void writeToCSV(StringBuilder content, boolean aggregated);
	public void print();
}

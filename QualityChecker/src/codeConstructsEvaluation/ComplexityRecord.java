package codeConstructsEvaluation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import astFileProcessor.astObjects.ASTGenericDecorator;
import codeConstructsEvaluation.entities.CyclomaticComplexity;
import codeConstructsEvaluation.entities.GeneralComplexity;
import codeConstructsEvaluation.entities.GeneralComplexityMeasure;
import codeConstructsEvaluation.entities.HalsteadMeasures;
import codeConstructsEvaluation.entities.SourceLinesOfCodeMeasure;


public class ComplexityRecord implements EntityComplexityDifference {

	protected static enum SourceType {
		FILE,
		METHOD,
		CLASS_METHOD,
		CLASS,
		GENERAL,
		UNDEFINED
	};
	
	//private AnalyzedEntityType analyzedEntityType = null;
	private CyclomaticComplexity cyclomaticComplexity = null;
	private GeneralComplexity generalComplexity = null;
	private HalsteadMeasures halsteadMeasures = null;
	private SourceLinesOfCodeMeasure slocMeasure = null;
	private String associatedWith = null;
	private SourceType sourceType = ComplexityRecord.SourceType.UNDEFINED;
	protected List<ASTGenericDecorator> associatedDecorators = null;
	
	
	public ComplexityRecord() {
		this.sourceType = ComplexityRecord.SourceType.GENERAL;
	}
	
	protected ComplexityRecord(SourceType sourceType) {
		this.sourceType = sourceType;
	}
	
	public void associateDecorators(List<ASTGenericDecorator> frameworkDecorators) {
		this.associatedDecorators = new ArrayList<ASTGenericDecorator>(frameworkDecorators);
	}
	
	protected int getNumberAssociatedDecorators() { 
		if(this.associatedDecorators == null) {
			return -1; 
		}
		return this.associatedDecorators.size(); 
	}
	
	public void setAssociatedWith(String associatedWith) { this.associatedWith = associatedWith; }
	
	public String getAssociatedWith() { return this.associatedWith; }
	
	public void setHalsteadMeasure(HalsteadMeasures halsteadMeasures) {
		this.halsteadMeasures = halsteadMeasures;
	}
	
	public void setCyclomaticComplexity(CyclomaticComplexity cyclomaticComplexity) {
		this.cyclomaticComplexity = cyclomaticComplexity;
	}
	
	public void setGeneralComplexity(GeneralComplexity generalComplexity) {
		this.generalComplexity = generalComplexity;
	}
	
	public void setSourceLinesOfCodeMeasure(SourceLinesOfCodeMeasure slocMeasure) {
		this.slocMeasure = slocMeasure;
	}
	
	public boolean insertGeneralComplexityMeasure(GeneralComplexityMeasure generalComplexityMeasure) {
		if (this.generalComplexity != null) {
			this.generalComplexity.addGeneralComplexityMeasure(generalComplexityMeasure);
			return true;
		}
		return false;
	}
	
	public HalsteadMeasures getHalsteadMeasure() { return this.halsteadMeasures; }
	
	public CyclomaticComplexity getCyclomaticComplexity() { return this.cyclomaticComplexity; }
	
	public GeneralComplexity getGeneralComplexity() { return this.generalComplexity; }
	
	public SourceLinesOfCodeMeasure getSourceLinesOfCodeMeasure() { return this.slocMeasure; }
	
	public SourceType getSource() { return this.sourceType; }

	@Override
	public EntityComplexityDifference makeDifference(EntityComplexityDifference compareWith) {
		ComplexityRecord compareWithComplexityRecord = (ComplexityRecord) compareWith;
		ComplexityRecord complexityRecordDifference = new ComplexityRecord();
		complexityRecordDifference.setAssociatedWith(this.associatedWith);
		if (this.associatedDecorators != null) {
			complexityRecordDifference.associateDecorators(this.associatedDecorators);
		}
		
		CyclomaticComplexity cyclomaticComplexityDifference, compareWithCyclomaticComplexity;
		//GeneralComplexity generalComplexityDifference; 
		HalsteadMeasures halsteadMeasuresDifference, compareWithHalsteadMeasures;
		SourceLinesOfCodeMeasure slocMeasuresDifference, compareWithSlocMeasures;

		if(this.cyclomaticComplexity != null) {
			compareWithCyclomaticComplexity = compareWithComplexityRecord.getCyclomaticComplexity();
			if (compareWithCyclomaticComplexity != null) {
				cyclomaticComplexityDifference = CyclomaticComplexity.getComplexityDifference(this.cyclomaticComplexity, compareWithCyclomaticComplexity);
				complexityRecordDifference.setCyclomaticComplexity(cyclomaticComplexityDifference);
			} else {
				complexityRecordDifference.setCyclomaticComplexity(this.cyclomaticComplexity);
			}
		}
		
		if(this.halsteadMeasures != null) {
			compareWithHalsteadMeasures = compareWithComplexityRecord.getHalsteadMeasure();
			if (compareWithHalsteadMeasures != null) {
				halsteadMeasuresDifference = HalsteadMeasures.getComplexityDifference(this.halsteadMeasures, compareWithHalsteadMeasures);
				complexityRecordDifference.setHalsteadMeasure(halsteadMeasuresDifference);
			} else {
				complexityRecordDifference.setHalsteadMeasure(this.halsteadMeasures);
			}
		}
		
		if(this.slocMeasure != null) {
			compareWithSlocMeasures = compareWithComplexityRecord.getSourceLinesOfCodeMeasure();
			if (compareWithSlocMeasures != null) {
				slocMeasuresDifference = SourceLinesOfCodeMeasure.getComplexityDifference(this.slocMeasure, compareWithSlocMeasures);
				complexityRecordDifference.setSourceLinesOfCodeMeasure(slocMeasuresDifference);
			} else {
				complexityRecordDifference.setSourceLinesOfCodeMeasure(this.slocMeasure);
			}
		}
		
		return complexityRecordDifference;
	}

	public void updateAccordingTo(ComplexityRecord sourceRecord) {
		this.cyclomaticComplexity = sourceRecord.getCyclomaticComplexity();
		this.halsteadMeasures = sourceRecord.getHalsteadMeasure();
		this.slocMeasure = sourceRecord.getSourceLinesOfCodeMeasure();
		this.generalComplexity = sourceRecord.getGeneralComplexity();
		this.associatedWith = sourceRecord.getAssociatedWith();
	}
	
	@Override
	public void print() {
		System.out.println("XXXXXXXXX___| COMPLEXITY RECORD " + this.sourceType + " |___XXXXXXXXX");
		if (this.cyclomaticComplexity != null) {
			this.cyclomaticComplexity.print();
		}
		if (this.halsteadMeasures != null) {
			this.halsteadMeasures.print();
		}
		if (this.slocMeasure != null) {
			this.slocMeasure.print();
		}
		System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
	}
	
	@Override
	public void putColumnName(List<String> columnNames, boolean aggregated) {
		//if (this.sourceType.equals(SourceType.GENERAL)) {
			columnNames.add("Associated with");
			if (this.associatedDecorators != null) {
				columnNames.add("Overall file decorastors");
			}
		//}
		if (this.cyclomaticComplexity != null) {
			this.cyclomaticComplexity.putColumnName(columnNames);
		} else {
			CyclomaticComplexity.putColumnNameStatic(columnNames);
		}
		if (this.halsteadMeasures != null) {
			this.halsteadMeasures.putColumnName(columnNames);
		} else {
			HalsteadMeasures.putColumnNameStatic(columnNames);
		}
		if (this.slocMeasure != null) {
			this.slocMeasure.putColumnName(columnNames);
		} else {
			SourceLinesOfCodeMeasure.putColumnNameStatic(columnNames);
		}
	}
	
	@Override
	public void writeToCSV(StringBuilder content, boolean aggregated) {
		//if (this.sourceType.equals(SourceType.GENERAL)) {
			content.append(this.associatedWith);
			content.append(";");
			if (this.associatedDecorators != null) {
				content.append(this.associatedDecorators.size());
				content.append(";");
			}
		//}
		if (this.cyclomaticComplexity != null) {
			this.cyclomaticComplexity.writeToCSV(content);
		} else {
			CyclomaticComplexity.writeToCSVStatic(content);
		}
		if (this.halsteadMeasures != null) {
			this.halsteadMeasures.writeToCSV(content);
		} else {
			HalsteadMeasures.writeToCSVStatic(content);
		}
		if (this.slocMeasure != null) {
			this.slocMeasure.writeToCSV(content);
		} else {
			SourceLinesOfCodeMeasure.writeToCSVStatic(content);
		}
	}

	@Override
	public Set<Entry<String, ? extends ComplexityRecord>> getComplexityRecordsEntrySet() {
		return null;
	}
}

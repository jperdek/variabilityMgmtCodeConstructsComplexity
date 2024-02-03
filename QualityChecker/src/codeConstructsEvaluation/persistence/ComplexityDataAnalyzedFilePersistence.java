package codeConstructsEvaluation.persistence;

import java.io.File;
import java.util.Set;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map.Entry;
import codeConstructsEvaluation.ComplexityRecord;
import codeConstructsEvaluation.FileComplexityRecord;


public class ComplexityDataAnalyzedFilePersistence {

	private String defaultPath = "./fileComparison/";
	private String additionalNamePart = "";
	private boolean omitSimilarContents = true;
	

	public ComplexityDataAnalyzedFilePersistence(String defaultPath) {
		this.defaultPath = defaultPath;
	}
	
	public void setAdditionalNamePart(String additionalNamePart) {
		this.additionalNamePart = additionalNamePart;
	}
	
	private void saveRecord(String filePath, String fileType, String fileContent) throws IOException {
		File file;
		FileWriter fileWriter;
		String mainFileTitleString;
		
		filePath = filePath.replaceAll("\\\\", "/").replaceAll("\\.", "-");
		mainFileTitleString = filePath.substring(filePath.lastIndexOf("/") + 1);
		file = new File(this.defaultPath + "/" + this.additionalNamePart + "_file" + fileType + "_" + mainFileTitleString + ".txt");
		fileWriter = new FileWriter(file);
		fileWriter.write("// FROM: " + filePath + "\n");
		fileWriter.write(fileContent);
		fileWriter.close();
	}
	
	public void saveFileContents(Set<Entry<String, ? extends ComplexityRecord>> analyzedEntries) throws IOException {
		String complexityRecordName;
		String contentBefore, contentAfter;
		ComplexityRecord complexityRecord;
		FileComplexityRecord analyzedFileComplexityRecord;

		Set<Entry<String, ? extends ComplexityRecord>> analyzedEntriesOfRecord;
		for(Entry<String, ? extends ComplexityRecord> analyzedEntry: analyzedEntries) {
			complexityRecord = analyzedEntry.getValue();
			if (complexityRecord instanceof FileComplexityRecord) {
				complexityRecordName = analyzedEntry.getKey();
		
				analyzedFileComplexityRecord = (FileComplexityRecord) complexityRecord;
				if (analyzedFileComplexityRecord.isComparatoryInstance()) {
					contentBefore = analyzedFileComplexityRecord.getAnalyzedFileContent(FileComplexityRecord.AnalyzedFileContentType.BEFORE);
					contentAfter =  analyzedFileComplexityRecord.getAnalyzedFileContent(FileComplexityRecord.AnalyzedFileContentType.AFTER);
					if (!this.omitSimilarContents || !contentBefore.equals(contentAfter)) {
						this.saveRecord(complexityRecordName, "BEFORE", contentBefore);
						this.saveRecord(complexityRecordName, "AFTER", contentAfter);
					} 
				} else {
					this.saveRecord(complexityRecordName, "STANDARD", analyzedFileComplexityRecord.getAnalyzedFileContent());
				}
			}

			analyzedEntriesOfRecord = complexityRecord.getComplexityRecordsEntrySet();
			if (analyzedEntriesOfRecord != null) {
				this.saveFileContents(analyzedEntriesOfRecord);
			}
		}
	}
}

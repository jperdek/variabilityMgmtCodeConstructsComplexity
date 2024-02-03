package entities.tools;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import entities.FileLoader;


public class ImportsManager {

	public static List<ImportRule> rules = new ArrayList<ImportRule>();
	
	public static List<String> extractImportsFromFile(String importsFilePath, String pathToSrc) {
		String stringToSearch = "import\s*\\{([^\\}]+)}\s*from\s+['\"]([^\"']+)[\"']";
		List<String> imports = FileLoader.getRegexDataFromFile(
			importsFilePath, stringToSearch);
		List<String> absoluteImports = ImportsManager.clearImports(imports, importsFilePath, stringToSearch, pathToSrc);
		return absoluteImports;
	}
	
	public static void applyBasicRules() {
		ImportRule rule1 = new ImportRule("^@.*");
		ImportsManager.setImportFilterRule(rule1);
		ImportRule rule2 = new ImportRule("^node_modules/.*");
		ImportsManager.setImportFilterRule(rule2);
	}

	public static void setImportFilterRule(ImportRule importRule) {
		ImportsManager.rules.add(importRule);
	}
	
	public static boolean checkAllImportRules(String importPath) {
		Iterator<ImportRule> rulesIterator = ImportsManager.rules.iterator();
		while(rulesIterator.hasNext()) {
			ImportRule actualRule = rulesIterator.next();
			boolean result = actualRule.check(importPath);
			if (!result) {
				return false;
			}
		}
		return true;
	}
	
	public static List<String> clearImports(List<String> importLines, String basePath, 
			String stringToSearch, String pathToSrc) {
		List<String> clearedImports = new ArrayList<String>();
	
		if (!basePath.endsWith("/")) {
			basePath = basePath.substring(0, basePath.lastIndexOf("/"));
		}
		Iterator<String> importLinesIterator = importLines.iterator();
		 
		Pattern pattern = Pattern.compile(stringToSearch);
		while(importLinesIterator.hasNext()) {
			String processedLine = importLinesIterator.next();
			Matcher matcher = pattern.matcher(processedLine);
			if (matcher.matches()) {
				String[] names = matcher.group(1).split(",");
				String importPath = matcher.group(2);
				
				for (int i = 0; i < names.length; i++) {
					String processedName = names[i].strip();
					if (importPath.startsWith(".")) {
						if (importPath.startsWith("./")) {
							importPath = importPath.replaceAll("^\\.\\/", basePath + "/");
						} else if (importPath.startsWith("../")) {
							String basePath2 = basePath;
							while (importPath.startsWith("../")) {
								basePath2 = basePath2.substring(0, basePath2.lastIndexOf("/"));
								importPath = importPath.replaceAll("^\\.\\.\\/", "");
							}
							importPath = importPath.replaceAll("^", basePath2 + "/");
						}
		
					}
					if (ImportsManager.checkAllImportRules(importPath)) {
						String cleanPath = importPath + ".ts";
						if (cleanPath.startsWith("src")) {
							cleanPath = pathToSrc + cleanPath;
						}
						clearedImports.add(cleanPath);
					}
				}
			} else {
				System.out.println("Matched line:");
				System.out.println(processedLine);
			}
		}
		
		return clearedImports;
	}
}

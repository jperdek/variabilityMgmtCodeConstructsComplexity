package entities;

import java.util.List;
import java.util.Map;

import entities.tools.ImportsManager;

import java.util.ArrayList;
import java.util.Iterator;
import metrics.MemberQuality;
import parser.javascriptObjectRepresentations.JavascriptRepresentation;
import parser.javascriptObjectRepresentations.JavascriptString;


public class ProjectEntity {

	private String type = "UNKNOWN";
	private String pathInProject = "";
	private List<String> imports = new ArrayList<String>();
	private JavascriptRepresentation structure = null;
	private boolean isCommon = true;
	private MemberQuality memberQuality;
	
	
	public ProjectEntity(String pathInProject, String pathToSrc)
	{
		this.type = this.guessEntityType(pathInProject);
		this.pathInProject = pathInProject;
		this.memberQuality = new MemberQuality(pathInProject);

		List<String> extractedImports = ImportsManager.extractImportsFromFile(this.pathInProject, pathToSrc);
		
		Iterator<String> importsIterator = extractedImports.iterator();
		while(importsIterator.hasNext()) {
			this.imports.add(importsIterator.next());
		}
	}

	public ProjectEntity(String pathInProject, List<String> extractedImports)
	{
		this.type = this.guessEntityType(pathInProject);
		this.pathInProject = pathInProject;
		this.memberQuality = new MemberQuality(pathInProject);
		
		Iterator<String> importsIterator = extractedImports.iterator();
		while(importsIterator.hasNext()) {
			this.imports.add(importsIterator.next());
		}
	}

	public ProjectEntity(String pathInProject, List<String> extractedImports, MemberQuality memberQuality)
	{
		this.type = this.guessEntityType(pathInProject);
		this.pathInProject = pathInProject;
		this.memberQuality = memberQuality;
		
		Iterator<String> importsIterator = extractedImports.iterator();
		while(importsIterator.hasNext()) {
			this.imports.add(importsIterator.next());
		}
	}
	
	public void setAsCommon() { this.isCommon = true; }
	
	public void setAsVariable() { this.isCommon = false; }
	
	public boolean isCommon() { return this.isCommon; }
	
	public List<String> getImports() { return this.imports; }
	
	public String getPathInProject() { return this.pathInProject; }

	public MemberQuality getMemberQuality() { return this.memberQuality; }

	public String guessEntityType(String pathInProject) {
		if (pathInProject.endsWith("service.ts")) {
			return "@Service";
		} else if (pathInProject.endsWith("component.ts")) {
			return "@Component";
		} else if (pathInProject.endsWith("directive.ts")) {
			return "@Directive";
		} else {
			return "UNKNOWN";
		}
	}
	
	public ProjectEntity(String pathInProject, String pathToSrc, String type) {
		this.type = type;
		this.pathInProject = pathInProject;
		ImportsManager.extractImportsFromFile(this.pathInProject, pathToSrc);
	}
	
	public void processEntity(TemplateParser templateParser) {
		this.getLinesOfCode();
		this.parseEntityStructure();
		this.prepareAndParseTemplateIfExists(templateParser);
		this.prepareAndParseStylesIfExists();
	}
	
	public void prepareAndParseTemplateIfExists(TemplateParser templateParser) {
		JavascriptRepresentation templateUrlStructure = this.structure.getAssociatedJavascriptRepresentation("templateUrl");
		if (templateUrlStructure != null) {
			String templatePath = ((JavascriptString) templateUrlStructure).getString();
			String basePath = this.pathInProject.substring(0, pathInProject.lastIndexOf("/"));
			
			if (templatePath.startsWith(".")) {
				if (templatePath.startsWith("./")) {
					templatePath = templatePath.replaceAll("^\\.\\/", basePath + "/");
				} else if (templatePath.startsWith("../")) {
					String basePath2 = basePath;
					while (templatePath.startsWith("../")) {
						basePath2 = basePath2.substring(0, basePath2.lastIndexOf("/"));
						templatePath = templatePath.replaceAll("^\\.\\.\\/", "");
					}
					templatePath = templatePath.replaceAll("^", basePath2 + "/");
				}

			}
			
			this.parseTemplateIfExist(templateParser, templatePath);
		}
	}

	public void prepareAndParseStylesIfExists() {
		JavascriptRepresentation styleUrlsStructure = this.structure.getAssociatedJavascriptRepresentation("styleUrls");
		if (styleUrlsStructure != null) {
			List<JavascriptRepresentation> styleUrls = styleUrlsStructure.getAllAssociatedJavascriptRepresentations();
			Iterator<JavascriptRepresentation> styleUrlsIterator = styleUrls.iterator();
			
			while(styleUrlsIterator.hasNext()) {
				String styleUrlPath = ((JavascriptString) styleUrlsIterator.next()).getString();
				if (styleUrlPath.startsWith("'") && styleUrlPath.endsWith("'")) {
					styleUrlPath = styleUrlPath.substring(1, styleUrlPath.length() - 1);
				}
				String basePath = this.pathInProject.substring(0, pathInProject.lastIndexOf("/"));
				
				if (styleUrlPath.startsWith(".")) {
					if (styleUrlPath.startsWith("./")) {
						styleUrlPath = styleUrlPath.replaceAll("^\\.\\/", basePath + "/");
					} else if (styleUrlPath.startsWith("../")) {
						String basePath2 = basePath;
						while (styleUrlPath.startsWith("../")) {
							basePath2 = basePath2.substring(0, basePath2.lastIndexOf("/"));
							styleUrlPath = styleUrlPath.replaceAll("^\\.\\.\\/", "");
						}
						styleUrlPath = styleUrlPath.replaceAll("^", basePath2 + "/");
					}
				}
				
				this.getNumberOfLinesOfStyles(styleUrlPath);
			}
		}
	}
	
	public void parseEntityStructure() {
		try {
			this.structure = FileLoader.getJSONObjectFromFile(this.pathInProject, this.type);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void parseTemplateIfExist(TemplateParser templateParser, String templatePathInProject) {
		String templateContent = FileLoader.loadWholeFile(templatePathInProject);
		long templateLinesCount = templateContent.chars().filter(ch -> ch == '\n').count();
		this.memberQuality.setNumberLinesOfTemplateCode(templateLinesCount);

		Map<String, Integer> selectorsOccurrences = templateParser.findAssociatedComponents(templateContent);
		
		if (selectorsOccurrences != null) {
			Iterator<String> iterator = selectorsOccurrences.keySet().iterator();
			while(iterator.hasNext()) {
				String path = iterator.next();
				String importPath = templateParser.getSelectorsValue(path);
				this.imports.add(importPath);
			}
		}
	}
	
	public void getLinesOfCode() {
		String templateContent = FileLoader.loadWholeFile(this.pathInProject);
		long codeLinesCount = templateContent.chars().filter(ch -> ch == '\n').count();
		this.memberQuality.setNumberLinesOfTemplateCode(codeLinesCount);
	}
	
	public void getNumberOfLinesOfStyles(String stylesPathsInProject) {
		String templateContent = FileLoader.loadWholeFile(stylesPathsInProject);
		long codeLinesCount = templateContent.chars().filter(ch -> ch == '\n').count();
		this.memberQuality.addNumberLinesOfStyles(codeLinesCount);
	}
}

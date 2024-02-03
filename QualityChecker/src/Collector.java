import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import entities.FileLoader;
import entities.tools.ImportsManager;
import entities.ProjectEntity;
import entities.SelectorAggregator;
import entities.TemplateParser;
import entities.VariableEntitiesMarker;
import metrics.CommonalityVariabilityMetrics;
import metrics.MetricsEvaluator;
import metrics.ReuseMetrics;
import parser.javascriptObjectRepresentations.JavascriptRepresentation;
import parser.javascriptObjectRepresentations.JavascriptString;
import pathFiltering.PathFilter;
import visualization.EntityAttributePrinter;


public class Collector {

	private PathFilter pathFilter = new PathFilter();
	private SelectorAggregator selectorAggregator = new SelectorAggregator();
	private TemplateParser templateParser;

	public Collector() {
		this.templateParser = new TemplateParser(this.selectorAggregator);
	}
	
	public void loadForbiddenPathsFromFile(String forbiddenPathsToPath) {
		try {
			this.pathFilter.addForbiddenPathsFromFile(forbiddenPathsToPath);
		} catch(IOException e) {
			System.out.println(e);
		}
	}
	
	public boolean filterSupportingCodeParts(String pathToFile) {
		if (pathToFile.endsWith(".css") || pathToFile.endsWith(".scss") || pathToFile.endsWith(".sass")) { // CASCADING STYLES
			return false;
		} else if (pathToFile.endsWith(".spec.ts")) { // UNIT TESTS
			return false;
		} else if (pathToFile.endsWith(".html")) { // TEMPLATES
			return false;
		} else if (pathToFile.endsWith(".module.ts")) { // WHOLE MODULE
			return false;
		}
		
		return true;
	}
	
	public void getSelectors(String pathToProjectTree) {
		Stream<Path> s = null;
		try {
			Path inputPath1 = Path.of(URI.create(pathToProjectTree));
			int baseLength = inputPath1.toUri().getRawPath().length();
			s = Files.walk(inputPath1);
			Iterator<Path> paths = s.iterator();
			while(paths.hasNext()) {
				Path actualPath = paths.next();
				String newDirectoryOrFileString = pathToProjectTree.replace("file:///", "") + "/" + actualPath.toUri().getRawPath().substring(baseLength);
				String structureType = "";
				
				if (this.pathFilter.filterPath(newDirectoryOrFileString)) {
		
					if(!Files.isDirectory(actualPath)) {
						try {
							if (newDirectoryOrFileString.endsWith(".component.ts")) {
								structureType = "@Component";
							} else {
								// UNSUPPORTED STRUCTURE TYPE
								continue;
							}
							JavascriptRepresentation javascriptRepresentation = FileLoader.getJSONObjectFromFile(newDirectoryOrFileString, structureType);
							this.selectorAggregator.add(((JavascriptString) javascriptRepresentation.getAssociatedJavascriptRepresentation("selector")).getString(), newDirectoryOrFileString.replace(".html", ".ts"));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			s.close();
		}
	}
	
	public Map<String, ProjectEntity> collect(String pathToProjectTree, String pathToProjectSrc) {
		this.getSelectors(pathToProjectTree);

		if (!pathToProjectSrc.endsWith("/")) {
			pathToProjectSrc = pathToProjectSrc + "/";
		}

		Map<String, ProjectEntity> allEntities = new HashMap<String, ProjectEntity>();
		
		Stream<Path> s = null;
		try {
			Path inputPath1 = Path.of(URI.create(pathToProjectTree));
			int baseLength = inputPath1.toUri().getRawPath().length();
			s = Files.walk(inputPath1);
			Iterator<Path> paths = s.iterator();
			while(paths.hasNext()) {
				Path actualPath = paths.next();
				String newDirectoryOrFileString = pathToProjectTree.replace("file:///", "") + "/" + actualPath.toUri().getRawPath().substring(baseLength);

				if (this.pathFilter.filterPath(newDirectoryOrFileString) && this.filterSupportingCodeParts(newDirectoryOrFileString)) {
		
					if(!Files.isDirectory(actualPath)) {
						ProjectEntity projectEntity = new ProjectEntity(newDirectoryOrFileString, pathToProjectSrc);
						projectEntity.processEntity(this.templateParser);
						allEntities.put(newDirectoryOrFileString, projectEntity);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			s.close();
		}
		return allEntities;
	}
	
	public List<ProjectEntity> getVariabilityPoints(String pathToProjectVariabilityTree, String pathToProjectSrc) {
		Stream<Path> s = null;
		List<ProjectEntity> featureManagementEntities = new ArrayList<ProjectEntity>();

		if (!pathToProjectSrc.endsWith("/")) {
			pathToProjectSrc = pathToProjectSrc + "/";
		}

		try {
			Path inputPath1 = Path.of(URI.create(pathToProjectVariabilityTree));
			int baseLength = inputPath1.toUri().getRawPath().length();
			s = Files.walk(inputPath1);
			Iterator<Path> paths = s.iterator();
			while(paths.hasNext()) {
				Path actualPath = paths.next();
				String newDirectoryOrFileString = pathToProjectVariabilityTree.replace("file:///", "") + "/" + actualPath.toUri().getRawPath().substring(baseLength);

				if (this.pathFilter.filterPath(newDirectoryOrFileString)) {
		
					if(!Files.isDirectory(actualPath)) {
						ProjectEntity projectEntity = new ProjectEntity(newDirectoryOrFileString, pathToProjectSrc);
						projectEntity.processEntity(this.templateParser);
						featureManagementEntities.add(projectEntity);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			s.close();
		}
		return featureManagementEntities;
	}
	
	/**
	 * Specify:
	 * productLineDirectory - path to your (Angular based) software product line which should be evaluated
	 * productLineAppDirectory - path to src/app folder of your (Angular based) software product line
	 * featureManagementDirectory - directory containing entire feature management used in (Angular based) software product line 
	 * 
	 * 
	 * Then:
	 * 1. run main() method
	 * 
	 * All values are printed to console.
	 * 
	 */
	public static void evaluateMetrics() {
		String productLineDirectory = "file:///E:/aspects/canvasSPLforSPA/canvasSPLforSPA/";
		String productLineAppDirectory = "file:///E:/aspects/canvasSPLforSPA/canvasSPLforSPA/src/app";
		String featureManagementDirectory = "E:/aspects/canvasSPLforSPA/canvasSPLforSPA/src/app/featureManagement";
		
		Collector collector = new Collector();
		ImportsManager.applyBasicRules();
		collector.loadForbiddenPathsFromFile(".//src//pathFiltering//ignorePaths.txt");
		List<ProjectEntity> featureManagementEntities = collector.getVariabilityPoints("file:///" + featureManagementDirectory, productLineDirectory);
		Map<String, ProjectEntity> functionalEntities = collector.collect(productLineAppDirectory, productLineDirectory);
		
		VariableEntitiesMarker variableEntitiesMarker = new VariableEntitiesMarker();
		variableEntitiesMarker.markVariableEntities(functionalEntities, featureManagementEntities, featureManagementDirectory);
		EntityAttributePrinter entityAttributePrinter = new EntityAttributePrinter();
		entityAttributePrinter.printEntityAttributes(functionalEntities, featureManagementDirectory);
		
		MetricsEvaluator metricsEvaluator = new MetricsEvaluator();
		CommonalityVariabilityMetrics commonalityVariabilityMetrics = metricsEvaluator.evaluateCommonalityVariability(functionalEntities, featureManagementDirectory);
		System.out.println("Structure similarity coefficient is: " + commonalityVariabilityMetrics.getStructureSimilarityCoefficient());
		System.out.println("Structure variability coefficient is: " + commonalityVariabilityMetrics.getStructureVariabilityCoefficient());
		commonalityVariabilityMetrics.printCardinalities();
		
		ReuseMetrics reuseMetrics = new ReuseMetrics();
		reuseMetrics.evaluateReusableQualityWithProcessing(functionalEntities, featureManagementEntities, featureManagementDirectory);
		try {
			reuseMetrics.evaluateReusableQualityToCSV(functionalEntities, featureManagementDirectory, featureManagementEntities,  "evaluation.csv", false);
			reuseMetrics.evaluateReusableQualityToCSV(functionalEntities, featureManagementDirectory, featureManagementEntities,  "evaluationVP.csv", true);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Collector.evaluateMetrics();
	}
}

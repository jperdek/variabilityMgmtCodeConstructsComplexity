package metrics;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class CommonalityVariabilityMetrics {

	private Set<String> variantParts = new HashSet<String>();
	private Set<String> commonParts = new HashSet<String>();
	
	public CommonalityVariabilityMetrics() {
		
	}
	
	public void addCommonality(String identifierPath) {
		this.commonParts.add(identifierPath);
	}
	
	public void addVariability(String identifierPath) {
		this.variantParts.add(identifierPath);
	}
	
	public double getStructureSimilarityCoefficient() {
		int numberVariantParts = this.variantParts.size();
		int numberCommonParts = this.commonParts.size();
		return (numberCommonParts + 0.0) / (numberCommonParts + numberVariantParts + 0.0);
	}
	
	public double getStructureVariabilityCoefficient() {
		int numberVariantParts = this.variantParts.size();
		int numberCommonParts = this.commonParts.size();
		return (numberVariantParts + 0.0) / (numberCommonParts + numberVariantParts + 0.0);
	}
	
	public void printCardinalities() {
		int numberVariantParts = this.variantParts.size();
		int numberCommonParts = this.commonParts.size();
		System.out.println("Common parts: " + numberCommonParts);
		System.out.println("Variant parts: " + numberVariantParts);
	}
	
	public void printAll() {
		Iterator<String> commonality = this.commonParts.iterator();
		System.out.println("COMMONALITY:");
		while(commonality.hasNext()) {
			System.out.println(commonality.next());
		}
		
		Iterator<String> variability = this.variantParts.iterator();
		System.out.println("VARIABILITY:");
		while(variability.hasNext()) {
			System.out.println(variability.next());
		}
	}
}

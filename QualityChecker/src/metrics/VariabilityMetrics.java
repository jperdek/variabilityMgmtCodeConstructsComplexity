package metrics;

import java.util.HashSet;
import java.util.Set;

public class VariabilityMetrics {
	private Set<String> variantParts = new HashSet<String>();
	private Set<String> independentVariantParts = new HashSet<String>(); //no relations with other
	private Set<String> weakVariantParts = new HashSet<String>(); 		 //relations with other
	
	public VariabilityMetrics() {
	}
	
	public double getStrongCouplingCoefficient() {
		int numberVariantParts = this.variantParts.size();
		int numberIndependentVariantParts = this.independentVariantParts.size();
		
		return 1.0 - (numberIndependentVariantParts / numberVariantParts);
	}
	
	public double getWeakCouplingCoefficient() {
		int numberVariantParts = this.variantParts.size();
		int numberWeakVariantParts = this.weakVariantParts.size();
		
		return (numberWeakVariantParts / numberVariantParts);
	}
}

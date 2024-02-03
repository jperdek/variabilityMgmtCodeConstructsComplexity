package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import entities.ProjectEntity;
import entities.ReusableQualityMarker;
import entities.VariableEntitiesMarker;
import metrics.MemberQuality;
import metrics.ReuseMetrics;

class TestingReuseMetrics {

	public String[] evaluateEntity(Map<String, ProjectEntity> functionalEntities, ProjectEntity featureManagementEntity) {	
		Iterator<String> usedImports = featureManagementEntity.getImports().iterator();
		while(usedImports.hasNext()) {
			String usedImport = usedImports.next().replace("file:///", "");
			if (functionalEntities.containsKey(usedImport)) {
				ProjectEntity foundEntity = functionalEntities.get(usedImport);
				MemberQuality qualityMemberOfFoundEntity = foundEntity.getMemberQuality();

				Set<String> usedEntities = qualityMemberOfFoundEntity.getReusingComponents();
		
				String[] usedEntityIterator = usedEntities.toArray(new String[usedEntities.size()]);
				Arrays.sort(usedEntityIterator);
				return usedEntityIterator; //returns only first to evaluate - simplified testing
			}
		}
		return null;
	}
	
	@Test
	void test() {
		String featureManagementDirectory = "None";
		ExampleTree tree = new ExampleTree();
		Map<String, ProjectEntity> functionalEntities = tree.createTree();
		ProjectEntity[] featureManagementArrayEntities = tree.getFeatureManagementArrayEntities();
		List<ProjectEntity> featureManagementEntities = Arrays.asList(featureManagementArrayEntities);
		
		ReusableQualityMarker reusableQualityMarker = new ReusableQualityMarker();
		reusableQualityMarker.markSharingEntities(functionalEntities, featureManagementEntities, featureManagementDirectory);
		
		//FOR NODE FEATURE test1 which affects A.component.ts
		String[] entityArray1 = this.evaluateEntity(functionalEntities, featureManagementArrayEntities[0]);
		assertEquals(Arrays.toString(entityArray1), "[A.component.ts, B.component.ts, C.component.ts, D.component.ts, E.component.ts, F.component.ts, G.component.ts, H.component.ts, I.component.ts]");

		//FOR NODE FEATURE test2 which affects B.component.ts
		String[] entityArray2 = this.evaluateEntity(functionalEntities, featureManagementArrayEntities[1]);
		assertEquals(Arrays.toString(entityArray2), "[B.component.ts, E.component.ts, F.component.ts, H.component.ts, I.component.ts]");
	}
	
	public MemberQuality getMemberQualityOfEntity(Map<String, ProjectEntity> functionalEntities, ProjectEntity featureManagementEntity) {	
		Iterator<String> usedImports = featureManagementEntity.getImports().iterator();
		while(usedImports.hasNext()) {
			String usedImport = usedImports.next().replace("file:///", "");
			if (functionalEntities.containsKey(usedImport)) {
				ProjectEntity foundEntity = functionalEntities.get(usedImport);
				return foundEntity.getMemberQuality();
			}
		}
		return null;
	}
	
	@Test
	void test2() {
		String featureManagementDirectory = "None";
		ExampleTree2 tree = new ExampleTree2();
		Map<String, ProjectEntity> functionalEntities = tree.createTree();
		ProjectEntity[] featureManagementArrayEntities = tree.getFeatureManagementArrayEntities();
		List<ProjectEntity> featureManagementEntities = Arrays.asList(featureManagementArrayEntities);
		
		ReusableQualityMarker reusableQualityMarker = new ReusableQualityMarker();
		reusableQualityMarker.markSharingEntities(functionalEntities, featureManagementEntities, featureManagementDirectory);
		ReuseMetrics reuseMetrics = new ReuseMetrics();
		reuseMetrics.countComponentValueForAllMappedComponets(functionalEntities, featureManagementDirectory);
		
		MemberQuality memberQuality1 = this.getMemberQualityOfEntity(functionalEntities, featureManagementArrayEntities[0]);
		assertEquals(memberQuality1.getWholeCompoundValue(), 135.0);

		MemberQuality memberQuality2 = this.getMemberQualityOfEntity(functionalEntities, featureManagementArrayEntities[1]);
		assertEquals(memberQuality2.getWholeCompoundValue(), 90.0);
	}

}

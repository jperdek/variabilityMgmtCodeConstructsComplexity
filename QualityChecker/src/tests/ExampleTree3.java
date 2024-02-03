package tests;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import entities.ProjectEntity;


public class ExampleTree3 {

	private Map<String, ProjectEntity> tree = new HashMap<String, ProjectEntity>();

	public ExampleTree3() {
		ProjectEntity a = new ProjectEntity("A.component.ts", Arrays.asList(new String[]{"D.component.ts", "B.component.ts", "C.component.ts"}));
		ProjectEntity b = new ProjectEntity("B.component.ts", Arrays.asList(new String[]{"E.component.ts", "F.component.ts"}));
		ProjectEntity c = new ProjectEntity("C.component.ts", Arrays.asList(new String[]{"G.component.ts"}));
		ProjectEntity d = new ProjectEntity("D.component.ts", Arrays.asList(new String[]{}));
		ProjectEntity e = new ProjectEntity("E.component.ts", Arrays.asList(new String[]{}));
		ProjectEntity f = new ProjectEntity("F.component.ts", Arrays.asList(new String[]{"H.component.ts"}));
		ProjectEntity g = new ProjectEntity("G.component.ts", Arrays.asList(new String[]{"H.component.ts"}));
		ProjectEntity h = new ProjectEntity("H.component.ts", Arrays.asList(new String[]{"I.component.ts"}));
		ProjectEntity i = new ProjectEntity("I.component.ts", Arrays.asList(new String[]{}));

		tree.put("A.component.ts", a);
		tree.put("B.component.ts", b);
		tree.put("C.component.ts", c);
		tree.put("D.component.ts", d);
		tree.put("E.component.ts", e);
		tree.put("F.component.ts", f);
		tree.put("G.component.ts", g);
		tree.put("H.component.ts", h);
		tree.put("I.component.ts", i);
	}
	
	public Map<String, ProjectEntity> createTree() {
		return this.tree;
	}
	
	public List<ProjectEntity> getFeatureManagementEntities() {
		ProjectEntity test1 = new ProjectEntity("Test1", Arrays.asList(new String[]{"B.component.ts"}));
		ProjectEntity test2 = new ProjectEntity("Test2", Arrays.asList(new String[]{"G.component.ts"}));
		return Arrays.asList(new ProjectEntity[] {test1, test2});
	}
	
	public ProjectEntity[] getFeatureManagementArrayEntities() {
		ProjectEntity test1 = new ProjectEntity("Test1", Arrays.asList(new String[]{"B.component.ts"}));
		ProjectEntity test2 = new ProjectEntity("Test2", Arrays.asList(new String[]{"G.component.ts"}));
		return new ProjectEntity[] {test1, test2};
	}
	
	public List<ProjectEntity> getFeatureManagementEntities2() {
		ProjectEntity test1 = new ProjectEntity("Test1", Arrays.asList(new String[]{"B.component.ts"}));
		return Arrays.asList(new ProjectEntity[] {test1});
	}
	
	public ProjectEntity[] getFeatureManagementArrayEntities2() {
		ProjectEntity test1 = new ProjectEntity("Test1", Arrays.asList(new String[]{"B.component.ts"}));
		return new ProjectEntity[] {test1};
	}
}

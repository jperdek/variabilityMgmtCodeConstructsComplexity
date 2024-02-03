package tests;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entities.ProjectEntity;
import metrics.MemberQuality;


public class ExampleTree2 {
	private Map<String, ProjectEntity> tree = new HashMap<String, ProjectEntity>();

	public ExampleTree2() {
		MemberQuality am = new MemberQuality("A.component.ts", 1, 0, 0);
		ProjectEntity a = new ProjectEntity("A.component.ts", Arrays.asList(
				new String[]{"D.component.ts", "B.component.ts", "C.component.ts"}), am);
	
		MemberQuality bm = new MemberQuality("B.component.ts", 2, 0, 0);
		ProjectEntity b = new ProjectEntity("B.component.ts", Arrays.asList(new String[]{"E.component.ts", "F.component.ts"}), bm);
		
		MemberQuality cm = new MemberQuality("C.component.ts", 3, 0, 0);
		ProjectEntity c = new ProjectEntity("C.component.ts", Arrays.asList(new String[]{"G.component.ts"}), cm);
		
		MemberQuality dm = new MemberQuality("D.component.ts", 4, 0, 0);
		ProjectEntity d = new ProjectEntity("D.component.ts", Arrays.asList(new String[]{}), dm);
		
		MemberQuality em = new MemberQuality("E.component.ts", 5, 0, 0);
		ProjectEntity e = new ProjectEntity("E.component.ts", Arrays.asList(new String[]{}), em);
		
		MemberQuality fm = new MemberQuality("F.component.ts", 6, 0, 0);
		ProjectEntity f = new ProjectEntity("F.component.ts", Arrays.asList(new String[]{"H.component.ts"}), fm);
		
		MemberQuality gm = new MemberQuality("G.component.ts", 7, 0, 0);
		ProjectEntity g = new ProjectEntity("G.component.ts", Arrays.asList(new String[]{"H.component.ts"}), gm);
		
		MemberQuality hm = new MemberQuality("H.component.ts", 8, 0, 0);
		ProjectEntity h = new ProjectEntity("H.component.ts", Arrays.asList(new String[]{"I.component.ts"}), hm);
		
		MemberQuality im = new MemberQuality("I.component.ts", 9, 0, 0);
		ProjectEntity i = new ProjectEntity("I.component.ts", Arrays.asList(new String[]{}), im);

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
		ProjectEntity test1 = new ProjectEntity("Test1", Arrays.asList(new String[]{"A.component.ts"}));
		ProjectEntity test2 = new ProjectEntity("Test2", Arrays.asList(new String[]{"B.component.ts"}));
		return Arrays.asList(new ProjectEntity[] {test1, test2});
	}
	
	public ProjectEntity[] getFeatureManagementArrayEntities() {
		ProjectEntity test1 = new ProjectEntity("Test1", Arrays.asList(new String[]{"A.component.ts"}));
		ProjectEntity test2 = new ProjectEntity("Test2", Arrays.asList(new String[]{"B.component.ts"}));
		return new ProjectEntity[] {test1, test2};
	}
}

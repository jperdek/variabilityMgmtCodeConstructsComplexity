package entities;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;


public class SelectorAggregator {
	Map<String, String> selectorMapping = new HashMap<String, String>();
	
	public SelectorAggregator() {
	}
	
	public void add(String selectorName, String entityPath) {
		this.selectorMapping.put(selectorName, entityPath);
	}
	
	public String getSelectedNode(String selectorName) {
		return this.selectorMapping.get(selectorName);
	}
	
	public List<String> getAllSelectors() {
		return new ArrayList<String>(this.selectorMapping.keySet());
	}
}

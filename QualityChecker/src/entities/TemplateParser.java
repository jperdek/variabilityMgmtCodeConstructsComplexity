package entities;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;


public class TemplateParser {

	private SelectorAggregator selectorAggregator;
	
	public TemplateParser(SelectorAggregator selectorAggregator) {
		this.selectorAggregator = selectorAggregator;
	}
	
	public String getSelectorsValue(String selectorName) {
		return this.selectorAggregator.getSelectedNode(selectorName);
	}
	
	public Map<String, Integer> findAssociatedComponents(String templateContent) {
		Document document = Jsoup.parse(templateContent);
		Map<String, Integer> associatedTemplates = null;
		Iterator<String> selectors = this.selectorAggregator.getAllSelectors().iterator();
		
		while(selectors.hasNext()) {
			String actualSelector = selectors.next();
			Elements elementsInDocument = document.select(actualSelector);
			Object[] elementArray = elementsInDocument.toArray();
			if (elementArray.length > 0) {
				if (associatedTemplates == null) {
					associatedTemplates = new HashMap<String, Integer>();
				}
				associatedTemplates.put(actualSelector, elementArray.length);
			}
		}
		
		for (String actualSelector: this.getHTML5MainSelectors()) {
			Elements elementsInDocument = document.select(actualSelector);
			Object[] elementArray = elementsInDocument.toArray();
			if (elementArray.length > 0) {
				if (associatedTemplates == null) {
					associatedTemplates = new HashMap<String, Integer>();
				}
				associatedTemplates.put(actualSelector, elementArray.length);
			}
		}
		
		return associatedTemplates;
	}
	
	public String[] getHTML5MainSelectors() {
		return new String[] {"main", "nav", "address", "section", "article", "header", "footer"};
	}
}

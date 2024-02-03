package entities.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ImportRule {
	private boolean allow = false;
	private String regexRule = "";
	
	public ImportRule(String regexRule) {
		this.regexRule = regexRule;
	}
	
	public boolean check(String importToCheck) {
		Pattern pattern = Pattern.compile(this.regexRule);
		Matcher matcher = pattern.matcher(importToCheck);
		if(matcher.matches()) {
			return this.allow;
		}
		return !this.allow;
	}
}

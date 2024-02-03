package codeConstructsEvaluation.transformation;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class JSONResponseReader {

	public JSONResponseReader() {	
	}
	
	public static JSONObject loadJSONConfig(String jsonContent) {
		try {
	        JSONParser parser = new JSONParser();
	        JSONObject configurationObject = (JSONObject) parser.parse(jsonContent);
	        return configurationObject;
	    } catch (ParseException e) {
	        e.printStackTrace();
	    }
		return null;
	}
}

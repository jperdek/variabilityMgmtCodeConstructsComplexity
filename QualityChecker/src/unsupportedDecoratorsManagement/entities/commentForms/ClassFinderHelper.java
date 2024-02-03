package unsupportedDecoratorsManagement.entities.commentForms;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ClassFinderHelper {

	public static JSONObject getClassRecord(JSONObject astPart, String className) {
		String key;

		JSONArray members = (JSONArray) astPart.get("members");
		JSONObject nameObject;
		if (members != null) {
			nameObject = (JSONObject) astPart.get("name");
			if (((String) nameObject.get("escapedText")).equals(className)) {
				return astPart;
			}
		}
		Object entryValue;
		JSONObject entryJSONObject;
		JSONObject foundClass;
		for(Object entryKey: astPart.keySet()) {
			key = (String) entryKey;
			entryValue = astPart.get(key);
			if (entryValue instanceof JSONObject) {
				entryJSONObject = (JSONObject) entryValue;
				foundClass = ClassFinderHelper.getClassRecord(entryJSONObject, className);
				if (foundClass != null) {
					return foundClass;
				}
			} else if(entryValue instanceof JSONArray) {
				for (int index = 0; index < ((JSONArray) entryValue).size(); index++) {
					entryJSONObject = (JSONObject) ((JSONArray) entryValue).get(index);
					foundClass = ClassFinderHelper.getClassRecord(entryJSONObject, className);
					if (foundClass != null) {
						return foundClass;
					}
				}
			}
		}
		return null;
	}
}

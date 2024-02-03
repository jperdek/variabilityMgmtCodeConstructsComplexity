package parser.javascriptObjectRepresentations;
import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;


public class JavascriptString extends JavascriptRepresentation {
	String stringName;
	private static final boolean PRINT_MAPPING = true;
	String associatedNode = null;
	List<JavascriptRepresentation> javascriptRepresentations = new ArrayList<JavascriptRepresentation>();
	
	JavascriptString() {
		super();
	}
	
	JavascriptString(String stringName) {
		super();
		this.stringName = stringName;
	}
	
	JavascriptString(String stringName, JavascriptRepresentation javascriptRepresentation) {
		super();
		this.stringName = stringName;
		if (javascriptRepresentation != null) {
			this.javascriptRepresentations.add(javascriptRepresentation);
		}
	}
	
	public JavascriptRepresentation parse(BufferedReader bufferedReader) throws IOException {
		char rodeChar = '"';
		
		StringBuilder variable = new StringBuilder();
		while(bufferedReader.ready()) {
			rodeChar = (char) bufferedReader.read();
			if (rodeChar == '\"' || rodeChar == '\'') break;
			variable.append(rodeChar);
		}
		this.stringName = variable.toString().strip();
		return this;
	}
	
	public String getString() {
		return this.stringName;
	}

	public void print() {
		System.out.println(stringName);
		if (associatedNode != null && PRINT_MAPPING) {
			//associatedNode.print();
		}
		System.out.println("");
		if (javascriptRepresentations.size() != 0) {
			System.out.println("Found parts: [>");
			Iterator<JavascriptRepresentation> iterator = javascriptRepresentations.iterator();
			while(iterator.hasNext()) {
				iterator.next().print();
			}
			System.out.println("<]");
		}
	}
	
	public boolean associateNode(String nodeIdentifier, String functionalNode) {
		if (nodeIdentifier.equals(this.stringName)) { // || this.stringName.equals("RouterModule") should not be created
			this.associatedNode = functionalNode;
			return true;
		}
		return false;
	}
	
	public String getAssociatedNode() {
		return this.associatedNode;
	}
	
	public void updateAssociatedNode(String associatedNode) {
		this.associatedNode = associatedNode;
	}
	
	public List<JavascriptRepresentation> getAllAssociatedJavascriptRepresentations() {
		return null;
	}
	
	public JavascriptRepresentation getAssociatedJavascriptRepresentation(String identifier) {
		return null;
	}
}

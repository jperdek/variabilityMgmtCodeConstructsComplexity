package pathFiltering;

import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class PathFilter {

	private List<String> forbiddenPaths = new ArrayList<String>();
	
	public PathFilter() {
	}

	public PathFilter(String[] forbiddenPaths) {
		for(String forbiddenPath: forbiddenPaths) {
			this.forbiddenPaths.add(forbiddenPath); 
		}
	}
	
	public boolean filterPath(String pathToCheck) {
		Iterator<String> pathIterator = this.forbiddenPaths.iterator();
		String forbiddenPath = "";
		while(pathIterator.hasNext()) {
			forbiddenPath = pathIterator.next();
			if (pathToCheck.startsWith(forbiddenPath)) {
				System.out.println("Ignoring: " + pathToCheck);
				return false;
			}
		}
		return true;
	}
	
	public void addForbiddenPathsFromFile(String inputPath) throws IOException {
		boolean shouldRemove = true;
		FileReader fr = null;
		Scanner scanner = null;
		
		try {
			fr = new FileReader(inputPath);
			scanner = new Scanner(fr);
		
			while(scanner.hasNextLine()) {
				this.forbiddenPaths.add(scanner.nextLine());
			}
		} catch(FileNotFoundException fnf) {
			fnf.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(scanner != null) { scanner.close(); };
			if(fr != null) { fr.close(); };
		}
	}
}

package codeConstructsEvaluation.transformation;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;


public class PostRequester {

	public static String doPost(String serviceUrl, String fileContent) throws IOException, InterruptedException {
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				  .uri(URI.create(serviceUrl))
				  .header("Content-Type", "text/plain")
				  .POST(HttpRequest.BodyPublishers.ofString(fileContent))
				  .build();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		return response.body();
	}
	
	public static String loadFileContent(String fileName) throws IOException {
	    String fileContent = new String(Files.readAllBytes(Paths.get(fileName)), StandardCharsets.UTF_8);
	    return fileContent;
	}
}

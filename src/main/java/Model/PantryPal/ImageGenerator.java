package PantryPal;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONArray;
import org.json.JSONObject;


public class ImageGenerator implements DallE {

    private static final String API_ENDPOINT = "https://api.openai.com/v1/images/generations";
    // Replace with your API_KEY
    private static final String API_KEY = "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";
    private static final String MODEL = "dall-e-2";

    public String generateRecipeImage(String title, String ingredients) throws IOException, InterruptedException, URISyntaxException {

        // Set request parameters
        String prompt = title + "dish" + "with these ingredients: " + ingredients + "on a table";
        int n = 1;
    
    
          // Create a request body which you will pass into request object
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", MODEL);
        requestBody.put("prompt", prompt);
        requestBody.put("n", n);
        requestBody.put("size", "256x256");


        // Create the HTTP client
        HttpClient client = HttpClient.newHttpClient();
    
        // Create the request object
        HttpRequest request = HttpRequest
        .newBuilder()
        .uri(URI.create(API_ENDPOINT))
        .header("Content-Type", "application/json")
        .header("Authorization", String.format("Bearer %s", API_KEY))
        .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
        .build();
    
    
        // Send the request and receive the response
        HttpResponse<String> response = client.send(
        request,
        HttpResponse.BodyHandlers.ofString());
    
    
        // Process the response
        String responseBody = response.body();
    

        JSONObject responseJson = new JSONObject(responseBody);


        String generatedImageURL = responseJson.getJSONArray("data").getJSONObject(0).getString("url");
    
        System.out.println("DALL-E Response:");
        System.out.println(generatedImageURL);

        return generatedImageURL;
    

    }
}


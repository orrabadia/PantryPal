package PantryPal;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;

public class RecipeGenerator implements ChatGPT{
    
    private static final String API_ENDPOINT = "https://api.openai.com/v1/completions";
    private static final String API_KEY = "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";
    private static final String MODEL = "text-davinci-003";
    
    public String generateRecipe(String mealType, String ingredients) throws IOException, InterruptedException, URISyntaxException  {
        
        // Set request parameters
        String prompt = "I have " + ingredients + "and i want to make " + mealType + ", return the meal name followed by a tilde, then after that give me step by step instructions on how to make it";
        int maxTokens = 1028;

        // Create a request body which you will pass into request object
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", MODEL);
        requestBody.put("prompt", prompt);
        requestBody.put("max_tokens", maxTokens);
        requestBody.put("temperature", 1.0);
    
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
    
        String responseBody = response.body();
        JSONObject responseJson = new JSONObject(responseBody);
        
        JSONArray choices = responseJson.getJSONArray("choices");
        String generatedText = choices.getJSONObject(0).getString("text");
        //delete commas and newlines and returns to facilitate csv saving
        generatedText = generatedText.replace(",", "");
        generatedText = generatedText.replace("\n", " ");
        generatedText = generatedText.replace("\r", " ");
    
        return generatedText;
    }
    // public static void main(String[] args) {
    //     try {
    //         String returnResponse = generateRecipe("breakfast","eggs, bacon, cheese, bagel, seasoning");
    //         System.out.println(returnResponse);
    //     } catch (IOException e) {
    //         // TODO Auto-generated catch block
    //         e.printStackTrace();
    //     } catch (InterruptedException e) {
    //         // TODO Auto-generated catch block
    //         e.printStackTrace();
    //     } catch (URISyntaxException e) {
    //         // TODO Auto-generated catch block
    //         e.printStackTrace();
    //     }
    // }
    
}   


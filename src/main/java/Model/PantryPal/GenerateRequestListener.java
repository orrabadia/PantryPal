package PantryPal;

import com.sun.net.httpserver.*;
import java.io.*;
import java.util.*;

public class GenerateRequestListener implements HttpHandler {

    // Here is where we'll store the meal type and ingredients
    private String mealType;
    private String ingredients;

    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "Request Received";
        String method = httpExchange.getRequestMethod();
        try {
            response = handlePut(httpExchange);
          } catch (Exception e) {
            System.out.println("An erroneous request");
            response = e.toString();
            e.printStackTrace();
          }
          //Sending back response to the client
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream outStream = httpExchange.getResponseBody();
        outStream.write(response.getBytes());
        outStream.close();
       
    }

    private String handlePut(HttpExchange httpExchange) throws IOException {
        InputStream inStream = httpExchange.getRequestBody();
        Scanner scanner = new Scanner(inStream);
        String putData = scanner.nextLine();
        System.out.println("putData = " + putData);
        String[] recipeValues = putData.split(",");
        String mealType = recipeValues[0];
        String ingredients = recipeValues[1];

        scanner.close();

        this.mealType = mealType;
        this.ingredients = ingredients;

        GPTHandler g = new GPTHandler();
        String recipe = g.generate(mealType, ingredients);

        return recipe;
    }
     

}
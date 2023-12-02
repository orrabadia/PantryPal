package PantryPal;

import com.sun.net.httpserver.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.Map;

import org.json.JSONObject;

public class ShareRequestListener implements HttpHandler {

    private final Map<String, String> data;
    private MongoDB m;
      
    public ShareRequestListener(Map<String, String> data) {
      this.data = data;
      m = new MongoDB();
    }

    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "Request Received";
        String method = httpExchange.getRequestMethod();
        try {
            response = handleGet(httpExchange);
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

    private String handleGet(HttpExchange httpExchange) throws IOException {
      String response = "Invalid GET request";
      URI uri = httpExchange.getRequestURI();
      
      // Parse path segments from the URI
      String[] pathSegments = uri.getPath().split("/");
      
      if (pathSegments.length == 4 && pathSegments[1].equals("share")) {
          String username = pathSegments[2];
          String index = pathSegments[3];

          String recipeString = m.getRecipe(username, index);

          JSONObject recipeJSON = new JSONObject(recipeString);

          String title = recipeJSON.getString("title");
          String mealType = recipeJSON.getString("mealType");
          String ingredients = recipeJSON.getString("ingredients");
          String instructions = recipeJSON.getString("instructions");
  
          StringBuilder htmlBuilder = new StringBuilder();
          htmlBuilder
              .append("<html>")
              .append("<body>")
              .append("<h1>")
              .append("Title: ")
              .append(title)
              .append("<br>")
              .append("Meal Type: ")
              .append(mealType)
              .append("<br>")
              .append("Ingredients: ")
              .append(ingredients)
              .append("<br>")
              .append("Instructions: ")
              .append(instructions)
              .append("<br>")
              .append("<img src=\"https://upload.wikimedia.org/wikipedia/commons/8/8a/Banana-Single.jpg\">")  // does not work
              .append("<br>")
              .append("</h1>")
              .append("</body>")
              .append("</html>");
  
          // encode HTML content
          httpExchange.getResponseHeaders().set("Content-Type", "text/html");
          response = htmlBuilder.toString();
  
          System.out.println("Username: " + username);
          System.out.println("Index: " + index);
      } else {
          System.out.println("Invalid path structure");
      }
  
      return response;
      }

    

}

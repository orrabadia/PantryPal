package PantryPal;

import com.sun.net.httpserver.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.Map;

import org.json.JSONObject;

public class ShareRequestListener implements HttpHandler {

    private ImageDisplayHandler i;
    private MongoDB m;
      
    public ShareRequestListener(ImageDisplayHandler i) {
      this.i = i;
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
      
      if (validLink(pathSegments)) {
          String username = pathSegments[2];
          String index = pathSegments[3];

          String recipeString = m.getRecipe(username, index);

          JSONObject recipeJSON = new JSONObject(recipeString);

          String title = recipeJSON.getString("title");
          String mealType = recipeJSON.getString("mealType");
          String ingredients = recipeJSON.getString("ingredients");
          String instructions = recipeJSON.getString("instructions");

          String url = i.getUrl(Integer.parseInt(index));

          HTMLBuilder htmlB = new HTMLBuilder(title, mealType,ingredients, instructions,url);
  
          // encode HTML content
          httpExchange.getResponseHeaders().set("Content-Type", "text/html");
          response = htmlB.buildHTML().toString();
  
          System.out.println("Username: " + username);
          System.out.println("Index: " + index);
          System.out.println("response: " + response);
      } else {
          System.out.println("Invalid path structure");
      }
  
      return response;
      }

      public boolean validLink(String[] pathSegments) {
        if (pathSegments.length == 4 && pathSegments[1].equals("share")) {
          return true;
        }
        return false;
      }

    

}

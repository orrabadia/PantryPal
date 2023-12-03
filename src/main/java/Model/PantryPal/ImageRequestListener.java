package PantryPal;
import com.mongodb.client.MongoDatabase;
import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.util.*;
import org.json.*;
public class ImageRequestListener implements HttpHandler {
    private DallE d;
  
    //TODO: all this has to do is call dalle method and return the url string

    public ImageRequestListener(){
        d = new ImageGenerator();
    }

    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "Request Received";
        String method = httpExchange.getRequestMethod();
        try {
            if (method.equals("POST")) {
              response = handlePost(httpExchange);
            } else {
              throw new Exception("Not Valid Request Method");
            }
          } catch (Exception e) {
            System.out.println("An erroneous request");
            response = e.toString();
            e.printStackTrace();
          }
          //Sending back response to the client
        //httpExchange.getResponseHeaders().set("Content-Type", "application/json");
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream outStream = httpExchange.getResponseBody();
        outStream.write(response.getBytes());
        outStream.close();
       
    }

    

    //** return url of generated image */
    private String handlePost(HttpExchange httpExchange) throws IOException {
      InputStream inStream = httpExchange.getRequestBody();
      Scanner scanner = new Scanner(inStream);
      String imageData = scanner.nextLine();
      String[] recipeValues = imageData.split(",");
      String title = recipeValues[0];
      String ingredients = recipeValues[1];

      scanner.close();

      String ret = "ERROR";
      try {
          ret = d.generateRecipeImage(title, ingredients);
      } catch (IOException e1) {
          System.out.println("IOEXCEPTION" + e1.toString());
      } catch (InterruptedException e2) {
          System.out.println("INTERRUPTEDEXCEPTION" + e2.toString());
      } catch (URISyntaxException e3){
          System.out.println("URISYNTAXEXCEPTION" + e3.toString());
      }
      return ret;

    }


}

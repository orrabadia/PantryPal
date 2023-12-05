package PantryPal;
import com.mongodb.client.MongoDatabase;
import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.util.*;
import org.json.*;
public class ImageRequestListener implements HttpHandler {
    private DallE d;
    private ImageDisplayHandler i;

    public ImageRequestListener(ImageDisplayHandler i){
        d = new ImageGenerator();
        this.i = i;
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
      String user = recipeValues[2];
      String index = recipeValues[3];
      scanner.close();

      String ret = "ERROR";
      try {
        i.setUser(user);
        //if exist use, if not generate 
        if(i.check(Integer.parseInt(index))){
          ret = i.getUrl(Integer.parseInt(index));
        } else {
          ret = d.generateRecipeImage(title, ingredients);
        }
        i.store(ret, Integer.parseInt(index), user);
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

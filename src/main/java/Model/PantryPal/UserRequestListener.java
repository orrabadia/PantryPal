package PantryPal;
import com.mongodb.client.MongoDatabase;
import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.util.*;
import org.json.*;
public class UserRequestListener implements HttpHandler {
    private MongoDB mongoDB;
    private final Map<String, String> data;
  
    //may need to add more?

    public UserRequestListener(Map<String, String> data){
        this.data = data;
        mongoDB = new MongoDB();
        
    }

    public void handle(HttpExchange httpExchange /*username parameter? */) throws IOException {
        //boolean responseBool = false;
        String response = "Request Received";
        String method = httpExchange.getRequestMethod();
        try {
            if (method.equals("GET")) {
              response = handleGet(httpExchange);
              //response = handleGet(httpExchange);
              httpExchange.getResponseHeaders().set("Content-Type", "application/json");
            } else if (method.equals("PUT")) {
                response = handlePut(httpExchange);
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

    


    private String handleGet(HttpExchange httpExchange) throws IOException {
      // Converting the JSON array to a string
      //note that this has to be a single string with no newlines before you send it back
      String username = "";
      URI uri = httpExchange.getRequestURI();
      String query = uri.getRawQuery();
      if (query != null) {
        username = query.substring(query.indexOf("=") + 1);
        System.out.println(username);
      }
      
      String check = mongoDB.find(username);
      JSONObject details = new JSONObject(check);
      //check = check.replaceAll("[\n\r]", "");

      System.out.println("REQUESTLISTENER RET: " + check);
      String pass = details.getString(username);
      System.out.println(username + " password is: " + pass +  " :END");

      return pass;

    }

    private String handlePut(HttpExchange httpExchange) throws IOException {
      //parse username from query
      String username = "";
      URI uri = httpExchange.getRequestURI();
      String query = uri.getRawQuery();
      if (query != null) {
        username = query.substring(query.indexOf("=") + 1);
      }
        InputStream inStream = httpExchange.getRequestBody();
        Scanner scanner = new Scanner(inStream);
        String putData = scanner.nextLine();
        String[] userCredentials = putData.split(",");
        String user = userCredentials[0];
        String pass = userCredentials[1];

        mongoDB.putUsername(user, pass);
        scanner.close();
        return "Success";
    }


}
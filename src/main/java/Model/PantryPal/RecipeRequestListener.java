package PantryPal;
import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.util.*;
import org.json.*;
public class RecipeRequestListener implements HttpHandler{
    private final Map<String, String> data;
    private RecipeList rList;
    private MongoDB m;


    public RecipeRequestListener(Map<String, String> data) {
      this.data = data;
      rList = new RecipeList();
      //initialze recipelist with data from our backend
      rList.refresh();
      m = new MongoDB();
    }

    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "Request Received";
        String method = httpExchange.getRequestMethod();
        try {
            if (method.equals("GET")) {
              response = handleGet(httpExchange);
              httpExchange.getResponseHeaders().set("Content-Type", "application/json");
            } else if (method.equals("POST")) {
              response = handlePost(httpExchange);
            } else if (method.equals("PUT")) {
                response = handlePut(httpExchange);
            } else if (method.equals("DELETE")) {
                response = handleDelete(httpExchange);
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
      //should return string version of current recipelist in backend

      // Converting the JSON array to a string
      //note that this has to be a single string with no newlines before you send it back
      String username = "";
      URI uri = httpExchange.getRequestURI();
      String query = uri.getRawQuery();
      if (query != null) {
        username = query.substring(query.indexOf("=") + 1);
      }
      
      String ret = m.get(username);
      ret = ret.replaceAll("[\n\r]", "");
      System.out.println("REQUESTLISTENER RET: " + ret);
      return ret;

      }

    private String handlePost(HttpExchange httpExchange) throws IOException {
      String username = "";
      URI uri = httpExchange.getRequestURI();
      String query = uri.getRawQuery();
      
      if (query != null) {
          username = query.substring(query.indexOf("=") + 1);
        }
        //this is for editing, do this later
        InputStream inStream = httpExchange.getRequestBody();
        Scanner scanner = new Scanner(inStream);
        String putData = scanner.nextLine();
        String[] recipeValues = putData.split(",");
        String title = recipeValues[0];
        String mealtype = recipeValues[1];
        String ingredients = recipeValues[2];
        String instructions = recipeValues[3];
        String index = recipeValues[4];
        
        m.post(username, title, mealtype, ingredients, instructions, index);
        scanner.close();
        return "Success";
        
        // InputStream inStream = httpExchange.getRequestBody();
        // Scanner scanner = new Scanner(inStream);
        // String postData = scanner.nextLine();
        // String language = postData.substring(
        //     0,
        //     postData.indexOf(",")
        // ), year = postData.substring(postData.indexOf(",") + 1);


        // // Store data in hashmap
        // data.put(language, year);


        // String response = "Posted entry {" + language + ", " + year + "}";
        // System.out.println(response);
        // scanner.close();


        // return response;
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
        String[] recipeValues = putData.split(",");
        String title = recipeValues[0];
        String mealtype = recipeValues[1];
        String ingredients = recipeValues[2];
        String instructions = recipeValues[3];

        System.out.println("RECIEVED: " + title + mealtype + ingredients + instructions);

        m.put(username, title, mealtype, ingredients, instructions);
        scanner.close();
        return "Success";

        // Recipe r = new Recipe(title, mealtype, ingredients, instructions);
        // //add adds to rlist and updates csv
        // rList.add(r);
        // rList.refresh();

        // //you might have to refresh again, it seems to clear after adding
        // scanner.close();
        // //returns csv
        // return rList.stringify();

        // String language = putData.substring(0, putData.indexOf(","));
        // String year = putData.substring(putData.indexOf(",") + 1);
    
        // if (data.containsKey(language)) {
        //     String previousYear = data.get(language);
        //     data.put(language, year);
        //     String response = "Updated entry {" + language + ", " + year + "} (previous year: " + previousYear + ")";
        //     System.out.println(response);
        //     scanner.close();
        //     return response;
        // } else {
        //     data.put(language, year);
        //     String response = "Added entry {" + language + ", " + year + "}";
        //     System.out.println(response);
        //     scanner.close();
        //     return response;
        // }
    }

    private String handleDelete(HttpExchange httpExchange) throws IOException {
      //delete recipe by title and return new list
      String username = "";
      URI uri = httpExchange.getRequestURI();
      String query = uri.getRawQuery();
      
      if (query != null) {
        username = query.substring(query.indexOf("=") + 1);
      }
        
        InputStream inStream = httpExchange.getRequestBody();
        Scanner scanner = new Scanner(inStream);
        String putData = scanner.nextLine();
        String[] recipeValues = putData.split(",");
        String title = recipeValues[0];
        String mealtype = recipeValues[1];
        String ingredients = recipeValues[2];
        String instructions = recipeValues[3];
        String index = recipeValues[4];

        // Recipe r = new Recipe(title, mealtype, ingredients, instructions);
        //this removes and updates csv
        //rList.remove(title);
        //scanner.close();
        //returns csv
        //return rList.stringify();
        m.delete(username, index);
        scanner.close();
        return "Success";


        // String response = "Invalid DELETE request";
        // URI uri = httpExchange.getRequestURI();
        // String query = uri.getRawQuery();
        
        // if (query != null) {
        //     String value = query.substring(query.indexOf("=") + 1);
            
            
        //     if (data.containsKey(value)) {
        //         String deletedYear = data.remove(value); 
        //         response = "Deleted entry {" + value + ", " + deletedYear + "}";
        //         System.out.println(response);
        //     } else {
        //         response = "No data found for " + value;
        //     }
        // }
        // return response;
    }
}

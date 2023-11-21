package PantryPal;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URI;

public class RequestHandler {
    public String performRecipeRequest(String method, String title, String mealtype, String ingredients, String instructions, String query) {
        // Implement your HTTP request logic here and return the response
        try {
            String urlString = "http://localhost:8100/recipe/";
            if (query != null) {
                urlString += "?=" + query;
            }
            URL url = new URI(urlString).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.setDoOutput(true);

            if (method.equals("POST") || method.equals("PUT")) {
                OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
                out.write(title + "," + mealtype + "," + ingredients + "," + instructions);
                out.flush();
                out.close();
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder responseBuilder = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                if(line.equals("")){
                    System.out.println("cringe");
                }
                responseBuilder.append(line);
                System.out.println(line);
                responseBuilder.append('\n');  // Optionally, add newline characters between lines
            }

            in.close();
            return responseBuilder.toString();
            //return new list, which should always be returned 
            // String response = in.readLine();
            // in.close();
            // return response;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "Error: " + ex.getMessage();
        }
    }
}

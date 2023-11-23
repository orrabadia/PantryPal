package PantryPal;

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class GenerateRequestListener implements HttpHandler {

    // TODO: Here is where we'll store the meal type and ingredients
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

        // TODO: Code below is a former part of the Lab 5 code, but we don't want to deal with year and language
        // Scanner scanner = new Scanner(inStream);
        // String putData = scanner.nextLine();
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

    // TODO: helper method to help read per line
    private static String readLine(InputStream is, String lineSeparator) 
    throws IOException {

    int off = 0, i = 0;
    byte[] separator = lineSeparator.getBytes("UTF-8");
    byte[] lineBytes = new byte[1024];
    
    while (is.available() > 0) {
        int nextByte = is.read();
        if (nextByte < -1) {
            throw new IOException(
                "Reached end of stream while reading the current line!");
        }
        
        lineBytes[i] = (byte) nextByte;
        if (lineBytes[i++] == separator[off++]) {
            if (off == separator.length) {
                return new String(
                    lineBytes, 0, i-separator.length, "UTF-8");
            }
        }
        else {
            off = 0;
        }
        
        if (i == lineBytes.length) {
            throw new IOException("Maximum line length exceeded: " + i);
        }
    }
    
    throw new IOException(
        "Reached end of stream while reading the current line!");       
    }
     

}
package PantryPal;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
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
            //if post or put write to outstream
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
                //System.out.println("REQUSTHANDLER RESPONSE:" + line);
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
    public String performAudioRequest(String method) {
        // TODO: the filepath here is localized to the client folder to ensure that we don't read off of the entire
        final File uploadFile = new File("src/main/java/View/PantryPal/recording.wav");

        // TODO: Below code is to help with multipart/form data, probably something with separating boundaries
        String boundary = Long.toHexString(System.currentTimeMillis());
        String CRLF = "\r\n";
        String charset = "UTF-8";
        
        // Implement your HTTP request logic here and return the response
        try {
            String urlString = "http://localhost:8100/audio";
            URL url = new URI(urlString).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.setDoOutput(true);

            //TODO: Below is to set the request property Content Type to indiicate request body will be in multipart/form data format
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            if (method.equals("POST") || method.equals("PUT")) {
                OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
                
                //TODO: use a PrintWriter to write character text to output stream
                PrintWriter writer = new PrintWriter(out, true);

                // TODO: write the multipart/form data headers
                writer.append("--" + boundary).append(CRLF);
                writer.append("Content-Disposition: form-data; name=\"audio\"; filename=\"" + uploadFile.getName() + "\"").append(CRLF);
                writer.append("Content-Length: " + uploadFile.length()).append(CRLF);
                writer.append("Content-Type: audio/wav").append(CRLF);
                writer.append("Content-Transfer-Encoding: binary").append(CRLF);
                writer.append(CRLF).flush();
                // Copy the file content to the output stream
                Files.copy(uploadFile.toPath(), conn.getOutputStream());

                // TODO: Commented out anything with language or year so we can only focus on the audio file
                //out.write(language + "," + year);
                out.flush();
                out.close();
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response = in.readLine();
            in.close();
            return response;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "Error: " + ex.getMessage();
        }
    }
}
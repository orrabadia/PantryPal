package PantryPal;

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class AudioRequestListener implements HttpHandler {

    private final Map<String, String> data;

    // TODO: Here is where we still store the audio file from the local
    private File audio;


    public AudioRequestListener(Map<String, String> data) {
    this.data = data;
    }

    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "Request Received";
        String method = httpExchange.getRequestMethod();
        try {
            if (method.equals("GET")) {
              response = handleGet(httpExchange);
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
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream outStream = httpExchange.getResponseBody();
        outStream.write(response.getBytes());
        outStream.close();
       
    }
    private String handleGet(HttpExchange httpExchange) throws IOException {
        String response = "Invalid GET request";
        URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery();
        if (query != null) {
          String value = query.substring(query.indexOf("=") + 1);
          String year = data.get(value); // Retrieve data from hashmap
          if (year != null) {
            response = year;
            System.out.println("Queried for " + value + " and found " + year);
          } else {
            response = "No data found for " + value;
          }
        }
        return response;
      }

    private String handlePost(HttpExchange httpExchange) throws IOException {
        InputStream inStream = httpExchange.getRequestBody();
        Scanner scanner = new Scanner(inStream);
        String postData = scanner.nextLine();
        String language = postData.substring(
            0,
            postData.indexOf(",")
        ), year = postData.substring(postData.indexOf(",") + 1);


        // Store data in hashmap
        data.put(language, year);


        String response = "Posted entry {" + language + ", " + year + "}";
        System.out.println(response);
        scanner.close();


        return response;
    }

    private String handlePut(HttpExchange httpExchange) throws IOException {
        // TODO: Since we only change the audiofile with PUT, this is where we do it
        // TODO: Below, we're doing some processing work
        String CRLF = "\r\n";
        int fileSize = 0;
        // TODO: the filepath here is localized to the server file to ensure that we don't read off of the entire
        String FILE_TO_RECEIVED = "src/main/java/Model/PantryPal/recording.wav";
        // not sure if the File f stuff is needed
        File f = new File(FILE_TO_RECEIVED);
        if (!f.exists()) {
            f.createNewFile();
        }
      
        InputStream inStream = httpExchange.getRequestBody();
        
        // TODO: Below we read the content-length header
        String nextLine = "";
        do {
          nextLine = readLine(inStream, CRLF);
            if (nextLine.startsWith("Content-Length:")) {
                fileSize = Integer.parseInt(
                    nextLine.replaceAll(" ", "").substring("Content-Length:".length())
                );
            }
            System.out.println(nextLine);
        } while (!nextLine.equals(""));
        
        // TODO: Read and save file data
        byte[] midFileByteArray = new byte[fileSize];
        int readOffset = 0;
        while (readOffset < fileSize) {
            int bytesRead = inStream.read(midFileByteArray, readOffset, fileSize);
            readOffset += bytesRead;
        }

        // TODO: Write file to disk
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(FILE_TO_RECEIVED));
        bos.write(midFileByteArray, 0, fileSize);
        bos.flush();
        bos.close();

        // TODO: this should set our uploaded file to the one in the server and check whether it has content
        audio = new File(FILE_TO_RECEIVED);

        String response = "bad transcript";

        // if (audio != null && audio.length() > 0) {
        //   response = "recording.wav uploaded successfully";
        // } else {
        //    response = "unsuccessful upload";
        // }

        //TODO: here implement the WhisperAPI (that performs the POST call to Whisper) and return the transcript
        WhisperHandler wHandler = new WhisperHandler();
        try {
          response = wHandler.transcribe();
        }
        catch (IOException e1){
          System.err.println("IOException");
        }
        catch (URISyntaxException e2){
            System.err.println("URISyntaxException");
        }

        return response;


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

    private String handleDelete(HttpExchange httpExchange) throws IOException {
        String response = "Invalid DELETE request";
        URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery();
        
        if (query != null) {
            String value = query.substring(query.indexOf("=") + 1);
            
            
            if (data.containsKey(value)) {
                String deletedYear = data.remove(value); 
                response = "Deleted entry {" + value + ", " + deletedYear + "}";
                System.out.println(response);
            } else {
                response = "No data found for " + value;
            }
        }
        return response;
    }
     

}
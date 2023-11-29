package PantryPal;

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class AudioRequestListener implements HttpHandler {

    private final Map<String, String> data;

    // Here is where we still store the audio file from the local
    private File audio;


    public AudioRequestListener(Map<String, String> data) {
    this.data = data;
    }

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
        // Since we only change the audiofile with PUT, this is where we do it
        // Below, we're doing some processing work
        String CRLF = "\r\n";
        int fileSize = 0;
        // the filepath here is localized to the server file to ensure that we don't read off of the entire
        String FILE_TO_RECEIVED = "src/main/java/Model/PantryPal/recording.wav";
        // not sure if the File f stuff is needed
        File f = new File(FILE_TO_RECEIVED);
        if (!f.exists()) {
            f.createNewFile();
        }
      
        InputStream inStream = httpExchange.getRequestBody();
        
        // Below we read the content-length header
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
        
        // Read and save file data
        byte[] midFileByteArray = new byte[fileSize];
        int readOffset = 0;
        while (readOffset < fileSize) {
            int bytesRead = inStream.read(midFileByteArray, readOffset, fileSize);
            readOffset += bytesRead;
        }

        // Write file to disk
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(FILE_TO_RECEIVED));
        bos.write(midFileByteArray, 0, fileSize);
        bos.flush();
        bos.close();

        // this should set our uploaded file to the one in the server and check whether it has content
        audio = new File(FILE_TO_RECEIVED);

        String response = "bad transcript";

        // if (audio != null && audio.length() > 0) {
        //   response = "recording.wav uploaded successfully";
        // } else {
        //    response = "unsuccessful upload";
        // }

        //here implement the WhisperAPI (that performs the POST call to Whisper) and return the transcript
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
    }

    // helper method to help read per line
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
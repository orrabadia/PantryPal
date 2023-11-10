package PantryPal;
import java.io.*;
import java.net.*;
import org.json.*;

public class MockWhisper implements Whisper {
    public String whisperTranscribe(String fileName) throws IOException, URISyntaxException {
        // see if you can find "recording.wav", if so, return dinner, else return error message
        File file = new File(fileName);
        if (file.exists()) {
            return "Dinner";
        } else {
            return "File not found";
        }

    }
}

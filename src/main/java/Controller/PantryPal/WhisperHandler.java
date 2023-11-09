package PantryPal;

import java.io.IOException;
import java.net.URISyntaxException;

public class WhisperHandler {
    WhisperApi whisper;
    public String transcribe() throws IOException, URISyntaxException {
        whisper = new WhisperApi();
        return whisper.whisperTranscribe("./recording.wav");
    }
}

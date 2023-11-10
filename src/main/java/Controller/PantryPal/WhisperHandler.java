package PantryPal;

import java.io.IOException;
import java.net.URISyntaxException;

public class WhisperHandler {
    private WhisperApi whisper;

    WhisperHandler () {
        whisper = new WhisperApi();
    }

    public String transcribe() throws IOException, URISyntaxException {
        whisper = new WhisperApi();
        return whisper.whisperTranscribe("./recording.wav");
    }
}

package PantryPal;

import java.io.IOException;
import java.net.URISyntaxException;

public class WhisperHandler {
    private Whisper whisper;

    WhisperHandler () {
        whisper = new WhisperApi();
    }

    WhisperHandler (boolean mock) {
        whisper = new MockWhisper();
    }

    public String transcribe() throws IOException, URISyntaxException {
        whisper = new WhisperApi();
        return whisper.whisperTranscribe("./recording.wav");
    }
}

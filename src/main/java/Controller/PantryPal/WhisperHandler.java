package PantryPal;

import java.io.IOException;
import java.net.URISyntaxException;

public class WhisperHandler {
    //handle whisper transcription between frontend and model
    private Whisper whisper;

    WhisperHandler () {
        whisper = new WhisperApi();
    }

    WhisperHandler (boolean mock) {
        whisper = new MockWhisper();
    }

    public String transcribe() throws IOException, URISyntaxException {
        return whisper.whisperTranscribe("./recording.wav");
    }
}

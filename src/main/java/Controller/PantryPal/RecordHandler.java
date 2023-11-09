package PantryPal;

import javafx.scene.control.Label;
import java.io.*;
import java.net.*;

public class RecordHandler {

    AudioRecorder audioRecorder;

    RecordHandler() {

    }

    RecordHandler(RecordAppFrame recordAppFrame) {
    
    }

    public void record() {
        audioRecorder = new AudioRecorder();
        audioRecorder.startRecording();
    }

    public String stop() throws IOException, URISyntaxException {
        audioRecorder.stopRecording();
        return audioRecorder.transcribe();
    }
}

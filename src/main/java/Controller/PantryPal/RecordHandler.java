package PantryPal;

import javafx.scene.control.Label;
import java.io.*;
import java.net.*;

public class RecordHandler {
    private Recorder ar;

    //accept argument, and thats how you decide to mock or not
    RecordHandler() {
        ar = new AudioRecorder();
    }

    RecordHandler(boolean mock) {
        ar = new MockRecorder();
    }

    public void record() throws IOException {
        //audioRecorder = new AudioRecorder();
        ar.startRecording();
    }

    public void stop() {
        ar.stopRecording();
        //return audioRecorder.transcribe();
        //return "";
    }
}

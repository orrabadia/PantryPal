package PantryPal;

import javafx.scene.control.Label;
import java.io.*;
import java.net.*;

public class RecordHandler {
    private AudioRecorder ar;

    //accept arguemtn, and thats how you decide to mock or not
    RecordHandler() {
        ar = new AudioRecorder();
    }

    RecordHandler(RecordAppFrame recordAppFrame) {
    
    }

    public void record() {
        //audioRecorder = new AudioRecorder();
        ar.startRecording();
    }

    public void stop() {
        ar.stopRecording();
        //return audioRecorder.transcribe();
        //return "";
    }
}

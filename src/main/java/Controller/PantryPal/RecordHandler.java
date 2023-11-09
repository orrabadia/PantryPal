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

    public void stop() {
        audioRecorder.stopRecording();
        //return audioRecorder.transcribe();
        //return "";
    }
}

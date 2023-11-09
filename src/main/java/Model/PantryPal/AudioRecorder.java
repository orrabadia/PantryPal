package PantryPal;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.geometry.Insets;
import java.io.*;
import javax.sound.sampled.*;

import java.net.*;
import org.json.*;

public class AudioRecorder extends Application {

    private TargetDataLine targetDataLine;

    @Override
    public void start(Stage primaryStage) throws Exception {

        // // Setting the Layout of the Window (Flow Pane)
        // RecordAppFrame root = new RecordAppFrame();

        // // Set the title of the app
        // primaryStage.setTitle("Audio Recorder");
        // // Create scene of mentioned size with the border pane
        // primaryStage.setScene(new Scene(root, 370, 120));
        // // Make window non-resizable
        // primaryStage.setResizable(false);
        // // Show the app
        // primaryStage.show();
    }

    public String transcribe() throws IOException, URISyntaxException {
        return WhisperApi.whisperTranscribe("./recording.wav");
    }

    private AudioFormat getAudioFormat() {
        // the number of samples of audio per second.
        // 44100 represents the typical sample rate for CD-quality audio.
        float sampleRate = 44100;

        // the number of bits in each sample of a sound that has been digitized.
        int sampleSizeInBits = 16;

        // the number of audio channels in this format (1 for mono, 2 for stereo).
        int channels = 1;

        // whether the data is signed or unsigned.
        boolean signed = true;

        // whether the audio data is stored in big-endian or little-endian order.
        boolean bigEndian = false;

        return new AudioFormat(
                sampleRate,
                sampleSizeInBits,
                channels,
                signed,
                bigEndian);
    }

    public void startRecording() {
        Thread t = new Thread(
            new Runnable() {
                @Override
                public void run() {
                    try {
                        // the format of the TargetDataLine
                        DataLine.Info dataLineInfo = new DataLine.Info(
                                TargetDataLine.class,
                                getAudioFormat());
                        // the TargetDataLine used to capture audio data from the microphone
                        targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
                        targetDataLine.open(getAudioFormat());
                        targetDataLine.start();

                        // the AudioInputStream that will be used to write the audio data to a file
                        AudioInputStream audioInputStream = new AudioInputStream(
                                targetDataLine);

                        // the file that will contain the audio data
                        File audioFile = new File("recording.wav");
                        AudioSystem.write(
                                audioInputStream,
                                AudioFileFormat.Type.WAVE,
                                audioFile);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
            t.start();
    }

    public void stopRecording() {
        targetDataLine.stop();
        targetDataLine.close();
    }

    public static void main(String[] args) {
        // launch(args);
        System.out.println("This should never be run on its own, only used for RecordAppFrame");
    }
}

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

class RecordAppFrame extends FlowPane {
    private NavigationHandler handler;
    private Button startButton;
    private Button stopButton;
    private Button backButton;
    private Button continueButton;
    private AudioFormat audioFormat;
    private TargetDataLine targetDataLine;
    private Label recordingLabel;
    private Label transcriptionLabel;
    private Label instructions;
    Thread t;

    // Set a default style for buttons and fields - background color, font size,
    // italics
    String defaultButtonStyle = "-fx-border-color: #000000; -fx-font: 13 arial; -fx-pref-width: 175px; -fx-pref-height: 50px;";
    String defaultLabelStyle = "-fx-font: 13 arial; -fx-pref-width: 175px; -fx-pref-height: 50px; -fx-text-fill: red; visibility: hidden";

    RecordAppFrame(NavigationHandler handler) throws IOException, URISyntaxException {
        // Set properties for the flowpane
        this.setPrefSize(370, 120);
        this.setPadding(new Insets(5, 0, 5, 5));
        this.setVgap(10);
        this.setHgap(10);
        this.setPrefWrapLength(170);

        this.handler = handler;

        // Add the buttons and text fields
        instructions = new Label("Please record your meal type");
        startButton = new Button("Start");
        startButton.setStyle(defaultButtonStyle);

        stopButton = new Button("Stop");
        stopButton.setStyle(defaultButtonStyle);

        recordingLabel = new Label("Recording...");
        recordingLabel.setStyle(defaultLabelStyle);

        this.getChildren().addAll(startButton, stopButton, recordingLabel);

        backButton = new Button("Back");
        this.getChildren().add(backButton);

        transcriptionLabel = new Label("Please say the meal type you want:");
        this.getChildren().add(transcriptionLabel);

        // Get the audio format
        audioFormat = getAudioFormat();

        // Add the listeners to the buttons
        addListeners();
    }

    public void addListeners() throws IOException, URISyntaxException {
        // Start Button
        startButton.setOnAction(e -> {
            startRecording();
        });

        // Stop Button
        stopButton.setOnAction(e -> {
            stopRecording();
            //RESULT OF TRANSCRIPTION STORED HERE
            String transcription = "";
            try {
                transcription = transcribe();
                Label l = (Label)this.getChildren().get(this.getChildren().size()-1);
                l.setText("Meal Type:" + transcription);
            }
            catch (IOException e1){
                System.err.println("IOException");
            }
            catch (URISyntaxException e2){
                System.err.println("URISyntaxException");
            }
            continueButton = new Button("Continue");
            this.getChildren().add(continueButton);
            continueButton.setOnAction(e1->{
                System.out.println("Unimplemented");
            });
        });

        //go back on back button
        backButton.setOnAction(e->{handler.menu();});

    }

    public String transcribe() throws IOException, URISyntaxException {
        String[] arguments = {"./recording.wav"};
        return Whisper.whisperapi(arguments);
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
                                audioFormat);
                        // the TargetDataLine used to capture audio data from the microphone
                        targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
                        targetDataLine.open(audioFormat);
                        targetDataLine.start();
                        recordingLabel.setVisible(true);

                        // the AudioInputStream that will be used to write the audio data to a file
                        AudioInputStream audioInputStream = new AudioInputStream(
                                targetDataLine);

                        // the file that will contain the audio data
                        File audioFile = new File("recording.wav");
                        AudioSystem.write(
                                audioInputStream,
                                AudioFileFormat.Type.WAVE,
                                audioFile);
                        recordingLabel.setVisible(false);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
            t.start();
    }

    private void stopRecording() {
        targetDataLine.stop();
        targetDataLine.close();
    }
}

public class AudioRecorder extends Application {

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

    public static void main(String[] args) {
        // launch(args);
        System.out.println("This should never be run on its own, only used for RecordAppFrame");
    }
}

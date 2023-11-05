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

public class AudioTranscriber extends Application {
    //Whisper whisper = new Whisper();
    AudioRecorder recorder = new AudioRecorder();
    @Override
    public void start(Stage primaryStage) throws Exception {

        // Setting the Layout of the Window (Flow Pane)
        AppFrame root = new AppFrame();

        // Set the title of the app
        primaryStage.setTitle("Transcriber");
        // Create scene of mentioned size with the border pane
        primaryStage.setScene(new Scene(root, 370, 120));
        // Make window non-resizable
        primaryStage.setResizable(false);
        // Show the app
        primaryStage.show();

    }
    public static void main(String[] args) {
        launch(args);
    }
}

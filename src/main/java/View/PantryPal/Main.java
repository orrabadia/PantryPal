package PantryPal;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.geometry.Insets;
import javafx.scene.text.*;
import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Comparator;
import org.json.JSONObject;

import javax.management.RuntimeErrorException;
import javax.sound.sampled.AudioFormat;


/*
 * Class Copied from Lab 1 for Main
 */
public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        //navigation handler to change scenes
        //this contains a map of all pages
        //appframe is initialized without navhandler
        //set its thing to primarystage
        NavigationHandler handler = new NavigationHandler();

        handler.setStage(primaryStage);
        //each UI element must have access to handler if it wants to do navigation
        // Create scene of mentioned size with the border pane
        //Scene recipeList = new Scene(root, 500,600);

        //handler initializes by adding recipe list to pagelist
        //moved to sign in/login on action
        //handler.initialize(recipeList);

        //if reachable, do regular app, if not show error
        boolean server = ServerPing.ping();
        if(server){
            UserAccDisplay userslDisplay = new UserAccDisplay(handler);
            //AppFrame root = new AppFrame(handler, userslDisplay.getUHandler());
            //root.setUHandler(userslDisplay.getUHandler());
            handler.showUserLogin(userslDisplay);

            // Make window non-resizable
            primaryStage.setResizable(false);
            // Show the app
            primaryStage.show();
        } else {
            Label statusLabel = new Label();
            //show red bold text where servers unreachable
            statusLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
            statusLabel.setText("Server is unreachable, please try again later :(");

            VBox root = new VBox(20);
            root.getChildren().add(statusLabel);

            Scene scene = new Scene(root, 300, 50);
            primaryStage.setTitle("Server Status");
            primaryStage.setScene(scene);

            primaryStage.show();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
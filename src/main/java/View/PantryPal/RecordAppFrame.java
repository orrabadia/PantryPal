package PantryPal;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.sound.sampled.AudioFormat;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

//ui page for recording
class RecordAppFrame extends FlowPane {
    private NavigationHandler handler;
    private Button startButton;
    private Button stopButton;
    private Button backButton;
    private Button continueButton;
    private AudioFormat audioFormat;
    private Label recordingLabel;
    private Label transcriptionLabel;
    private Label instructions;
    Thread t;
    RecordHandler rHandler;
    WhisperHandler wHandler;
    Label l;
    String name;
    CreateHandler createHandler;
    RequestHandler reqHandler;

// Set a default style for buttons and fields - background color, font size,
    // italics
    String defaultButtonStyle = "-fx-border-color: #000000; -fx-font: 13 arial; -fx-pref-width: 175px; -fx-pref-height: 50px;";
    String defaultLabelStyle = "-fx-font: 13 arial; -fx-pref-width: 175px; -fx-pref-height: 50px; -fx-text-fill: red; visibility: hidden";

    RecordAppFrame(NavigationHandler handler, String name, CreateHandler createHandler) throws IOException, URISyntaxException {
        // Set properties for the flowpane
        this.setPrefSize(370, 120);
        this.setPadding(new Insets(5, 0, 5, 5));
        this.setVgap(10);
        this.setHgap(10);
        this.setPrefWrapLength(170);

        this.handler = handler;

        this.name = name;

        this.createHandler = createHandler;

        this.reqHandler = new RequestHandler();
        // Add the buttons and text fields

        startButton = new Button("Start");
        startButton.setStyle(defaultButtonStyle);

        stopButton = new Button("Stop");
        stopButton.setStyle(defaultButtonStyle);

        recordingLabel = new Label("Recording...");
        recordingLabel.setStyle(defaultLabelStyle);

        backButton = new Button("Back");




        if (name == "meal") {
            instructions = new Label("Please record your meal type");
            transcriptionLabel = new Label("Please say the meal type you want:");
            //go back on back button
            backButton.setOnAction(e2->{handler.menu();});
        }
        else {
            instructions = new Label("Please list your ingredients");
            transcriptionLabel = new Label("Please say the ingredients you will use:");
            //go back to the meal type page
            backButton.setOnAction(e2->{handler.recordMeal(createHandler);});
            Button cancelButton = new Button("Cancel");
            cancelButton.setOnAction(e3->{handler.menu();});
            this.getChildren().add(cancelButton);
        }

        this.getChildren().addAll(startButton, stopButton, recordingLabel);

        this.getChildren().add(transcriptionLabel);

        l = (Label)this.getChildren().get(this.getChildren().size()-1);

        this.getChildren().add(backButton);

        //added so that continue button is not continuously added
        continueButton = new Button("Continue");
        this.getChildren().add(continueButton);
        continueButton.setVisible(false);


        // Add the listeners to the buttons
        addListeners();
    }

    public RequestHandler getRequestHandler(){
        return reqHandler;
    }

    public void addListeners() {
        // Start Button
        startButton.setOnAction(e -> {
            rHandler = new RecordHandler();
                recordingLabel.setVisible(true);
                try {
                    rHandler.record();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

        });

        // Stop Button
        stopButton.setOnAction(e -> {
            recordingLabel.setVisible(false);
            rHandler.stop();
            //RESULT OF TRANSCRIPTION STORED HERE
            String transcription = "";
            //continueButton = new Button("Continue");
            //this.getChildren().add(continueButton);
            //added
            continueButton.setVisible(true);
            if (name == "meal") {
                transcription = reqHandler.performAudioRequest("PUT");
                //new line
                String trans2 = transcription.toLowerCase();
                System.out.println(trans2);
                //sometimes it adds periods and its weird delete those
                trans2 = trans2.replaceAll("\\.", "");
                String mealtype = "";
                //parse for checking and return proper Mealtype
                if(trans2.contains("breakfast")){
                    mealtype = "Breakfast";
                    createHandler.getRecipe().setMealType(mealtype);
                    l.setText("Meal Type:" + createHandler.getRecipe().getMealType());

                    continueButton.setOnAction(e1->{
                        handler.recordIngredients(createHandler);
                    });
                } else if(trans2.contains("lunch")){
                    mealtype = "Lunch";
                    createHandler.getRecipe().setMealType(mealtype);
                    l.setText("Meal Type:" + createHandler.getRecipe().getMealType());

                    continueButton.setOnAction(e1->{
                        handler.recordIngredients(createHandler);
                    });
                } else if(trans2.contains("dinner")){
                    mealtype = "Dinner";
                    createHandler.getRecipe().setMealType(mealtype);
                    l.setText("Meal Type:" + createHandler.getRecipe().getMealType());

                    continueButton.setOnAction(e1->{
                        handler.recordIngredients(createHandler);
                    });
                } else{
                    l.setText("Error: Not a valid meal type. Please try again.");
                }
            }
            else {
                transcription = reqHandler.performAudioRequest("PUT");
                //sometimes it adds periods and its weird delete those
                transcription = transcription.replaceAll("\\.", "");
                createHandler.getRecipe().setIngredients(transcription);
                l.setText("Ingredients:" + createHandler.getRecipe().getIngredients());

                continueButton.setOnAction(e1->{
                    //on continue, get the transcription and move to new page
                    Recipe r = createHandler.getRecipe();
                    String mealtype = r.getMealType();
                    System.out.println("mealType " + mealtype);
                    String ingredients = r.getIngredients();
                    System.out.println("ingredients " + ingredients);
                    String recipe = reqHandler.performGenerateRequest("PUT", mealtype, ingredients);
                    System.out.println("recipe = " + recipe);
                    String title = recipe.substring(0,recipe.indexOf("~"));
                    //take out the newlines and returns for formatting
                    String strippedString = title.replaceAll("[\\n\\r]+", "");
                    r.setInstructions(recipe);
                    r.setTitle(strippedString);
                    GPTResultsDisplay results = new GPTResultsDisplay(handler, createHandler);
                    handler.showGPTResults(results);
                    //System.out.println(title +"TITLE LEFT+RECIPE RIGHT " +recipe  + " ");
                    //System.out.println(mealtype +"+ " +ingredients  + " " + createHandler.getRecipe().getTitle());
                });
            }
        });



    }


}
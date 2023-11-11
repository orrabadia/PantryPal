package PantryPal;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.TextAlignment;
import javafx.geometry.Insets;
import javafx.scene.text.*;
import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.management.RuntimeErrorException;
import javax.sound.sampled.AudioFormat;

class UIRecipe extends HBox { // extend HBox
    private NavigationHandler handler;
    private Text title;
    private Label index; // for use in RecipeList
    private Text mealType;
    private Button displayButton;
    private String ingredients; // change to different data struct?
    private String recipeInstructions; // change to different data struct?
    // add UI variables

    /*
     * Constructor for Recipe class
     *
     */
    UIRecipe(Text title, Text mealType, String ingredients, String recipeInstructions, NavigationHandler handler) {
        this.handler = handler;
        // is being displayed
        this.title = title;
        // not being displayed
        this.ingredients = ingredients;
        this.recipeInstructions = recipeInstructions;
        this.mealType = mealType;

        // copied from lab 1, to display
        this.setPrefSize(500, 20); // sets size of task
        this.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0; -fx-font-weight: bold;");
        index = new Label();
        index.setText("");
        index.setPrefSize(40, 20); // set size of Index label
        index.setTextAlignment(TextAlignment.CENTER); // Set alignment of index label
        index.setPadding(new Insets(10, 0, 10, 0)); // adds some padding to the task
        index.setTextAlignment(TextAlignment.LEFT);
        this.getChildren().add(index);

        title.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;");
        title.setTextAlignment(TextAlignment.LEFT);
        //this.getChildren().add(title);

        HBox hbox = new HBox();
        hbox.getChildren().add(title);

        //TODO: fix THE SPACING AND PUT IT AT THE RIGHT IDK HOW TO DO IT

        //add display recipe button
        displayButton = new Button("Display Recipe");
        displayButton.setAlignment(Pos.CENTER_RIGHT);
        //for display recipe button

        displayButton.setOnAction(e->{
            this.handler.displayRecipe(this);
        });

        hbox.getChildren().add(displayButton);

        this.getChildren().add(hbox);

    }

    public void setRecipeIndex(int num){
        this.index.setText(num + "");
    }

    Text getTitle() {
        return this.title;
    }

    Label getIndex() {
        return this.index;
    }

    Text getMealType() {
        return this.mealType;
    }

    String getIngredients() {
        return this.ingredients;
    }

    String getRecipeInstructions() {
        return this.recipeInstructions;
        // may want to print in a certain manner
    }

    public Button getDisplayButton(){
        return this.displayButton;
    }

    /*
     * Set Methods, think its in a different task
     *
     * void setTitle(Recipe recipe, String newTitle) { // Not for MS1, Assess
     * recipe.title = newTitle;
     * }
     *
     * void setMealType(Recipe recipe, String newMealType) {
     * recipe.mealType = newMealType;
     * }
     */

    /*
     * SetIngredients and RecipeInstructions more complex
     */

    void setRecipeIndex(UIRecipe recipe, Label newIndex) {
        recipe.index = newIndex;
    }
}

class UIRecipeList extends VBox { // extends HBox?
    private RecipeList rList;
    private NavigationHandler nHandler;
    // new RecipeList
    UIRecipeList(RecipeList rList, NavigationHandler nHandler) {
        this.rList = rList;
        this.nHandler = nHandler;
        // UI elements
        this.setSpacing(5); // sets spacing between tasks
        this.setPrefSize(500, 560);
        this.setStyle("-fx-background-color: #F0F8FF;");
    }

    //taken from lab 1
    public void updateRecipeIndices() {
        int index = 1;
        for (int i = 0; i < this.getChildren().size(); i++) {
            if (this.getChildren().get(i) instanceof UIRecipe) {
                ((UIRecipe) this.getChildren().get(i)).setRecipeIndex(index);
                index++;
            }
        }
    }

    public void updateList(NavigationHandler nHandler){
        this.getChildren().clear();
        ArrayList<Recipe> list = rList.getList();
        for(Recipe r : list){
            String title = r.getTitle();
            String mealType = r.getMealType();
            String ingredients = r.getIngredients();
            String instructions = r.getInstructions();
            UIRecipe uiR = new UIRecipe(new Text(title), new Text(mealType), ingredients, instructions, nHandler);
            this.getChildren().add(uiR);
            this.updateRecipeIndices();
        }
    }

    //add when delete implementing
    // public void updateRecipeIndices() {
    // int index = 1;
    // for (int i = 0; i < this.getChildren(i); i++) {
    // if (this.getChildren().get(i) instanceof Recipe) {
    // ((Recipe) this.getChildren().get(i)).setRecipeIndex(index);
    // index++;
    // }
    // }
    // }
}

/*
 * Class Copied from Lab 1 for footer
 */
class ListFooter extends HBox {
    private Button newRecipeButton;
    ListFooter() {
        this.setPrefSize(500, 60);
        this.setStyle("-fx-background-color: #F0F8FF;");
        this.setSpacing(15);
        // set a default style for buttons - background color, font size, italics
        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";
        this.setAlignment(Pos.CENTER); // aligning the buttons to center

        newRecipeButton = new Button("New Recipe");
        newRecipeButton.setStyle(defaultButtonStyle);
        this.getChildren().add(newRecipeButton);
        this.setAlignment(Pos.CENTER);
    }

    public Button getNewRecipeButton(){
        return newRecipeButton;
    }
}

class DisplayFooter extends HBox {
    private Button editButton;
    private Button backButton;
    DisplayFooter() {
        this.setPrefSize(500, 60);
        this.setStyle("-fx-background-color: #F0F8FF;");
        this.setSpacing(15);
        // set a default style for buttons - background color, font size, italics
        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";
        this.setAlignment(Pos.CENTER); // aligning the buttons to center

        editButton = new Button("Edit");
        backButton = new Button("Back");
        editButton.setStyle(defaultButtonStyle);
        backButton.setStyle(defaultButtonStyle);
        this.getChildren().add(editButton);
        this.getChildren().add(backButton);
        this.setAlignment(Pos.CENTER);
    }

    public Button getEditButton(){
        return editButton;
    }
    public Button getBackButton(){
        return backButton;
    }
}

/*
 * Class Copied from Lab 1 for Header
 */
class Header extends HBox {
    Header(String title) {
        this.setPrefSize(500, 60); // Size of the header
        this.setStyle("-fx-background-color: #F0F8FF;");
        Text titleText = new Text(title); // Text of the Header
        titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
        this.getChildren().add(titleText);
        this.setAlignment(Pos.CENTER); // Align the text to the Center
    }

    public void setTitle(String s){
        Text titleText = (Text)this.getChildren().get(0);
        titleText.setText(s);
    }
}

/**
 * Class Copied from Lab 1 for AppFrame
 */
class AppFrame extends BorderPane {
    private Header header;
    private ListFooter footer;
    private UIRecipeList recipeList;
    private Button newRecipeButton;
    private NavigationHandler nHandler;
    private RecipeList list;
    private RecipeHandler rHandler;

    AppFrame(NavigationHandler handler) {
        this.nHandler = handler;
        // Initialise the header Object
        header = new Header("Recipe List");
        //create new recipelist and handler
        list = new RecipeList();
        rHandler = new RecipeHandler(list);
        //create ui recipe list to display recipes
        recipeList = new UIRecipeList(list,nHandler);
        // Initialise the recipelist footer Object
        footer = new ListFooter();

        ScrollPane Scroller = new ScrollPane(recipeList);
        Scroller.setFitToHeight(true);
        Scroller.setFitToWidth(true);
        // Add header to the top of the BorderPane
        this.setTop(header);
        // Add scroller to the centre of the BorderPane
        this.setCenter(Scroller);

        // Add footer to the bottom of the BorderPane
        this.setBottom(footer);
        // Initialise Button Variables through the getters in Footer
        newRecipeButton = footer.getNewRecipeButton();

        // Call Event Listeners for the Buttons
        addListeners();
    }

    public void addListeners()
    {
    newRecipeButton.setOnAction(e -> {
        // just dummy values for now, gotta get the tokens from Chat GPT and parse them and pass them into here
        //SAMPLE VALUES FOR TESTING RECIPE DISPLAY
        String title = "Test Recipe 1";
        String mealtype = "Lunch";
        String ingredients = "food";
        String instructions = "cook food";
        Recipe r = new Recipe(title, mealtype, ingredients, instructions);
        
        //send to controller
        this.rHandler.addRecipe(r);
        this.recipeList.updateList(nHandler);

        //send to record page, and also add a recipe for test purposes
        CreateHandler createHandler = new CreateHandler();
        nHandler.recordMeal(createHandler);
    });
    
    }

    // public void debugAddRecipe(String title, String meal, String ingredients, String recipeinstructions){
    //     // just dummy values for now, gotta get the tokens from Chat GPT and parse them and pass them into here
    //     UIRecipe recipe = new UIRecipe(new Text(title), new Text(meal),ingredients, recipeinstructions, this.nHandler);
    //     // Add recipe to recipelist
    //     recipeList.getChildren().add(recipe);
    //     recipeList.updateRecipeIndices();
    // }

    public NavigationHandler getNavHandler(){
        return this.nHandler;
    }

    public RecipeHandler getRecipeHandler(){
        return this.rHandler;
    }

    public UIRecipeList getRecipeList(){
        return this.recipeList;
    }

}

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

        this.getChildren().addAll(startButton, stopButton, recordingLabel);

        this.getChildren().add(transcriptionLabel);

        l = (Label)this.getChildren().get(this.getChildren().size()-1);

        this.getChildren().add(backButton);

        // Add the listeners to the buttons
        addListeners();
    }

    public void addListeners() {
        // Start Button
        startButton.setOnAction(e -> {
            rHandler = new RecordHandler();
            recordingLabel.setVisible(true);
            try {
                rHandler.record();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            
        });

        // Stop Button
        stopButton.setOnAction(e -> {
            recordingLabel.setVisible(false);
            rHandler.stop();
            //RESULT OF TRANSCRIPTION STORED HERE
            String transcription = "";
            continueButton = new Button("Continue");
            this.getChildren().add(continueButton);
            if (name == "meal") {  
                try {
                    wHandler = new WhisperHandler();
                    transcription = wHandler.transcribe();
                    createHandler.getRecipe().setMealType(transcription);
                    l.setText("Meal Type:" + createHandler.getRecipe().getMealType());
                    
                }
                catch (IOException e1){
                    System.err.println("IOException");
                }
                catch (URISyntaxException e2){
                    System.err.println("URISyntaxException");
                }
                continueButton.setOnAction(e1->{
                    System.out.println("Not implemented yet");
                });
            }
        });

    }

    
}

/**
 * Page for detailed recipe display
 */
class RecipeDisplay extends BorderPane {
    private Header header;
    private DisplayFooter footer;
    private Button editButton;
    private Button backButton;

    private NavigationHandler handler;
    private UIRecipe r;

    RecipeDisplay(NavigationHandler handler) {
        this.handler = handler;
        // Initialise the header Object
        //header = new Header(r.getTitle().getText());
        header = new Header("YOU SHOULDNT BE HERE");
        // Initialise the Footer Object
        footer = new DisplayFooter();

        // Create a VBox in the center
        VBox centerBox = new VBox();
        centerBox.setSpacing(10); // Adjust the spacing between scrollable boxes

        // Create two scrollable boxes with text
        ScrollPane scrollPane1 = createScrollableBox("Ingredients: YOU");
        ScrollPane scrollPane2 = createScrollableBox("Instructions: RUN");
        // ScrollPane scrollPane1 = createScrollableBox("Ingredients: " + r.getIngredients().toString());
        // ScrollPane scrollPane2 = createScrollableBox("Instructions: " + r.getRecipeInstructions().toString());

        centerBox.getChildren().addAll(scrollPane1, scrollPane2);

        // Set the VBox in the center of the BorderPane
        this.setCenter(centerBox);
        // Add header to the top of the BorderPane
        this.setTop(header);
        // Add footer to the bottom of the BorderPane
        this.setBottom(footer);
        // Initialise Button Variables through the getters in Footer

        // Call Event Listeners for the Buttons
        addListeners();
    }

    public void setTitle(String s){
        //called when displaying from handler, handler has blank one by default
        //access header settext
        header.setTitle(s);
    }

    public void setIngredients(String s){
        //called when displaying from handler, handler has blank one by default
        VBox v = (VBox)this.getCenter();
        //THIS SHOULD BE THE FIRST ELEMENT IF IT CHANGES THINGS WILL NOT BE GOOD
        ScrollPane scroll1 = (ScrollPane)v.getChildren().get(0);
        TextField textField = (TextField) scroll1.getContent();
        textField.setText(s);
    }

    public void setInstructions(String s){
        //called when displaying from handler, handler has blank one by default
        VBox v = (VBox)this.getCenter();
        //THIS SHOULD BE THE FIRST ELEMENT IF IT CHANGES THINGS WILL NOT BE GOOD
        ScrollPane scroll2 = (ScrollPane)v.getChildren().get(1);
        TextField textField = (TextField) scroll2.getContent();
        textField.setText(s);

    }

    public void addListeners()
    {
        Button backButton = footer.getBackButton();
        backButton.setOnAction(e ->{
            handler.menu();
        });
    }

    // Helper method to create a scrollable text box
    private ScrollPane createScrollableBox(String content) {
        TextField textArea = new TextField(content);

        ScrollPane scrollPane = new ScrollPane(textArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        return scrollPane;
    }

}

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
        AppFrame root = new AppFrame(handler);
        // Create scene of mentioned size with the border pane
        Scene recipeList = new Scene(root, 500,600);
        
        //handler initializes by adding recipe list to pagelist
        handler.initialize(recipeList);

        // Make window non-resizable
        primaryStage.setResizable(false);
        // Show the app
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
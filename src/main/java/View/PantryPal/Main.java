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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.net.*;
import javax.sound.sampled.*;

import javax.management.RuntimeErrorException;

class Recipe extends HBox { // extend HBox
    private NavigationHandler handler;
    private Text title;
    private Label index; // for use in RecipeList
    private Text mealType;
    private Button displayButton;
    private ArrayList<String> ingredients; // change to different data struct?
    private ArrayList<String> recipeInstructions; // change to different data struct?
    // add UI variables

    /*
     * Constructor for Recipe class
     *
     */
    Recipe(Text title, Text mealType, ArrayList<String> ingredients, ArrayList<String> recipeInstructions, NavigationHandler handler) {
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

    Label getIndex(Recipe recipe) {
        return recipe.index;
    }

    Text getMealType(Recipe recipe) {
        return recipe.mealType;
    }

    ArrayList<String> getIngredients() {
        return this.ingredients;
    }

    ArrayList<String> getRecipeInstructions() {
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

    void setRecipeIndex(Recipe recipe, Label newIndex) {
        recipe.index = newIndex;
    }
}

class RecipeList extends VBox { // extends HBox?
    // new RecipeList
    RecipeList() {
        // UI elements
        this.setSpacing(5); // sets spacing between tasks
        this.setPrefSize(500, 560);
        this.setStyle("-fx-background-color: #F0F8FF;");
    }

    public void updateRecipeIndices() {
        int index = 1;
        for (int i = 0; i < this.getChildren().size(); i++) {
            if (this.getChildren().get(i) instanceof Recipe) {
                ((Recipe) this.getChildren().get(i)).setRecipeIndex(index);
                index++;
            }
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
    private RecipeList recipeList;
    private Button newRecipeButton;
    private NavigationHandler handler;

    AppFrame(NavigationHandler handler) {
        this.handler = handler;
        // Initialise the header Object
        header = new Header("Recipe List");
        // Create a recipelist Object to hold the tasks
        recipeList = new RecipeList();
        // Initialise the Footer Object
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

        //INITIALIZE IT WITH ONE FOR NOW, because we cannot create yet

        // just dummy values for now, gotta get the tokens from Chat GPT and parse them and pass them into here
        //SAMPLE VALUES FOR TESTING RECIPE DISPLAY
        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add("hot dogs");
        ArrayList<String> instructions = new ArrayList<>();
        instructions.add("1.freeze hot dogs");
        instructions.add("2.eat");
        Recipe recipe = new Recipe(new Text("Hot Dog Ice Cream"), new Text("Lunch"), ingredients, instructions, this.handler);
        // Add task to tasklist
        recipeList.getChildren().add(recipe);
        recipeList.updateRecipeIndices();

        // Call Event Listeners for the Buttons
        addListeners();
    }

    public void addListeners()
    {
    newRecipeButton.setOnAction(e -> {
        //TODO: when adding new page, link to navhandler and create a navhandler method for new page
        handler.recordMeal();
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
    private Recipe r;

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

/**class to handle navigation, has map of all scenes and pointer to primarystage
 * */
class NavigationHandler{
    public static final String RECIPE_LIST = "RecipeList";
    public static final String DISPLAY_RECIPE = "DisplayRecipe";
    public static final String RECORD_MEALTYPE = "RecordMealType";
    private Stage primaryStage;
    private HashMap<String, Scene> pageList;
    NavigationHandler(Stage primaryStage){
        this.primaryStage = primaryStage;
        this.pageList = new HashMap<>();
        pageList.put(DISPLAY_RECIPE, null);
        //initialize this to blank, we will fill in each time
        RecipeDisplay display = new RecipeDisplay(this);
        Scene details = new Scene(display, 500,600);
        pageList.put(DISPLAY_RECIPE, details);
    }

    boolean showRecipeList(){
        boolean ret = false;

        return ret;
    }
    /**called when initializing, takes a scene(in this case the recipelist) and shows it
     * */
    void initialize(Scene RecipeList){

        try {
            pageList.put("RecipeList", RecipeList);
            // Set the title of the Recipe Page
            primaryStage.setTitle("PantryPal");
            primaryStage.setScene(RecipeList);
            // Make window non-resizable
            primaryStage.setResizable(false);
            // Show the app
            primaryStage.show();
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * takes a recipe, link this with new recipe button
     * creates new recipe page, adds to map, displays it
     */
    void displayRecipe(Recipe r){
        //get the display page and set its content
        Scene s = pageList.get(DISPLAY_RECIPE);
        RecipeDisplay rd = (RecipeDisplay)s.getRoot();
        rd.setTitle(r.getTitle().getText());
        rd.setIngredients(r.getIngredients().toString());
        rd.setInstructions(r.getRecipeInstructions().toString());
        primaryStage.setScene(s);
    }

    void menu(){
        Scene f = pageList.get(RECIPE_LIST);
        if(f != null){
            primaryStage.setScene(f);
        } else {
            throw new RuntimeErrorException(null);
        }
    }

    void recordMeal(){

        Scene r = pageList.get(RECORD_MEALTYPE);
        //new page for recording meal
        try {
            RecordAppFrame mealrecorder = new RecordAppFrame(this);
            //Scene mealrecord = new Scene(mealrecorder, 370, 120);
            Scene mealrecord = new Scene(mealrecorder, 370, 400);
            pageList.put(RECORD_MEALTYPE, mealrecord);
        } catch (IOException e1) {
            System.out.println(e1);
        } catch (URISyntaxException e2) {
            System.out.println(e2);
        }
        //switches to mealtype page
        r = pageList.get(RECORD_MEALTYPE);
        primaryStage.setScene(r);
    }
}

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

        // Add the listeners to the buttons
        addListeners();
    }

    public void addListeners() {
        // Start Button
        startButton.setOnAction(e -> {
            rHandler = new RecordHandler();
            recordingLabel.setVisible(true);
            rHandler.record();
            
        });

        // Stop Button
        stopButton.setOnAction(e -> {
            recordingLabel.setVisible(false);
            rHandler.stop();
            //RESULT OF TRANSCRIPTION STORED HERE
            String transcription = "";
            try {
                wHandler = new WhisperHandler();
                transcription = wHandler.transcribe();
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

    
}

/*
 * Class Copied from Lab 1 for Main
 */
public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        //navigation handler to change scenes
        //this contains a map of all pages
        NavigationHandler handler = new NavigationHandler(primaryStage);
        //each UI element must have access to handler if it wants to do navigation
        AppFrame root = new AppFrame(handler);
        // Create scene of mentioned size with the border pane
        Scene recipeList = new Scene(root, 500,600);
        
        //handler initializes by adding recipe list to pagelist and displaying it
        handler.initialize(recipeList);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
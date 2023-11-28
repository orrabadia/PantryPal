package PantryPal;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
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
    //private RecipeList rList;
    private NavigationHandler nHandler;
    private RecipeHandler rHandler;


    //u handler parameter?
    // new RecipeList
    UIRecipeList(RecipeHandler rHandler, NavigationHandler nHandler) {
        //this.rList.setList(rHandler.getRecipeList());
        this.rHandler = rHandler;
        this.nHandler = nHandler;
        this.updateList(nHandler,0);
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

    /**
     * 
     * Updates list based on what is currently in backend
     * takes behavior based on what style of sort you want to use-default is newest first(0)
     * later add more to have different sorting orders
     */
    public void updateList(NavigationHandler nHandler, int sortorder){

        //takes behavior based on what style of sort you want to use-default is revchron
        this.getChildren().clear();
        //replace current list with whats in backend

        // added .getList()
        ArrayList<Recipe> list = rHandler.getRecipeList(((UserAccDisplay)this.nHandler.getMap().get("UserSL").getRoot()).getUHandler().getUserName()).getList();
        System.out.println("LIST SIZE:" + list.size());
        //sort last to first
        class revchronComparator implements Comparator<Recipe>{
            @Override
            public int compare(Recipe r1, Recipe r2){
                return Integer.compare(r2.getIndex(), r1.getIndex());
            }
        }
        if(sortorder == 0){
            //sort backwards if order 0
            revchronComparator comp = new revchronComparator();
            Collections.sort(list, comp);
        }
        for(Recipe r : list){
            if(sortorder == 0){
                int index = r.getIndex();
                String title = r.getTitle();
                String mealType = r.getMealType();
                String ingredients = r.getIngredients();
                String instructions = r.getInstructions();
                UIRecipe uiR = new UIRecipe(new Text(title), new Text(mealType), ingredients, instructions, nHandler);
                this.getChildren().add(uiR);
                this.updateRecipeIndices();
            } else {
                System.out.println("unimplemented sort method");
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
    private Button logOutButton;
    ListFooter() {
        this.setPrefSize(500, 60);
        this.setStyle("-fx-background-color: #F0F8FF;");
        this.setSpacing(15);
        // set a default style for buttons - background color, font size, italics
        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";
        this.setAlignment(Pos.CENTER); // aligning the buttons to center

        newRecipeButton = new Button("New Recipe");
        logOutButton = new Button("Log Out");
        newRecipeButton.setStyle(defaultButtonStyle);
        logOutButton.setStyle(defaultButtonStyle);

        this.getChildren().addAll(logOutButton, newRecipeButton);
        this.setAlignment(Pos.CENTER);
    }

    public Button getNewRecipeButton(){
        return newRecipeButton;
    }

    public Button getLogOutButton() {
        return logOutButton;
    }
}

class DisplayFooter extends HBox {
    private Button editButton;
    private Button backButton;
    private Button deleteButton;
    DisplayFooter() {
        this.setPrefSize(500, 60);
        this.setStyle("-fx-background-color: #F0F8FF;");
        this.setSpacing(15);
        // set a default style for buttons - background color, font size, italics
        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";
        this.setAlignment(Pos.CENTER); // aligning the buttons to center

        editButton = new Button("Edit");
        backButton = new Button("Back");
        deleteButton = new Button("Delete");
        editButton.setStyle(defaultButtonStyle);
        backButton.setStyle(defaultButtonStyle);
        deleteButton.setStyle(defaultButtonStyle);
        this.getChildren().add(editButton);
        this.getChildren().add(backButton);
        this.getChildren().add(deleteButton);
        this.setAlignment(Pos.CENTER);
    }

    public Button getEditButton(){
        return editButton;
    }
    public Button getBackButton(){
        return backButton;
    }

    public Button getDeleteButton(){ 
        return deleteButton;
    }
}

class GPTFooter extends HBox {
    private Button saveButton;
    private Button cancelButton;
    GPTFooter() {
        this.setPrefSize(500, 60);
        this.setStyle("-fx-background-color: #F0F8FF;");
        this.setSpacing(15);
        // set a default style for buttons - background color, font size, italics
        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";
        this.setAlignment(Pos.CENTER); // aligning the buttons to center

        saveButton = new Button("Save");
        cancelButton = new Button("Cancel");
        saveButton.setStyle(defaultButtonStyle);
        cancelButton.setStyle(defaultButtonStyle);
        this.getChildren().add(saveButton);
        this.getChildren().add(cancelButton);
        this.setAlignment(Pos.CENTER);
    }

    public Button getSaveButton(){
        return saveButton;
    }
    public Button getCancelButton(){
        return cancelButton;
    }
}

class UserAccFooter extends HBox {
    private Button logInButton;
    private Button signUpButton;
   
    UserAccFooter() {
        this.setPrefSize(500, 60);
        this.setStyle("-fx-background-color: #F0F8FF;");
        this.setSpacing(15);
        // set a default style for buttons - background color, font size, italics
        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";
        this.setAlignment(Pos.CENTER); // aligning the buttons to center

        logInButton = new Button("Log In");
        signUpButton = new Button("Sign Up");
        logInButton.setStyle(defaultButtonStyle);
        signUpButton.setStyle(defaultButtonStyle);
        this.getChildren().add(logInButton);
        this.getChildren().add(signUpButton);
        this.setAlignment(Pos.CENTER);
    }

    public Button getLogInButton(){
        return logInButton;
    }
    public Button getSignUpButton(){
        return signUpButton;
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
    private Button logOutButton;
    private NavigationHandler nHandler;
    private UserHandler uHandler;
    private RecipeList list;
    private RecipeHandler rHandler;
    //private String defaultTextFieldStyle;

    AppFrame(NavigationHandler handler, UserHandler uHandler) {
        this.nHandler = handler;
        // Initialise the header Object
        header = new Header("Recipe List");
        //create new recipelist and handler
        list = new RecipeList();
        System.out.println("refreshing");
        //TODO: get list based on backend not csv
        //list.refresh();
        rHandler = new RecipeHandler(list);
        rHandler.getRecipeList(uHandler.getUserName());
        //create ui recipe list to display recipes
        //in its initialization it will get from backend and display
        //can also call updatelist later to reget from backend
        recipeList = new UIRecipeList(rHandler,nHandler);
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
        logOutButton = footer.getLogOutButton();

        // Call Event Listeners for the Buttons
        addListeners();
    }

    public void addListeners()
    {
    newRecipeButton.setOnAction(e -> {
      
        //send to record page, and also add a recipe for test purposes
        CreateHandler createHandler = new CreateHandler();
        nHandler.recordMeal(createHandler);
    });

    logOutButton.setOnAction(e1 ->{
        nHandler.userSL();
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
    
    public UserHandler getUserHandler() {
        return this.uHandler;
    }

    public void setUHandler(UserHandler uHandler) {
        this.uHandler = uHandler;
    }

}

class GPTResultsDisplay extends BorderPane{
    private Header header;
    private GPTFooter footer;

    private String instructions;
    private NavigationHandler nHandler;
    private RecipeHandler rHandler;
    private CreateHandler cHandler;
    private UIRecipe r;

    GPTResultsDisplay(NavigationHandler handler, CreateHandler cHandler) {
        this.nHandler = handler;
        this.cHandler = cHandler;
        // Initialise the header Object
        header = new Header(cHandler.getRecipe().getTitle());
        // Initialise the Footer Object
        footer = new GPTFooter();

        // Create a VBox in the center
        VBox centerBox = new VBox();
        centerBox.setSpacing(10); // Adjust the spacing between scrollable boxes

        // Create two scrollable boxes with text
        ScrollPane scrollPane1 = createScrollableBox("Ingredients: " +cHandler.getRecipe().getIngredients());
        ScrollPane scrollPane2 = createScrollableBox("Instructions: "+ cHandler.getRecipe().getInstructions());

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
        Button saveButton = footer.getSaveButton();
        saveButton.setOnAction(e ->{
            //get recipehandler from navhandler
            System.out.println("1");
            HashMap<String,Scene> pagelist = nHandler.getPageList();
            //System.out.println(pagelist.get("RecipeList").getRoot().getClass().toString());
            AppFrame rlist = (AppFrame)pagelist.get("RecipeList").getRoot(); 
            RecipeHandler recipeHandler = rlist.getRecipeHandler();
            //add recipe to backend, check new backend, return to menu()
            recipeHandler.addRecipe(cHandler.getRecipe(), rlist.getUserHandler().getUserName());
            //recipeHandler.getRecipeList();
            //addrecipe above updates the recipehandler list
            UIRecipeList uiList = rlist.getRecipeList();
            //note that this calls get, uilist has the rhandler above
            uiList.updateList(nHandler, 0);
            nHandler.menu();

        });
        Button cancelButton = footer.getCancelButton();
        cancelButton.setOnAction(e->{
            nHandler.menu();
        });
    }

    // Helper method to create a scrollable text box
    private ScrollPane createScrollableBox(String content) {
        TextArea textArea = new TextArea(content);
        textArea.setEditable(false);
        ScrollPane scrollPane = new ScrollPane(textArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        return scrollPane;
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
                transcription = reqHandler.performAudioRequest("PUT");
                createHandler.getRecipe().setMealType(transcription);
                l.setText("Meal Type:" + createHandler.getRecipe().getMealType());

                continueButton.setOnAction(e1->{
                    handler.recordIngredients(createHandler);
                });
            }
            else {
                transcription = reqHandler.performAudioRequest("PUT");
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

class UserAccDisplay extends BorderPane {
    private Header header;
    private UserAccFooter footer;
    private Button logInButton;
    private Button signUpButton;
    private Label inputAlert;
    
    
    String defaultLabelStyle = "-fx-font: 13 arial; -fx-pref-width: 300px; -fx-pref-height: 100px; -fx-text-fill: red;";
    String badFieldStyle = "-fx-border-color: red ; -fx-border-width: 2px ;";
    String defaultTextFieldStyle = "-fx-border-color: #F0F8FF ; -fx-border-width: 2px ;";
    private UserHandler uHandler;
    private NavigationHandler nhandler;

    UserAccDisplay(NavigationHandler nhandler) {
        this.nhandler = nhandler;

        uHandler = new UserHandler();
        header = new Header("Welcome to PantryPal");

        // Initialise the Footer Object
        footer = new UserAccFooter();

        // Create a VBox in the center
        VBox centerBox = new VBox();
        centerBox.setSpacing(10); // Adjust the spacing between scrollable boxes

        // Create two scrollable boxes with text
        TextField user = new TextField();
        TextField pass = new TextField();

        user.setStyle(defaultTextFieldStyle);
        pass.setStyle(defaultTextFieldStyle);

        user.setPromptText("Username");
        pass.setPromptText("Password");
        // ScrollPane scrollPane1 = createScrollableBox("Ingredients: " + r.getIngredients().toString());
        // ScrollPane scrollPane2 = createScrollableBox("Instructions: " + r.getRecipeInstructions().toString());

        centerBox.getChildren().addAll(user, pass);

        // Set the VBox in the center of the BorderPane
        this.setCenter(centerBox);
        // Add header to the top of the BorderPane
        this.setTop(header);
        // Add footer to the bottom of the BorderPane
        this.setBottom(footer);
        // Initialise Button Variables through the getters in Footer

        inputAlert = new Label();
        inputAlert.setStyle(defaultLabelStyle);
        inputAlert.setVisible(false);
        ((VBox)this.getCenter()).getChildren().add(inputAlert);        

        // Call Event Listeners for the Buttons
        addListeners();
    }

    public UserHandler getUHandler(){
        return this.uHandler;
    }

    public void addListeners(){

    Button logButton = footer.getLogInButton();
    logButton.setOnAction(e1->{
         /*will have to check whether or not the username is a collection within the database. 
            If it is -> Check that password is correct (will probably have to create a new collection within the database that only has username and passwords)
            if both correct-> set username as the inputted username and use the collection associated with it, and lead to the recipe list
            if password incorrect -> say password is incorrect
            If it is not -> say username not found
        */
        inputAlert.setVisible(false);
        String username = ((TextField)((VBox)this.getCenter()).getChildren().get(0)).getText();
        String password = ((TextField)((VBox)this.getCenter()).getChildren().get(1)).getText();

        System.out.println(uHandler.getUser(username,password) + "THIS IS WHAT IS BEING RETURNED IN LOG BUTTON");
        
        //correct password, log in to their recipe list
        if (password.equals(uHandler.getUser(username, password))) {
            ((TextField)((VBox)this.getCenter()).getChildren().get(0)).clear();
            ((TextField)((VBox)this.getCenter()).getChildren().get(1)).clear();
            uHandler.setUser(username);
            AppFrame root = new AppFrame(this.nhandler, uHandler);
            root.setUHandler(uHandler);
            uHandler.setAppFrame(root);
            Scene recipeList = new Scene(root, 500,600);
            //recipeList.setRoot(root);
            this.nhandler.initialize(recipeList);
        } else { //if password is incorrect alert them
            ((TextField)((VBox)this.getCenter()).getChildren().get(0)).setStyle(badFieldStyle);
            ((TextField)((VBox)this.getCenter()).getChildren().get(1)).setStyle(badFieldStyle);
            inputAlert.setText("Incorrect Pass");
            inputAlert.setVisible(true);
        }
        


    });
    
    Button signUpButton = footer.getSignUpButton();
    signUpButton.setOnAction( e1-> {

        inputAlert.setVisible(false);
         //will have to check whether or not the username is already a collection within the database (maybe create a function for this)
        /*
        if it is -> say that username is already taken and try again
        done - if it is not -> insert username and password into the user collection (within the database), create a new collection named the username, go to recipe list*/
        String username = ((TextField)((VBox)this.getCenter()).getChildren().get(0)).getText();
        String password = ((TextField)((VBox)this.getCenter()).getChildren().get(1)).getText();
        
        
        // password length control for signup
        if (username.length() < 1 || password.length() < 1){
            ((TextField)((VBox)this.getCenter()).getChildren().get(0)).setStyle(badFieldStyle);
            ((TextField)((VBox)this.getCenter()).getChildren().get(1)).setStyle(badFieldStyle);
            inputAlert.setText("Username or Password too short");
            inputAlert.setVisible(true);
            return;
            
        }
        //need to check if it is not a username already
        /*String ret;
        try{
            ret = uHandler.getUser(username, password );
        }catch(Exception e){
           // The above code is printing the value of the variable "e" to the console.
            e.printStackTrace();
            return "Error: " + e.getMessage();
            ret = null;
        }*/
        String ret = uHandler.getUser(username, password ); 
        System.out.println(ret + "RETTTTTTTTTTTTTTTTTTTTT");
        
        if (ret.contains("JSONException")){
            uHandler.putUser(username, password);
            ((TextField)((VBox)this.getCenter()).getChildren().get(0)).clear();
            ((TextField)((VBox)this.getCenter()).getChildren().get(1)).clear();
            ((TextField)((VBox)this.getCenter()).getChildren().get(0)).setStyle(defaultTextFieldStyle);
            ((TextField)((VBox)this.getCenter()).getChildren().get(1)).setStyle(defaultTextFieldStyle);

            uHandler.setUser(username);
            AppFrame root = new AppFrame(this.nhandler, uHandler);
            root.setUHandler(uHandler);
            uHandler.setAppFrame(root);
            Scene recipeList = new Scene(root, 500,600);
            this.nhandler.initialize(recipeList);
        } else {
            ((TextField)((VBox)this.getCenter()).getChildren().get(0)).setStyle(badFieldStyle);
            inputAlert.setText("Username: \""+username+"\" taken. Please try again.");
            inputAlert.setVisible(true);
            
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
    private Button deleteButton;

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
        ScrollPane scrollPane1 = createScrollableBox("Ingredients: ");
        ScrollPane scrollPane2 = createScrollableBox("Instructions: ");
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

    public void setUIR(UIRecipe recipe){
        this.r = recipe;
    }
  

    public void setTitle(String s){
        //called when displaying from handler, handler has blank one by default
        //access header settext
        header.setTitle(s);
    }

    public void setIngredients(String s){
        //called when displaying from handler, handler has blank one by default
        VBox v = (VBox) this.getCenter();
        //THIS SHOULD BE THE FIRST ELEMENT IF IT CHANGES THINGS WILL NOT BE GOOD
        ScrollPane scroll1 = (ScrollPane)v.getChildren().get(0);
        TextArea textField = (TextArea) scroll1.getContent();
        textField.setText(s);
    }

    public void setInstructions(String s){
        //called when displaying from handler, handler has blank one by default
        VBox v = (VBox)this.getCenter();
        //THIS SHOULD BE THE FIRST ELEMENT IF IT CHANGES THINGS WILL NOT BE GOOD
        ScrollPane scroll2 = (ScrollPane)v.getChildren().get(1);
        TextArea textField = (TextArea) scroll2.getContent();
        textField.setText(s);

    }

    public void addListeners()
    {

        Button backButton = footer.getBackButton();
        backButton.setOnAction(e ->{
            if (this.footer.getBackButton().getText() == "Back"){
                handler.menu();
            } else {
                //reverts the displayed ingredients back to orriginal (what is being saved in the rlist (not UIRList))
               //((TextArea)((ScrollPane)((VBox)this.getCenter()).getChildren().get(0)).getContent()).setText(((AppFrame)this.handler
               //.getMap().get("RecipeList").getRoot()).getRecipeHandler().getRecipeList().get(r.getTitle().getText()).getIngredients());
               
               ((TextArea)((ScrollPane)((VBox)this.getCenter()).getChildren().get(0)).getContent()).setText(r.getIngredients());
               //reverts the displayed instructions back to orriginal (what is being saved in the rlist (not UIRList))
               //((TextArea)((ScrollPane)((VBox)this.getCenter()).getChildren().get(1)).getContent()).setText(((AppFrame)this.handler
               //.getMap().get("RecipeList").getRoot()).getRecipeHandler().getRecipeList().get(r.getTitle().getText()).getInstructions());


               ((TextArea)((ScrollPane)((VBox)this.getCenter()).getChildren().get(1)).getContent()).setText(r.getRecipeInstructions());
                //sets textfields to non-editable
                ((TextArea)((ScrollPane)((VBox)this.getCenter()).getChildren().get(0)).getContent()).setEditable(false);
                ((TextArea)((ScrollPane)((VBox)this.getCenter()).getChildren().get(1)).getContent()).setEditable(false);

                //reverts buttons back
                this.footer.getBackButton().setText("Back");
                this.footer.getEditButton().setText("Edit");
            }
        });

        Button editButton = footer.getEditButton();        
        editButton.setOnAction(e -> {

            if (this.footer.getEditButton().getText() == "Edit") {
                //sets textfields editable
                ((TextArea)((ScrollPane)((VBox)this.getCenter()).getChildren().get(0)).getContent()).setEditable(true);
                ((TextArea)((ScrollPane)((VBox)this.getCenter()).getChildren().get(1)).getContent()).setEditable(true);
                //changes button text
                this.footer.getEditButton().setText("Save");
                this.footer.getBackButton().setText("Cancel");
            } else {
                if (this.footer.getEditButton().getText() == "Save") {
                    //sets fields uneditable
                    ((TextArea)((ScrollPane)((VBox)this.getCenter()).getChildren().get(0)).getContent()).setEditable(false);
                    ((TextArea)((ScrollPane)((VBox)this.getCenter()).getChildren().get(1)).getContent()).setEditable(false);
                    /*
                    Gets the recipe from the recipelist which is gotten from the handler gotten from the appframe. Calls editRecipe()
                    from RecipeHandler, passing in the recipe which is retrieved from the appframes recipe handlers recipelist, the new
                    ingredients and the new instructions retrieved from the fields
                    */

                   /*ArrayList<Recipe> rList = ((AppFrame)this.handler.getMap().get("RecipeList").getRoot()).getRecipeHandler().getRecipeList();
                   
                   for (Recipe temp : rList){
                        if(temp.getTitle() == r.getTitle().getText()){
                            break;
                        }
                   }
                    */
                   // not gonna work
                    ((AppFrame)this.handler.getMap().get("RecipeList").getRoot()).getRecipeHandler().editRecipe(
                    ((AppFrame)this.handler.getMap().get("RecipeList").getRoot()).getRecipeHandler()
                    .getRecipeList(((UserAccDisplay)this.handler.getMap().get("UserSL").getRoot()).getUHandler().getUserName()).get(r.getTitle().getText()),
                    ((TextArea)((ScrollPane)((VBox)this.getCenter()).getChildren().get(0)).getContent()).getText(),
                    ((TextArea)((ScrollPane)((VBox)this.getCenter()).getChildren().get(1)).getContent()).getText(), ((AppFrame)this.handler.getMap().get("RecipeList").getRoot()).getUserHandler().getUserName());
                    //Updatethe UIList
                    ((AppFrame)this.handler.getMap().get("RecipeList").getRoot()).getRecipeList().updateList(this.handler, 0);
                    //Revert button text back
                    this.footer.getEditButton().setText("Edit");
                    this.footer.getBackButton().setText("Back");
                }
            }
        });

        Button deleteButton = footer.getDeleteButton();
        deleteButton.setOnAction(e -> {

            AppFrame mainAppFrame = ((AppFrame)this.handler.getMap().get("RecipeList").getRoot());
            
            int indexValue = mainAppFrame.getRecipeHandler().getRecipeList(((UserAccDisplay)this.handler.getMap().get("UserSL").getRoot()).getUHandler()
            .getUserName()).get(r.getTitle().getText()).getIndex(); //Convert Index label to string to int
            //mainAppFrame.getRecipeHandler().deleteRecipe(r.getTitle().getText().toString());
            mainAppFrame.getRecipeHandler().deleteRecipe(indexValue, mainAppFrame.getUserHandler().getUserName());
            mainAppFrame.getRecipeList().updateList(handler, 0);

            //then call update UI recipe
            handler.menu();
            
        });

        

    }

    // Helper method to create a scrollable text box
    private ScrollPane createScrollableBox(String content) {
        TextArea textArea = new TextArea(content);

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
        // Create scene of mentioned size with the border pane
        //Scene recipeList = new Scene(root, 500,600);

        //handler initializes by adding recipe list to pagelist
        //moved to sign in/login on action
        //handler.initialize(recipeList);

        UserAccDisplay userslDisplay = new UserAccDisplay(handler);
        //AppFrame root = new AppFrame(handler, userslDisplay.getUHandler());
        //root.setUHandler(userslDisplay.getUHandler());
        handler.showUserLogin(userslDisplay);

        // Make window non-resizable
        primaryStage.setResizable(false);
        // Show the app
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
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
import javafx.scene.control.CheckBox;
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

class UserAccDisplay extends BorderPane {
    private Header header;
    private UserAccFooter footer;
    private Button logInButton;
    private Button signUpButton;
    private CheckBox rememberMe;
    private Label inputAlert;
    private TextField user;
    private TextField pass;


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
        this.user = new TextField();
        this.pass = new TextField();

        user.setStyle(defaultTextFieldStyle);
        pass.setStyle(defaultTextFieldStyle);

        user.setPromptText("Username");
        pass.setPromptText("Password");
        // ScrollPane scrollPane1 = createScrollableBox("Ingredients: " + r.getIngredients().toString());
        // ScrollPane scrollPane2 = createScrollableBox("Instructions: " + r.getRecipeInstructions().toString());

        ArrayList<String> details = AutoLogin.load();
        //if csv exists, fill it in with last info when remember me was checked
        if(!details.isEmpty()){
            String u = details.get(0);
            String p = details.get(1);
            user.setText(u);
            pass.setText(p);
        }

        centerBox.getChildren().addAll(user, pass);

        rememberMe = new CheckBox("Remember Me");
        centerBox.getChildren().add(rememberMe);

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

    public boolean remembered(){
        return this.rememberMe.isSelected();
    }

    public void load(String user, String pass){
        this.user.setText(user);
        this.pass.setText(pass);
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

        System.out.println(uHandler.getUser(username,password) + "<-Returned password");
        
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

        boolean remembered = rememberMe.isSelected();
        if(remembered){
            System.out.println("REMEMBER ME");
            AutoLogin.save(username,password);
        } else {
            //if not remembered, dont load next time
            AutoLogin.clear();
            System.out.println("DONT REMEMBER");
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

        } else if(username.contains(",") || password.contains(",")){
            ((TextField)((VBox)this.getCenter()).getChildren().get(0)).setStyle(badFieldStyle);
            ((TextField)((VBox)this.getCenter()).getChildren().get(1)).setStyle(badFieldStyle);
            inputAlert.setText("Commas are invalid characters");
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

        //if does not exist already
        if (ret.contains("JSONException")){
            uHandler.putUser(username, password);
            ((TextField)((VBox)this.getCenter()).getChildren().get(0)).clear();
            ((TextField)((VBox)this.getCenter()).getChildren().get(1)).clear();
            ((TextField)((VBox)this.getCenter()).getChildren().get(0)).setStyle(defaultTextFieldStyle);
            ((TextField)((VBox)this.getCenter()).getChildren().get(1)).setStyle(defaultTextFieldStyle);

            //add remember on signup
            boolean remembered = rememberMe.isSelected();
            if(remembered){
                System.out.println("REMEMBER ME");
                AutoLogin.save(username,password);
            } else {
                //if not remembered, dont load next time
                AutoLogin.clear();
                System.out.println("DONT REMEMBER");
            }

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
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
import javafx.scene.layout.BorderPane;
import javafx.scene.text.TextAlignment;
import javafx.geometry.Insets;
import javafx.scene.text.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Recipe extends HBox { // extend HBox
    private String title;
    private int index; // for use in RecipeList
    private String mealType;
    private ArrayList<String> ingredients; // change to different data struct?
    private ArrayList<String> recipeInstructions; // change to different data struct?
    // add UI variables

    /*
     * Constructor for Recipe class
     *
     */
    Recipe(String title, int index, String mealType, ArrayList<String> ingredients,
        ArrayList<String> recipeInstructions) {
        this.title = title;
        this.index = index;
        this.ingredients = ingredients;
        this.recipeInstructions = recipeInstructions;
        this.mealType = mealType;

        this.setPrefSize(500, 20); // sets size of task
        this.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0; -fx-font-weight: bold;");

    }

    String getTitle(Recipe recipe) {
        return recipe.title;
    }

    int getIndex(Recipe recipe) {
        return recipe.index;
    }

    String getMealType(Recipe recipe) {
        return recipe.mealType;
    }

    ArrayList<String> getIngredients(Recipe recipe) {
        return recipe.ingredients;
    }

    ArrayList<String> getRecipeInstructions(Recipe recipe) {
        return recipe.recipeInstructions;
        // may want to print in a certain manner
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

    void setRecipeIndex(Recipe recipe, int newIndex) {
        recipe.index = newIndex;
    }
}

class RecipeList extends HBox { // extends HBox?

    // new RecipeList
    RecipeList() {
        // UI elements
        this.setSpacing(5); // sets spacing between tasks
        this.setPrefSize(500, 560);
        this.setStyle("-fx-background-color: #F0F8FF;");
    }

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
class Footer extends HBox {
    private Button addButton;
    private Button clearButton;
    private Button loadButton;
    private Button saveButton;
    private Button sortButton;
    Footer() {
        this.setPrefSize(500, 60);
        this.setStyle("-fx-background-color: #F0F8FF;");
        this.setSpacing(15);
        // set a default style for buttons - background color, font size, italics
        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";
        this.setAlignment(Pos.CENTER); // aligning the buttons to center
    }
}

/*
 * Class Copied from Lab 1 for Header
 */
class Header extends HBox {
    Header() {
        this.setPrefSize(500, 60); // Size of the header
        this.setStyle("-fx-background-color: #F0F8FF;");
        Text titleText = new Text("Recipe List"); // Text of the Header
        titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
        this.getChildren().add(titleText);
        this.setAlignment(Pos.CENTER); // Align the text to the Center
    }
}

/*
 * Class Copied from Lab 1 for AppFrame
 */
class AppFrame extends BorderPane {
    private Header header;
    private Footer footer;
    private RecipeList recipeList;

    AppFrame() {
        // Initialise the header Object
        header = new Header();
        // Create a recipelist Object to hold the tasks
        recipeList = new RecipeList();
        // Initialise the Footer Object
        footer = new Footer();

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

        // Call Event Listeners for the Buttons
        //addListeners();
    }
    // public void addListeners()
    // {
    // /* // Add button functionality
    // addButton.setOnAction(e -> {
    // // Create a new task
    // Task task = new Task();
    // // Add task to tasklist
    // taskList.getChildren().add(task);
    // // Add doneButtonToggle to the Done button
    // Button doneButton = task.getDoneButton();
    // doneButton.setOnAction(e1 -> {
    // // Call toggleDone on click
    // task.toggleDone();
    // });
    // // Update task indices
    // taskList.updateTaskIndices();
    // });
    // */
    // }

}

/*
 * Class Copied from Lab 1 for Main
 */
public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Setting the Layout of the Window- Should contain a Header, Footer and the
        // TaskList
        AppFrame root = new AppFrame();
        // Set the title of the app
        primaryStage.setTitle("Recipe List");
        // Create scene of mentioned size with the border pane
        primaryStage.setScene(new Scene(root, 500, 600));
        // Make window non-resizable
        primaryStage.setResizable(false);
        // Show the app
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
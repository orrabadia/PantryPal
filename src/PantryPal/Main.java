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
    private Text title;
    private Label index; // for use in RecipeList
    private Text mealType;
    private ArrayList<String> ingredients; // change to different data struct?
    private ArrayList<String> recipeInstructions; // change to different data struct?
    // add UI variables

    /*
     * Constructor for Recipe class
     *
     */
    Recipe(Text title, Text mealType, ArrayList<String> ingredients, ArrayList<String> recipeInstructions) {
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
        this.getChildren().add(title);
    }

    public void setRecipeIndex(int num){
        this.index.setText(num + "");
    }

    Text getTitle(Recipe recipe) {
        return recipe.title;
    }

    Label getIndex(Recipe recipe) {
        return recipe.index;
    }

    Text getMealType(Recipe recipe) {
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
class Footer extends HBox {
    private Button newRecipeButton;
    Footer() {
        this.setPrefSize(500, 60);
        this.setStyle("-fx-background-color: #F0F8FF;");
        this.setSpacing(15);
        // set a default style for buttons - background color, font size, italics
        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";
        this.setAlignment(Pos.CENTER); // aligning the buttons to center

        newRecipeButton = new Button("new Recipe");
        newRecipeButton.setStyle(defaultButtonStyle);
        this.getChildren().add(newRecipeButton);
        this.setAlignment(Pos.CENTER);
    }

    public Button getNewRecipeButton(){
        return newRecipeButton;
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
    private Button newRecipeButton;

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
        newRecipeButton = footer.getNewRecipeButton();

        // Call Event Listeners for the Buttons
        addListeners();
    }

    public void addListeners()
    {
    newRecipeButton.setOnAction(e -> {
        // just dummy values for now, gotta get the tokens from Chat GPT and parse them and pass them into here
        Recipe recipe = new Recipe(new Text("title"), new Text("meal"), new ArrayList<>(), new ArrayList<>());
        // Add task to tasklist
        recipeList.getChildren().add(recipe);
        recipeList.updateRecipeIndices();
    });
    // */
    }

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
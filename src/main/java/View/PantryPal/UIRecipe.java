package PantryPal;
import java.util.HashMap;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
class UIRecipe extends HBox { // extend HBox
    private NavigationHandler handler;
    private Text title;
    private Label index; // for use in RecipeList
    private Text mealType;
    private Button displayButton;
    private String ingredients; // change to different data struct?
    private String recipeInstructions; // change to different data struct?
    private int DBindex; //index of recipe(same as recipe index used througout app)
    // add UI variables

    /*
     * Constructor for Recipe class
     *
     */
    UIRecipe(Text title, Text mealType, String ingredients, String recipeInstructions, int DBindex , NavigationHandler handler) {
        this.handler = handler;
        // is being displayed
        this.title = title;
        // not being displayed
        this.ingredients = ingredients;
        this.recipeInstructions = recipeInstructions;
        this.mealType = mealType;
        this.DBindex = DBindex;

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
        hbox.getChildren().add(mealType);
        hbox.getChildren().add(title);
        //TODO: fix THE SPACING AND PUT IT AT THE RIGHT IDK HOW TO DO IT

        //add display recipe button
        displayButton = new Button("Display Recipe");
        displayButton.setAlignment(Pos.CENTER_RIGHT);
        //for display recipe button

        displayButton.setOnAction(e->{
            //this.handler.displayRecipe(this);
            try {
                this.handler.displayRecipe(this);
            } catch (Exception e1) {
                System.out.println("ERROR" + e1.toString());
            }
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

    int getDBIndex(){
        return this.DBindex;
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

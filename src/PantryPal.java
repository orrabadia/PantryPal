// package PantryPal;

// import javafx.application.Application;
// import javafx.scene.Scene;
// import javafx.stage.Stage;
// import javafx.geometry.Pos;
// import javafx.scene.control.Button;
// import javafx.scene.control.TextField;
// import javafx.scene.layout.*;
// import javafx.scene.control.Label;
// import javafx.scene.control.ScrollPane;
// import javafx.scene.layout.BorderPane;
// import javafx.scene.text.TextAlignment;
// import javafx.geometry.Insets;
// import javafx.scene.text.*;
// import java.io.*;
import java.util.ArrayList;
// import java.util.Collections;
// import java.util.List;

class Recipe { // extend HBox?
    private String title;
    // private String index; -- for use in RecipeList
    private String mealType;
    private ArrayList<String> ingredients; // change to different data struct?
    private ArrayList<String> recipeInstructions; // change to different data struct?
    // add UI variables

    /*
     * Constructor for Recipe class
     * 
     */
    Recipe(String title, String mealType, ArrayList<String> ingredients, ArrayList<String> recipeInstructions) {
        this.title = title;
        this.ingredients = ingredients;
        this.recipeInstructions = recipeInstructions;
        this.mealType = mealType;
    }

    String getTitle(Recipe recipe) {
        return recipe.title;
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
}
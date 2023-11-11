package PantryPal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;

import PantryPal.Main;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import PantryPal.AppFrame;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestAll {
    private static AppFrame appFrame;


    @BeforeAll
     public static void setUp() {
        //appFrame = new AppFrame();
        System.setProperty("java.awt.headless", "true");
        javafx.application.Platform.startup(() -> { });
        //Platform.setImplicitExit(false);
        appFrame = new AppFrame();
     }

     @AfterAll
     public static void tearDown() {
        //javafx.application.Platform.exit();
     }

     @AfterEach
     public void clearrecipes(){
        //clear recipe list each time
        appFrame.getRecipeList().getChildren().clear();
     }


    @Test 
    public void storyTestS1NoRecipes() {
        //at start, should be no recipes
        int initialRecipeCount = appFrame.getRecipeList().getChildren().size();
        assertEquals(initialRecipeCount, 0);
    }

    @Test 
    //having recipes should increase recipe count(these are displayed)
    public void storyTestS1ExistRecipe() {
        //add 2 recipes, 2 should be displayed
        appFrame.debugAddRecipe("Test Recipe 1", "Lunch", new ArrayList<String>(), new ArrayList<String>());
        appFrame.debugAddRecipe("Test Recipe 2", "Lunch", new ArrayList<String>(), new ArrayList<String>());
        int recipeCount = appFrame.getRecipeList().getChildren().size();
        assertEquals(recipeCount, 2);
    }

    @Test 
    //if remove recipes, count should go down(none should be displayed in UI)
    //actual delete functionality with recipe is for later, just want to make sure nothing is displayed
    public void storyTestS1RemovedRecipes() {
        //add 2 recipes, 2 should be displayed
        appFrame.debugAddRecipe("Test Recipe 1", "Lunch", new ArrayList<String>(), new ArrayList<String>());
        appFrame.debugAddRecipe("Test Recipe 2", "Lunch", new ArrayList<String>(), new ArrayList<String>());
        int recipeCount = appFrame.getRecipeList().getChildren().size();
        assertEquals(recipeCount, 2);

        //delete recipes, nothing should be displayed
        appFrame.getRecipeList().getChildren().clear();
        recipeCount = appFrame.getRecipeList().getChildren().size();
        assertEquals(recipeCount, 0);
    }

    @Test 
    //test that the dummy add recipe button works(full gpt functionality later, but just test the function linked to button works)
    public void unitTestS1AddButton() {
        //this is what is linked to add recipe button
        appFrame.debugAddRecipe("Test Recipe 1", "Lunch", new ArrayList<String>(), new ArrayList<String>());

        //test that it does what it should(in this case add a recipe to the list)
        Recipe r = (Recipe)appFrame.getRecipeList().getChildren().get(0);
        assertEquals(r.getTitle().getText(), "Test Recipe 1");
        assertEquals(r.getMealType().getText(), "Lunch");
        assertEquals(r.getRecipeInstructions(), new ArrayList<String>());
        assertEquals(r.getIngredients(), new ArrayList<String>());
    }

    @Test 
    //test creating a recipe and seeing if you can access all its fields
    public void unitTestS1Recipe(){
        //make new recipe with these fields
        ArrayList<String> instructions = new ArrayList<>();
        ArrayList<String> ingredients = new ArrayList<>();
        instructions.add("Make the food");
        ingredients.add("Food");
        Text title = new Text("Test Recipe 1");
        Text mealType = new Text("Lunch");

        Recipe r = new Recipe(title, mealType, ingredients, instructions);
        //test if can access fields unchanged
        assertEquals(r.getTitle().getText(), "Test Recipe 1");
        assertEquals(r.getMealType().getText(), "Lunch");
        assertEquals(r.getRecipeInstructions(), instructions);
        assertEquals(r.getIngredients(), ingredients);
    }
    @Test 
    //this tests that the recipe list can add and contain recipes, and that we can get them unchanged
    public void unitTestS1RecipeList() {
        ArrayList<String> instructions = new ArrayList<>();
        ArrayList<String> ingredients = new ArrayList<>();
        instructions.add("Make the food");
        ingredients.add("Food");
        Text title = new Text("Test Recipe 1");
        Text mealType = new Text("Lunch");
        Recipe rog = new Recipe(title, mealType, ingredients, instructions);

        //add 2 recipes
        appFrame.debugAddRecipe("Test Recipe 1", "Lunch", rog.getIngredients(), rog.getRecipeInstructions());
        appFrame.debugAddRecipe("Test Recipe 2", "Lunch", rog.getIngredients(), rog.getRecipeInstructions());
        //test if fields are the same after getting it
        //tests that the recipelist can store and access recipes
        Recipe r = (Recipe)appFrame.getRecipeList().getChildren().get(0);
        assertEquals(r.getTitle().getText(), rog.getTitle().getText());
        assertEquals(r.getMealType().getText(), rog.getMealType().getText());
        assertEquals(r.getRecipeInstructions(), rog.getRecipeInstructions());
        assertEquals(r.getIngredients(), rog.getIngredients());


        //test if they are ordered correctly when adding(second one added should be second)
        Recipe r1 = (Recipe)appFrame.getRecipeList().getChildren().get(1);
        assertEquals(r1.getTitle().getText(), "Test Recipe 2");
    }
}
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class Story1UnitTests {
    private static AppFrame appFrame;
    private static Recipe rog;


    @BeforeAll
     public static void setUp() {
        //declare og test recipe and start javafx
        javafx.application.Platform.startup(() -> { });
        appFrame = new AppFrame();
        ArrayList<String> instructions = new ArrayList<>();
        ArrayList<String> ingredients = new ArrayList<>();
        instructions.add("Make the food");
        ingredients.add("Food");
        Text title = new Text("Test Recipe 1");
        Text mealType = new Text("Lunch");
        rog = new Recipe(title, mealType, ingredients, instructions);
     }

     @AfterAll
     public static void tearDown() {
        //exit to prevent errors
        javafx.application.Platform.exit();
     }

     @AfterEach
     public void clearrecipes(){
        //clear recipe list each time
        appFrame.getRecipeList().getChildren().clear();
     }

    @Test 
    //test that the dummy add recipe button works(full gpt functionality later, but just test the function linked to button works)
    public void testAddButton() {
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
    public void testRecipe(){
        Recipe r = rog;

        ArrayList<String> instructions = new ArrayList<>();
        ArrayList<String> ingredients = new ArrayList<>();
        instructions.add("Make the food");
        ingredients.add("Food");

        assertEquals(r.getTitle().getText(), rog.getTitle().getText());
        assertEquals(r.getMealType().getText(), rog.getMealType().getText());
        assertEquals(r.getRecipeInstructions(), rog.getRecipeInstructions());
        assertEquals(r.getIngredients(), rog.getIngredients());
    }
    @Test 
    //this tests that the recipe list can add and contain recipes, and that we can get them unchanged
    public void testRecipeList() {

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


        //test if they are ordered correctly when adding
        Recipe r1 = (Recipe)appFrame.getRecipeList().getChildren().get(1);
        assertEquals(r1.getTitle().getText(), "Test Recipe 2");
    }



}
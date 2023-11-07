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
import javafx.stage.Stage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class Story1Test {
    private static AppFrame appFrame;


    @BeforeAll
     public static void setUp() {
        //appFrame = new AppFrame();
        javafx.application.Platform.startup(() -> { });
        appFrame = new AppFrame();
     }

     @AfterAll
     public static void tearDown() {
        javafx.application.Platform.exit();
     }


    @Test 
    public void testNoRecipes() {
        //at start, should be no recipes
        int initialRecipeCount = appFrame.getRecipeList().getChildren().size();
        assertEquals(initialRecipeCount, 0);
    }

    @Test 
    //having recipes should increase recipe count(these are displayed)
    public void testExistRecipe() {
        //add 2 recipes, 2 should be displayed
        appFrame.debugAddRecipe("Test Recipe 1", "Lunch", new ArrayList<String>(), new ArrayList<String>());
        appFrame.debugAddRecipe("Test Recipe 2", "Lunch", new ArrayList<String>(), new ArrayList<String>());
        int recipeCount = appFrame.getRecipeList().getChildren().size();
        assertEquals(recipeCount, 2);
    }

    @Test 
    //if remove recipes, count should go down(none should be displayed in UI)
    //actual delete functionality with recipe is for later, just want to make sure nothing is displayed
    public void testRemovedRecipes() {
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
}
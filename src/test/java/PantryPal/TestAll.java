package PantryPal;
import org.junit.jupiter.api.Test;

import PantryPal.AppFrame;
import PantryPal.Main;
import PantryPal.NavigationHandler;
import PantryPal.Recipe;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;


public class TestAll {
    private RecipeList rList;
    private RecipeHandler rHandler;

     @BeforeEach
     public void initialize(){
        rList = new RecipeList();
        rHandler = new RecipeHandler(rList);
        //this represents the initial app where no recipes are in the recipelist
        //every time you click add it adds to list, so this represents the list
        //TODO: update the list?
     }

     @AfterEach
     public void clearrecipes(){
        //clear recipe list each time
        rList.clear();
     }

    //story 1, test the gettitle and add and delete
    @Test 
    //this tests that the recipe list can add recipes(this is called when you push button)
    public void unitTestS1RecipeAdd() {
        String title1 = "Test Recipe 1";
        String mealtype = "Lunch";
        String ingredients = "food";
        String instructions = "cook food";
        Recipe r1= new Recipe(title1, mealtype, ingredients, instructions);
        rHandler.addRecipe(r1);
        //add two and check if you can get title, maybe 2 messes it up
        assertEquals(rList.size(), 1);
    }

    //test that you can delete recipes
    public void unitTestS1RecipeDelete() {
        String title1 = "Test Recipe 1";
        String mealtype = "Lunch";
        String ingredients = "food";
        String instructions = "cook food";
        Recipe r1= new Recipe(title1, mealtype, ingredients, instructions);
        rHandler.addRecipe(r1);
        //add two and check if you can get title, maybe 2 messes it up
        assertEquals(rList.size(), 1);
        rList.remove(title1);
        assertEquals(rList.size(), 0);
    }

    @Test 
    //test that you can get the title(this is used to display in the ui)
    public void unitTestS1RecipeDisplay(){
        String title1 = "Test Recipe 1";
        String title2 = "Test Recipe 2";
        String mealtype = "Lunch";
        String ingredients = "food";
        String instructions = "cook food";
        Recipe r1= new Recipe(title1, mealtype, ingredients, instructions);
        Recipe r2= new Recipe(title2, mealtype, ingredients, instructions);
        rHandler.addRecipe(r1);
        rHandler.addRecipe(r2);
        //add two and check if you can get title, maybe 2 messes it up
        Recipe r = rList.get(title1);
        assertEquals(r.getTitle(), "Test Recipe 1");
    }


    @Test 
    public void storyTestS1NoRecipes() {
        //at start, should be no recipes
        int initialRecipeCount = rList.size();
        assertEquals(initialRecipeCount, 0);
    }

    @Test 
    //having recipes should increase recipe count(these are displayed)
    public void storyTestS1ExistRecipe() {
        //add 2 recipes, 2 should be displayed
        String title = "Test Recipe 1";
        String mealtype = "Lunch";
        String ingredients = "food";
        String instructions = "cook food";
        Recipe r1= new Recipe(title, mealtype, ingredients, instructions);
        Recipe r2= new Recipe("Test Recipe 2", mealtype, ingredients, instructions);
        rHandler.addRecipe(r1);
        rHandler.addRecipe(r2);
        assertEquals(rList.size(), 2);
    }

    @Test 
    //if remove recipes, count should go down(because UI displays this list none should be displayed in UI)
    //actual delete UI functionality with recipe is for later, just want to make sure nothing will be displayed
    public void storyTestS1RemovedRecipes() {
        //add 2 recipes, 2 should be displayed
        String title1 = "Test Recipe 1";
        String title2 = "Test Recipe 2";
        String mealtype = "Lunch";
        String ingredients = "food";
        String instructions = "cook food";
        Recipe r1= new Recipe(title1, mealtype, ingredients, instructions);
        Recipe r2= new Recipe(title2, mealtype, ingredients, instructions);
        rHandler.addRecipe(r1);
        rHandler.addRecipe(r2);
        assertEquals(rList.size(), 2);
        //delete 
        rList.remove(title1);
        assertEquals(rList.size(), 1);
        rList.remove(title2);
        assertEquals(rList.size(), 0);

    }
    //story 2, test the other get methods that are used
    @Test
    public void unitTestS2getMethods(){
        String title1 = "Test Recipe 1";
        String title2 = "Test Recipe 2";
        String mealtype = "Lunch";
        String ingredients = "food";
        String instructions = "cook food";
        Recipe r1= new Recipe(title1, mealtype, ingredients, instructions);
        Recipe r2= new Recipe(title2, mealtype, ingredients, instructions);
        rHandler.addRecipe(r1);
        rHandler.addRecipe(r2);
        //add two and check if you can get title, maybe 2 messes it up
        Recipe r = rList.get(title1);
        assertEquals(r.getTitle(), "Test Recipe 1");
        assertEquals(r.getMealType(), "Lunch");
        assertEquals(r.getIngredients(), "food");
        assertEquals(r.getInstructions(), "cook food");
    }

}
package PantryPal;

import java.io.FileWriter;
import java.util.ArrayList;

public class RecipeHandler {
    private RecipeList list;
    private RequestHandler reqHandler;
    RecipeHandler(RecipeList list){
        this.list = list;
        this.reqHandler = new RequestHandler();
    }

    // public void addRecipe(Recipe r){
    //     //eventually these will call database methods, but right now we just call on the recipelist class
    //     //this would be a post request otherwise
    //     this.list.add(r);
    // }

    public void deleteRecipe(String title){
        this.list.remove(title);
    }

    public RecipeList getRecipeList(){
        //this should return the csv maybe? 
        return this.list;
    }

    public void addRecipe(Recipe r){
        //get current list from backend(csv) and replace this objects list with it
        String title = r.getTitle();
        String mealType = r.getMealType();
        String ingredients = r.getIngredients();
        String instructions = r.getInstructions();
        String newList = reqHandler.performRecipeRequest("PUT", title, mealType, ingredients, instructions, "recipe");
        if(newList.equals("")){
            System.out.println("fuck");
        }
        ArrayList<Recipe> replace = this.parseList(newList);
        this.list.setList(replace);
    }

    public void testgetRecipeList(){
        String newList = reqHandler.performRecipeRequest("GET", "", "", "", "", "");
        //do get, it should return csv list, parse and set current one
        ArrayList<Recipe> replace = this.parseList(newList);
        this.list.setList(replace);
    }

    public ArrayList<Recipe> parseList(String rList){
        ArrayList<Recipe> ret = new ArrayList<>();
        String[] lines = rList.split("\n");
        //parse the string and return a recipe arraylist
        //System.out.println("RLIST " +rList + " END");
        for (int i = 0; i < lines.length; i++) {
            String[] recipeValues = lines[i].split(",");
            System.out.println(recipeValues[0]);
            String title = recipeValues[0];
            String mealtype = recipeValues[1];
            String ingredients = recipeValues[2];
            String instructions = recipeValues[3];
            Recipe r = new Recipe(title, mealtype, ingredients, instructions);
            ret.add(r);
        }
        return ret;

    }

    //add methods later for edit
}

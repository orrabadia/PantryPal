package PantryPal;

import java.io.FileWriter;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

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

    public void deleteRecipe(int index, String username){
        //this.list.remove(title);
        //this.list.update();

        // String username = "MOGUSMAN";
        String newList = reqHandler.performRecipeRequest("DELETE", "", "", "","", index, username);

    }


    //added user parameter
    public void addRecipe(Recipe r, String username){
        //add new list to backend
        String title = r.getTitle();
        String mealType = r.getMealType();
        String ingredients = r.getIngredients();
        String instructions = r.getInstructions();
        int index = r.getIndex();
        //TODO: on user account, replace mogusman with something
        //String username = "MOGUSMAN"; // fix this to be current user
        //Added index field 
        String newList = reqHandler.performRecipeRequest("PUT", title, mealType, ingredients, instructions, index, username);

    }
    //added username parameter
    public RecipeList getRecipeList(String username){

        //Added index field 
        String newList = reqHandler.performRecipeRequest("GET", "", "", "", "", -1, username);
        JSONArray test = new JSONArray(newList);
        ArrayList<Recipe> replace = new ArrayList<>();
        //System.out.println("RHANDLER : Printing keys and values:");
            for (int i = 0; i < test.length(); i++) {
            JSONObject jsonObject = test.getJSONObject(i);
            Recipe add = new Recipe();
            //System.out.println("Element " + (i + 1) + ":");
            for (String key : jsonObject.keySet()) {
                Object value = jsonObject.get(key);
                //System.out.println("Key: " + key + ", Value: " + value);
                if(key.equals("title")){
                    add.setTitle(value.toString());
                } else if(key.equals("mealType")){
                    add.setMealType(value.toString());
                }else if(key.equals("ingredients")){
                    add.setIngredients(value.toString());
                }else if(key.equals("instructions")){
                    add.setInstructions(value.toString());
                }else if(key.equals("index")){
                    add.setIndex(Integer.parseInt(value.toString()));
                }
            }
            //System.out.println();
            //add recipe to list as you update
            replace.add(add);
        }


        //ArrayList<Recipe> replace = this.parseList(newList);
        this.list.setList(replace);
        return this.list; // changed from replace to this.list
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
    //username parameter?
    public void editRecipe(Recipe r, String ingredients, String instructions, String username){
        String title = r.getTitle();
        String mealType = r.getMealType();
        int index = r.getIndex();
        

        String newList = reqHandler.performRecipeRequest("POST", title, mealType, ingredients, instructions, index, username);

    }
    
}

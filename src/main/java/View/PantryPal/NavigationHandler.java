package PantryPal;

import java.util.HashMap;

import javax.management.RuntimeErrorException;

import javafx.scene.Scene;
import javafx.stage.Stage;

/**class to handle navigation, has map of all scenes and pointer to primarystage
 * */
class NavigationHandler{
    public static final String RECIPE_LIST = "RecipeList";
    public static final String DISPLAY_RECIPE = "DisplayRecipe";
    private Stage primaryStage;
    private HashMap<String, Scene> pageList;
    NavigationHandler(){
        this.pageList = new HashMap<>();
        pageList.put(DISPLAY_RECIPE, null);
        //initialize this to blank, we will fill in each time
        RecipeDisplay display = new RecipeDisplay(this);
        Scene details = new Scene(display, 500,600);
        pageList.put(DISPLAY_RECIPE, details);
    }

    boolean showRecipeList(){
        boolean ret = false;

        return ret;
    }
    /**called when initializing, takes a scene(in this case the recipelist) and shows it
     * */
    void initialize(Scene RecipeList){

        try {
            pageList.put("RecipeList", RecipeList);
            // Set the title of the Recipe Page
            primaryStage.setTitle("PantryPal");
            primaryStage.setScene(RecipeList);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * takes a recipe, link this with new recipe button
     * creates new recipe page, adds to map, displays it
     */
    void displayRecipe(UIRecipe r){
        //get the display page and set its content
        Scene s = pageList.get(DISPLAY_RECIPE);
        RecipeDisplay rd = (RecipeDisplay)s.getRoot();
        rd.setTitle(r.getTitle().getText());
        rd.setIngredients(r.getIngredients().toString());
        rd.setInstructions(r.getRecipeInstructions().toString());
        primaryStage.setScene(s);
    }

    void menu(){
        Scene f = pageList.get(RECIPE_LIST);
        if(f != null){
            primaryStage.setScene(f);
        } else {
            throw new RuntimeErrorException(null);
        }
    }

    void setStage(Stage s){
        this.primaryStage = s;
    }
}
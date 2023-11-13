package PantryPal;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;

import javax.management.RuntimeErrorException;

import javafx.scene.Scene;
import javafx.stage.Stage;

/**class to handle navigation, has map of all scenes and pointer to primarystage
 * */
class NavigationHandler{
    public static final String RECIPE_LIST = "RecipeList";
    public static final String DISPLAY_RECIPE = "DisplayRecipe";
    public static final String RECORD_MEALTYPE = "RecordMealType";
    public static final String RECORD_INGREDIENTS = "RecordIngredients";
    public static final String GPT_RESULTS = "GptResults";
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
        rd.setUIR(r);
        rd.setTitle(r.getTitle().getText());
        rd.setIngredients(r.getIngredients().toString());
        rd.setInstructions(r.getRecipeInstructions().toString());
        primaryStage.setScene(s);
    }

    void showGPTResults(GPTResultsDisplay g){
        //show gpt results page
        Scene s = new Scene(g, 500, 600);
        pageList.put(GPT_RESULTS, s);
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

    //STORY 3, RECORDMEAL
    void recordMeal(CreateHandler createHandler){
        Scene r = pageList.get(RECORD_MEALTYPE);
        //new page for recording meal
        try {
            RecordAppFrame mealrecorder = new RecordAppFrame(this, "meal", createHandler);
            //Scene mealrecord = new Scene(mealrecorder, 370, 120);
            Scene mealrecord = new Scene(mealrecorder, 370, 400);
            pageList.put(RECORD_MEALTYPE, mealrecord);
        } catch (IOException e1) {
            System.out.println(e1);
        } catch (URISyntaxException e2) {
            System.out.println(e2);
        }
        //switches to mealtype page
        r = pageList.get(RECORD_MEALTYPE);
        primaryStage.setScene(r);
    }

    //Story 4, Record Ingredients
    void recordIngredients(CreateHandler createHandler) {
        Scene r = pageList.get(RECORD_INGREDIENTS);
        //new page for recording ingredients
        try {
            RecordAppFrame ingredientrecorder = new RecordAppFrame(this, "ingredients", createHandler);
            //Scene mealrecord = new Scene(mealrecorder, 370, 120);
            Scene ingredientrecord = new Scene(ingredientrecorder, 370, 400);
            pageList.put(RECORD_INGREDIENTS, ingredientrecord);
        } catch (IOException e1) {
            System.out.println(e1);
        } catch (URISyntaxException e2) {
            System.out.println(e2);
        }
        //switches to ingredient page
        r = pageList.get(RECORD_INGREDIENTS);
        primaryStage.setScene(r);
    }

    //get the actual map
    HashMap<String, Scene> getPageList(){
        return this.pageList;
    }
}
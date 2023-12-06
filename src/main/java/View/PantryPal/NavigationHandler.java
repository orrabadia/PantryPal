package PantryPal;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.management.RuntimeErrorException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

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
    //scene for user sign in and login
    public static final String USER_S_L = "UserSL";
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

    HashMap<String, Scene> getMap(){
        return this.pageList;
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
    void displayRecipe(UIRecipe r) throws IOException, InterruptedException, URISyntaxException{
        //get the display page and set its content
        Scene s = pageList.get(DISPLAY_RECIPE);
        RecipeDisplay rd = (RecipeDisplay)s.getRoot();
        rd.setUIR(r);
        rd.setTitle(r.getTitle().getText());
        rd.setIngredients(r.getIngredients().toString());
        rd.setInstructions(r.getRecipeInstructions().toString());

        //get url, download image and display
        AppFrame main = (AppFrame)this.pageList.get(RECIPE_LIST).getRoot();
        String username = main.getUserHandler().getUserName();
        String index = String.valueOf(r.getDBIndex());
        DallEHandler d = new DallEHandler();
        String url = d.generate(r.getTitle().getText(), username, index, r.getIngredients());
        System.out.println("GENERATED LINK: "+ url);
        //create images directory in view
        Path imagesDir = Paths.get("images");
        if (!Files.exists(imagesDir)) {
            Files.createDirectory(imagesDir);
        }

        Path imagePath = imagesDir.resolve(username + " " +index + ".jpg");
        // if (!Files.exists(imagePath)) {
        //     // If it doesn't exist, download and save the image
        //     try (InputStream in = new URI(url).toURL().openStream()) {
        //         Files.copy(in, imagePath);
        //         System.out.println("Image saved successfully: " + imagePath);
        //     } catch (IOException e) {
        //         e.printStackTrace();
        //     }
        // } else {
        //     System.out.println("Image with index " + index + " already exists.");
        // }
        //replace every time regardless
        try (InputStream in = new URI(url).toURL().openStream()) {
            Files.copy(in, imagePath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Image saved successfully: " + imagePath);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        rd.setImg(imagePath.toString());

        Link l = new Link(username, Integer.parseInt(index));
        String shareUrl = l.getHTTPLink();
        rd.setShare(shareUrl);
        primaryStage.setScene(s);
    }

    void showGPTResults(GPTResultsDisplay g) throws IOException, InterruptedException, URISyntaxException{
        //show gpt results page
        Scene s = new Scene(g, 500, 600);
        //first we must get the temporary index(what it would be after saving)
        //access rhandler from main
        AppFrame main = (AppFrame)this.pageList.get(RECIPE_LIST).getRoot();
        RecipeHandler r = main.getRecipeHandler();
        String username = main.getUserHandler().getUserName();
        ArrayList<Recipe> checklist = r.getRecipeList(username).getList();
        //find the largest index in checklist
        int largest = 0;
        for(Recipe check: checklist){
            int i = check.getIndex();
            if(i>largest){
                largest = i;
            }
        }
        //increment largest so this is what it would be
        largest++;
        //largest is now the index we will use
        g.setIndex(largest);
        g.setUser(username);
        //get recipe info and generate a link
        DallEHandler d = new DallEHandler();
        String title = g.getCreateHandler().getRecipe().getTitle();
        String ingredients = g.getCreateHandler().getRecipe().getIngredients();
        String url = d.generate(title, username, String.valueOf(largest), ingredients);
        //ASSOCIATE IHANDLER WITH GPT DISPLAy
        g.setDallE(d);
        //create images directory in view
        Path imagesDir = Paths.get("images");
        if (!Files.exists(imagesDir)) {
            Files.createDirectory(imagesDir);
        }

        Path imagePath = imagesDir.resolve(username + " " +largest + ".jpg");
        // if (!Files.exists(imagePath)) {
        //     // if doesnt exist download and save
        //     try (InputStream in = new URI(url).toURL().openStream()) {
        //         Files.copy(in, imagePath);
        //         System.out.println("Image saved successfully: " + imagePath);
        //     } catch (IOException e) {
        //         e.printStackTrace();
        //     }
        // } else {
        //     System.out.println("Image with index " + largest + " already exists.");
        // }

        //download and replace current image
        try (InputStream in = new URI(url).toURL().openStream()) {
            Files.copy(in, imagePath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Image saved successfully: " + imagePath);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        //set the image to be the recipe
        g.setImg(imagePath.toString());
        pageList.put(GPT_RESULTS, s);
        primaryStage.setScene(s);
    }

     void showUserLogin(UserAccDisplay u){
        //show gpt results page
        Scene s = new Scene(u, 500, 600);
        pageList.put(USER_S_L, s);
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

    void userSL(){
        Scene f = pageList.get(USER_S_L);
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
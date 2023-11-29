package PantryPal;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.bson.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.io.File;

public class TestM2 {
    private MongoDatabase database;
    private MongoClient clientMongoDB;
    private String user;
    private MongoDB m;
    private RecipeList rList;
    private MongoCollection<Document> collection;
    @BeforeEach
     public void initialize(){

        m = new MongoDB();

        user = "amogus123";
        rList = new RecipeList();

        //insert your uri
        //clientMongoDB = MongoClients.create( "mongodb+srv://mogusman:amogus69@110.wfvva6a.mongodb.net/?retryWrites=true&w=majority");
        clientMongoDB = MongoClients.create( "mongodb+srv://orrabadia:yDIYYtTjsP0REJcl@cluster0.0b39ssz.mongodb.net/?retryWrites=true&w=majority");
        
        database = clientMongoDB.getDatabase("PantryPal");
        //sample user we will be using

        collection = database.getCollection(user);

     }

     @AfterEach
     public void clearrecipes(){
        rList.clear();
        //deletes mogus collection
        collection.drop();
     }

    /**get the most updated recipes for user specified at top */
    public ArrayList<Recipe> update(){
        JSONArray test = new JSONArray(m.get(user));
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
        return replace;
    }

    //Feature 1, recipe functions are now associated with a user
    @Test
    //user adds, edits, and deletes a recipe
    public void StoryTestF1() {
        //login is ui and cannot be tested, server cannot be tested
        //thus we test the mongodb methods that are being called

        String title = "test1";
        String mealType = "Breakfast";
        String ingredients = "Hot dog";
        String instructions = "cook food";
        Recipe r1 = new Recipe(title, mealType, ingredients, instructions);
        
        //add a new recipe
        m.put(user, title, mealType, ingredients, instructions);

        //get user list from mongo and replace current one
        ArrayList<Recipe> replace = update();
        rList.setList(replace);

        System.out.println(replace.toString());

        Recipe r2 = rList.get(title);
        assertEquals(r1.getTitle(), r2.getTitle());
        assertEquals(r1.getMealType(), r2.getMealType());
        assertEquals(r1.getIngredients(), r2.getIngredients());
        assertEquals(r1.getInstructions(), r2.getInstructions());

        //edit that recipe
        m.post(user, title, mealType, "two hot dogs", instructions, "0");
        //check if ingredients changed
        replace = update();
        rList.setList(replace);
        assertEquals("two hot dogs", rList.get(title).getIngredients());

        //delete the recipe
        m.delete(user, "0");

        //collection should now be empty
        assertEquals(0, collection.countDocuments());
    }

    public void delCSV(){
        String filePath = "./users.csv"; // Replace with the file path of the CSV file to delete

        File fileToDelete = new File(filePath);

        if (fileToDelete.exists()) { // Check if the file exists
            boolean isDeleted = fileToDelete.delete(); // Attempt to delete the file

            if (isDeleted) {
                System.out.println("File deleted successfully.");
            } else {
                System.out.println("Failed to delete the file.");
            }
        } else {
            System.out.println("File does not exist.");
        }
    }

    //Feature 1, automatic login
    @Test
    //user remembers password, loads in again later
    public void StoryTestF2() {
        //login is ui and cannot be tested, server cannot be tested
        //thus we test the actual csv methods that are being called

        ArrayList<String> details = AutoLogin.load();
        //this should be empty as there is no csv
        AutoLogin.save("mogusman", "sus123");
        //load checks if there is a csv, and also reads from it, so load should work
        details = AutoLogin.load();
        assertEquals("mogusman", details.get(0));
        assertEquals("sus123", details.get(1));
        //clear csv
        delCSV();
    }
}

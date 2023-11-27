package PantryPal;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;
import static com.mongodb.client.model.Updates.unset;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import org.bson.json.JsonWriterSettings;
import org.json.JSONArray;
import org.json.JSONObject;

public class MockMongoDB implements MongoDBInterface {
    
    String username = "Test_Username";
    RecipeList recipeCollection = new RecipeList();
    Recipe r1 = new Recipe("Bacon Cheese Sandwich", username, username, username);
    


    public String generateRecipe(String mealType, String ingredients) throws IOException, InterruptedException, URISyntaxException {
        String ret = "Diet Plan ~ Maybe you should just go on a diet.";
        return ret;
    }

    public void put(String username, String title, String mealType, String ingredients,
        String instructions){
            return;
        }

    public String get(String username) {
        return "";
    }

    public void delete(String username, String index){
        return;
    }

    public void post(String username, String title, String mealtype, String ingredients, String instructions, String index){
        return;
    }

    public void test(MongoClient mongoClient, String title, String mealType, String ingredients, String instructions){
        return;
    }
    
}
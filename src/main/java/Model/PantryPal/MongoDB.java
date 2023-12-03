package PantryPal;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.exists;
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
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import org.bson.json.JsonWriterSettings;
import org.json.JSONArray;
import org.json.JSONObject;

public class MongoDB {
    public static void main(String[] args) {
        //for testing
    }

    private MongoClient mongoClient;

    public MongoDB(){
        // USE THE SYSTEM GETENV WHEN COMMITTING TO GITHUB OTHERWISE TESTS WILL NOT WORK
        String uri = "mongodb://tlmeyers:Fxg8Y1uITtMHAYju@ac-ejtueax-shard-00-00.5pzdfsj.mongodb.net:27017,ac-ejtueax-shard-00-01.5pzdfsj.mongodb.net:27017,ac-ejtueax-shard-00-02.5pzdfsj.mongodb.net:27017/?ssl=true&replicaSet=atlas-10bgc3-shard-0&authSource=admin&retryWrites=true&w=majority";

        //System.out.println("uri = " + uri);

        // Establish MongoDB connection
        this.mongoClient = MongoClients.create(uri);

        //DELETE ALL EACH TIME
        // MongoDatabase database = this.mongoClient.getDatabase("PantryPal");
        // MongoCollection<Document> collection = database.getCollection("MOGUSMAN");
        //collection.deleteMany(new Document());
    }
    /** find and return json for a user, if not return notfound  */
    public String find(String username) {

        MongoDatabase database = this.mongoClient.getDatabase("PantryPal");
        MongoCollection<Document> collection = database.getCollection("Users");

        // filter for specific username
        Bson filter = Filters.exists(username);

        //find the specific username
        FindIterable<Document> result = collection.find(filter).projection(Projections.include(username));

        // should only be one
        Document document = result.first();

        // return if found, blank if not
        if (document != null) {
            return document.toJson();
        } else {
            return "{}";
        }

    }

    public void putUsername(String username, String password) {
        MongoDatabase dataBase = this.mongoClient.getDatabase("PantryPal");
        //database.users.insertOne(username :: password);
        Document doc = new Document();
        doc.append(username, password);
        dataBase.getCollection("Users").insertOne(doc);
        System.out.println("COLLECTION CREATED");
        dataBase.createCollection(username); // should we do this


    }
    /** adds to mongodb  */
    public void put(String username, String title, String mealType, String ingredients,
        String instructions) {
        MongoDatabase database = this.mongoClient.getDatabase("PantryPal");
        MongoCollection<Document> collection = database.getCollection(username);
        int check = (int)collection.countDocuments();
        if(check == 0){
            //do nothing, if nothing in there it should be 0
        } else {
            // get highest index currently
            FindIterable<Document> iterable = collection.find();
            int maxValue = Integer.MIN_VALUE; // Initialize with minimum possible integer value

            for (Document document : iterable) {
                // Assuming yourField contains integers
                int fieldValue = Integer.parseInt(document.getString("index"));

                // Check if fieldValue is greater than maxValue
                if (fieldValue > maxValue) {
                    maxValue = fieldValue;
                }
            }

            // Print the highest value found
            System.out.println("Highest value in 'index': " + maxValue);
            check = maxValue+1;
        }
        String index = String.valueOf(check);

        //index is current amount of things in the collection, this is how we will store creation order
        String[] headers = new String[] { "title", "mealType", "ingredients", "instructions" , "index"};
        String[] values = new String[] { title, mealType, ingredients, instructions, index };

        Document doc = new Document();
        // ADd pairs of headers/values in csv
        for (int i = 0; i < headers.length; i++) {
            doc.append(headers[i], values[i]);
        }
        collection.insertOne(doc);

        System.out.println("Added new recipe");
        System.out.println("After insertion: " + collection.countDocuments());
    }
    /** gets from mongodb with username being collection */
    public String get(String username) {
        MongoDatabase database = mongoClient.getDatabase("PantryPal");

        MongoCollection<Document> collection = database.getCollection(username);

        FindIterable<Document> documents = collection.find();
        //IF THERE IS NOTHING IN THERE, RETURN NOTHING
        int total = (int)collection.countDocuments();
        if(total == 0){
            return "[]";
        }
        JsonWriterSettings prettyPrint = JsonWriterSettings.builder().indent(true).build();
        //get all documents and append to json to send back
        StringBuilder combinedJson = new StringBuilder();
        for (Document doc : documents) {
            combinedJson.append(doc.toJson(prettyPrint));
            combinedJson.append(",");
            //System.out.println(doc.toJson(prettyPrint));
        }
        //delete the last comma
        combinedJson.deleteCharAt(combinedJson.length() - 1);
        String ret = combinedJson.toString();

        ret = "[" + ret + "]";
        System.out.println(ret);
        JSONArray test = new JSONArray(ret);
        System.out.println("SERVER MONGO: Printing keys and values:");
            for (int i = 0; i < test.length(); i++) {
            JSONObject jsonObject = test.getJSONObject(i);
            System.out.println("Element " + (i + 1) + ":");
            for (String key : jsonObject.keySet()) {
                Object value = jsonObject.get(key);
                System.out.println("Key: " + key + ", Value: " + value);
            }
            System.out.println(); // Separate each object's output
        }
        return ret;
    }

    public void delete(String username, String index) {
        MongoDatabase database = mongoClient.getDatabase("PantryPal");
        MongoCollection<Document> collection = database.getCollection(username);
        System.out.println(collection.countDocuments() + "Deleted a Recipe");
        Bson filter = eq("index", index);
        System.out.println(collection.countDocuments());
        collection.deleteOne(filter);
    }

    public void post(String username, String title, String mealtype, String ingredients, String instructions, String index) {
        MongoDatabase database = mongoClient.getDatabase("PantryPal");
        MongoCollection<Document> collection = database.getCollection(username);
        Bson filterIndex = eq("index", index);
        Bson updateMealType = set("mealType", mealtype);
        Bson updateIngredients = set("ingredients", ingredients);
        Bson updateInstructions = set("instructions", instructions);
        collection.updateOne(filterIndex, updateMealType);
        collection.updateOne(filterIndex, updateIngredients);
        collection.updateOne(filterIndex, updateInstructions);


    }

    public void test(MongoClient mongoClient, String title, String mealType, String ingredients, String instructions) {
        MongoDatabase database = mongoClient.getDatabase("PantryPal");
        MongoCollection<Document> collection = database.getCollection("Adolf");

        String csvFilePath = "./recipes.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            String[] headers = br.readLine().split(",");

            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");

                Document doc = new Document();
                // ADd pairs of headers/values in csv
                for (int i = 0; i < headers.length; i++) {
                    doc.append(headers[i], values[i]);
                }
                collection.insertOne(doc);
            }

            System.out.println("WOOOOOOOOOOOO");
            System.out.println("After insertion: " + collection.countDocuments());

            // update and print
            Bson filter = eq("Recipe", "Savory Spinach Delight");
            Bson updateOperation = set("Hours", "4.5");
            UpdateResult updateResult = collection.updateOne(filter, updateOperation);
            JsonWriterSettings prettyPrint = JsonWriterSettings.builder().indent(true).build();
            System.out.println(collection.find(filter).first().toJson(prettyPrint));

            // delete
            Bson delFilter = eq("Recipe", "Spicy Shrimp Tacos");
            DeleteResult result = collection.deleteOne(delFilter);
            System.out.println("After delete: " + collection.countDocuments());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

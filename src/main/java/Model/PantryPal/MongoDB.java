package PantryPal;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

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

public class MongoDB {
    public static void main(String[] args) {
        //for testing
        MongoDB m = new MongoDB();
        m.put("MOGUSMAN", "hot dog soup", "breakfast", "hot dogs", "cook hot dog");
        m.put("MOGUSMAN", null, null, null, null);
        //System.out.println(m.get("MOGUSMAN"));
        m.get("MOGUSMAN");
    }

    private MongoClient mongoClient;

    public MongoDB(){
        // Replace the placeholder with your MongoDB deployment's connection string
        String uri = "mongodb+srv://tqdo:EmgIlBckP64H9rGn@cluster0.8psfiwy.mongodb.net/?retryWrites=true&w=majority";
        // Establish MongoDB connection
        this.mongoClient = MongoClients.create(uri);

        //DELETE ALL EACH TIME
        // MongoDatabase database = this.mongoClient.getDatabase("PantryPal");
        // MongoCollection<Document> collection = database.getCollection("MOGUSMAN");
        //collection.deleteMany(new Document());
    }

    public void put(String username, String title, String mealType, String ingredients,
        String instructions) {
        MongoDatabase database = this.mongoClient.getDatabase("PantryPal");
        MongoCollection<Document> collection = database.getCollection(username);
        
        String[] headers = new String[] { "title", "mealType", "ingredients", "instructions" };
        String[] values = new String[] { title, mealType, ingredients, instructions };

        Document doc = new Document();
        // ADd pairs of headers/values in csv
        for (int i = 0; i < headers.length; i++) {
            doc.append(headers[i], values[i]);
        }
        collection.insertOne(doc);

        System.out.println("Added new recipe");
        System.out.println("After insertion: " + collection.countDocuments());

        // //update and print
        // Bson filter = eq("Recipe", "Savory Spinach Delight");
        // Bson updateOperation = set("Hours", "4.5");
        // UpdateResult updateResult = collection.updateOne(filter, updateOperation);
        // JsonWriterSettings prettyPrint =
        // JsonWriterSettings.builder().indent(true).build();
        // System.out.println(collection.find(filter).first().toJson(prettyPrint));

        // //delete
        // Bson delFilter = eq("Recipe", "Spicy Shrimp Tacos");
        // DeleteResult result = collection.deleteOne(delFilter);
        // System.out.println("After delete: " + collection.countDocuments());
    }

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

    public static void delete(MongoClient mongoClient) {

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
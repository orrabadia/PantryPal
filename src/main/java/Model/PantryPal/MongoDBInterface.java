package PantryPal;

import com.mongodb.client.MongoClient;


/**
 * MongoDBInterace
 */
public interface MongoDBInterface {

    public void put(String username, String title, String mealType, String ingredients,
        String instructions);

        public String get(String username);

        public void delete(String username, String index);

        public void post(String username, String title, String mealtype, String ingredients, String instructions, String index);

        public void test(MongoClient mongoClient, String title, String mealType, String ingredients, String instructions);
    
} 
    


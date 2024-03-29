package PantryPal;
import org.junit.jupiter.api.Test;

import com.mongodb.client.MongoDatabase;

import PantryPal.AppFrame;
import PantryPal.Main;
import PantryPal.NavigationHandler;
import PantryPal.Recipe;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.lt;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;
import static com.mongodb.client.model.Updates.unset;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;


public class TestAll {
    private RecipeList rList;
    private RecipeHandler rHandler;
    private RecordHandler recordHandler;
    private WhisperHandler whisperHandler;
    private CreateHandler createHandler;
    private GPTHandler gptHandler;
    private UserHandler userHandler;
    private MongoDatabase database;
    private MongoClient clientMongoDB;
    private MongoDB m;

    private MongoCollection<Document> collection1;
    private MongoCollection<Document> collection2;
    private String usernameTest1;
    private String passwordTest1;
    private String usernameTest2;
    private String passwordTest2;


     @BeforeEach
     public void initialize(){
        m = new MongoDB();
        rList = new RecipeList();
        rHandler = new RecipeHandler(rList);
        userHandler = new UserHandler();
        //this represents the initial app where no recipes are in the recipelist
        //every time you click add it adds to list, so this represents the list
        //the displayed list is gotten from the recipelist instance

        // creates a new RecordHandler (with MockRecorder)
        recordHandler = new RecordHandler(true);

        whisperHandler = new WhisperHandler(true);

        createHandler = new CreateHandler();

        gptHandler = new GPTHandler(true);

        String uri = System.getenv("MONGODB_CONNECTION_STRING");

        //insert your uri
        clientMongoDB = MongoClients.create(uri);

        database = clientMongoDB.getDatabase("PantryPal");

        usernameTest1 = "MOGUSMAN";
        passwordTest1 = "pass1";
        usernameTest2 = "HUMAN";
        passwordTest2 = "pass2";

        // Document doc1 = new Document();
        // Document doc2 = new Document();

        // doc1.append(usernameTest1, passwordTest1);
        // doc2.append(usernameTest2, passwordTest2);

        userHandler.putUser(usernameTest1, passwordTest1);
        userHandler.putUser(usernameTest2, passwordTest2);


        collection1 = database.getCollection(usernameTest1);
        collection2 = database.getCollection(usernameTest2);
        // userHandler.setUser(usernameTest1);
        // userHandler.setPass(passwordTest1);



     }

    public void deleteRecording() {
        String fileName = "src/main/java/Model/PantryPal/recording.wav";
        File file = new File(fileName);

        if (file.exists()) {
            file.delete();
        }

        fileName = "src/main/java/View/PantryPal/recording.wav";
        file = new File(fileName);

        if (file.exists()) {
            file.delete();
        }

    }

     @AfterEach
     public void clearrecipes(){
        //clear recipe list each time
        // rHandler.getRecipeList(usernameTest1).clear();
        // rHandler.getRecipeList(usernameTest2).clear();
        rList.clear();
        //deletes test_username collection

        Document doc1 = new Document();
        Document doc2 = new Document();

        doc1.append(usernameTest1, passwordTest1);
        doc2.append(usernameTest2, passwordTest2);
        collection1.drop();
        collection2.drop();

     }

      /**get the most updated recipes for user specified at top */
    public ArrayList<Recipe> update(){
        JSONArray test = new JSONArray(m.get(usernameTest1));
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

    //story 1, test the gettitle and add and delete
    @Test
    //this tests that the recipe list can add recipes(this is called when you push button)
    public void unitTestS1RecipeAdd() {

        String title1 = "Test Recipe 1";
        String mealtype = "Lunch";
        String ingredients = "food";
        String instructions = "cook food";
        Recipe r1= new Recipe(title1, mealtype, ingredients, instructions);
        //rHandler.addRecipe(r1, usernameTest1);
        m.put(usernameTest1, title1, mealtype, ingredients, instructions);
        ArrayList<Recipe> replace = update();
        rList.setList(replace);
        //add two and check if you can get title, maybe 2 messes it up

        //maybe have username input and make test collection?
         assertEquals(1, collection1.countDocuments());
        //assertEquals(rHandler.getRecipeList(usernameTest2).getList().size(), 0);
         assertEquals(0, collection2.countDocuments());
        //rHandler.addRecipe(r1, usernameTest2);
        m.put(usernameTest2, title1, mealtype, ingredients, instructions);
        replace = update();
        rList.setList(replace);
        //assertEquals(rHandler.getRecipeList(usernameTest1).getList().size(), 1);
    }

    //test that you can delete recipes
    @Test
    public void unitTestS1RecipeDelete() {
        String title1 = "Test Recipe 1";
        String mealtype = "Lunch";
        String ingredients = "food";
        String instructions = "cook food";
        Recipe r1= new Recipe(title1, mealtype, ingredients, instructions);
        // rHandler.addRecipe(r1, usernameTest1);
        // rHandler.addRecipe(r1, usernameTest2);

        m.put(usernameTest1, title1, mealtype, ingredients, instructions);
        m.put(usernameTest2, title1, mealtype, ingredients, instructions);
        ArrayList<Recipe> replace = update();
        rList.setList(replace);

        //check if you can delete recipes, and whether it effects other collections
        //assertEquals(rHandler.getRecipeList(usernameTest1).getList().size(), 1);
        assertEquals(1, collection1.countDocuments());
        //rHandler.deleteRecipe(r1.getIndex(), usernameTest1);
        m.delete(usernameTest1, "0");
        assertEquals(0, collection1.countDocuments());
        //assertEquals(rHandler.getRecipeList(usernameTest2).getList().size(), 1);
        assertEquals(1, collection2.countDocuments());
    }

    @Test
    //test that you can get the title(this is used to display in the ui)
    public void unitTestS1RecipeDisplay(){
        String title1 = "Test Recipe 1";
        String title2 = "Test Recipe 2";
        String mealtype = "Lunch";
        String ingredients = "food";
        String instructions = "cook food";
        Recipe r1= new Recipe(title1, mealtype, ingredients, instructions);
        Recipe r2= new Recipe(title2, mealtype, ingredients, instructions);
        // rHandler.addRecipe(r1, usernameTest1);
        // rHandler.addRecipe(r2, usernameTest1);
        m.put(usernameTest1, title1, mealtype, ingredients, instructions);
        m.put(usernameTest1, title2, mealtype, ingredients, instructions);
        //check if ingredients changed
        ArrayList<Recipe>replace = update();
        rList.setList(replace);
        //add two and check if you can get title, maybe 2 messes it up
        Recipe r = rList.get(title1);
        assertEquals(r.getTitle(), "Test Recipe 1");
    }


    @Test
    public void storyTestS1NoRecipes() {
        //at start, should be no recipes
        //int initialRecipeCount = rList.size();
        assertEquals(collection1.countDocuments(), 0);
        assertEquals(collection2.countDocuments(), 0);
        // assertEquals(rHandler.getRecipeList(usernameTest1).getList().size(), initialRecipeCount);
        // assertEquals(rHandler.getRecipeList(usernameTest2).getList().size(), initialRecipeCount);
    }

    @Test
    //having recipes should increase recipe count(these are displayed)
    public void storyTestS1ExistRecipe() {
        //add 2 recipes, 2 should be displayed
        String title = "Test Recipe 1";
        String mealtype = "Lunch";
        String ingredients = "food";
        String instructions = "cook food";
        Recipe r1= new Recipe(title, mealtype, ingredients, instructions);
        Recipe r2= new Recipe("Test Recipe 2", mealtype, ingredients, instructions);
        // rHandler.addRecipe(r1, usernameTest1);
        // rHandler.addRecipe(r2, usernameTest1);
        // assertEquals(rHandler.getRecipeList(usernameTest1).getList().size(), 2);

        m.put(usernameTest1, title, mealtype, ingredients, instructions);
        ArrayList<Recipe> replace = update();
        rList.setList(replace);
        assertEquals(1, collection1.countDocuments());
        assertEquals(1, rList.size());
    }

    @Test
    //if remove recipes, count should go down(because UI displays this list none should be displayed in UI)
    //actual delete UI functionality with recipe is for later, just want to make sure nothing will be displayed
    public void storyTestS1RemovedRecipes() {
        //add 2 recipes, 2 should be displayed
        String title1 = "Test Recipe 1";
        String title2 = "Test Recipe 2";
        String mealtype = "Lunch";
        String ingredients = "food";
        String instructions = "cook food";
        Recipe r1 = new Recipe(title1, mealtype, ingredients, instructions);
        Recipe r2 = new Recipe(title2, mealtype, ingredients, instructions);
        // rHandler.addRecipe(r1, usernameTest1);
        // rHandler.addRecipe(r2, usernameTest1);

        m.put(usernameTest1, title1, mealtype, ingredients, instructions);
        m.put(usernameTest1, title2, mealtype, ingredients, instructions);

        //get user list from mongo and replace current one
        ArrayList<Recipe> replace = update();
        rList.setList(replace);
        // assertEquals(rHandler.getRecipeList(usernameTest1).getList().size(), 2);
        assertEquals(2, collection1.countDocuments());
        //delete
        //rHandler.deleteRecipe(r1.getIndex(), usernameTest1);
        m.delete(usernameTest1, "0");
        assertEquals(1, collection1.countDocuments());
        m.delete(usernameTest1, "1");
        assertEquals(0, collection1.countDocuments());

        // rHandler.deleteRecipe(r2.getIndex(), usernameTest1);


        // assertEquals(0 , rHandler.getRecipeList(usernameTest1).getList().size());

    }

    //story 2, test getTitle
    @Test
    public void unitTestS2getTitle(){
        String title1 = "Test Recipe 1";
        String title2 = "Test Recipe 2";
        String mealtype = "Lunch";
        String ingredients = "food";
        String instructions = "cook food";
        Recipe r1 = new Recipe(title1, mealtype, ingredients, instructions);
        Recipe r2 = new Recipe(title2, mealtype, ingredients, instructions);
        // rHandler.addRecipe(r1, usernameTest1);
        // rHandler.addRecipe(r2, usernameTest1);

        m.put(usernameTest1, title1, mealtype, ingredients, instructions);
        m.put(usernameTest1, title2, mealtype, ingredients, instructions);


        //get user list from mongo and replace current one
        ArrayList<Recipe> replace = update();
        rList.setList(replace);
        //add two and check if you can get title, maybe 2 messes it up
        Recipe r = rList.get(title1);
        assertEquals(r.getTitle(), "Test Recipe 1");
    }

    //story 2, test getMealType
    @Test
    public void unitTestS2getMealType(){
        String title1 = "Test Recipe 1";
        String title2 = "Test Recipe 2";
        String mealtype = "Lunch";
        String ingredients = "food";
        String instructions = "cook food";
        Recipe r1 = new Recipe(title1, mealtype, ingredients, instructions);
        Recipe r2 = new Recipe(title2, mealtype, ingredients, instructions);
        // rHandler.addRecipe(r1, usernameTest1);
        // rHandler.addRecipe(r2, usernameTest1);
        m.put(usernameTest1, title1, mealtype, ingredients, instructions);
        m.put(usernameTest1, title2, mealtype, ingredients, instructions);


        //get user list from mongo and replace current one
        ArrayList<Recipe> replace = update();
        rList.setList(replace);
        //add two and check if you can get title, maybe 2 messes it up
        Recipe r = rList.get(title1);
        assertEquals(r.getMealType(), "Lunch");
    }

    //story 2, test getMealType
    @Test
    public void unitTestS2getIngredients(){
        String title1 = "Test Recipe 1";
        String title2 = "Test Recipe 2";
        String mealtype = "Lunch";
        String ingredients = "food";
        String instructions = "cook food";
        Recipe r1 = new Recipe(title1, mealtype, ingredients, instructions);
        Recipe r2 = new Recipe(title2, mealtype, ingredients, instructions);
        // rHandler.addRecipe(r1, usernameTest1);
        // rHandler.addRecipe(r2, usernameTest1);
        // //add two and check if you can get title, maybe 2 messes it up
        // Recipe r = rHandler.getRecipeList(usernameTest1).get(title1);
        m.put(usernameTest1, title1, mealtype, ingredients, instructions);
        m.put(usernameTest1, title2, mealtype, ingredients, instructions);


        //get user list from mongo and replace current one
        ArrayList<Recipe> replace = update();
        rList.setList(replace);
        //add two and check if you can get title, maybe 2 messes it up
        Recipe r = rList.get(title1);
        assertEquals(r.getIngredients(), "food");
    }

    //story 2, test getMealType
    @Test
    public void unitTestS2getInstructions(){
        String title1 = "Test Recipe 1";
        String title2 = "Test Recipe 2";
        String mealtype = "Lunch";
        String ingredients = "food";
        String instructions = "cook food";
        Recipe r1 = new Recipe(title1, mealtype, ingredients, instructions);
        Recipe r2 = new Recipe(title2, mealtype, ingredients, instructions);
        // rHandler.addRecipe(r1, usernameTest1);
        // rHandler.addRecipe(r2, usernameTest1);
        // //add two and check if you can get title, maybe 2 messes it up
        // Recipe r = rHandler.getRecipeList(usernameTest1).get(title1);
        m.put(usernameTest1, title1, mealtype, ingredients, instructions);
        m.put(usernameTest1, title2, mealtype, ingredients, instructions);


        //get user list from mongo and replace current one
        ArrayList<Recipe> replace = update();
        rList.setList(replace);
        //add two and check if you can get title, maybe 2 messes it up
        Recipe r = rList.get(title1);
        assertEquals(r.getInstructions(), "cook food");
    }

    //story 2, test the other get methods that are used
    @Test
    public void storyTestS2getMethods(){
        String title1 = "Test Recipe 1";
        String title2 = "Test Recipe 2";
        String mealtype = "Lunch";
        String ingredients = "food";
        String instructions = "cook food";
        Recipe r1 = new Recipe(title1, mealtype, ingredients, instructions);
        Recipe r2 = new Recipe(title2, mealtype, ingredients, instructions);
        // rHandler.addRecipe(r1, usernameTest1);
        // rHandler.addRecipe(r2, usernameTest1);
        // //add two and check if you can get title, maybe 2 messes it up
        // Recipe r = rHandler.getRecipeList(usernameTest1).get(title1);
        m.put(usernameTest1, title1, mealtype, ingredients, instructions);
        m.put(usernameTest1, title2, mealtype, ingredients, instructions);


        //get user list from mongo and replace current one
        ArrayList<Recipe> replace = update();
        rList.setList(replace);
        //add two and check if you can get title, maybe 2 messes it up
        Recipe r = rList.get(title1);
        assertEquals(r.getTitle(), "Test Recipe 1");
        assertEquals(r.getMealType(), "Lunch");
        assertEquals(r.getIngredients(), "food");
        assertEquals(r.getInstructions(), "cook food");
    }

    // Story 3, test that when you press the Record button (and press stop record), src/main/java/Model/PantryPal/recording.wav is made
    @Test
    public void unitTestS3Record() {
        try {
                recordHandler.record();
            }
        catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
        }
        File file = new File("src/main/java/View/PantryPal/recording.wav");
        assertEquals(true, file.exists());
    }

    // Story 3, test that the "functionality" of getting src/main/java/Model/PantryPal/recording.wav and processing

    // it in Whisper and see whether it's text matches what it should be
    @Test
    public void unitTestS3Whisper() {
        File audioFile = new File("src/main/java/View/PantryPal/recording.wav");

         // the number of samples of audio per second.
        // 44100 represents the typical sample rate for CD-quality audio.
        float sampleRate = 44100;

        // the number of bits in each sample of a sound that has been digitized.
        int sampleSizeInBits = 16;

        // the number of audio channels in this format (1 for mono, 2 for stereo).
        int channels = 1;

        // whether the data is signed or unsigned.
        boolean signed = true;

        // whether the audio data is stored in big-endian or little-endian order.
        boolean bigEndian = false;

        AudioFormat audioFormat = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);

        // creates an empty audioInputStream
        AudioInputStream audioInputStream = new AudioInputStream(null, audioFormat, 0);

        try {
            AudioSystem.write(
                        audioInputStream,
                        AudioFileFormat.Type.WAVE,
                        audioFile);
        }
        catch (IOException e1){
            System.err.println("IOException");
        }
        File file = new File("src/main/java/View/PantryPal/recording.wav");
        assertEquals(true, file.exists());
        try {
            assertEquals( "Dinner", whisperHandler.transcribe());
        }
        catch (IOException e1) {
            System.err.println("IOException");
        }
        catch (URISyntaxException e2){
                System.err.println("URISyntaxException");
        }
    }

    // Story 3, test whether our program saves a user's meal type info
    // Here we separately test for whether our program can save a meal type
    // without the funcitonality of Record or Whisper (we can test all of them in the story test)
    @Test
    public void unitTestS3SaveMealType() {
        createHandler.getRecipe().setMealType("Dinner");
        assertEquals("Dinner", createHandler.getRecipe().getMealType());
    }

    @Test
    // tests the combined functionality of record and transcribe and saving meal type
    public void storyTestS3MealType() {
        try {
                recordHandler.record();
            }
        catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
        }
        File file = new File("src/main/java/View/PantryPal/recording.wav");
        assertEquals(true, file.exists());

        // Server not active during JUnit, so we "simulate" recording.wav being transferred up

        File audioFile = new File("src/main/java/Model/PantryPal/recording.wav");

         // the number of samples of audio per second.
        // 44100 represents the typical sample rate for CD-quality audio.
        float sampleRate = 44100;

        // the number of bits in each sample of a sound that has been digitized.
        int sampleSizeInBits = 16;

        // the number of audio channels in this format (1 for mono, 2 for stereo).
        int channels = 1;

        // whether the data is signed or unsigned.
        boolean signed = true;

        // whether the audio data is stored in big-endian or little-endian order.
        boolean bigEndian = false;

        AudioFormat audioFormat = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);

        // creates an empty audioInputStream
        AudioInputStream audioInputStream = new AudioInputStream(null, audioFormat, 0);

        try {
            AudioSystem.write(
                        audioInputStream,
                        AudioFileFormat.Type.WAVE,
                        audioFile);
        }
        catch (IOException e1){
            System.err.println("IOException");
        }
        String transcription = "";
        try {
            transcription = whisperHandler.transcribe();
        }
        catch (IOException e1) {
            System.err.println("IOException");
        }
        catch (URISyntaxException e2){
                System.err.println("URISyntaxException");
        }
        assertEquals( "Dinner", transcription);
        createHandler.getRecipe().setMealType(transcription);
        assertEquals("Dinner", createHandler.getRecipe().getMealType());
    }

    @Test
    // tests a rerecord situation, where the user doesn't like the result of the first
    // record they did and rerecord again
    public void storyTestS3Rerecord() {
        // first record
        try {
                recordHandler.record();
            }
        catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
        }
        File file = new File("src/main/java/View/PantryPal/recording.wav");
        assertEquals(true, file.exists());

        // second record
        try {
                recordHandler.record();
            }
        catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
        }
        assertEquals(true, file.exists());

        // Server not active during JUnit, so we "simulate" recording.wav being transferred up

        File audioFile = new File("src/main/java/Model/PantryPal/recording.wav");

         // the number of samples of audio per second.
        // 44100 represents the typical sample rate for CD-quality audio.
        float sampleRate = 44100;

        // the number of bits in each sample of a sound that has been digitized.
        int sampleSizeInBits = 16;

        // the number of audio channels in this format (1 for mono, 2 for stereo).
        int channels = 1;

        // whether the data is signed or unsigned.
        boolean signed = true;

        // whether the audio data is stored in big-endian or little-endian order.
        boolean bigEndian = false;

        AudioFormat audioFormat = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);

        // creates an empty audioInputStream
        AudioInputStream audioInputStream = new AudioInputStream(null, audioFormat, 0);

        try {
            AudioSystem.write(
                        audioInputStream,
                        AudioFileFormat.Type.WAVE,
                        audioFile);
        }
        catch (IOException e1){
            System.err.println("IOException");
        }
        String transcription = "";
        try {
            transcription = whisperHandler.transcribe();
        }
        catch (IOException e1) {
            System.err.println("IOException");
        }
        catch (URISyntaxException e2){
                System.err.println("URISyntaxException");
        }
        assertEquals( "Dinner", transcription);
        createHandler.getRecipe().setMealType(transcription);
        assertEquals("Dinner", createHandler.getRecipe().getMealType());
    }

    @Test
    // integration test of stories 1-3
    // Story 1: No recipes
    // Story 3: Record

    public void integrationTest1() {
        // story 1
        //at start, should be no recipes
        int initialRecipeCount = rList.size();
        assertEquals(initialRecipeCount, 0);
        // story 3
        try {
                recordHandler.record();
            }
        catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
        }
        File file = new File("src/main/java/View/PantryPal/recording.wav");
        assertEquals(true, file.exists());

        // Server not active during JUnit, so we "simulate" recording.wav being transferred up

        File audioFile = new File("src/main/java/Model/PantryPal/recording.wav");

         // the number of samples of audio per second.
        // 44100 represents the typical sample rate for CD-quality audio.
        float sampleRate = 44100;

        // the number of bits in each sample of a sound that has been digitized.
        int sampleSizeInBits = 16;

        // the number of audio channels in this format (1 for mono, 2 for stereo).
        int channels = 1;

        // whether the data is signed or unsigned.
        boolean signed = true;

        // whether the audio data is stored in big-endian or little-endian order.
        boolean bigEndian = false;

        AudioFormat audioFormat = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);

        // creates an empty audioInputStream
        AudioInputStream audioInputStream = new AudioInputStream(null, audioFormat, 0);

        try {
            AudioSystem.write(
                        audioInputStream,
                        AudioFileFormat.Type.WAVE,
                        audioFile);
        }
        catch (IOException e1){
            System.err.println("IOException");
        }
        String transcription = "";
        try {
            transcription = whisperHandler.transcribe();
        }
        catch (IOException e1) {
            System.err.println("IOException");
        }
        catch (URISyntaxException e2){
                System.err.println("URISyntaxException");
        }
        assertEquals( "Dinner", transcription);
        createHandler.getRecipe().setMealType(transcription);
        assertEquals("Dinner", createHandler.getRecipe().getMealType());
    }

    @Test
    // integration test of stories 1-3
    // Story 1: (inserted it before hand but sneakily) Exist recipe
    // Story 2: Expand details of recipe (just make sure that recipe details match)
    // Story 3: Rerecord

    public void integrationTest2() {
        // story 1
        //add 2 recipes, 2 should be displayed
        String title = "Test Recipe 1";
        String mealtype = "Lunch";
        String ingredients = "food";
        String instructions = "cook food";
        Recipe r1= new Recipe(title, mealtype, ingredients, instructions);
        Recipe r2= new Recipe("Test Recipe 2", mealtype, ingredients, instructions);
        // rHandler.addRecipe(r1, usernameTest1);
        // rHandler.addRecipe(r2, usernameTest1);
        m.put(usernameTest1, title, mealtype, ingredients, instructions);
        m.put(usernameTest1, "Test Recipe 2", mealtype, ingredients, instructions);

        //get user list from mongo and replace current one
        ArrayList<Recipe> replace = update();
        rList.setList(replace);
        assertEquals(2, collection1.countDocuments());
        //add two and check if you can get title, maybe 2 messes it up

        //assertEquals(rHandler.getRecipeList(usernameTest1).getList().size(), 2);

        // story 2
        //add two and check if you can get title, maybe 2 messes it up
        Recipe r = rList.get(title);
        assertEquals(r.getTitle(), "Test Recipe 1");
        assertEquals(r.getMealType(), "Lunch");
        assertEquals(r.getIngredients(), "food");
        assertEquals(r.getInstructions(), "cook food");

        // story 3
        // first record
        try {
                recordHandler.record();
            }
        catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
        }
        File file = new File("src/main/java/View/PantryPal/recording.wav");
        assertEquals(true, file.exists());

        // second record
        try {
                recordHandler.record();
            }
        catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
        }
        assertEquals(true, file.exists());

        // Server not active during JUnit, so we "simulate" recording.wav being transferred up

        File audioFile = new File("src/main/java/Model/PantryPal/recording.wav");

         // the number of samples of audio per second.
        // 44100 represents the typical sample rate for CD-quality audio.
        float sampleRate = 44100;

        // the number of bits in each sample of a sound that has been digitized.
        int sampleSizeInBits = 16;

        // the number of audio channels in this format (1 for mono, 2 for stereo).
        int channels = 1;

        // whether the data is signed or unsigned.
        boolean signed = true;

        // whether the audio data is stored in big-endian or little-endian order.
        boolean bigEndian = false;

        AudioFormat audioFormat = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);

        // creates an empty audioInputStream
        AudioInputStream audioInputStream = new AudioInputStream(null, audioFormat, 0);

        try {
            AudioSystem.write(
                        audioInputStream,
                        AudioFileFormat.Type.WAVE,
                        audioFile);
        }
        catch (IOException e1){
            System.err.println("IOException");
        }
        String transcription = "";
        try {
            transcription = whisperHandler.transcribe();
        }
        catch (IOException e1) {
            System.err.println("IOException");
        }
        catch (URISyntaxException e2){
                System.err.println("URISyntaxException");
        }
        assertEquals( "Dinner", transcription);
        createHandler.getRecipe().setMealType(transcription);
        assertEquals("Dinner", createHandler.getRecipe().getMealType());
    }

     @Test
    // integration test of stories 1-3
    // Story 1: remove preexisting recipes (2 to 0)
    // Story 3: Record

    public void integrationTest3 () {
        // Story 1
        //add 2 recipes, 2 should be displayed
        String title1 = "Test Recipe 1";
        String title2 = "Test Recipe 2";
        String mealtype = "Lunch";
        String ingredients = "food";
        String instructions = "cook food";
        Recipe r1 = new Recipe(title1, mealtype, ingredients, instructions);
        Recipe r2 = new Recipe(title2, mealtype, ingredients, instructions);
        m.put(usernameTest1, title1, mealtype, ingredients, instructions);
        m.put(usernameTest1, title2, mealtype, ingredients, instructions);
        ArrayList<Recipe> replace = update();
        rList.setList(replace);
        assertEquals(2, collection1.countDocuments());
        //delete
        m.delete(usernameTest1, "0");
        //assertEquals(rHandler.getRecipeList(usernameTest1).getList().size(), 1);
        assertEquals(1, collection1.countDocuments());
        //rHandler.getRecipeList(usernameTest1).remove(title2);
        replace = update();
        rList.setList(replace);
         m.delete(usernameTest1, "1"); //may be wrong
        //assertEquals(rHandler.getRecipeList(usernameTest1).getList().size(), 0);
        assertEquals(0, collection1.countDocuments());

        // Story 3
        try {
                recordHandler.record();
            }
        catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
        }
        File file = new File("src/main/java/View/PantryPal/recording.wav");
        assertEquals(true, file.exists());

        // Server not active during JUnit, so we "simulate" recording.wav being transferred up

        File audioFile = new File("src/main/java/Model/PantryPal/recording.wav");

         // the number of samples of audio per second.
        // 44100 represents the typical sample rate for CD-quality audio.
        float sampleRate = 44100;

        // the number of bits in each sample of a sound that has been digitized.
        int sampleSizeInBits = 16;

        // the number of audio channels in this format (1 for mono, 2 for stereo).
        int channels = 1;

        // whether the data is signed or unsigned.
        boolean signed = true;

        // whether the audio data is stored in big-endian or little-endian order.
        boolean bigEndian = false;

        AudioFormat audioFormat = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);

        // creates an empty audioInputStream
        AudioInputStream audioInputStream = new AudioInputStream(null, audioFormat, 0);

        try {
            AudioSystem.write(
                        audioInputStream,
                        AudioFileFormat.Type.WAVE,
                        audioFile);
        }
        catch (IOException e1){
            System.err.println("IOException");
        }
        String transcription = "";
        try {
            transcription = whisperHandler.transcribe();
        }
        catch (IOException e1) {
            System.err.println("IOException");
        }
        catch (URISyntaxException e2){
                System.err.println("URISyntaxException");
        }
        assertEquals( "Dinner", transcription);
        createHandler.getRecipe().setMealType(transcription);
        assertEquals("Dinner", createHandler.getRecipe().getMealType());
    }


    // Story 4, test that when you press the Record button (and press stop record), src/main/java/Model/PantryPal/recording.wav is made
    @Test
    public void unitTestS4Record() {
        try {
                recordHandler.record();
            }
        catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
        }
        File file = new File("src/main/java/View/PantryPal/recording.wav");
        assertEquals(true, file.exists());
    }

    // Story 3, test that the "functionality" of getting src/main/java/Model/PantryPal/recording.wav and processing
    // it in Whisper and see whether it's text matches what it should be
    @Test
    public void unitTestS4Whisper() {
        File audioFile = new File("src/main/java/Model/PantryPal/recording.wav");

         // the number of samples of audio per second.
        // 44100 represents the typical sample rate for CD-quality audio.
        float sampleRate = 44100;

        // the number of bits in each sample of a sound that has been digitized.
        int sampleSizeInBits = 16;

        // the number of audio channels in this format (1 for mono, 2 for stereo).
        int channels = 1;

        // whether the data is signed or unsigned.
        boolean signed = true;

        // whether the audio data is stored in big-endian or little-endian order.
        boolean bigEndian = false;

        AudioFormat audioFormat = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);

        // creates an empty audioInputStream
        AudioInputStream audioInputStream = new AudioInputStream(null, audioFormat, 0);

        try {
            AudioSystem.write(
                        audioInputStream,
                        AudioFileFormat.Type.WAVE,
                        audioFile);
        }
        catch (IOException e1){
            System.err.println("IOException");
        }
        File file = new File("src/main/java/Model/PantryPal/recording.wav");
        assertEquals(true, file.exists());
        String transcription = "";
        try {
            transcription = whisperHandler.transcribe();
        }
        catch (IOException e1) {
            System.err.println("IOException");
        }
        catch (URISyntaxException e2){
                System.err.println("URISyntaxException");
        }
        assertEquals( "Dinner", transcription);
        createHandler.getRecipe().setMealType(transcription);
        assertEquals("Dinner", createHandler.getRecipe().getMealType());
    }


    // Story 4, test whether our program saves a user's ingredient info
    // Here we separately test for whether our program can save ingredients
    // without the funcitonality of Record or Whisper (we can test all of them in the story test)
    @Test
    public void unitTestS4SaveIngredients() {
        createHandler.getRecipe().setIngredients("Corn, Peas, Carrots, Chicken");
        assertEquals("Corn, Peas, Carrots, Chicken", createHandler.getRecipe().getIngredients());
    }

    @Test
    // tests the combined functionality of record and transcribe and saving ingredients
    public void storyTestS4Ingredients() {
        try {
                recordHandler.record();
            }
        catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
        }
        File file = new File("src/main/java/View/PantryPal/recording.wav");
        assertEquals(true, file.exists());

        // Server not active during JUnit, so we "simulate" recording.wav being transferred up

        File audioFile = new File("src/main/java/Model/PantryPal/recording.wav");

         // the number of samples of audio per second.
        // 44100 represents the typical sample rate for CD-quality audio.
        float sampleRate = 44100;

        // the number of bits in each sample of a sound that has been digitized.
        int sampleSizeInBits = 16;

        // the number of audio channels in this format (1 for mono, 2 for stereo).
        int channels = 1;

        // whether the data is signed or unsigned.
        boolean signed = true;

        // whether the audio data is stored in big-endian or little-endian order.
        boolean bigEndian = false;

        AudioFormat audioFormat = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);

        // creates an empty audioInputStream
        AudioInputStream audioInputStream = new AudioInputStream(null, audioFormat, 0);

        try {
            AudioSystem.write(
                        audioInputStream,
                        AudioFileFormat.Type.WAVE,
                        audioFile);
        }
        catch (IOException e1){
            System.err.println("IOException");
        }
        String transcription = "";
        try {
            transcription = whisperHandler.transcribe();
        }
        catch (IOException e1) {
            System.err.println("IOException");
        }
        catch (URISyntaxException e2){
                System.err.println("URISyntaxException");
        }
        assertEquals( "Dinner", transcription);
        createHandler.getRecipe().setIngredients(transcription);
        assertEquals("Dinner", createHandler.getRecipe().getIngredients());
    }

    @Test
    // tests a rerecord situation, where the user doesn't like the result of the first
    // record they did and rerecord again
    public void storyTestS4Rerecord() {
        // first record
        try {
                recordHandler.record();
            }
        catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
        }
        File file = new File("src/main/java/View/PantryPal/recording.wav");
        assertEquals(true, file.exists());

        // second record
        try {
                recordHandler.record();
            }
        catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
        }
        assertEquals(true, file.exists());

        // Server not active during JUnit, so we "simulate" recording.wav being transferred up

        File audioFile = new File("src/main/java/Model/PantryPal/recording.wav");

         // the number of samples of audio per second.
        // 44100 represents the typical sample rate for CD-quality audio.
        float sampleRate = 44100;

        // the number of bits in each sample of a sound that has been digitized.
        int sampleSizeInBits = 16;

        // the number of audio channels in this format (1 for mono, 2 for stereo).
        int channels = 1;

        // whether the data is signed or unsigned.
        boolean signed = true;

        // whether the audio data is stored in big-endian or little-endian order.
        boolean bigEndian = false;

        AudioFormat audioFormat = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);

        // creates an empty audioInputStream
        AudioInputStream audioInputStream = new AudioInputStream(null, audioFormat, 0);

        try {
            AudioSystem.write(
                        audioInputStream,
                        AudioFileFormat.Type.WAVE,
                        audioFile);
        }
        catch (IOException e1){
            System.err.println("IOException");
        }
        String transcription = "";
        try {
            transcription = whisperHandler.transcribe();
        }
        catch (IOException e1) {
            System.err.println("IOException");
        }
        catch (URISyntaxException e2){
                System.err.println("URISyntaxException");
        }
        assertEquals( "Dinner", transcription);
        createHandler.getRecipe().setIngredients(transcription);
        assertEquals("Dinner", createHandler.getRecipe().getIngredients());
    }

    // Story 5, test that after you press the continue button on the ingredients page
    // it begins to generate a recipe from the info earlier given
    // here we assume that we created a blank recipe and filled in the mealtype and
    // ingredients as we went, like in the actual app

    @Test
    public void unitTestS5Generate() {
        // simulates us setting the recipe's meal type when user says mealtype
        createHandler.getRecipe().setMealType("Lunch");
        // simulates us setting the recipe's ingredients when user says mealtype
        createHandler.getRecipe().setIngredients("food");

        Recipe r = createHandler.getRecipe();

        String mealtype = r.getMealType();
        String ingredients = r.getIngredients();

        // generate our instructions and titles
        String recipe = gptHandler.generate(mealtype, ingredients);
        // extract the title from instructions
        String title = recipe.substring(0,recipe.indexOf("~"));
        //take out the newlines and returns for formatting
        String strippedString = title.replaceAll("[\\n\\r]+", "");
        // set the instructions and title
        r.setInstructions(recipe);
        r.setTitle(strippedString);
        // check whether the display of title is correct
        assertEquals("Diet Plan ", createHandler.getRecipe().getTitle());
        // check whether the display of ingredients is correct
        assertEquals("Ingredients: food","Ingredients: " +createHandler.getRecipe().getIngredients());
        // check whether the display of the instructions is correct
        assertEquals("Instructions: Diet Plan ~ Maybe you should just go on a diet.","Instructions: " +createHandler.getRecipe().getInstructions());
    }

    // Story 5, test that after you press the save button, it not only updates recipeList to include
    // the new recipe, but also adds it to the save.csv

    @Test
    public void unitTestS5Save() {
        // precreate our recipe, assuming we generated it correctly earlier after feeding to ChatGPT
        // below info for the recipe is from an actual ChatGPT and whisper response in save.csv
        createHandler.getRecipe().setTitle("BLT Sandwich");
        createHandler.getRecipe().setMealType("Lunch.");
        createHandler.getRecipe().setIngredients("Bacon lettuce tomatoes white bread mayonnaise.");

        createHandler.getRecipe().setInstructions("BLT Sandwich~  1. Toast the white bread. 2. " +
        "Spread mayonnaise on one side of each piece of toast. 3. Layer the bacon lettuce and tomato in between " +

        "the two pieces of toast.  4. Cut the sandwich in half and serve.");
        // add the recipe to both recipeList and save.csv
        //rHandler.addRecipe(createHandler.getRecipe(), usernameTest1);
        m.put(usernameTest1, createHandler.getRecipe().getTitle(), createHandler.getRecipe().getMealType(), createHandler.getRecipe().getIngredients(), createHandler.getRecipe().getInstructions());

        //get user list from mongo and replace current one
        ArrayList<Recipe> replace = update();
        rList.setList(replace);

        // check whether recipeList was updated
        //assertEquals(rHandler.getRecipeList(usernameTest1).getList().size(), 1);
        assertEquals(1, collection1.countDocuments());
        // check whether the save.csv is there
        File file = new File("save.csv");
        assertEquals(true, file.exists());
        // check the contents of save.csv
        String csvFile = "./save.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                // split for comma
                String[] fields = line.split(",");

                // get each field
                if (fields.length >= 4) {
                    String title = fields[0].trim();
                    assertEquals("BLT Sandwich", title);
                    String mealtype = fields[1].trim();
                    assertEquals("Lunch.", mealtype);
                    String ingredients = fields[2].trim();
                    assertEquals("Bacon lettuce tomatoes white bread mayonnaise.", ingredients);
                    String instructions = fields[3].trim();

                    assertEquals("BLT Sandwich~  1. Toast the white bread. 2. " +
                        "Spread mayonnaise on one side of each piece of toast. 3. Layer the bacon lettuce and tomato in between " +
                        "the two pieces of toast.  4. Cut the sandwich in half and serve.", instructions);
                }
            }

        } catch (IOException e) {
            System.out.println("No File found");
            e.printStackTrace();
        }
    }


    @Test
    // tests when the user generates the recipe and decides to save
    public void storyTestS5Save() {
        // simulates us setting the recipe's meal type when user says mealtype
        createHandler.getRecipe().setMealType("Lunch");
        // simulates us setting the recipe's ingredients when user says mealtype
        createHandler.getRecipe().setIngredients("food");

        Recipe r = createHandler.getRecipe();

        String mealtype = r.getMealType();
        String ingredients = r.getIngredients();

        // generate our instructions and titles
        String recipe = gptHandler.generate(mealtype, ingredients);
        // extract the title from instructions
        String title = recipe.substring(0,recipe.indexOf("~"));
        //take out the newlines and returns for formatting
        String strippedString = title.replaceAll("[\\n\\r]+", "");
        // set the instructions and title
        r.setInstructions(recipe);
        r.setTitle(strippedString);
        // check whether the display of title is correct
        assertEquals("Diet Plan ", createHandler.getRecipe().getTitle());
        // check whether the display of ingredients is correct
        assertEquals("Ingredients: food","Ingredients: " +createHandler.getRecipe().getIngredients());
        // check whether the display of the instructions is correct
        assertEquals("Instructions: Diet Plan ~ Maybe you should just go on a diet.","Instructions: " +createHandler.getRecipe().getInstructions());


        // add the recipe to both recipeList and save.csv
        //rHandler.addRecipe(createHandler.getRecipe(), usernameTest1);
        m.put(usernameTest1, createHandler.getRecipe().getTitle(), createHandler.getRecipe().getMealType(), createHandler.getRecipe().getIngredients(), createHandler.getRecipe().getInstructions());

        //get user list from mongo and replace current one
        ArrayList<Recipe> replace = update();
        rList.setList(replace);
        // check whether recipeList was updated
        //assertEquals(rHandler.getRecipeList(usernameTest1).getList().size(), 1);
        assertEquals(1, collection1.countDocuments());
        // check whether the save.csv is there
        File file = new File("save.csv");
        assertEquals(true, file.exists());
        // check the contents of save.csv
        String csvFile = "./save.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                // split for comma
                String[] fields = line.split(",");

                // get each field
                if (fields.length >= 4) {
                    String title2 = fields[0].trim();
                    assertEquals("Diet Plan", title2);
                    String mealtype2 = fields[1].trim();
                    assertEquals("Lunch", mealtype2);
                    String ingredients2 = fields[2].trim();
                    assertEquals("food", ingredients2);
                    String instructions2 = fields[3].trim();
                    assertEquals("Diet Plan ~ Maybe you should just go on a diet.", instructions2);

                }

            }
        } catch (IOException e) {
            System.out.println("No File found");
            e.printStackTrace();
        }
    }




    @Test
    // tests when the user generates the recipe and but doesn't save (cancel)
    public void storyTestS5Cancel() {
        // simulates us setting the recipe's meal type when user says mealtype
        createHandler.getRecipe().setMealType("Lunch");
        // simulates us setting the recipe's ingredients when user says mealtype
        createHandler.getRecipe().setIngredients("food");

        Recipe r = createHandler.getRecipe();

        String mealtype = r.getMealType();
        String ingredients = r.getIngredients();

        // generate our instructions and titles
        String recipe = gptHandler.generate(mealtype, ingredients);
        // extract the title from instructions
        String title = recipe.substring(0,recipe.indexOf("~"));
        //take out the newlines and returns for formatting
        String strippedString = title.replaceAll("[\\n\\r]+", "");
        // set the instructions and title
        r.setInstructions(recipe);
        r.setTitle(strippedString);
        // check whether the display of title is correct
        assertEquals("Diet Plan ", createHandler.getRecipe().getTitle());
        // check whether the display of ingredients is correct
        assertEquals("Ingredients: food","Ingredients: " +createHandler.getRecipe().getIngredients());
        // check whether the display of the instructions is correct
        assertEquals("Instructions: Diet Plan ~ Maybe you should just go on a diet.","Instructions: " +createHandler.getRecipe().getInstructions());

        // don't add it in

        // check whether recipeList was not updated
        assertEquals(rList.size(), 0);
    }


    @Test
    public void storyTestS6EditRecipe() {

        String title = "Omelet";
        String mealType = "Breakfast";
        String oldIngredients = "Eggs, Cheese";
        String oldInstructions = "1. Heat pan 2. Crack Eggs 3. Flip 4. Wait 5. Eat";
        String newIngredients = "Eggs, Cheese, Milk";
        String newInstructions = "1. Heat pan 2. Crack Eggs 3. Pour Milk in Pan 4. Flip 5. Wait 6. Eat";
        Recipe oldRecipe = new Recipe(title, mealType, oldIngredients, oldInstructions);
        Recipe newRecipe = new Recipe(title, mealType, newIngredients, newInstructions);
        //rHandler.addRecipe(oldRecipe, usernameTest1);
        m.put(usernameTest1, title, mealType, oldIngredients, oldInstructions);

        //get user list from mongo and replace current one
        ArrayList<Recipe> replace = update();
        rList.setList(replace);

        // edit oldRecipe with newInstructions and newIngredients
        //rHandler.editRecipe(oldRecipe, newIngredients, newInstructions, usernameTest1);
        m.post(usernameTest1, title, mealType, newIngredients, newInstructions, "0");
        //check if ingredients changed
        replace = update();
        rList.setList(replace);

        //assertEquals(1, rHandler.getRecipeList(usernameTest1).getList().size());

        // check whether the oldRecipe is updated properly, comparing it to the newRecipe we want
        assertEquals(rList.get(title).getIngredients(), newIngredients);
        assertEquals(rList.get(title).getInstructions(), newInstructions);
    }


    @Test
    public void storyTestS7DeleteRecipe() {

        String title1 = "Test Recipe 1";
        String mealtype = "Lunch";
        String ingredients = "food";
        String instructions = "cook food";
        Recipe r1= new Recipe(title1, mealtype, ingredients, instructions);
        //rHandler.addRecipe(r1, usernameTest1);
        m.put(usernameTest1, title1, mealtype, ingredients, instructions);

        //get user list from mongo and replace current one
        ArrayList<Recipe> replace = update();
        rList.setList(replace);
        //add two and check if you can get title, maybe 2 messes it up
        //assertEquals(rHandler.getRecipeList(usernameTest1).getList().size(), 1);
        assertEquals(1, collection1.countDocuments());
        //rHandler.deleteRecipe(r1.getIndex(), usernameTest1);
        m.delete(usernameTest1, "0");
        replace = update();
        rList.setList(replace);
        //assertEquals(rHandler.getRecipeList(usernameTest1).getList().size(), 0);
        assertEquals(0, collection1.countDocuments());
    }

    @Test
    // integration test of stories 4-7
    // Story 4: Ingredients
    // Story 5: Save
    // Story 6: Edit
    // Story 7: Delete

    public void integrationTest4() {
        // Story 4: Ingredients
        try {
                recordHandler.record();
            }
        catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
        }
        File file = new File("src/main/java/Model/PantryPal/recording.wav");
        assertEquals(true, file.exists());
        File audioFile = new File("src/main/java/Model/PantryPal/recording.wav");

         // the number of samples of audio per second.
        // 44100 represents the typical sample rate for CD-quality audio.
        float sampleRate = 44100;

        // the number of bits in each sample of a sound that has been digitized.
        int sampleSizeInBits = 16;

        // the number of audio channels in this format (1 for mono, 2 for stereo).
        int channels = 1;

        // whether the data is signed or unsigned.
        boolean signed = true;

        // whether the audio data is stored in big-endian or little-endian order.
        boolean bigEndian = false;

        AudioFormat audioFormat = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);

        // creates an empty audioInputStream
        AudioInputStream audioInputStream = new AudioInputStream(null, audioFormat, 0);

        try {
            AudioSystem.write(
                        audioInputStream,
                        AudioFileFormat.Type.WAVE,
                        audioFile);
        }
        catch (IOException e1){
            System.err.println("IOException");
        }
        String transcription = "";
        try {
            transcription = whisperHandler.transcribe();
        }
        catch (IOException e1) {
            System.err.println("IOException");
        }
        catch (URISyntaxException e2){
                System.err.println("URISyntaxException");
        }
        assertEquals( "Dinner", transcription);
        createHandler.getRecipe().setIngredients(transcription);
        assertEquals("Dinner", createHandler.getRecipe().getIngredients());

        // Story 5: Save
        // simulates us setting the recipe's meal type when user says mealtype
        createHandler.getRecipe().setMealType("Lunch");

        Recipe r = createHandler.getRecipe();

        String mealtype = r.getMealType();
        String ingredients = r.getIngredients();

        // generate our instructions and titles
        String recipe = gptHandler.generate(mealtype, ingredients);
        // extract the title from instructions
        String title = recipe.substring(0,recipe.indexOf("~"));
        //take out the newlines and returns for formatting
        String strippedString = title.replaceAll("[\\n\\r]+", "");
        // set the instructions and title
        r.setInstructions(recipe);
        r.setTitle(strippedString);
        // check whether the display of title is correct
        assertEquals("Diet Plan ", createHandler.getRecipe().getTitle());
        // check whether the display of ingredients is correct
        assertEquals("Ingredients: Dinner","Ingredients: " +createHandler.getRecipe().getIngredients());
        // check whether the display of the instructions is correct
        assertEquals("Instructions: Diet Plan ~ Maybe you should just go on a diet.","Instructions: " +createHandler.getRecipe().getInstructions());


        // add the recipe to both recipeList and save.csv
        //rHandler.addRecipe(createHandler.getRecipe(), usernameTest1); //this adds to documents
        m.put(usernameTest1, createHandler.getRecipe().getTitle(), createHandler.getRecipe().getMealType(), createHandler.getRecipe().getIngredients(), createHandler.getRecipe().getInstructions());
        ArrayList<Recipe> replace = update();
        rList.setList(replace);

        // check whether recipeList was updated
        //assertEquals(rHandler.getRecipeList(usernameTest1).getList().size(), 1);
        assertEquals(1, collection1.countDocuments());
        // check whether the save.csv is there
        File file2= new File("save.csv");
        assertEquals(true, file2.exists());
        // check the contents of save.csv
        String csvFile = "./save.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                // split for comma
                String[] fields = line.split(",");

                // get each field
                if (fields.length >= 4) {
                    String title2 = fields[0].trim();
                    assertEquals("Diet Plan", title2);
                    String mealtype2 = fields[1].trim();
                    assertEquals("Lunch", mealtype2);
                    String ingredients2 = fields[2].trim();
                    assertEquals("Dinner", ingredients2);
                    String instructions2 = fields[3].trim();
                    assertEquals("Diet Plan ~ Maybe you should just go on a diet.", instructions2);

                }

            }
        } catch (IOException e) {
            System.out.println("No File found");
            e.printStackTrace();
        }

        // Story 6: Edit
        Recipe oldRecipe = createHandler.getRecipe();
        String newIngredients = "Eggs, Cheese, Milk";
        String newInstructions = "1. Heat pan 2. Crack Eggs 3. Pour Milk in Pan 4. Flip 5. Wait 6. Eat";
        Recipe newRecipe = new Recipe(strippedString, "Lunch", newIngredients, newInstructions);

        // edit oldRecipe with newInstructions and newIngredients
        //rHandler.editRecipe(oldRecipe, newIngredients, newInstructions, usernameTest1);
        m.post(usernameTest1, createHandler.getRecipe().getTitle(), createHandler.getRecipe().getMealType(), newIngredients, newInstructions, "0");
        //check if ingredients changed
        replace = update();
        rList.setList(replace);

        assertEquals(1, rList.size());

        // check whether the oldRecipe is updated properly, comparing it to the newRecipe we want
        assertEquals(newIngredients, rList.get(createHandler.getRecipe().getTitle()).getIngredients());
        assertEquals(newInstructions, rList.get(createHandler.getRecipe().getTitle()).getInstructions());

        // assertEquals(newRecipe.getIngredients(), oldRecipe.getIngredients());
        // assertEquals(newRecipe.getInstructions(), oldRecipe.getInstructions());

        //Story 7: Delete
         //add two and check if you can get title, maybe 2 messes it up
        //assertEquals(rList.size(), 1);
        // rHandler.deleteRecipe(newRecipe.getIndex(), usernameTest1);
        // assertEquals(rList.size(), 0);
        m.delete(usernameTest1, "0");

        //collection should now be empty
        assertEquals(0, collection1.countDocuments());
    }

    // integration test of stories 4-7
    // Story 4: Rerecord
    // Story 5: Cancel
    @Test
    public void integrationTest5() {
        //4-7
        //Re-record -> Cancel
        //Re-record test
        try {
                recordHandler.record();
            }
        catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
        }
        File file = new File("src/main/java/View/PantryPal/recording.wav");
        assertEquals(true, file.exists());

        // second record
        try {
                recordHandler.record();
            }
        catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
        }
        assertEquals(true, file.exists());

        // Server not active during JUnit, so we "simulate" recording.wav being transferred up

        File audioFile = new File("src/main/java/Model/PantryPal/recording.wav");

         // the number of samples of audio per second.
        // 44100 represents the typical sample rate for CD-quality audio.
        float sampleRate = 44100;

        // the number of bits in each sample of a sound that has been digitized.
        int sampleSizeInBits = 16;

        // the number of audio channels in this format (1 for mono, 2 for stereo).
        int channels = 1;

        // whether the data is signed or unsigned.
        boolean signed = true;

        // whether the audio data is stored in big-endian or little-endian order.
        boolean bigEndian = false;

        AudioFormat audioFormat = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);

        // creates an empty audioInputStream
        AudioInputStream audioInputStream = new AudioInputStream(null, audioFormat, 0);

        try {
            AudioSystem.write(
                        audioInputStream,
                        AudioFileFormat.Type.WAVE,
                        audioFile);
        }
        catch (IOException e1){
            System.err.println("IOException");
        }
        String transcription = "";
        try {
            transcription = whisperHandler.transcribe();
        }
        catch (IOException e1) {
            System.err.println("IOException");
        }
        catch (URISyntaxException e2){
                System.err.println("URISyntaxException");
        }
        assertEquals( "Dinner", transcription);
        createHandler.getRecipe().setIngredients(transcription);
        assertEquals("Dinner", createHandler.getRecipe().getIngredients());

        //end of rerocord test
        //cancel test
        //simulates us setting the recipe's meal type when user says mealtype
        createHandler.getRecipe().setMealType("Dinner");
        //simulates us setting the recipe's ingredients when user says mealtype
        //createHandler.getRecipe().setIngredients("Dinner");

        Recipe r = createHandler.getRecipe();

        String mealtype = r.getMealType();
        String ingredients = r.getIngredients();

        // generate our instructions and titles
        String recipe = gptHandler.generate(mealtype, ingredients);
        // extract the title from instructions
        String title = recipe.substring(0,recipe.indexOf("~"));
        //take out the newlines and returns for formatting
        String strippedString = title.replaceAll("[\\n\\r]+", "");
        // set the instructions and title
        r.setInstructions(recipe);
        r.setTitle(strippedString);
        // check whether the display of title is correct
        assertEquals("Diet Plan ", createHandler.getRecipe().getTitle());
        // check whether the display of ingredients is correct
        assertEquals("Ingredients: Dinner","Ingredients: " +createHandler.getRecipe().getIngredients());
        // check whether the display of the instructions is correct
        assertEquals("Instructions: Diet Plan ~ Maybe you should just go on a diet.","Instructions: " +createHandler.getRecipe().getInstructions());

        // don't add it in

        // check whether recipeList was not updated
        assertEquals(rList.size(), 0);

        //end of cancel test
    }

    // scenario based test of stories 1-7 (integration 2 and integration 4)
    // Story 1: (inserted it before hand but sneakily) Exist recipe
    // Story 2: Expand details of recipe (just make sure that recipe details match)
    // Story 3: Rerecord
    // Story 4: Ingredients
    // Story 5: Save
    // Story 6: Edit
    // Story 7: Delete
    @Test
    public void sbstMS1Test1() {
        // story 1
        //add 2 recipes, 2 should be displayed
        String title = "Test Recipe 1";
        String titleTwo = "Test Recipe 2";
        String mealtype = "Lunch";
        String ingredients = "food";
        String instructions = "cook food";
        Recipe r1= new Recipe(title, mealtype, ingredients, instructions);
        Recipe r2= new Recipe(titleTwo, mealtype, ingredients, instructions);
        // rHandler.addRecipe(r1, usernameTest1);
        // rHandler.addRecipe(r2, usernameTest1);
        m.put(usernameTest1, title, mealtype, ingredients, instructions);
        m.put(usernameTest1, titleTwo, mealtype, ingredients, instructions);
        ArrayList<Recipe> replace = update();
        rList.setList(replace);
        //assertEquals(rHandler.getRecipeList(usernameTest1).getList().size(), 2);
        assertEquals(2, collection1.countDocuments());

        // story 2
        Recipe r = rList.get(title);
        assertEquals(r.getTitle(), "Test Recipe 1");
        assertEquals(r.getMealType(), "Lunch");
        assertEquals(r.getIngredients(), "food");
        assertEquals(r.getInstructions(), "cook food");

        // story 3
        // first record
        try {
                recordHandler.record();
            }
        catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
        }
        File file = new File("src/main/java/View/PantryPal/recording.wav");
        assertEquals(true, file.exists());

        // second record
        try {
                recordHandler.record();
            }
        catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
        }
        assertEquals(true, file.exists());

        // Server not active during JUnit, so we "simulate" recording.wav being transferred up

        File audioFile = new File("src/main/java/Model/PantryPal/recording.wav");

         // the number of samples of audio per second.
        // 44100 represents the typical sample rate for CD-quality audio.
        float sampleRate = 44100;

        // the number of bits in each sample of a sound that has been digitized.
        int sampleSizeInBits = 16;

        // the number of audio channels in this format (1 for mono, 2 for stereo).
        int channels = 1;

        // whether the data is signed or unsigned.
        boolean signed = true;

        // whether the audio data is stored in big-endian or little-endian order.
        boolean bigEndian = false;

        AudioFormat audioFormat = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);

        // creates an empty audioInputStream
        AudioInputStream audioInputStream = new AudioInputStream(null, audioFormat, 0);

        try {
            AudioSystem.write(
                        audioInputStream,
                        AudioFileFormat.Type.WAVE,
                        audioFile);
        }
        catch (IOException e1){
            System.err.println("IOException");
        }
        String transcription = "";
        try {
            transcription = whisperHandler.transcribe();
        }
        catch (IOException e1) {
            System.err.println("IOException");
        }
        catch (URISyntaxException e2){
                System.err.println("URISyntaxException");
        }
        assertEquals( "Dinner", transcription);
        createHandler.getRecipe().setMealType(transcription);
        assertEquals("Dinner", createHandler.getRecipe().getMealType());

        // Story 4: Ingredients
        try {
                recordHandler.record();
            }
        catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
        }

        try {
            AudioSystem.write(
                        audioInputStream,
                        AudioFileFormat.Type.WAVE,
                        audioFile);
        }
        catch (IOException e1){
            System.err.println("IOException");
        }
        String transcription2 = "";
        try {
            transcription2 = whisperHandler.transcribe();
        }
        catch (IOException e1) {
            System.err.println("IOException");
        }
        catch (URISyntaxException e2){
                System.err.println("URISyntaxException");
        }
        assertEquals( "Dinner", transcription2);
        createHandler.getRecipe().setIngredients(transcription2);
        assertEquals("Dinner", createHandler.getRecipe().getIngredients());

        // Story 5: Save
        // simulates us setting the recipe's meal type when user says mealtype
        createHandler.getRecipe().setMealType("Lunch");

        r = createHandler.getRecipe();

        mealtype = r.getMealType();
        ingredients = r.getIngredients();

        // generate our instructions and titles
        String recipe = gptHandler.generate(mealtype, ingredients);
        // extract the title from instructions
        title = recipe.substring(0,recipe.indexOf("~"));
        //take out the newlines and returns for formatting
        String strippedString = title.replaceAll("[\\n\\r]+", "");
        // set the instructions and title
        r.setInstructions(recipe);
        r.setTitle(strippedString);
        // check whether the display of title is correct
        assertEquals("Diet Plan ", createHandler.getRecipe().getTitle());
        // check whether the display of ingredients is correct
        assertEquals("Ingredients: Dinner","Ingredients: " +createHandler.getRecipe().getIngredients());
        // check whether the display of the instructions is correct
        assertEquals("Instructions: Diet Plan ~ Maybe you should just go on a diet.","Instructions: " +createHandler.getRecipe().getInstructions());


        // add the recipe to both recipeList and save.csv
            //rHandler.addRecipe(createHandler.getRecipe(), usernameTest1);
        m.put(usernameTest1, createHandler.getRecipe().getTitle(), createHandler.getRecipe().getMealType(), createHandler.getRecipe().getIngredients(), createHandler.getRecipe().getInstructions());
        replace = update();
        rList.setList(replace);
        // check whether recipeList was updated
            //assertEquals(rHandler.getRecipeList(usernameTest1).getList().size(), 3);
        assertEquals(3, collection1.countDocuments());
        // check whether the save.csv is there
        File file2= new File("save.csv");
        assertEquals(true, file2.exists());
        // check the contents of save.csv
        String csvFile = "./save.csv";
        int i = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                // split for comma
                String[] fields = line.split(",");

                // get each field
                if (fields.length >= 4) {
                    String title2 = fields[0].trim();
                    assertEquals(rList.getList().get(i).getTitle().trim(), title2);
                    String mealtype2 = fields[1].trim();
                    assertEquals(rList.getList().get(i).getMealType(), mealtype2);
                    String ingredients2 = fields[2].trim();
                    assertEquals(rList.getList().get(i).getIngredients(), ingredients2);
                    String instructions2 = fields[3].trim();
                    assertEquals(rList.getList().get(i).getInstructions(), instructions2);
                    i++;
                }

            }
        } catch (IOException e) {
            System.out.println("No File found");
            e.printStackTrace();
        }

        // Story 6: Edit
        Recipe oldRecipe = createHandler.getRecipe();
        //System.out.println(oldRecipe.getTitle() + "ddfdfdfddsfdsfdsfdsfsfdsfsdfsdfsdfsdfsfdsf");
        String newIngredients = "Eggs, Cheese, Milk";
        String newInstructions = "1. Heat pan 2. Crack Eggs 3. Pour Milk in Pan 4. Flip 5. Wait 6. Eat";
        Recipe newRecipe = new Recipe(strippedString, "Lunch", newIngredients, newInstructions);

        // edit oldRecipe with newInstructions and newIngredients
        //rHandler.editRecipe(oldRecipe, newIngredients, newInstructions, usernameTest1);
        m.post(usernameTest1, createHandler.getRecipe().getTitle(), "Lunch", newIngredients, newInstructions, "0");
        //check if ingredients changed
        replace = update();
        rList.setList(replace);

        assertEquals(3, rList.size());

        // check whether the oldRecipe is updated properly, comparing it to the newRecipe we want ????????????????????????????
        assertEquals(rList.get(createHandler.getRecipe().getTitle()).getIngredients(), oldRecipe.getIngredients());
        assertEquals(rList.get(createHandler.getRecipe().getTitle()).getInstructions(), oldRecipe.getInstructions());

        //Story 7: Delete
         //add two and check if you can get title, maybe 2 messes it up
        assertEquals(rList.size(), 3);
        //rHandler.deleteRecipe(r1.getIndex(), usernameTest1);
        m.delete(usernameTest1, "0");
        replace = update();
        rList.setList(replace);

        assertEquals(rList.size(), 2);
    }

    // scenario based test of stories 1-7
    // Story 1: Remove Preexisting recipes
    // Story 3: Record
    // Story 4: Rerecord
    // Story 5: Cancel
    @Test
    public void sbstMS1Test2() {
        // Story 1: Remove Preexisting recipes
        //add 2 recipes, 2 should be displayed
        String title1 = "Test Recipe 1";
        String title2 = "Test Recipe 2";
        String mealtype = "Lunch";
        String ingredients = "food";
        String instructions = "cook food";
        Recipe r1 = new Recipe(title1, mealtype, ingredients, instructions);
        Recipe r2 = new Recipe(title2, mealtype, ingredients, instructions);
        // rHandler.addRecipe(r1, usernameTest1);
        // rHandler.addRecipe(r2, usernameTest1);
        m.put(usernameTest1, title1, mealtype, ingredients, instructions);
        m.put(usernameTest1, title2, mealtype, ingredients, instructions);
        ArrayList<Recipe> replace = update();
        rList.setList(replace);
        //assertEquals(rHandler.getRecipeList(usernameTest1).getList().size(), 2);
        assertEquals(2, collection1.countDocuments());
        //delete
        //rHandler.deleteRecipe(r1.getIndex(), usernameTest1);
        m.delete(usernameTest1, "0");
        replace = update();
        rList.setList(replace);
        //assertEquals(rHandler.getRecipeList(usernameTest1).getList().size(), 1);
        assertEquals(1, collection1.countDocuments());
        //rHandler.deleteRecipe(r2.getIndex(), usernameTest1);
        m.delete(usernameTest1, "1");
        replace = update();
        rList.setList(replace);
        assertEquals(0, collection1.countDocuments());

        // Story 3: Record
        try {
                recordHandler.record();
            }
        catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
        }
        File file = new File("src/main/java/View/PantryPal/recording.wav");
        assertEquals(true, file.exists());

        // Server not active during JUnit, so we "simulate" recording.wav being transferred up

        File audioFile = new File("src/main/java/Model/PantryPal/recording.wav");

         // the number of samples of audio per second.
        // 44100 represents the typical sample rate for CD-quality audio.
        float sampleRate = 44100;

        // the number of bits in each sample of a sound that has been digitized.
        int sampleSizeInBits = 16;

        // the number of audio channels in this format (1 for mono, 2 for stereo).
        int channels = 1;

        // whether the data is signed or unsigned.
        boolean signed = true;

        // whether the audio data is stored in big-endian or little-endian order.
        boolean bigEndian = false;

        AudioFormat audioFormat = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);

        // creates an empty audioInputStream
        AudioInputStream audioInputStream = new AudioInputStream(null, audioFormat, 0);

        try {
            AudioSystem.write(
                        audioInputStream,
                        AudioFileFormat.Type.WAVE,
                        audioFile);
        }
        catch (IOException e1){
            System.err.println("IOException");
        }
        String transcription = "";
        try {
            transcription = whisperHandler.transcribe();
        }
        catch (IOException e1) {
            System.err.println("IOException");
        }
        catch (URISyntaxException e2){
                System.err.println("URISyntaxException");
        }
        assertEquals( "Dinner", transcription);
        createHandler.getRecipe().setMealType(transcription);
        assertEquals("Dinner", createHandler.getRecipe().getMealType());

        // Story 4: Rerecord
        // first record
        try {
                recordHandler.record();
            }
        catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
        }
        file = new File("src/main/java/View/PantryPal/recording.wav");
        assertEquals(true, file.exists());

        // second record
        try {
                recordHandler.record();
            }
        catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
        }
        assertEquals(true, file.exists());

        // Server not active during JUnit, so we "simulate" recording.wav being transferred up

        audioFile = new File("src/main/java/Model/PantryPal/recording.wav");

        try {
            AudioSystem.write(
                        audioInputStream,
                        AudioFileFormat.Type.WAVE,
                        audioFile);
        }
        catch (IOException e1){
            System.err.println("IOException");
        }
        String transcription2 = "";
        try {
            transcription2 = whisperHandler.transcribe();
        }
        catch (IOException e1) {
            System.err.println("IOException");
        }
        catch (URISyntaxException e2){
                System.err.println("URISyntaxException");
        }
        assertEquals( "Dinner", transcription2);
        createHandler.getRecipe().setIngredients(transcription2);
        assertEquals("Dinner", createHandler.getRecipe().getIngredients());

        // Story 5: Cancel

        Recipe r = createHandler.getRecipe();

        mealtype = r.getMealType();
        ingredients = r.getIngredients();

        // generate our instructions and titles
        String recipe = gptHandler.generate(mealtype, ingredients);
        // extract the title from instructions
        String title = recipe.substring(0,recipe.indexOf("~"));
        //take out the newlines and returns for formatting
        String strippedString = title.replaceAll("[\\n\\r]+", "");
        // set the instructions and title
        r.setInstructions(recipe);
        r.setTitle(strippedString);
        // check whether the display of title is correct
        assertEquals("Diet Plan ", createHandler.getRecipe().getTitle());
        // check whether the display of ingredients is correct
        assertEquals("Ingredients: Dinner","Ingredients: " +createHandler.getRecipe().getIngredients());
        // check whether the display of the instructions is correct
        assertEquals("Instructions: Diet Plan ~ Maybe you should just go on a diet.","Instructions: " +createHandler.getRecipe().getInstructions());

        // don't add it in

        // check whether recipeList was not updated
        assertEquals(rList.size(), 0);
    }

    // 1 - No Recipes
    // 3 - Record
    // 4 - Ingredients
    // 5 - Save
    // 2 - Expand
    // 7 - Delete
    @Test
    public void sbstMS1Test3() {
        //Test 1 no recipies
        int initialRecipeCount = rList.size();
        assertEquals(initialRecipeCount, 0);
        //end of 1

        //begin of test 3
        try {
            recordHandler.record();
            }
        catch (IOException e1) {
            e1.printStackTrace();
        }
        File file = new File("src/main/java/View/PantryPal/recording.wav");
        assertEquals(true, file.exists());

        // Server not active during JUnit, so we "simulate" recording.wav being transferred up

        File audioFile = new File("src/main/java/Model/PantryPal/recording.wav");
         // the number of samples of audio per second.
        // 44100 represents the typical sample rate for CD-quality audio.
        float sampleRate = 44100;

        // the number of bits in each sample of a sound that has been digitized.
        int sampleSizeInBits = 16;

        // the number of audio channels in this format (1 for mono, 2 for stereo).
        int channels = 1;

        // whether the data is signed or unsigned.
        boolean signed = true;

        // whether the audio data is stored in big-endian or little-endian order.
        boolean bigEndian = false;

        AudioFormat audioFormat = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);

        // creates an empty audioInputStream
        AudioInputStream audioInputStream = new AudioInputStream(null, audioFormat, 0);

        try {
            AudioSystem.write(
                        audioInputStream,
                        AudioFileFormat.Type.WAVE,
                        audioFile);
        }
        catch (IOException e1){
            System.err.println("IOException");
        }
        String transcription = "";
        try {
            transcription = whisperHandler.transcribe();
        }
        catch (IOException e1) {
            System.err.println("IOException");
        }
        catch (URISyntaxException e2){
                System.err.println("URISyntaxException");
        }

        assertEquals( "Dinner", transcription);
        createHandler.getRecipe().setMealType(transcription);
        assertEquals("Dinner", createHandler.getRecipe().getMealType());

        //end of 3

        //begin of test 4 - Ingredients
        try {
                recordHandler.record();
            }
        catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
        }
        assertEquals(true, file.exists());

        // Server not active during JUnit, so we "simulate" recording.wav being transferred up

        try {
            AudioSystem.write(
                        audioInputStream,
                        AudioFileFormat.Type.WAVE,
                        audioFile);
        }
        catch (IOException e1){
            System.err.println("IOException");
        }
        transcription = "";
        try {
            transcription = whisperHandler.transcribe();
        }
        catch (IOException e1) {
            System.err.println("IOException");
        }
        catch (URISyntaxException e2){
                System.err.println("URISyntaxException");
        }
        assertEquals("Dinner", transcription);
        createHandler.getRecipe().setIngredients(transcription);
        assertEquals("Dinner", createHandler.getRecipe().getIngredients());
        //end of test 4 - ingredients

        //beggining of test 5 - save
         // precreate our recipe, assuming we generated it correctly earlier after feeding to ChatGPT
        // below info for the recipe is from an actual ChatGPT and whisper response in save.csv
        createHandler.getRecipe().setTitle("BLT Sandwich");
        createHandler.getRecipe().setMealType("Lunch.");
        createHandler.getRecipe().setIngredients("Bacon lettuce tomatoes white bread mayonnaise.");
        createHandler.getRecipe().setInstructions("BLT Sandwich~  1. Toast the white bread. 2. " +
        "Spread mayonnaise on one side of each piece of toast. 3. Layer the bacon lettuce and tomato in between " +
        "the two pieces of toast.  4. Cut the sandwich in half and serve.");
        // add the recipe to both recipeList and save.csv
        //rHandler.addRecipe(createHandler.getRecipe(), usernameTest1);
        //add a new recipe
        m.put(usernameTest1, createHandler.getRecipe().getTitle(), createHandler.getRecipe().getMealType(), createHandler.getRecipe().getIngredients(), createHandler.getRecipe().getInstructions());

        //get user list from mongo and replace current one
        ArrayList<Recipe> replace = update();
        rList.setList(replace);
        // check whether recipeList was updated
        //assertEquals(rHandler.getRecipeList(usernameTest1).getList().size(), 1);
        assertEquals(1, collection1.countDocuments());

        // check whether the save.csv is there
        File csvfile = new File("save.csv");
        assertEquals(true, csvfile.exists());
        // check the contents of save.csv
        String csvOutput = "./save.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(csvOutput))) {
            String line;
            while ((line = br.readLine()) != null) {
                // split for comma
                String[] fields = line.split(",");
                // get each field
                if (fields.length >= 4) {
                    String title = fields[0].trim();
                    assertEquals("BLT Sandwich", title);
                    String mealtype = fields[1].trim();
                    assertEquals("Lunch.", mealtype);
                    String ingredients = fields[2].trim();
                    assertEquals("Bacon lettuce tomatoes white bread mayonnaise.", ingredients);
                    String instructions = fields[3].trim();
                    assertEquals("BLT Sandwich~  1. Toast the white bread. 2. " +
                        "Spread mayonnaise on one side of each piece of toast. 3. Layer the bacon lettuce and tomato in between " +
                        "the two pieces of toast.  4. Cut the sandwich in half and serve.", instructions);
                }
            }
        } catch (IOException e) {
            System.out.println("No File found");
            e.printStackTrace();
        }
        //end of story 5 - save

        // story 2
        assertEquals(createHandler.getRecipe().getTitle(), "BLT Sandwich");
        assertEquals(createHandler.getRecipe().getMealType(), "Lunch.");
        assertEquals(createHandler.getRecipe().getIngredients(), "Bacon lettuce tomatoes white bread mayonnaise.");
        assertEquals(createHandler.getRecipe().getInstructions(), "BLT Sandwich~  1. Toast the white bread. 2. " +
                        "Spread mayonnaise on one side of each piece of toast. 3. Layer the bacon lettuce and tomato in between " +
                        "the two pieces of toast.  4. Cut the sandwich in half and serve.");


        //beggining of story 7 - Delete
        //add two and check if you can get title, maybe 2 messes it up
        assertEquals(1, collection1.countDocuments());

        //rHandler.deleteRecipe(createHandler.getRecipe().getIndex(), usernameTest1);
        m.delete(usernameTest1, "0");
        replace = update();
        rList.setList(replace);
        assertEquals(0, collection1.countDocuments());
        //end of 7 - Delete
    }

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
            m.put(usernameTest1, title, mealType, ingredients, instructions);

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
            m.post(usernameTest1, title, mealType, "two hot dogs", instructions, "0");
            //check if ingredients changed
            replace = update();
            rList.setList(replace);
            assertEquals("two hot dogs", rList.get(title).getIngredients());

            //delete the recipe
            m.delete(usernameTest1, "0");

            //collection should now be empty
            assertEquals(0, collection1.countDocuments());
        }

        @Test
        //user adds, edits, and deletes a recipe
        public void UnitTestF1Add() {
            //login is ui and cannot be tested, server cannot be tested
            //thus we test the mongodb methods that are being called

            String title = "test1";
            String mealType = "Breakfast";
            String ingredients = "Hot dog";
            String instructions = "cook food";
            Recipe r1 = new Recipe(title, mealType, ingredients, instructions);

            //add a new recipe
            m.put(usernameTest1, title, mealType, ingredients, instructions);

            //get user list from mongo and replace current one
            ArrayList<Recipe> replace = update();
            rList.setList(replace);

            System.out.println(replace.toString());

            Recipe r2 = rList.get(title);
            assertEquals(r1.getTitle(), r2.getTitle());
            assertEquals(r1.getMealType(), r2.getMealType());
            assertEquals(r1.getIngredients(), r2.getIngredients());
            assertEquals(r1.getInstructions(), r2.getInstructions());
        }

        @Test
        //user edits a recipe
        public void UnitTestF1Edit() {
            //login is ui and cannot be tested, server cannot be tested
            //thus we test the mongodb methods that are being called

            String title = "test1";
            String mealType = "Breakfast";
            String ingredients = "Hot dog";
            String instructions = "cook food";
            Recipe r1 = new Recipe(title, mealType, ingredients, instructions);

            //add a new recipe
            m.put(usernameTest1, title, mealType, ingredients, instructions);

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
            m.post(usernameTest1, title, mealType, "two hot dogs", instructions, "0");
            //check if ingredients changed
            replace = update();
            rList.setList(replace);
            assertEquals("two hot dogs", rList.get(title).getIngredients());

            //delete the recipe
            m.delete(usernameTest1, "0");

            //collection should now be empty
            assertEquals(0, collection1.countDocuments());
        }

        @Test
        //user edits a recipe
        public void UnitTestF1Delete() {
            //login is ui and cannot be tested, server cannot be tested
            //thus we test the mongodb methods that are being called

            String title = "test1";
            String mealType = "Breakfast";
            String ingredients = "Hot dog";
            String instructions = "cook food";
            Recipe r1 = new Recipe(title, mealType, ingredients, instructions);

            //add a new recipe
            m.put(usernameTest1, title, mealType, ingredients, instructions);

            //get user list from mongo and replace current one
            ArrayList<Recipe> replace = update();
            rList.setList(replace);

            System.out.println(replace.toString());

            Recipe r2 = rList.get(title);
            assertEquals(r1.getTitle(), r2.getTitle());
            assertEquals(r1.getMealType(), r2.getMealType());
            assertEquals(r1.getIngredients(), r2.getIngredients());
            assertEquals(r1.getInstructions(), r2.getInstructions());

            //delete the recipe
            m.delete(usernameTest1, "0");

            //collection should now be empty
            assertEquals(0, collection1.countDocuments());
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

        //Feature 2, automatic login
        @Test
        //user remembers password, loads in again later
        public void StoryTestF2() {
            //login is ui and cannot be tested, server cannot be tested
            //thus we test the actual csv methods that are being called

            //no unit tests for these because you test save by calling load and vice versa
            //so doing one for load would just be the same as this
            delCSV();
            ArrayList<String> details = AutoLogin.load();
            //this should be empty as there is no csv
            AutoLogin.save(usernameTest1, passwordTest1);
            //load checks if there is a csv, and also reads from it, so load should work
            details = AutoLogin.load();
            assertEquals(usernameTest1, details.get(0));
            assertEquals(passwordTest1, details.get(1));
            //clear csv
            delCSV();
        }

        @Test
        //user remembers password, loads in again later
        public void UnitTestF2Del() {
            //test deleting of the csv
            delCSV();
            ArrayList<String> details = AutoLogin.load();
            //this should be empty as there is no csv
            AutoLogin.save(usernameTest1, passwordTest1);
            //load checks if there is a csv, and also reads from it, so load should work
            details = AutoLogin.load();
            assertEquals(usernameTest1, details.get(0));
            assertEquals(passwordTest1, details.get(1));
            //clear csv

            String filePath = "./users.csv";
            File file = new File(filePath);
            //should exist before, then not
            assertTrue(file.exists());
            AutoLogin.clear();
            assertFalse(file.exists());
        }

        @Test//testing of things that filter the recipeList
        public void StoryTestF5() {
            ArrayList<Recipe> testList = new ArrayList<>();
            ArrayList<Recipe> filteredList = new ArrayList<>();
            Recipe r1 = new Recipe("Hot Dog", "Lunch", "hot dogs", "cook");
            Recipe r2 = new Recipe("BLT", "Dinner", "blt", "cook");
            Recipe r3 = new Recipe("Cereal", "Breakfast", "cereal", "put in milk");
            testList.add(r1);
            testList.add(r2);
            testList.add(r3);

            filteredList = FilterHandler.filterMealType(testList, "Breakfast");
            //Should only be one Breakfast item populated in the list
            assertEquals("Cereal", filteredList.get(0).getTitle());
            assertEquals(1, filteredList.size());

            filteredList = FilterHandler.filterMealType(testList, "Lunch");
            //Should only be one Lunch item populated in the list
            assertEquals("Hot Dog", filteredList.get(0).getTitle());
            assertEquals(1, filteredList.size());

            filteredList = FilterHandler.filterMealType(testList, "Dinner");
            //Should only be one Dinner item populated in the list
            assertEquals("BLT", filteredList.get(0).getTitle());
            assertEquals(1, filteredList.size());

            filteredList = FilterHandler.filterMealType(testList, "All");
            //All recipes should populate the list
            assertEquals("Hot Dog",filteredList.get(0).getTitle());
            assertEquals("BLT", filteredList.get(1).getTitle());
            assertEquals("Cereal", filteredList.get(2).getTitle());
            assertEquals(3, filteredList.size());


        }

        @Test
        //testing Breakfast filter
        public void UnitTestF5BreakfastFilter() {
            ArrayList<Recipe> testList = new ArrayList<>();
            ArrayList<Recipe> filteredList = new ArrayList<>();
            Recipe r1 = new Recipe("Hot Dog", "Lunch", "hot dogs", "cook");
            Recipe r2 = new Recipe("BLT", "Dinner", "blt", "cook");
            Recipe r3 = new Recipe("Cereal", "Breakfast", "cereal", "put in milk");
            Recipe r4 = new Recipe("Toast", "Breakfast", "toast", "cook" );
            testList.add(r1);
            testList.add(r2);
            testList.add(r3);
            testList.add(r4);

            filteredList = FilterHandler.filterMealType(testList, "Breakfast");
            assertEquals("Cereal", filteredList.get(0).getTitle());
            assertEquals("Toast", filteredList.get(1).getTitle());
            assertEquals(2,filteredList.size());
        }

         @Test
        //testing Breakfast filter
        public void UnitTestF5LunchFilter() {
            ArrayList<Recipe> testList = new ArrayList<>();
            ArrayList<Recipe> filteredList = new ArrayList<>();
            Recipe r1 = new Recipe("Hot Dog", "Lunch", "hot dogs", "cook");
            Recipe r2 = new Recipe("BLT", "Dinner", "blt", "cook");
            Recipe r3 = new Recipe("Cereal", "Breakfast", "cereal", "put in milk");
            Recipe r4 = new Recipe("Tomato Soup", "Lunch", "tomato", "cook" );
            testList.add(r1);
            testList.add(r2);
            testList.add(r3);
            testList.add(r4);

            filteredList = FilterHandler.filterMealType(testList, "Lunch");
            assertEquals("Hot Dog", filteredList.get(0).getTitle());
            assertEquals("Tomato Soup", filteredList.get(1).getTitle());
            assertEquals(2,filteredList.size());
        }

         @Test
        //testing Breakfast filter
        public void UnitTestF5DinnerFilter() {
            ArrayList<Recipe> testList = new ArrayList<>();
            ArrayList<Recipe> filteredList = new ArrayList<>();
            Recipe r1 = new Recipe("Hot Dog", "Lunch", "hot dogs", "cook");
            Recipe r2 = new Recipe("BLT", "Dinner", "blt", "cook");
            Recipe r3 = new Recipe("Cereal", "Breakfast", "cereal", "put in milk");
            Recipe r4 = new Recipe("Pizza", "Dinner", "pizza", "cook" );
            testList.add(r1);
            testList.add(r2);
            testList.add(r3);
            testList.add(r4);

            filteredList = FilterHandler.filterMealType(testList, "Dinner");
            assertEquals("BLT", filteredList.get(0).getTitle());
            assertEquals("Pizza", filteredList.get(1).getTitle());
            assertEquals(2,filteredList.size());
        }

         @Test
        //testing Breakfast filter
        public void UnitTestF5AllFilter() {
            ArrayList<Recipe> testList = new ArrayList<>();
            ArrayList<Recipe> filteredList = new ArrayList<>();
            Recipe r1 = new Recipe("Hot Dog", "Lunch", "hot dogs", "cook");
            Recipe r2 = new Recipe("BLT", "Dinner", "blt", "cook");
            Recipe r3 = new Recipe("Cereal", "Breakfast", "cereal", "put in milk");
            Recipe r4 = new Recipe("Toast", "Breakfast", "toast", "cook" );
            testList.add(r1);
            testList.add(r2);
            testList.add(r3);
            testList.add(r4);

            filteredList = FilterHandler.filterMealType(testList, "All");
            assertEquals("Hot Dog", filteredList.get(0).getTitle());
            assertEquals("BLT", filteredList.get(1).getTitle());
            assertEquals("Cereal", filteredList.get(2).getTitle());
            assertEquals("Toast", filteredList.get(3).getTitle());
            assertEquals(4,filteredList.size());
        }
        // Feature 7, unit test to make sure buildHTML method works
        @Test
        public void unitTestF7build() {
            String title1 = "Test Recipe 1";
            String mealtype = "Lunch";
            String ingredients = "food";
            String instructions = "cook food";

            //used in f7 tests
            String banana = "https://upload.wikimedia.org/wikipedia/commons/8/8a/Banana-Single.jpg";

            // the HTML content you see below should match the info from above
            HTMLBuilder htmlB = new HTMLBuilder(title1, mealtype,ingredients, instructions, banana);

            assertEquals(htmlB.buildHTML().toString(), "<html><body><h1>Title: Test Recipe 1<br>Meal Type: Lunch<br>Ingredients: food<br>Instructions: cook food<br><img src=https://upload.wikimedia.org/wikipedia/commons/8/8a/Banana-Single.jpg><br></h1></body></html>");
        }

        // Feature 7, demonstrating that you can see a recipe from a shared link
        @Test
        public void StoryTestF7Share() {
            // a recipe was added to user1
            String title1 = "Test Recipe 1";
            String mealtype = "Lunch";
            String ingredients = "food";
            String instructions = "cook food";
            m.put(usernameTest1, title1, mealtype, ingredients, instructions);
            assertEquals(1, collection1.countDocuments());

            // the user has shared a link to their friend and the friend uses that link to search up the recipe
            // in their browser
            // we'll assume the query is correct and the username and index is extracted out
            String username = usernameTest1;
            // is 0 because it's the first recipe ever made, also is string because we extract it out
            String index = "0";

            // we'll simulate getting the recipe info from the database

            String recipeString = m.getRecipe(username, index);

            // we'll extract out the parts from the returned JSON and see if they match
            // this demonstrates the other person can see the info of the recipe
            JSONObject recipeJSON = new JSONObject(recipeString);
            String testtitle = recipeJSON.getString("title");
            String testmealType = recipeJSON.getString("mealType");
            String testingredients = recipeJSON.getString("ingredients");
            String testinstructions = recipeJSON.getString("instructions");

                        //used in f7 tests
            String banana = "https://upload.wikimedia.org/wikipedia/commons/8/8a/Banana-Single.jpg";

            // the HTML content you see below should match the info from above
            HTMLBuilder htmlB = new HTMLBuilder(title1, mealtype,ingredients, instructions, banana);
            HTMLBuilder htmlB2 = new HTMLBuilder(testtitle, testmealType, testingredients, testinstructions, banana);

            assertEquals(htmlB.buildHTML().toString(), htmlB2.buildHTML().toString());

            // String recipeString = m.getRecipe(username, index);

            // // we'll extract out the parts from the returned JSON and see if they match
            // // this demonstrates the other person can see the info of the recipe
            // JSONObject recipeJSON = new JSONObject(recipeString);
            // String testtitle = recipeJSON.getString("title");
            // String testmealType = recipeJSON.getString("mealType");
            // String testingredients = recipeJSON.getString("ingredients");
            // String testinstructions = recipeJSON.getString("instructions");
            // assertEquals(title1, testtitle);
            // assertEquals(mealtype, testmealType);
            // assertEquals(ingredients, testingredients);
            // assertEquals(instructions, testinstructions);
        }

        // Feature 7, demonstrating you can't access a shared recipe when it's deleted
        @Test
        public void StoryTestF7Delete() {
            // a recipe was added to user1
            String title1 = "Test Recipe 1";
            String mealtype = "Lunch";
            String ingredients = "food";
            String instructions = "cook food";
            m.put(usernameTest1, title1, mealtype, ingredients, instructions);
            assertEquals(1, collection1.countDocuments());

            // the user has shared a link to their friend and the friend uses that link to search up the recipe
            // in their browser
            // we'll assume the query is correct and the username and index is extracted out
             // the HTML content you see below should match the info from above
             String username = usernameTest1;
             String index = "0";

             // we'll simulate getting the recipe info from the database

            String recipeString = m.getRecipe(username, index);

            // we'll extract out the parts from the returned JSON and see if they match
            // this demonstrates the other person can see the info of the recipe
            JSONObject recipeJSON = new JSONObject(recipeString);
            String testtitle = recipeJSON.getString("title");
            String testmealType = recipeJSON.getString("mealType");
            String testingredients = recipeJSON.getString("ingredients");
            String testinstructions = recipeJSON.getString("instructions");

            //used in f7 tests
            String banana = "https://upload.wikimedia.org/wikipedia/commons/8/8a/Banana-Single.jpg";

             HTMLBuilder htmlB = new HTMLBuilder(title1, mealtype,ingredients, instructions, banana);
            HTMLBuilder htmlB2 = new HTMLBuilder(testtitle, testmealType, testingredients, testinstructions, banana);

            assertEquals(htmlB.buildHTML().toString(), htmlB2.buildHTML().toString());


            // now we'll delete the recipe
            m.delete(usernameTest1, "0");
            assertEquals(0, collection1.countDocuments());

            // now we'll see if we can see the recipe if we try the link again (we shouldn't)
            // because we can't get it, that means our code can't create the HTML page and display an error
            recipeString = m.getRecipe(username, index);
            assertEquals("{}", recipeString);
        }

        // Feature 7, demonstrating changes to a shared recipe when edited in the app
        @Test
        public void StoryTestF7Edit() {
            // a recipe was added to user1
            String title1 = "Test Recipe 1";
            String mealtype = "Lunch";
            String ingredients = "food";
            String instructions = "cook food";
            m.put(usernameTest1, title1, mealtype, ingredients, instructions);
            assertEquals(1, collection1.countDocuments());

            // the user has shared a link to their friend and the friend uses that link to search up the recipe
            // in their browser
            // we'll assume the query is correct and the username and index is extracted out
            String username = usernameTest1;
            // is 0 because it's the first recipe ever made, also is string because we extract it out
            String index = "0";
             // we'll simulate getting the recipe info from the database

            String recipeString = m.getRecipe(username, index);

            // we'll extract out the parts from the returned JSON and see if they match
            // this demonstrates the other person can see the info of the recipe
            JSONObject recipeJSON = new JSONObject(recipeString);
            String testtitle = recipeJSON.getString("title");
            String testmealType = recipeJSON.getString("mealType");
            String testingredients = recipeJSON.getString("ingredients");
            String testinstructions = recipeJSON.getString("instructions");

                        //used in f7 tests
            String banana = "https://upload.wikimedia.org/wikipedia/commons/8/8a/Banana-Single.jpg";

             HTMLBuilder htmlB = new HTMLBuilder(title1, mealtype,ingredients, instructions, banana);
            HTMLBuilder htmlB2 = new HTMLBuilder(testtitle, testmealType, testingredients, testinstructions, banana);

            assertEquals(htmlB.buildHTML().toString(), htmlB2.buildHTML().toString());

            // now we'll edit the recipe
            // except the title since the user can't edit that
            String newMeal = "Dinner";
            String newIngredients = "Honey, Bread";
            String newInstructions = "smear honey, eat";
            m.post(usernameTest1, title1, newMeal, newIngredients, newInstructions, "0");
            assertEquals(1, collection1.countDocuments());

            // we'll extract out the parts from the returned JSON and see if they match
            // this demonstrates the other person can see the info of the recipe

            recipeString = m.getRecipe(username, index);
            recipeJSON = new JSONObject(recipeString);
            testtitle = recipeJSON.getString("title");
            testmealType = recipeJSON.getString("mealType");
            testingredients = recipeJSON.getString("ingredients");
            testinstructions = recipeJSON.getString("instructions");

            // our HTML page should match the correct one
             htmlB = new HTMLBuilder(title1, newMeal,newIngredients, newInstructions, banana);
             htmlB2 = new HTMLBuilder(testtitle, testmealType, testingredients, testinstructions, banana);
             assertEquals(htmlB.buildHTML().toString(), htmlB2.buildHTML().toString());
        }


        @Test
        //testing of things that sort the list
        public void StoryTestF6(){
            ArrayList<Recipe> test = new ArrayList<>();
            String title = "test1";
            String mealType = "Breakfast";
            String ingredients = "Hot dog";
            String instructions = "cook food";
            Recipe r1 = new Recipe(title, mealType, ingredients, instructions);
            Recipe r2 = new Recipe("test2", mealType, ingredients, instructions);
            r1.setIndex(0);
            r2.setIndex(1);
            test.add(r1);
            test.add(r2);

            //right now 1 should be before 2, reverse then 2 should be first
            test = SortHandler.sortRevChronological(test);
            assertEquals(test.get(0).getTitle(), "test2");
            assertEquals(test.get(1).getTitle(), "test1");

            test = SortHandler.sortAlphabetical(test);
            //now 1 should be first
            assertEquals(test.get(0).getTitle(), "test1");
            assertEquals(test.get(1).getTitle(), "test2");

            test = SortHandler.sortRevAlphabetical(test);
            //now 2 should be first
            assertEquals(test.get(0).getTitle(), "test2");
            assertEquals(test.get(1).getTitle(), "test1");

            test = SortHandler.sortChronological(test);
            //now 1 should be first
            assertEquals(test.get(0).getTitle(), "test1");
            assertEquals(test.get(1).getTitle(), "test2");
        }

        @Test
        //testing of things that sort the list
        public void UnitTestF6RevChron(){
            ArrayList<Recipe> test = new ArrayList<>();
            String title = "test1";
            String mealType = "Breakfast";
            String ingredients = "Hot dog";
            String instructions = "cook food";
            Recipe r1 = new Recipe(title, mealType, ingredients, instructions);
            Recipe r2 = new Recipe("test2", mealType, ingredients, instructions);
            r1.setIndex(0);
            r2.setIndex(1);
            test.add(r1);
            test.add(r2);

            //right now 1 should be before 2, reverse then 2 should be first
            test = SortHandler.sortRevChronological(test);
            assertEquals(test.get(0).getTitle(), "test2");
            assertEquals(test.get(1).getTitle(), "test1");
        }

        @Test
        //testing of things that sort the list
        public void UnitTestF6Chron(){
            ArrayList<Recipe> test = new ArrayList<>();
            String title = "test1";
            String mealType = "Breakfast";
            String ingredients = "Hot dog";
            String instructions = "cook food";
            Recipe r1 = new Recipe(title, mealType, ingredients, instructions);
            Recipe r2 = new Recipe("test2", mealType, ingredients, instructions);
            //add in opposite order
            r1.setIndex(1);
            r2.setIndex(0);
            test.add(r1);
            test.add(r2);

            test = SortHandler.sortChronological(test);
            //now 1 should be first
            assertEquals(test.get(0).getTitle(), "test2");
            assertEquals(test.get(1).getTitle(), "test1");
        }

        @Test
        //testing of things that sort the list
        public void UnitTestF6Alpha(){
            ArrayList<Recipe> test = new ArrayList<>();
            String title = "test1";
            String mealType = "Breakfast";
            String ingredients = "Hot dog";
            String instructions = "cook food";
            Recipe r1 = new Recipe("bbb", mealType, ingredients, instructions);
            Recipe r2 = new Recipe("aaa", mealType, ingredients, instructions);
            r1.setIndex(0);
            r2.setIndex(1);
            test.add(r1);
            test.add(r2);

            test = SortHandler.sortAlphabetical(test);
            //now 1 should be first
            assertEquals(test.get(0).getTitle(), "aaa");
            assertEquals(test.get(1).getTitle(), "bbb");
        }

        @Test
        //testing of things that sort the list
        public void UnitTestF6RevAlpha(){
            ArrayList<Recipe> test = new ArrayList<>();
            String title = "test1";
            String mealType = "Breakfast";
            String ingredients = "Hot dog";
            String instructions = "cook food";
            Recipe r1 = new Recipe("aaa", mealType, ingredients, instructions);
            Recipe r2 = new Recipe("bbb", mealType, ingredients, instructions);
            r1.setIndex(0);
            r2.setIndex(1);
            test.add(r1);
            test.add(r2);

            test = SortHandler.sortRevAlphabetical(test);
            //now 2 should be first
            assertEquals(test.get(0).getTitle(), "bbb");
            assertEquals(test.get(1).getTitle(), "aaa");

        }

        @Test
        //test image generation
        public void StoryTestF3(){
            //cannot test ui/server, so test imagedisplayhandler
            //with mocked images
            //how workflow works is that dallehandler requests url from server, server generates it and returns url
            //server makes sure we dont recreate if we already did using imagedisplayhandler
            String user = usernameTest1;
            ImageDisplayHandler i = new ImageDisplayHandler();
            MockImageGenerator m = new MockImageGenerator();
            String title = "hot dog soup";
            String ingredients = "hot dogs";
            String index = "0";

            String url = "";

            //this is the code that is ran in server when we send a reqeuest for an image
            //when we generate a url, we pass a user + index identifier in http request and associate recipe with user
            //user should not be in there
            assertFalse(i.getImageMap().containsKey(user));
            i.setUser(user);
            //test if it added the user
            assertTrue(i.getImageMap().containsKey(user));
            //if exists in map(should not), then generate and store
            assertFalse(i.getUserMap().containsKey(Integer.parseInt(index)));
            try {
                url = m.generateRecipeImage(title, ingredients);
                i.store(url, Integer.parseInt(index), user);
            } catch (Exception e) {
                System.out.println("ERROR" + e);
            }
            //now test if the map has what we want
            assertTrue(i.check(Integer.parseInt(index)));
            assertEquals(i.getUrl(Integer.parseInt(index)),  "https://assets.teenvogue.com/photos/5ab665d06d36ed4396878433/master/pass/GettyImages-519526540.jpg");

            //when a recipe is deleted it also deletes from imagedisplayhandler
            //now we test the delete method, because server is not up
            i.delete(user, Integer.parseInt(index));
            assertFalse(i.getUserMap().containsKey(Integer.parseInt(index)));
        }

        @Test
        public void UnitTestF3setUser(){
            String user = usernameTest1;
            ImageDisplayHandler i = new ImageDisplayHandler();

            assertFalse(i.getImageMap().containsKey(user));
            i.setUser(user);
            //test if it added the user
            assertTrue(i.getImageMap().containsKey(user));
        }

        @Test
        public void UnitTestF3store(){
            String user = usernameTest1;
            ImageDisplayHandler i = new ImageDisplayHandler();

            assertFalse(i.getImageMap().containsKey(user));
            i.setUser(user);
            //test if it added the user
            assertTrue(i.getImageMap().containsKey(user));

            try {
                i.store("AMONG US", 0, user);
            } catch (Exception e) {
                System.out.println(e);
            }

            //now test if the map has what we want
            assertTrue(i.check(Integer.parseInt("0")));
            assertEquals(i.getUrl(Integer.parseInt("0")),  "AMONG US");

        }

        @Test
        public void UnitTestF3delete(){
            String user = usernameTest1;
            ImageDisplayHandler i = new ImageDisplayHandler();

            assertFalse(i.getImageMap().containsKey(user));
            i.setUser(user);
            //test if it added the user
            assertTrue(i.getImageMap().containsKey(user));

            try {
                i.store("AMONG US", 0, user);
            } catch (Exception e) {
                System.out.println(e);
            }

            //now test if the map has what we want
            assertTrue(i.check(Integer.parseInt("0")));
            assertEquals(i.getUrl(Integer.parseInt("0")),  "AMONG US");

            //delete from map
            i.delete(user, Integer.parseInt("0"));
            assertFalse(i.getUserMap().containsKey(Integer.parseInt("0")));
        }

        @Test
        //Testing re-gen function of story4
        //no unit tests since these were tested already, we are just running these methods again since it is regeneration
        public void StoryTestF4(){
        createHandler.getRecipe().setMealType("Lunch");
        createHandler.getRecipe().setIngredients("chicken");
        createHandler.getRecipe().setInstructions("Example Instructions");
        createHandler.getRecipe().setTitle("Example instruction");
        Recipe r = createHandler.getRecipe();
        //To be used to ensure that they are not changed
        String mealType = r.getMealType();
        String ingredients = r.getIngredients();
        //To be used to ensure that it is changed
        String instructions = r.getInstructions();
        String checkTitle = r.getTitle();

        String recipe = gptHandler.generate(mealType,ingredients);
        String title = recipe.substring(0, recipe.indexOf("~"));
        String strippedString = title.replaceAll("[\\n\\r]+", "");
        r.setInstructions(recipe);
        r.setTitle(strippedString);


        //making sure old values are updated
        assertNotEquals(checkTitle, r.getTitle());
        assertNotEquals(instructions,r.getInstructions());
        //making sure old values are NOT updated
        assertEquals(mealType, r.getMealType());
        assertEquals(ingredients, r.getIngredients());
        //Checking display+Actually recipe is correct.
        assertEquals("Instructions: Diet Plan ~ Maybe you should just go on a diet.","Instructions: " + r.getInstructions());
        assertEquals("Instructions: Diet Plan ~ Maybe you should just go on a diet.","Instructions: " +createHandler.getRecipe().getInstructions());

        }

        //Integration tests of Features 1,2,7
        // Feature 1: User adds a recipe and edits it
        // Feature 2: User saves their username/password
        // Feature 7: Demonstrate that we can see recipe from shared link
        @Test
        public void integrationMS2Test1() {
            //login is ui and cannot be tested, server cannot be tested
            //thus we test the mongodb methods that are being called

            delCSV();
            ArrayList<String> details = AutoLogin.load();
            //this should be empty as there is no csv
            AutoLogin.save(usernameTest1, passwordTest1);
            //load checks if there is a csv, and also reads from it, so load should work
            details = AutoLogin.load();
            assertEquals(usernameTest1, details.get(0));
            assertEquals(passwordTest1, details.get(1));
            //clear csv
            delCSV();

            String title = "test1";
            String mealType = "Breakfast";
            String ingredients = "Hot dog";
            String instructions = "cook food";
            Recipe r1 = new Recipe(title, mealType, ingredients, instructions);

            //add a new recipe
            m.put(usernameTest1, title, mealType, ingredients, instructions);

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
            ingredients = "two hot dogs";
            m.post(usernameTest1, title, mealType, "two hot dogs", instructions, "0");
            //check if ingredients changed
            replace = update();
            rList.setList(replace);
            assertEquals("two hot dogs", rList.get(title).getIngredients());

            //used in f7 tests
            String banana = "https://upload.wikimedia.org/wikipedia/commons/8/8a/Banana-Single.jpg";

            // the HTML content you see below should match the info from above
            HTMLBuilder htmlB = new HTMLBuilder(title, mealType,ingredients, instructions, banana);

            assertEquals(htmlB.buildHTML().toString(), "<html><body><h1>Title: test1<br>Meal Type: Breakfast<br>Ingredients: two hot dogs<br>Instructions: cook food<br><img src=https://upload.wikimedia.org/wikipedia/commons/8/8a/Banana-Single.jpg><br></h1></body></html>");

            assertEquals(1, collection1.countDocuments());

            // the user has shared a link to their friend and the friend uses that link to search up the recipe
            // in their browser
            // we'll assume the query is correct and the username and index is extracted out
            String username = usernameTest1;
            // is 0 because it's the first recipe ever made, also is string because we extract it out
            String index = "0";

            // we'll simulate getting the recipe info from the database

            String recipeString = m.getRecipe(username, index);

            // we'll extract out the parts from the returned JSON and see if they match
            // this demonstrates the other person can see the info of the recipe
            JSONObject recipeJSON = new JSONObject(recipeString);
            String testtitle = recipeJSON.getString("title");
            String testmealType = recipeJSON.getString("mealType");
            String testingredients = recipeJSON.getString("ingredients");
            String testinstructions = recipeJSON.getString("instructions");

                        //used in f7 tests
            banana = "https://upload.wikimedia.org/wikipedia/commons/8/8a/Banana-Single.jpg";

            // the HTML content you see below should match the info from above
            htmlB = new HTMLBuilder(title, mealType,ingredients, instructions, banana);
            HTMLBuilder htmlB2 = new HTMLBuilder(testtitle, testmealType, testingredients, testinstructions, banana);

            assertEquals(htmlB.buildHTML().toString(), htmlB2.buildHTML().toString());
        }

        //Integration tests of Features 1,2,7
        // Feature 1: User adds a recipe and deletes it
        // Feature 2: User does not save their username/password
        // Feature 7: Demonstrate changes to shared recipe when edited in app/cannot access when it is deleted
        @Test
        public void integrationMS2Test2() {
            delCSV();
            ArrayList<String> details = AutoLogin.load();
            //this should be empty as there is no csv bc the user did not save their login info
            String filePath = "./users.csv";
            File file = new File(filePath);
            assertFalse(file.exists());

            String title = "test1";
            String mealType = "Breakfast";
            String ingredients = "Hot dog";
            String instructions = "cook food";
            Recipe r1 = new Recipe(title, mealType, ingredients, instructions);

            //add a new recipe
            m.put(usernameTest1, title, mealType, ingredients, instructions);

            //get user list from mongo and replace current one
            ArrayList<Recipe> replace = update();
            rList.setList(replace);

            System.out.println(replace.toString());

            Recipe r2 = rList.get(title);
            assertEquals(r1.getTitle(), r2.getTitle());
            assertEquals(r1.getMealType(), r2.getMealType());
            assertEquals(r1.getIngredients(), r2.getIngredients());
            assertEquals(r1.getInstructions(), r2.getInstructions());

            //delete the recipe
            m.delete(usernameTest1, "0");
            //collection should now be empty
            assertEquals(0, collection1.countDocuments());

            //add a new recipe
            m.put(usernameTest1, title, mealType, ingredients, instructions);
            assertEquals(1, collection1.countDocuments());

            String username = usernameTest1;
            // is 0 because it's the first recipe ever made, also is string because we extract it out
            String index = "0";
             // we'll simulate getting the recipe info from the database

            String recipeString = m.getRecipe(username, index);

            // we'll extract out the parts from the returned JSON and see if they match
            // this demonstrates the other person can see the info of the recipe
            JSONObject recipeJSON = new JSONObject(recipeString);
            String testtitle = recipeJSON.getString("title");
            String testmealType = recipeJSON.getString("mealType");
            String testingredients = recipeJSON.getString("ingredients");
            String testinstructions = recipeJSON.getString("instructions");

            String banana = "https://upload.wikimedia.org/wikipedia/commons/8/8a/Banana-Single.jpg";

            HTMLBuilder htmlB = new HTMLBuilder(title, mealType,ingredients, instructions, banana);
            HTMLBuilder htmlB2 = new HTMLBuilder(testtitle, testmealType, testingredients, testinstructions, banana);

            assertEquals(htmlB.buildHTML().toString(), htmlB2.buildHTML().toString());

            // now we'll edit the recipe
            // except the title since the user can't edit that
            String newMeal = "Dinner";
            String newIngredients = "Honey, Bread";
            String newInstructions = "smear honey, eat";
            m.post(usernameTest1, title, newMeal, newIngredients, newInstructions, "0");
            assertEquals(1, collection1.countDocuments());

            // we'll extract out the parts from the returned JSON and see if they match
            // this demonstrates the other person can see the info of the recipe

            recipeString = m.getRecipe(username, index);
            recipeJSON = new JSONObject(recipeString);
            testtitle = recipeJSON.getString("title");
            testmealType = recipeJSON.getString("mealType");
            testingredients = recipeJSON.getString("ingredients");
            testinstructions = recipeJSON.getString("instructions");

            // our HTML page should match the correct one
             htmlB = new HTMLBuilder(title, newMeal,newIngredients, newInstructions, banana);
             htmlB2 = new HTMLBuilder(testtitle, testmealType, testingredients, testinstructions, banana);
             assertEquals(htmlB.buildHTML().toString(), htmlB2.buildHTML().toString());

            //Now we will delete the recipe and see that we cannot get the HTML page for it
            m.delete(username, "0");
            assertEquals(0, collection1.countDocuments());

            // now we'll see if we can see the recipe if we try the link again (we shouldn't)
            // because we can't get it, that means our code can't create the HTML page and display an error
            recipeString = m.getRecipe(username, index);
            assertEquals("{}", recipeString);
        }

        //Integration tests of Features 3,5,6
        // Feature 3: Checks that image map correctly adds the user and stores the image url at index
        // Feature 5: Filter recipes for Breakfast and Lunch
        // Feature 6: Sort recipes chronological and then alphabetical
        @Test
        public void integrationMS2Test3() {
            //cannot test ui/server, so test imagedisplayhandler
            //with mocked images
            //how workflow works is that dallehandler requests url from server, server generates it and returns url
            //server makes sure we dont recreate if we already did using imagedisplayhandler
            String username = usernameTest1;
            ImageDisplayHandler i = new ImageDisplayHandler();
            MockImageGenerator m = new MockImageGenerator();
            String title = "Pizza";
            String ingredients = "Cheese";
            String index = "0";

            String url = "";

            //this is the code that is ran in server when we send a reqeuest for an image
            //when we generate a url, we pass a user + index identifier in http request and associate recipe with user
            //user should not be in there
            assertFalse(i.getImageMap().containsKey(username));
            i.setUser(username);
            //test if it added the user
            assertTrue(i.getImageMap().containsKey(username));
            //if exists in map(should not), then generate and store
            assertFalse(i.getUserMap().containsKey(Integer.parseInt(index)));
            try {
                url = m.generateRecipeImage(title, ingredients);
                i.store(url, Integer.parseInt(index), username);
            } catch (Exception e) {
                System.out.println("ERROR" + e);
            }
            //now test if the map has what we want
            assertTrue(i.check(Integer.parseInt(index)));
            assertEquals(i.getUrl(Integer.parseInt(index)),  "https://assets.teenvogue.com/photos/5ab665d06d36ed4396878433/master/pass/GettyImages-519526540.jpg");

            //Filter the recipeList using Breakfast and Lunch filters
            //ArrayList<Recipe> sortedList = new ArrayList<>();
            ArrayList<Recipe> testList = new ArrayList<>();
            ArrayList<Recipe> filteredList = new ArrayList<>();
            Recipe r1 = new Recipe("Pizza", "Lunch", "cheese", "cook");
            Recipe r2 = new Recipe("BLT", "Dinner", "blt", "cook");
            Recipe r3 = new Recipe("Cereal", "Breakfast", "cereal", "put in milk");
            r1.setIndex(0);
            r2.setIndex(1);
            r3.setIndex(2);
            testList.add(r1);
            testList.add(r2);
            testList.add(r3);

            filteredList = FilterHandler.filterMealType(testList, "Breakfast");
            //Should only be one Breakfast item populated in the list
            assertEquals("Cereal", filteredList.get(0).getTitle());
            assertEquals(1, filteredList.size());

            filteredList = FilterHandler.filterMealType(testList, "Lunch");
            //Should only be one Lunch item populated in the list
            assertEquals("Pizza", filteredList.get(0).getTitle());
            assertEquals(1, filteredList.size());

            testList = SortHandler.sortChronological(testList);
            //Sorts recipes based on chronological date added
            assertEquals(testList.get(0).getTitle(), "Pizza");
            assertEquals(testList.get(1).getTitle(), "BLT");
            assertEquals(testList.get(2).getTitle(), "Cereal");

            testList = SortHandler.sortAlphabetical(testList);
            //Sorts recipes based on Alphabetical
            assertEquals(testList.get(0).getTitle(), "BLT");
            assertEquals(testList.get(1).getTitle(), "Cereal");
            assertEquals(testList.get(2).getTitle(), "Pizza");


        }

         //Integration tests of Features 3,4,5,6
        // Feature 3: Delete user from Map
        // Feature 4: Regenerate Recipe
        // Feature 5: Filter recipes for All recipes and Dinner
        // Feature 6: Sort recipes reverse chronological and then reverse alphabetical
        @Test
        public void integrationMS2Test4() {
            //cannot test ui/server, so test imagedisplayhandler
            //with mocked images
            //how workflow works is that dallehandler requests url from server, server generates it and returns url
            //server makes sure we dont recreate if we already did using imagedisplayhandler
            String username = usernameTest1;
            ImageDisplayHandler i = new ImageDisplayHandler();
            MockImageGenerator m = new MockImageGenerator();
            String title = "Pizza";
            String ingredients = "Cheese";
            String index = "0";

            String url = "";

            //this is the code that is ran in server when we send a reqeuest for an image
            //when we generate a url, we pass a user + index identifier in http request and associate recipe with user
            //user should not be in there
            assertFalse(i.getImageMap().containsKey(username));
            i.setUser(username);
            //test if it added the user
            assertTrue(i.getImageMap().containsKey(username));
            //if exists in map(should not), then generate and store
            assertFalse(i.getUserMap().containsKey(Integer.parseInt(index)));
            try {
                url = m.generateRecipeImage(title, ingredients);
                i.store(url, Integer.parseInt(index), username);
            } catch (Exception e) {
                System.out.println("ERROR" + e);
            }
            //now test if the map has what we want
            assertTrue(i.check(Integer.parseInt(index)));
            assertEquals(i.getUrl(Integer.parseInt(index)),  "https://assets.teenvogue.com/photos/5ab665d06d36ed4396878433/master/pass/GettyImages-519526540.jpg");

            //when a recipe is deleted it also deletes from imagedisplayhandler
            //now we test the delete method, because server is not up
            i.delete(username, Integer.parseInt(index));
            assertFalse(i.getUserMap().containsKey(Integer.parseInt(index)));

            //Filter recipes
            ArrayList<Recipe> testList = new ArrayList<>();
            ArrayList<Recipe> filteredList = new ArrayList<>();
            Recipe r1 = new Recipe("Pizza", "Lunch", "cheese", "cook");
            Recipe r2 = new Recipe("BLT", "Dinner", "blt", "cook");
            Recipe r3 = new Recipe("Cereal", "Breakfast", "cereal", "put in milk");
            r1.setIndex(0);
            r2.setIndex(1);
            r3.setIndex(2);
            testList.add(r1);
            testList.add(r2);
            testList.add(r3);

            filteredList = FilterHandler.filterMealType(testList, "Dinner");
            //Should only be one Dinner item populated in the list
            assertEquals("BLT", filteredList.get(0).getTitle());
            assertEquals(1, filteredList.size());

            filteredList = FilterHandler.filterMealType(testList, "All");
            //All recipes should populate the list
            assertEquals("Pizza",filteredList.get(0).getTitle());
            assertEquals("BLT", filteredList.get(1).getTitle());
            assertEquals("Cereal", filteredList.get(2).getTitle());
            assertEquals(3, filteredList.size());

            testList = SortHandler.sortRevChronological(testList);
            //Recipes sorted by reverse chronological
            assertEquals(testList.get(0).getTitle(), "Cereal");
            assertEquals(testList.get(1).getTitle(), "BLT");
            assertEquals(testList.get(2).getTitle(), "Pizza");

            testList = SortHandler.sortRevAlphabetical(testList);
            //Recipes sorted by reverse Alphabetical
            assertEquals(testList.get(0).getTitle(), "Pizza");
            assertEquals(testList.get(1).getTitle(), "Cereal");
            assertEquals(testList.get(2).getTitle(), "BLT");

            //Mocking regeneration of a new recipe
            createHandler.getRecipe().setMealType("Lunch");
            createHandler.getRecipe().setIngredients("chicken");
            createHandler.getRecipe().setInstructions("Example Instructions");
            createHandler.getRecipe().setTitle("Example instruction");
            Recipe r = createHandler.getRecipe();
            //To be used to ensure that they are not changed
            String mealType = r.getMealType();
            ingredients = r.getIngredients();
            //To be used to ensure that it is changed
            String instructions = r.getInstructions();
            String checkTitle = r.getTitle();

            String recipe = gptHandler.generate(mealType,ingredients);
            title = recipe.substring(0, recipe.indexOf("~"));
            String strippedString = title.replaceAll("[\\n\\r]+", "");
            r.setInstructions(recipe);
            r.setTitle(strippedString);


            //making sure old values are updated
            assertNotEquals(checkTitle, r.getTitle());
            assertNotEquals(instructions,r.getInstructions());
            //making sure old values are NOT updated
            assertEquals(mealType, r.getMealType());
            assertEquals(ingredients, r.getIngredients());
            //Checking display+Actually recipe is correct.
            assertEquals("Instructions: Diet Plan ~ Maybe you should just go on a diet.","Instructions: " + r.getInstructions());
            assertEquals("Instructions: Diet Plan ~ Maybe you should just go on a diet.","Instructions: " +createHandler.getRecipe().getInstructions());

        }


        // Feature 1 (5), Feature 1 (3), Feature 2 (1), Feature 6 (1), Feature 7 (1 then 2), Feature 3 (1), Feature 5 (1 with Feature 4 (1) and Feature 6 (2), 2)
        // Feature 1 (5) The server is down
        // Feature 1 (3) - User has an account
        // Feature 2 (1) - User has auto-login enabled
        // Feature 6 (1) - User has multiple recipes and is in default order
        // Feature 7 (1 then 2) (That webpage contains the same recipe display info that the user would have access to locally. But then deletes)
        // Feature 3 (1) - User clicks display recipe
        // Feature 5 (1 with Feature 4 (1) and Feature 6 (2), 2) - User adding recipe, user regenerates recipe, recipe then added with tag and recipes
        // should be in default order, then they only filter for lunch recipes
        @Test
        public void sbstMS2Test1() {
            String user = usernameTest1;
            ImageDisplayHandler i = new ImageDisplayHandler();
            MockImageGenerator mig = new MockImageGenerator();

            String url = "";

            // Feature 1 (5)
            // if the server is down (which we can't really test for, then the user just waits a bit)

            // server is back up

            // Feature 1 (3)
            // user has an account, but we already added it earlier so they log in

            // user decides to test out the capabilities of the app, with add, edit, and delete (StoryTestF1)
            //login is ui and cannot be tested, server cannot be tested
            //thus we test the mongodb methods that are being called

            String index = "0";

            String title = "test1";
            String mealType = "Breakfast";
            String ingredients = "Hot dog";
            String instructions = "cook food";
            Recipe r1 = new Recipe(title, mealType, ingredients, instructions);

            // image is generated as well
            //this is the code that is ran in server when we send a reqeuest for an image
            //when we generate a url, we pass a user + index identifier in http request and associate recipe with user
            //user should not be in there
            assertFalse(i.getImageMap().containsKey(user));
            i.setUser(user);
            //test if it added the user
            assertTrue(i.getImageMap().containsKey(user));
            //if exists in map(should not), then generate and store
            assertFalse(i.getUserMap().containsKey(Integer.parseInt(index)));
            try {
                url = mig.generateRecipeImage(title, ingredients);
                i.store(url, Integer.parseInt(index), user);
            } catch (Exception e) {
                System.out.println("ERROR" + e);
            }
            //now test if the map has what we want
            assertTrue(i.check(Integer.parseInt(index)));
            assertEquals(i.getUrl(Integer.parseInt(index)),  "https://assets.teenvogue.com/photos/5ab665d06d36ed4396878433/master/pass/GettyImages-519526540.jpg");

            //add a new recipe
            m.put(usernameTest1, title, mealType, ingredients, instructions);

            //get user list from mongo and replace current one
            ArrayList<Recipe> replace = update();
            rList.setList(replace);

            Recipe r2 = rList.get(title);
            assertEquals(r1.getTitle(), r2.getTitle());
            assertEquals(r1.getMealType(), r2.getMealType());
            assertEquals(r1.getIngredients(), r2.getIngredients());
            assertEquals(r1.getInstructions(), r2.getInstructions());

            //edit that recipe
            m.post(usernameTest1, title, mealType, "two hot dogs", instructions, "0");
            //check if ingredients changed
            replace = update();
            rList.setList(replace);
            assertEquals("two hot dogs", rList.get(title).getIngredients());

            //delete the recipe
            m.delete(usernameTest1, "0");

            //collection should now be empty
            assertEquals(0, collection1.countDocuments());

            // image associated with it should be gone too
            i.delete(usernameTest1, 0);
            assertFalse(i.check(Integer.parseInt(index)));


            // user logsout

            // user decides to test auto-login - Feature 2 (1) (StoryTestF2)
            delCSV();
            ArrayList<String> details = AutoLogin.load();
            //this should be empty as there is no csv
            AutoLogin.save(usernameTest1, passwordTest1);
            //load checks if there is a csv, and also reads from it, so load should work
            details = AutoLogin.load();
            assertEquals(usernameTest1, details.get(0));
            assertEquals(passwordTest1, details.get(1));
            //clear csv
            delCSV();

            // user decides to create a recipe to then be shared - Feature 7 (1) - StoryTestF7Delete
            // a recipe was added to user1
            String title1 = "Test Recipe 1";
            String mealtype = "Lunch";
            ingredients = "food";
            instructions = "cook food";
            m.put(usernameTest1, title1, mealtype, ingredients, instructions);
            assertEquals(1, collection1.countDocuments());

            // image should be generated here
            try {
                url = mig.generateRecipeImage(title, ingredients);
                i.store(url, Integer.parseInt(index), user);
            } catch (Exception e) {
                System.out.println("ERROR" + e);
            }
            //now test if the map has what we want
            assertTrue(i.check(Integer.parseInt(index)));
            assertEquals(i.getUrl(Integer.parseInt(index)),  "https://assets.teenvogue.com/photos/5ab665d06d36ed4396878433/master/pass/GettyImages-519526540.jpg");

            // the user has shared a link to their friend and the friend uses that link to search up the recipe
            // in their browser
            // we'll assume the query is correct and the username and index is extracted out
             // the HTML content you see below should match the info from above
             String username = usernameTest1;

             // we'll simulate getting the recipe info from the database

            String recipeString = m.getRecipe(username, index);

            // we'll extract out the parts from the returned JSON and see if they match
            // this demonstrates the other person can see the info of the recipe
            JSONObject recipeJSON = new JSONObject(recipeString);
            String testtitle = recipeJSON.getString("title");
            String testmealType = recipeJSON.getString("mealType");
            String testingredients = recipeJSON.getString("ingredients");
            String testinstructions = recipeJSON.getString("instructions");

            //used in f7 tests
            // should be able to get our new url from here, just like in code
            String banana = i.getUrl(Integer.parseInt(index));

            HTMLBuilder htmlB = new HTMLBuilder(title1, mealtype,ingredients, instructions, banana);
            HTMLBuilder htmlB2 = new HTMLBuilder(testtitle, testmealType, testingredients, testinstructions, banana);

            assertEquals(htmlB.buildHTML().toString(), htmlB2.buildHTML().toString());

            // but now the user wants to delete their shared recipe, meaning the page
            // won't share - Feature 7 (2)

            // now we'll delete the recipe
            m.delete(usernameTest1, "0");
            assertEquals(0, collection1.countDocuments());

            // image associated with it should be gone too
            i.delete(usernameTest1, 0);
            assertFalse(i.check(Integer.parseInt(index)));

            // now we'll see if we can see the recipe if we try the link again (we shouldn't)
            // because we can't get it, that means our code can't create the HTML page and display an error
            recipeString = m.getRecipe(username, index);
            assertEquals("{}", recipeString);


            // user tries to create a new recipe, but then regenerates their results (Story Test F4) - Feature 4 (1)
            createHandler.getRecipe().setMealType("Lunch");
            createHandler.getRecipe().setIngredients("chicken");
            createHandler.getRecipe().setInstructions("Example Instructions");
            createHandler.getRecipe().setTitle("Example instruction");
            Recipe r = createHandler.getRecipe();

            // generate an image when we first create
            try {
                url = mig.generateRecipeImage(title, ingredients);
                i.store(url, Integer.parseInt(index), user);
            } catch (Exception e) {
                System.out.println("ERROR" + e);
            }
            //now test if the map has what we want
            assertTrue(i.check(Integer.parseInt(index)));
            assertEquals(i.getUrl(Integer.parseInt(index)),  "https://assets.teenvogue.com/photos/5ab665d06d36ed4396878433/master/pass/GettyImages-519526540.jpg");


            //To be used to ensure that they are not changed
            mealType = r.getMealType();
            ingredients = r.getIngredients();
            //To be used to ensure that it is changed
            instructions = r.getInstructions();
            String checkTitle = r.getTitle();

            // user presses regeneration button below

            // make sure we delete our old image
            // image associated with it should be gone too
            i.delete(usernameTest1, 0);
            assertFalse(i.check(Integer.parseInt(index)));

            String recipe = gptHandler.generate(mealType,ingredients);
            title = recipe.substring(0, recipe.indexOf("~"));
            String strippedString = title.replaceAll("[\\n\\r]+", "");
            r.setInstructions(recipe);
            r.setTitle(strippedString);

            // generate an image when we regenerate
            try {
                url = mig.generateRecipeImage(title, ingredients);
                i.store(url, Integer.parseInt(index), user);
            } catch (Exception e) {
                System.out.println("ERROR" + e);
            }
            //now test if the map has what we want
            assertTrue(i.check(Integer.parseInt(index)));
            assertEquals(i.getUrl(Integer.parseInt(index)),  "https://assets.teenvogue.com/photos/5ab665d06d36ed4396878433/master/pass/GettyImages-519526540.jpg");

            //making sure old values are updated
            assertNotEquals(checkTitle, r.getTitle());
            assertNotEquals(instructions,r.getInstructions());
            //making sure old values are NOT updated
            assertEquals(mealType, r.getMealType());
            assertEquals(ingredients, r.getIngredients());
            //Checking display+Actually recipe is correct.
            assertEquals("Instructions: Diet Plan ~ Maybe you should just go on a diet.","Instructions: " + r.getInstructions());
            assertEquals("Instructions: Diet Plan ~ Maybe you should just go on a diet.","Instructions: " +createHandler.getRecipe().getInstructions());

            title = r.getTitle();
            instructions = r.getInstructions();

            // user adds the recipe
            m.put(usernameTest1, title, mealType, ingredients, instructions);
            assertEquals(1, collection1.countDocuments());

            // will update the recipeList to reflect this
            replace = update();
            rList.setList(replace);

            // User clicks display recipe to see the recipe they just made (StoryTestF3) - Feature 3 (1)

            // see if we can get the recently made recipe and it's image
            r = rList.get(title);
            String title0 = title;
            assertEquals(r.getTitle(), title);
            assertEquals(r.getInstructions(), instructions);
            assertEquals(r.getIngredients(), ingredients);
            assertEquals(r.getMealType(), mealType);
            //now test if the map has what we want
            assertTrue(i.check(Integer.parseInt(index)));
            assertEquals(i.getUrl(Integer.parseInt(index)),  "https://assets.teenvogue.com/photos/5ab665d06d36ed4396878433/master/pass/GettyImages-519526540.jpg");

            // user decides to add some more new recipes, one dinner and one breakfast
            // each time we add a recipe, we want to make sure that the recipes are being added in chronological order
            // Feature 6 (1) - UnitTestF6Chron
            title = "Steak";
            mealType = "Dinner";
            ingredients = "Beef";
            instructions = "Cook";
            //add a new recipe
            m.put(usernameTest1, title, mealType, ingredients, instructions);
            assertEquals(2, collection1.countDocuments());

            // will update the recipeList to reflect this
            replace = update();
            rList.setList(replace);

            // in our UI, we have a separate ArrayList<Recipe> that is based on recipe list
            ArrayList<Recipe> listDisplay = rList.getList();

            // we have been in chronological order by default
            assertEquals(listDisplay.get(0).getTitle(), title0);
            assertEquals(listDisplay.get(1).getTitle(), "Steak");

            title = "Eggs";
            mealType = "Breakfast";
            ingredients = "Yolk";
            instructions = "Boil";
            //add a new recipe
            m.put(usernameTest1, title, mealType, ingredients, instructions);
            assertEquals(3, collection1.countDocuments());

            // will update the recipeList to reflect this
            replace = update();
            rList.setList(replace);

            // in our UI, we have a separate ArrayList<Recipe> that is based on recipe list
            listDisplay = rList.getList();

            // we have been in chronological order by default
            assertEquals(listDisplay.get(0).getTitle(), title0);
            assertEquals(listDisplay.get(1).getTitle(), "Steak");
            assertEquals(listDisplay.get(2).getTitle(), "Eggs");

            // Now it's noon and the user wants to see only their lunch recipes
            // Feature 5 (2) - UnitTestF5LunchFilter
            // there should only be 1 lunch item
            listDisplay = FilterHandler.filterMealType(listDisplay, "Lunch");
            assertEquals(title0, listDisplay.get(0).getTitle());
            assertEquals(1,listDisplay.size());
        }
        @Test
        public void sbstMS2Test2(){
            //F1: Cannot test/is being tested in other sbst

            /*
            * start of F7:1
            * Click create a recipe and go through the steps to create a new recipe. Then click on the display recipe and click the share recipe button.
            * A unique URL should be displayed in a section labeled “Shareable Link”.  (User story 7 scenario 1)
            */
            String title1 = "Test Recipe 1";
            String mealtype = "Lunch";
            String ingredients = "food";
            String instructions = "cook food";
            m.put(usernameTest1, title1, mealtype, ingredients, instructions);
            assertEquals(1, collection1.countDocuments());

            // the user has shared a link to their friend and the friend uses that link to search up the recipe
            // in their browser
            // we'll assume the query is correct and the username and index is extracted out
            String username = usernameTest1;
            // is 0 because it's the first recipe ever made, also is string because we extract it out
            String index = "0";

            // we'll simulate getting the recipe info from the database

            String recipeString = m.getRecipe(username, index);

            // we'll extract out the parts from the returned JSON and see if they match
            // this demonstrates the other person can see the info of the recipe
            JSONObject recipeJSON = new JSONObject(recipeString);
            String testtitle = recipeJSON.getString("title");
            String testmealType = recipeJSON.getString("mealType");
            String testingredients = recipeJSON.getString("ingredients");
            String testinstructions = recipeJSON.getString("instructions");

                        //used in f7 tests
            String banana = "https://upload.wikimedia.org/wikipedia/commons/8/8a/Banana-Single.jpg";

            // the HTML content you see below should match the info from above
            HTMLBuilder htmlB = new HTMLBuilder(title1, mealtype,ingredients, instructions, banana);
            HTMLBuilder htmlB2 = new HTMLBuilder(testtitle, testmealType, testingredients, testinstructions, banana);

            assertEquals(htmlB.buildHTML().toString(), htmlB2.buildHTML().toString());
            //end of F7:1

            //start of F7:3
            /*
            * Click the edit button within the recipe display and make some changes to the recipe.
            * When you go back to the URL for that recipe, the changes should be reflected in
            * the webpage. (User story 7 scenario 3)
            */
            // a recipe was added to user1
            String newMeal = "Dinner";
            String newIngredients = "Honey, Bread";
            String newInstructions = "smear honey, eat";
            m.post(usernameTest1, title1, newMeal, newIngredients, newInstructions, "0");
            assertEquals(1, collection1.countDocuments());

            recipeString = m.getRecipe(username, index);
            recipeJSON = new JSONObject(recipeString);
            testtitle = recipeJSON.getString("title");
            testmealType = recipeJSON.getString("mealType");
            testingredients = recipeJSON.getString("ingredients");
            testinstructions = recipeJSON.getString("instructions");

            // our HTML page should match the correct one
             htmlB = new HTMLBuilder(title1, newMeal,newIngredients, newInstructions, banana);
             htmlB2 = new HTMLBuilder(testtitle, testmealType, testingredients, testinstructions, banana);
             assertEquals(htmlB.buildHTML().toString(), htmlB2.buildHTML().toString());
            //end of F7:3

            /*
            * Go back to the recipe display page and choose the filter by meal type button. Click all 3 switches and then click apply.
            * You should be directed back to the main recipe display but it should be empty (User story 5, feature 4).
            */
            //start of F5:4
            ArrayList<Recipe> testList = new ArrayList<>();
            ArrayList<Recipe> filteredList = new ArrayList<>();
            Recipe r1 = new Recipe(testtitle,testmealType,testingredients,testinstructions);
            r1.setIndex(0);
            testList.add(r1);

            filteredList = FilterHandler.filterMealType(testList, "Lunch");
            assertEquals(0,filteredList.size());
            //end of F5:4

            /*
            * Choose the filter by meal type button once again, but choose to toggle an arbitrary tag and click apply. You should be directed back to the
            * main display but the display should be populated with recipes that have the particular tag.(User story 5, feature 3).
            */
            //start of F5:3
            filteredList = FilterHandler.filterMealType(testList, "Dinner");
            assertEquals(1,filteredList.size());
            //end of F5:3

            //start of F6:3
            /*
             * Choose to sort the recipes by alphabetical order.
             * The recipes should then be displayed in alphabetical order. (User story 6, scenario 3).
             */
            Recipe r2 = new Recipe("title2", mealtype, ingredients, instructions);
            r2.setIndex(1);
            testList.add(r2);

            testList = SortHandler.sortAlphabetical(testList);
            //now 1 should be first
            assertEquals(testList.get(0).getTitle(), r1.getTitle());
            assertEquals(testList.get(1).getTitle(), r2.getTitle());
            //end of F6:3

            //start of F6:4
            /*
             * Then choose to create a new recipe and go through the steps to create the recipe. The new recipe should be created and populated
             * in a spot relative to the other recipes based upon its alphabetical value. (User story 6, scenario 4)
             */
            Recipe r3 = new Recipe("A",mealtype,ingredients,instructions);
            r3.setIndex(2);
            testList.add(r3);
            testList = SortHandler.sortAlphabetical(testList);
            assertEquals(testList.get(0).getTitle(), r3.getTitle());
            assertEquals(testList.get(1).getTitle(), r1.getTitle());
            assertEquals(testList.get(2).getTitle(), r2.getTitle());

            //end of F6:4

            //start of F6:7
            /*
             * Choose to revert back to default sorted order.The recipe display should then be displayed
             * in chronological order (User story 6, scenario 7).
             */
            testList = SortHandler.sortChronological(testList);
            assertEquals(testList.get(0).getTitle(), r1.getTitle());
            assertEquals(testList.get(1).getTitle(), r2.getTitle());
            assertEquals(testList.get(2).getTitle(), r3.getTitle());
            //end of F6:7

            //start of F6:5
            /*
             * Then choose to sort the recipes in reverse-chronological order. The recipes should then be
             *  displayed in reverse chronological order (User story 6, scenario 5).
             */
            testList = SortHandler.sortRevChronological(testList);
            assertEquals(testList.get(0).getTitle(), r3.getTitle());
            assertEquals(testList.get(1).getTitle(), r2.getTitle());
            assertEquals(testList.get(2).getTitle(), r1.getTitle());
            //end of F6:5

            //start of F6:6
            /*
             * Then choose to create a new recipe and go through the steps to create the recipe. The new recipe should be created and populated in a spot relative to the
             * other recipes based upon its reverse chronological value. (User story 6, scenario 6).
             */
            Recipe r4 = new Recipe("B", mealtype,ingredients,instructions);
            r4.setIndex(3);
            testList.add(r4);
            testList = SortHandler.sortRevChronological(testList);
            assertEquals(testList.get(0).getTitle(), r4.getTitle());
            assertEquals(testList.get(1).getTitle(), r3.getTitle());
            assertEquals(testList.get(2).getTitle(), r2.getTitle());
            assertEquals(testList.get(3).getTitle(), r1.getTitle());;

        }
    }


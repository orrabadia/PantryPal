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

import static org.junit.jupiter.api.Assertions.assertEquals;

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


public class TestAll {
    private RecipeList rList;
    private RecipeHandler rHandler;
    private RecordHandler recordHandler;
    private WhisperHandler whisperHandler;
    private CreateHandler createHandler;
    private GPTHandler gptHandler;
    private MongoDatabase database;
    private MongoClient clientMongoDB;
    private MongoCollection<Document> collection;


     @BeforeEach
     public void initialize(){
        rList = new RecipeList();
        rHandler = new RecipeHandler(rList);
        //this represents the initial app where no recipes are in the recipelist
        //every time you click add it adds to list, so this represents the list
        //the displayed list is gotten from the recipelist instance

        // creates a new RecordHandler (with MockRecorder)
        recordHandler = new RecordHandler(true);

        whisperHandler = new WhisperHandler(true);

        createHandler = new CreateHandler();

        gptHandler = new GPTHandler(true);

        //insert your uri
        clientMongoDB = MongoClients.create( "mongodb+srv://orrabadia:yDIYYtTjsP0REJcl@cluster0.0b39ssz.mongodb.net/?retryWrites=true&w=majority");
        
        database = clientMongoDB.getDatabase("PantryPal");

        collection = database.getCollection("MOGUSMAN");

     }

    public void deleteRecording() {
        String fileName = "recording.wav";
        File file = new File(fileName);

        if (file.exists()) {
            file.delete();
        }

    }

     @AfterEach
     public void clearrecipes(){
        //clear recipe list each time
        rList.clear();
        //deletes test_username collection
        database.getCollection("MOGUSMAN").drop();
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
        rHandler.addRecipe(r1);
        //add two and check if you can get title, maybe 2 messes it up

        //maybe have username input and make test collection?
        assertEquals(/*rList.size()*/ rHandler.getRecipeList().getList().size(), 1);
    }

    //test that you can delete recipes
    @Test
    public void unitTestS1RecipeDelete() {
        String title1 = "Test Recipe 1";
        String mealtype = "Lunch";
        String ingredients = "food";
        String instructions = "cook food";
        Recipe r1= new Recipe(title1, mealtype, ingredients, instructions);
        rHandler.addRecipe(r1);
        //add two and check if you can get title, maybe 2 messes it up
        assertEquals(rHandler.getRecipeList().getList().size(), 1);
        rHandler.deleteRecipe(r1.getIndex());
        assertEquals(rHandler.getRecipeList().getList().size(), 0);
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
        rHandler.addRecipe(r1);
        rHandler.addRecipe(r2);
        //add two and check if you can get title, maybe 2 messes it up
        Recipe r = rHandler.getRecipeList().get(title1);
        assertEquals(r.getTitle(), "Test Recipe 1");
    }


    @Test
    public void storyTestS1NoRecipes() {
        //at start, should be no recipes
        int initialRecipeCount = rList.size();
        assertEquals(rHandler.getRecipeList().getList().size(), initialRecipeCount);
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
        rHandler.addRecipe(r1);
        rHandler.addRecipe(r2);
        assertEquals(rHandler.getRecipeList().getList().size(), 2);
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
        Recipe r1= new Recipe(title1, mealtype, ingredients, instructions);
        Recipe r2= new Recipe(title2, mealtype, ingredients, instructions);
        rHandler.addRecipe(r1);
        rHandler.addRecipe(r2);
        assertEquals(rHandler.getRecipeList().getList().size(), 2);
        //delete
        rHandler.deleteRecipe(r1.getIndex());
        assertEquals(rHandler.getRecipeList().getList().size(), 1);
        rHandler.deleteRecipe(r2.getIndex());
        assertEquals(rHandler.getRecipeList().getList().size(), 0);

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
        rHandler.addRecipe(r1);
        rHandler.addRecipe(r2);
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
        rHandler.addRecipe(r1);
        rHandler.addRecipe(r2);
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
        rHandler.addRecipe(r1);
        rHandler.addRecipe(r2);
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
        rHandler.addRecipe(r1);
        rHandler.addRecipe(r2);
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
        rHandler.addRecipe(r1);
        rHandler.addRecipe(r2);
        //add two and check if you can get title, maybe 2 messes it up
        Recipe r = rList.get(title1);
        assertEquals(r.getTitle(), "Test Recipe 1");
        assertEquals(r.getMealType(), "Lunch");
        assertEquals(r.getIngredients(), "food");
        assertEquals(r.getInstructions(), "cook food");
    }

    // Story 3, test that when you press the Record button (and press stop record), recording.wav is made
    @Test
    public void unitTestS3Record() {
        try {
                recordHandler.record();
            }
        catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
        }
        File file = new File("recording.wav");
        assertEquals(true, file.exists());
    }

    // Story 3, test that the "functionality" of getting recording.wav and processing

    // it in Whisper and see whether it's text matches what it should be
    @Test
    public void unitTestS3Whisper() {
        File audioFile = new File("recording.wav");

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
        File file = new File("recording.wav");
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
        File file = new File("recording.wav");
        assertEquals(true, file.exists());
        File audioFile = new File("recording.wav");

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
        File file = new File("recording.wav");
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

        File audioFile = new File("recording.wav");

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
        File file = new File("recording.wav");
        assertEquals(true, file.exists());
        File audioFile = new File("recording.wav");

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
        rHandler.addRecipe(r1);
        rHandler.addRecipe(r2);
        assertEquals(rList.size(), 2);

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
        File file = new File("recording.wav");
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

        File audioFile = new File("recording.wav");

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

    public void integrationTest3() {
        // Story 1
        //add 2 recipes, 2 should be displayed
        String title1 = "Test Recipe 1";
        String title2 = "Test Recipe 2";
        String mealtype = "Lunch";
        String ingredients = "food";
        String instructions = "cook food";
        Recipe r1= new Recipe(title1, mealtype, ingredients, instructions);
        Recipe r2= new Recipe(title2, mealtype, ingredients, instructions);
        rHandler.addRecipe(r1);
        rHandler.addRecipe(r2);
        assertEquals(rList.size(), 2);
        //delete
        rList.remove(title1);
        assertEquals(rList.size(), 1);
        rList.remove(title2);
        assertEquals(rList.size(), 0);


        // Story 3
        try {
                recordHandler.record();
            }
        catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
        }
        File file = new File("recording.wav");
        assertEquals(true, file.exists());
        File audioFile = new File("recording.wav");

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


    // Story 4, test that when you press the Record button (and press stop record), recording.wav is made
    @Test
    public void unitTestS4Record() {
        try {
                recordHandler.record();
            }
        catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
        }
        File file = new File("recording.wav");
        assertEquals(true, file.exists());
    }

    // Story 3, test that the "functionality" of getting recording.wav and processing
    // it in Whisper and see whether it's text matches what it should be
    @Test
    public void unitTestS4Whisper() {
        File audioFile = new File("recording.wav");

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
        File file = new File("recording.wav");
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
        File file = new File("recording.wav");
        assertEquals(true, file.exists());
        File audioFile = new File("recording.wav");

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
        File file = new File("recording.wav");
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

        File audioFile = new File("recording.wav");

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
        rHandler.addRecipe(createHandler.getRecipe());
        // check whether recipeList was updated
        assertEquals(rList.size(), 1);
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
        rHandler.addRecipe(createHandler.getRecipe());
        // check whether recipeList was updated
        assertEquals(rList.size(), 1);
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
        rHandler.addRecipe(oldRecipe);

        // edit oldRecipe with newInstructions and newIngredients
        rHandler.editRecipe(oldRecipe, newIngredients, newInstructions);

        assertEquals(1, rList.size());

        // check whether the oldRecipe is updated properly, comparing it to the newRecipe we want
        assertEquals(newRecipe.getIngredients(), oldRecipe.getIngredients());
        assertEquals(newRecipe.getInstructions(), oldRecipe.getInstructions());
    }


    @Test
    public void storyTestS7DeleteRecipe() {
        
        String title1 = "Test Recipe 1";
        String mealtype = "Lunch";
        String ingredients = "food";
        String instructions = "cook food";
        Recipe r1= new Recipe(title1, mealtype, ingredients, instructions);
        rHandler.addRecipe(r1);
        //add two and check if you can get title, maybe 2 messes it up
        assertEquals(rList.size(), 1);
        rHandler.deleteRecipe(r1.getIndex());
        assertEquals(rList.size(), 0);
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
        File file = new File("recording.wav");
        assertEquals(true, file.exists());
        File audioFile = new File("recording.wav");

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
        rHandler.addRecipe(createHandler.getRecipe());
        // check whether recipeList was updated
        assertEquals(rList.size(), 1);
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
        rHandler.editRecipe(oldRecipe, newIngredients, newInstructions);

        assertEquals(1, rList.size());

        // check whether the oldRecipe is updated properly, comparing it to the newRecipe we want
        assertEquals(newRecipe.getIngredients(), oldRecipe.getIngredients());
        assertEquals(newRecipe.getInstructions(), oldRecipe.getInstructions());

        //Story 7: Delete
         //add two and check if you can get title, maybe 2 messes it up
        assertEquals(rList.size(), 1);
        rHandler.deleteRecipe(newRecipe.getIndex());
        assertEquals(rList.size(), 0);
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
        File file = new File("recording.wav");
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

        File audioFile = new File("recording.wav");

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
        String mealtype = "Lunch";
        String ingredients = "food";
        String instructions = "cook food";
        Recipe r1= new Recipe(title, mealtype, ingredients, instructions);
        Recipe r2= new Recipe("Test Recipe 2", mealtype, ingredients, instructions);
        rHandler.addRecipe(r1);
        rHandler.addRecipe(r2);
        assertEquals(rList.size(), 2);

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
        File file = new File("recording.wav");
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

        File audioFile = new File("recording.wav");

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
        rHandler.addRecipe(createHandler.getRecipe());
        // check whether recipeList was updated
        assertEquals(rList.size(), 3);
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
        String newIngredients = "Eggs, Cheese, Milk";
        String newInstructions = "1. Heat pan 2. Crack Eggs 3. Pour Milk in Pan 4. Flip 5. Wait 6. Eat";
        Recipe newRecipe = new Recipe(strippedString, "Lunch", newIngredients, newInstructions);
        
        // edit oldRecipe with newInstructions and newIngredients
        rHandler.editRecipe(oldRecipe, newIngredients, newInstructions);

        assertEquals(3, rList.size());

        // check whether the oldRecipe is updated properly, comparing it to the newRecipe we want
        assertEquals(newRecipe.getIngredients(), oldRecipe.getIngredients());
        assertEquals(newRecipe.getInstructions(), oldRecipe.getInstructions());

        //Story 7: Delete
         //add two and check if you can get title, maybe 2 messes it up
        assertEquals(rList.size(), 3);
        rHandler.deleteRecipe(r1.getIndex());
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
        Recipe r1= new Recipe(title1, mealtype, ingredients, instructions);
        Recipe r2= new Recipe(title2, mealtype, ingredients, instructions);
        rHandler.addRecipe(r1);
        rHandler.addRecipe(r2);
        assertEquals(rList.size(), 2);
        //delete
        rList.remove(title1);
        assertEquals(rList.size(), 1);
        rList.remove(title2);
        assertEquals(rList.size(), 0);

        // Story 3: Record 
        try {
                recordHandler.record();
            }
        catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
        }
        File file = new File("recording.wav");
        assertEquals(true, file.exists());
        File audioFile = new File("recording.wav");

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
        file = new File("recording.wav");
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

        audioFile = new File("recording.wav");

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
        File file = new File("recording.wav");
        assertEquals(true, file.exists());
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
        File audioFile = new File("recording.wav");
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
        rHandler.addRecipe(createHandler.getRecipe());
        // check whether recipeList was updated
        assertEquals(rList.size(), 1);
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
        assertEquals(rList.size(), 1);
        rHandler.deleteRecipe(createHandler.getRecipe().getIndex());
        assertEquals(rList.size(), 0);
        //end of 7 - Delete
    }
}

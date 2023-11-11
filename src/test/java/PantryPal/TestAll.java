package PantryPal;
import org.junit.jupiter.api.Test;

import PantryPal.AppFrame;
import PantryPal.Main;
import PantryPal.NavigationHandler;
import PantryPal.Recipe;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;

//import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;


public class TestAll {
    private RecipeList rList;
    private RecipeHandler rHandler;
    private RecordHandler recordHandler;
    private WhisperHandler whisperHandler;
    private Recipe recipe;

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

        recipe = new Recipe();
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
        assertEquals(rList.size(), 1);
    }

    //test that you can delete recipes
    public void unitTestS1RecipeDelete() {
        String title1 = "Test Recipe 1";
        String mealtype = "Lunch";
        String ingredients = "food";
        String instructions = "cook food";
        Recipe r1= new Recipe(title1, mealtype, ingredients, instructions);
        rHandler.addRecipe(r1);
        //add two and check if you can get title, maybe 2 messes it up
        assertEquals(rList.size(), 1);
        rList.remove(title1);
        assertEquals(rList.size(), 0);
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
        Recipe r = rList.get(title1);
        assertEquals(r.getTitle(), "Test Recipe 1");
    }


    @Test 
    public void storyTestS1NoRecipes() {
        //at start, should be no recipes
        int initialRecipeCount = rList.size();
        assertEquals(initialRecipeCount, 0);
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
        assertEquals(rList.size(), 2);
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
        assertEquals(rList.size(), 2);
        //delete 
        rList.remove(title1);
        assertEquals(rList.size(), 1);
        rList.remove(title2);
        assertEquals(rList.size(), 0);

    }
    //story 2, test the other get methods that are used
    @Test
    public void unitTestS2getMethods(){
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
        recipe.setMealType("Dinner");
        assertEquals("Dinner", recipe.getMealType());
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
        recipe.setMealType(transcription);
        assertEquals("Dinner", recipe.getMealType());
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
        recipe.setMealType(transcription);
        assertEquals("Dinner", recipe.getMealType());
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

    // Story 4, test whether our program saves a user's ingredient info
    // Here we separately test for whether our program can save ingredients
    // without the funcitonality of Record or Whisper (we can test all of them in the story test)
    @Test 
    public void unitTestS4SaveIngredients() {
        recipe.setIngredients("Corn, Peas, Carrots, Chicken");
        assertEquals("Corn, Peas, Carrots, Chicken", recipe.getIngredients());
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
        recipe.setIngredients(transcription);
        assertEquals("Dinner", recipe.getIngredients());
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
        recipe.setIngredients(transcription);
        assertEquals("Dinner", recipe.getIngredients());
    }
}
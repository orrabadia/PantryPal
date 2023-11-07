package PantryPal;

// imports for unit tests
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

// imports for Main.java
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.TextAlignment;
import javafx.geometry.Insets;
import javafx.scene.text.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.net.*;

public class Story3Test {
    private static AppFrame appFrame;

    @BeforeAll
     public static void setUp() {
        //declare og test recipe and start javafx
        javafx.application.Platform.startup(() -> { });
        appFrame = new AppFrame();
     }

     @AfterAll
     public static void tearDown() {
        //exit to prevent errors
        javafx.application.Platform.exit();
     }

     @AfterEach
     public void clearrecipes(){
        //clear recipe list each time
        appFrame.getRecipeList().getChildren().clear();
     }

     @Test 
    // test to see whether the function linkd to the "New Recipe" button brings you to the Record MealType page
    public void testAddButton() {
        //this is what is linked to add recipe button
        appFrame.handler.recordMeal();
        // see whether we switched pages by 
        assertEquals(appFrame.handler.primaryStage, pageList.get(RECORD_MEALTYPE));
    }
}

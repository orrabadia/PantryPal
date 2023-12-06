package PantryPal;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
/*
 * Class Copied from Lab 1 for footer
 */
class ListFooter extends HBox {
    private Button newRecipeButton;
    private Button logOutButton;
    private ComboBox<String> sortBox;
    private ComboBox<String> filterBox;
    ListFooter() {
        this.setPrefSize(500, 60);
        this.setStyle("-fx-background-color: #F0F8FF;");
        this.setSpacing(15);
        // set a default style for buttons - background color, font size, italics
        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";
        this.setAlignment(Pos.CENTER); // aligning the buttons to center

        newRecipeButton = new Button("New Recipe");
        logOutButton = new Button("Log Out");
        newRecipeButton.setStyle(defaultButtonStyle);
        logOutButton.setStyle(defaultButtonStyle);

        //dropdown for sort
        sortBox = new ComboBox<>();
        sortBox.getItems().addAll("Newest to Oldest", "Oldest to Newest", "Alphabetical", "Reverse Alphabetical");
        //default value
        sortBox.setValue("Newest to Oldest");

        //dropdown for filter
        filterBox = new ComboBox<>();
        filterBox.getItems().addAll("All", "Breakfast", "Lunch", "Dinner");
        filterBox.setValue("All");

        this.getChildren().addAll(logOutButton, newRecipeButton, sortBox, filterBox);
        this.setAlignment(Pos.CENTER);
    }

    public Button getNewRecipeButton(){
        return newRecipeButton;
    }

    public Button getLogOutButton() {
        return logOutButton;
    }

    public ComboBox<String> getSortBox(){
        return this.sortBox;
    }

    public ComboBox<String> getFilterBox() {
        return this.filterBox;
    }
}
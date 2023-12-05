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

class DisplayFooter extends HBox {
    private Button editButton;
    private Button backButton;
    private Button deleteButton;
    private Button shareButton;
    DisplayFooter() {
        this.setPrefSize(500, 60);
        this.setStyle("-fx-background-color: #F0F8FF;");
        this.setSpacing(15);
        // set a default style for buttons - background color, font size, italics
        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";
        this.setAlignment(Pos.CENTER); // aligning the buttons to center

        editButton = new Button("Edit");
        backButton = new Button("Back");
        deleteButton = new Button("Delete");
        shareButton = new Button("Share");
        editButton.setStyle(defaultButtonStyle);
        backButton.setStyle(defaultButtonStyle);
        deleteButton.setStyle(defaultButtonStyle);
        shareButton.setStyle(defaultButtonStyle);
        this.getChildren().add(editButton);
        this.getChildren().add(backButton);
        this.getChildren().add(deleteButton);
        this.getChildren().add(shareButton);
        this.setAlignment(Pos.CENTER);
    }

    public Button getEditButton(){
        return editButton;
    }
    public Button getBackButton(){
        return backButton;
    }

    public Button getDeleteButton(){
        return deleteButton;
    }

    public Button getShareButton(){
        return shareButton;
    }

}
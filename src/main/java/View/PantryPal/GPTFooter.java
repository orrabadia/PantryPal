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

class GPTFooter extends HBox {
    private Button saveButton;
    private Button cancelButton;
    private Button reGenButton;
    GPTFooter() {
        this.setPrefSize(500, 60);
        this.setStyle("-fx-background-color: #F0F8FF;");
        this.setSpacing(15);
        // set a default style for buttons - background color, font size, italics
        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";
        this.setAlignment(Pos.CENTER); // aligning the buttons to center

        saveButton = new Button("Save");
        cancelButton = new Button("Cancel");
        reGenButton = new Button("Regenerate");
        saveButton.setStyle(defaultButtonStyle);
        cancelButton.setStyle(defaultButtonStyle);
        reGenButton.setStyle(defaultButtonStyle);
        this.getChildren().add(saveButton);
        this.getChildren().add(cancelButton);
        this.getChildren().add(reGenButton);
        this.setAlignment(Pos.CENTER);
    }

    public Button getSaveButton(){
        return saveButton;
    }
    public Button getCancelButton(){
        return cancelButton;
    }
    public Button getReGenButton(){
        return reGenButton;
    }
}

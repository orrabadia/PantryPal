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

class UserAccFooter extends HBox {
    private Button logInButton;
    private Button signUpButton;

    UserAccFooter() {
        this.setPrefSize(500, 60);
        this.setStyle("-fx-background-color: #F0F8FF;");
        this.setSpacing(15);
        // set a default style for buttons - background color, font size, italics
        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";
        this.setAlignment(Pos.CENTER); // aligning the buttons to center

        logInButton = new Button("Log In");
        signUpButton = new Button("Sign Up");
        logInButton.setStyle(defaultButtonStyle);
        signUpButton.setStyle(defaultButtonStyle);
        this.getChildren().add(logInButton);
        this.getChildren().add(signUpButton);
        this.setAlignment(Pos.CENTER);
    }

    public Button getLogInButton(){
        return logInButton;
    }
    public Button getSignUpButton(){
        return signUpButton;
    }

}
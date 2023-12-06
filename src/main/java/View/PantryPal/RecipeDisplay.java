package PantryPal;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.sound.sampled.AudioFormat;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * Page for detailed recipe display
 */
class RecipeDisplay extends BorderPane {
    private Header header;
    private DisplayFooter footer;
    private ImageView imgView;
    private Button editButton;
    private Button backButton;
    private Button deleteButton;

    private NavigationHandler handler;
    private ImageDisplayHandler iHandler;
    private UIRecipe r;


    RecipeDisplay(NavigationHandler handler) {
        this.handler = handler;
        // Initialise the header Object
        //header = new Header(r.getTitle().getText());
        header = new Header("YOU SHOULDNT BE HERE");
        // Initialise the Footer Object
        footer = new DisplayFooter();

        // Create a VBox in the center
        VBox centerBox = new VBox();
        centerBox.setSpacing(10);
        //get image and display
        imgView = new ImageView();
        try {
            FileInputStream input = new FileInputStream("./kriby.jpeg");
            Image image = new Image(input);

            imgView.setImage(image);
            imgView.setFitWidth(150);
            imgView.setFitHeight(150);
            imgView.setPreserveRatio(true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ScrollPane scrollPane1 = createScrollableBox("Ingredients: ");
        ScrollPane scrollPane2 = createScrollableBox("Instructions: ");
        // AppFrame mainAppFrame = ((AppFrame)this.handler.getMap().get("RecipeList").getRoot());
        // String username = mainAppFrame.getUserHandler().getUserName();
        // int index = mainAppFrame.getRecipeHandler().getRecipeList(((UserAccDisplay)this.handler.getMap().get("UserSL").getRoot()).getUHandler().getUserName()).get(r.getTitle().getText()).getIndex();;
        // String shareUrl = "http://localhost:8100/share/" + username + "/" + index;
        TextField shareLink = new TextField();
        // shareLink.setText(shareUrl);
        shareLink.setVisible(false);
        shareLink.setEditable(false);
        // ScrollPane scrollPane1 = createScrollableBox("Ingredients: " + r.getIngredients().toString());
        // ScrollPane scrollPane2 = createScrollableBox("Instructions: " + r.getRecipeInstructions().toString());

        centerBox.getChildren().addAll(imgView, scrollPane1, scrollPane2, shareLink);
        // Set the VBox in the center of the BorderPane
        this.setCenter(centerBox);
        // Add header to the top of the BorderPane
        this.setTop(header);
        // Add footer to the bottom of the BorderPane
        this.setBottom(footer);
        // Initialise Button Variables through the getters in Footer

        // Call Event Listeners for the Buttons
        addListeners();
    }

    public void setIDHandler(ImageDisplayHandler i){
        this.iHandler = i;
    }

    public void setUIR(UIRecipe recipe){
        this.r = recipe;
    }

    public void setImg(String filepath){
        try {
            FileInputStream input = new FileInputStream(filepath);
            Image image = new Image(input);

            imgView.setImage(image);
            imgView.setFitWidth(150);
            imgView.setFitHeight(150);
            imgView.setPreserveRatio(true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
  

    public void setTitle(String s){
        //called when displaying from handler, handler has blank one by default
        //access header settext
        header.setTitle(s);
    }


    public void setIngredients(String s){
        //called when displaying from handler, handler has blank one by default
        VBox v = (VBox) this.getCenter();
        //THIS SHOULD BE THE second ELEMENT IF IT CHANGES THINGS WILL NOT BE GOOD
        ScrollPane scroll1 = (ScrollPane)v.getChildren().get(1);
        TextArea textField = (TextArea) scroll1.getContent();
        textField.setText(s);
    }

    public void setInstructions(String s){
        //called when displaying from handler, handler has blank one by default
        VBox v = (VBox)this.getCenter();
        //THIS SHOULD BE THE third ELEMENT IF IT CHANGES THINGS WILL NOT BE GOOD
        ScrollPane scroll2 = (ScrollPane)v.getChildren().get(2);
        TextArea textField = (TextArea) scroll2.getContent();
        textField.setText(s);

    }

    public void setShare(String s){
        VBox v = (VBox)this.getCenter();
        TextField share = ((TextField)((VBox)this.getCenter()).getChildren().get(3));
        share.setText(s);
    }



    public void addListeners()
    {

        Button backButton = footer.getBackButton();
        backButton.setOnAction(e ->{
            if (this.footer.getBackButton().getText() == "Back"){
                // here we'll make our shareButton visible and our shareLink invisible
                this.footer.getShareButton().setVisible(true);
                ((TextField)((VBox)this.getCenter()).getChildren().get(3)).setVisible(false);
                // return back to main menu
                handler.menu();
            } else {
                //reverts the displayed ingredients back to orriginal (what is being saved in the rlist (not UIRList))
               //((TextArea)((ScrollPane)((VBox)this.getCenter()).getChildren().get(0)).getContent()).setText(((AppFrame)this.handler
               //.getMap().get("RecipeList").getRoot()).getRecipeHandler().getRecipeList().get(r.getTitle().getText()).getIngredients());
               
               ((TextArea)((ScrollPane)((VBox)this.getCenter()).getChildren().get(1)).getContent()).setText(r.getIngredients());
               //reverts the displayed instructions back to orriginal (what is being saved in the rlist (not UIRList))
               //((TextArea)((ScrollPane)((VBox)this.getCenter()).getChildren().get(1)).getContent()).setText(((AppFrame)this.handler
               //.getMap().get("RecipeList").getRoot()).getRecipeHandler().getRecipeList().get(r.getTitle().getText()).getInstructions());


               ((TextArea)((ScrollPane)((VBox)this.getCenter()).getChildren().get(2)).getContent()).setText(r.getRecipeInstructions());
                //sets textfields to non-editable
                ((TextArea)((ScrollPane)((VBox)this.getCenter()).getChildren().get(1)).getContent()).setEditable(false);
                ((TextArea)((ScrollPane)((VBox)this.getCenter()).getChildren().get(2)).getContent()).setEditable(false);

                //reverts buttons back
                this.footer.getBackButton().setText("Back");
                this.footer.getEditButton().setText("Edit");
            }
        });

        Button editButton = footer.getEditButton();
        editButton.setOnAction(e -> {

            if (this.footer.getEditButton().getText() == "Edit") {
                //sets textfields editable
                ((TextArea)((ScrollPane)((VBox)this.getCenter()).getChildren().get(1)).getContent()).setEditable(true);
                ((TextArea)((ScrollPane)((VBox)this.getCenter()).getChildren().get(2)).getContent()).setEditable(true);
                //changes button text
                this.footer.getEditButton().setText("Save");
                this.footer.getBackButton().setText("Cancel");
            } else {
                if (this.footer.getEditButton().getText() == "Save") {
                    //sets fields uneditable
                    ((TextArea)((ScrollPane)((VBox)this.getCenter()).getChildren().get(1)).getContent()).setEditable(false);
                    ((TextArea)((ScrollPane)((VBox)this.getCenter()).getChildren().get(2)).getContent()).setEditable(false);
                    /*
                    Gets the recipe from the recipelist which is gotten from the handler gotten from the appframe. Calls editRecipe()
                    from RecipeHandler, passing in the recipe which is retrieved from the appframes recipe handlers recipelist, the new
                    ingredients and the new instructions retrieved from the fields
                    */

                   /*ArrayList<Recipe> rList = ((AppFrame)this.handler.getMap().get("RecipeList").getRoot()).getRecipeHandler().getRecipeList();

                   for (Recipe temp : rList){
                        if(temp.getTitle() == r.getTitle().getText()){
                            break;
                        }
                   }
                    */
                   // not gonna work
                    ((AppFrame)this.handler.getMap().get("RecipeList").getRoot()).getRecipeHandler().editRecipe(
                    ((AppFrame)this.handler.getMap().get("RecipeList").getRoot()).getRecipeHandler()
                    .getRecipeList(((UserAccDisplay)this.handler.getMap().get("UserSL").getRoot()).getUHandler().getUserName()).get(r.getTitle().getText()),
                    ((TextArea)((ScrollPane)((VBox)this.getCenter()).getChildren().get(1)).getContent()).getText(),
                    ((TextArea)((ScrollPane)((VBox)this.getCenter()).getChildren().get(2)).getContent()).getText(), ((AppFrame)this.handler.getMap().get("RecipeList").getRoot()).getUserHandler().getUserName());
                    //Updatethe UIList
                    ((AppFrame)this.handler.getMap().get("RecipeList").getRoot()).getRecipeList().updateList(this.handler);
                    //Revert button text back
                    this.footer.getEditButton().setText("Edit");
                    this.footer.getBackButton().setText("Back");
                }
            }
        });

        Button deleteButton = footer.getDeleteButton();
        deleteButton.setOnAction(e -> {

            AppFrame mainAppFrame = ((AppFrame)this.handler.getMap().get("RecipeList").getRoot());
            //get username from handler
            String username = ((UserAccDisplay)this.handler.getMap().get("UserSL").getRoot()).getUHandler().getUserName();
            String title = r.getTitle().getText();
            //get index of recipe(the one used in backend)
            int indexValue = mainAppFrame.getRecipeHandler().getRecipeList(username).get(title).getIndex();
            //delete recipe
            mainAppFrame.getRecipeHandler().deleteRecipe(indexValue, mainAppFrame.getUserHandler().getUserName());
            mainAppFrame.getRecipeList().updateList(handler);

            //then call update UI recipe
            handler.menu();

        });

        Button shareButton = footer.getShareButton();
        shareButton.setOnAction(e -> {
            shareButton.setVisible(false);
            TextField share = ((TextField)((VBox)this.getCenter()).getChildren().get(3));
            share.setVisible(true);

            //(((TextField)((VBox)this.getCenter()).getChildren().get(2)).getContent()).setText(shareUrl);

            //((TextArea)((TextField)((VBox)this.getCenter()).getChildren().get(2)).getContent()).setVisible(true);


            // TextField shareLink = new TextField();
            // shareLink.setText(shareUrl);
            // shareLink.setEditable(false);
            // ((VBox)this.getCenter()).getChildren().add(shareLink);
        });

    }

    // Helper method to create a scrollable text box
    private ScrollPane createScrollableBox(String content) {
        TextArea textArea = new TextArea(content);

        ScrollPane scrollPane = new ScrollPane(textArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        return scrollPane;
    }
}
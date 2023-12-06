package PantryPal;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

class GPTResultsDisplay extends BorderPane{
    private Header header;
    private GPTFooter footer;
    private ImageView imgView;

    private String instructions;
    private NavigationHandler nHandler;
    private RecipeHandler rHandler;
    private CreateHandler cHandler;
    private UIRecipe r;
    private DallEHandler d;
    private String user;
    private int tempIndex;

    GPTResultsDisplay(NavigationHandler handler, CreateHandler cHandler) {
        this.nHandler = handler;
        this.cHandler = cHandler;
        // Initialise the header Object
        header = new Header(cHandler.getRecipe().getTitle());
        // Initialise the Footer Object
        footer = new GPTFooter();

        //initialize imageview
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

        // Create a VBox in the center
        VBox centerBox = new VBox();
        centerBox.setSpacing(10); // Adjust the spacing between scrollable boxes

        // Create two scrollable boxes with text
        ScrollPane scrollPane1 = createScrollableBox("Ingredients: " +cHandler.getRecipe().getIngredients());
        ScrollPane scrollPane2 = createScrollableBox("Instructions: "+ cHandler.getRecipe().getInstructions());

        centerBox.getChildren().addAll(imgView, scrollPane1, scrollPane2);

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

    public CreateHandler getCreateHandler(){
        return this.cHandler;
    }

    public void setIndex(int i){
        this.tempIndex = i;
    }

    public int getIndex(){
        return this.tempIndex;
    }

    public void setUser(String u){
        this.user = u;
    }

    public String getUser(){
        return this.user;
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

    public void setDallE(DallEHandler d){
        this.d = d;
    }

    public void setIngredients(String s){
        //called when displaying from handler, handler has blank one by default
        VBox v = (VBox)this.getCenter();
        //THIS SHOULD BE THE SECOND ELEMENT IF IT CHANGES THINGS WILL NOT BE GOOD
        ScrollPane scroll1 = (ScrollPane)v.getChildren().get(1);
        TextField textField = (TextField) scroll1.getContent();
        textField.setText(s);
    }

    public void setInstructions(String s){
        //called when displaying from handler, handler has blank one by default
        VBox v = (VBox)this.getCenter();
        //THIS SHOULD BE THE THIRD ELEMENT IF IT CHANGES THINGS WILL NOT BE GOOD
        ScrollPane scroll2 = (ScrollPane)v.getChildren().get(2);
        TextField textField = (TextField) scroll2.getContent();
        textField.setText(s);

    }

    public void addListeners()
    {
        Button saveButton = footer.getSaveButton();
        saveButton.setOnAction(e ->{
            //get recipehandler from navhandler
            System.out.println("1");
            HashMap<String,Scene> pagelist = nHandler.getPageList();
            //System.out.println(pagelist.get("RecipeList").getRoot().getClass().toString());
            AppFrame rlist = (AppFrame)pagelist.get("RecipeList").getRoot();
            RecipeHandler recipeHandler = rlist.getRecipeHandler();
            //add recipe to backend, check new backend, return to menu()
            recipeHandler.addRecipe(cHandler.getRecipe(), rlist.getUserHandler().getUserName());
            //recipeHandler.getRecipeList();
            //addrecipe above updates the recipehandler list
            UIRecipeList uiList = rlist.getRecipeList();
            //note that this calls get, uilist has the rhandler above
            uiList.updateList(nHandler);
            nHandler.menu();

        });
        Button cancelButton = footer.getCancelButton();
        cancelButton.setOnAction(e->{
            //delete from server map
            d.delete(user, String.valueOf(tempIndex));
            nHandler.menu();
        });
        Button reGenButton = footer.getReGenButton();
        reGenButton.setOnAction(e->{
            RequestHandler reqHandler = ((RecordAppFrame)this.nHandler.getMap().get("RecordIngredients").getRoot()).getRequestHandler();
            Recipe Rrecipe = cHandler.getRecipe();
            String recipe = reqHandler.performGenerateRequest("PUT", Rrecipe.getMealType().toString(), Rrecipe.getIngredients());
            //reset the title
            String title = recipe.substring(0,recipe.indexOf("~"));
            //take out the newlines and returns for formatting
            String strippedString = title.replaceAll("[\\n\\r]+", "");
            Rrecipe.setTitle(strippedString);
            this.setTitle(strippedString);
            Rrecipe.setInstructions(recipe);
            GPTResultsDisplay gptResD = (GPTResultsDisplay)this.nHandler.getMap().get("GptResults").getRoot();
            VBox vBox = (VBox)gptResD.getCenter();
            ((ScrollPane)vBox.getChildren().get(2)).setContent(createScrollableBox("Instructions: " + recipe));

            //delete old image from server
            d.delete(user, String.valueOf(tempIndex));
            //REGENERATE A NEW IMAGE AND SET IT
            DallEHandler d = new DallEHandler();
            String ingredients = this.getCreateHandler().getRecipe().getIngredients();
            String url = d.generate(title, user, String.valueOf(tempIndex), ingredients);
            
            //create images directory in view
            Path imagesDir = Paths.get("images");
            try {
                if (!Files.exists(imagesDir)) {
                    Files.createDirectory(imagesDir);
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            //download and replace current image
            Path imagePath = imagesDir.resolve(user + " " +tempIndex + ".jpg");
            try (InputStream in = new URI(url).toURL().openStream()) {
                Files.copy(in, imagePath, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Image saved successfully: " + imagePath);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            //set the image to be the recipe
            this.setImg(imagePath.toString());
            });
    }

    // Helper method to create a scrollable text box
    private ScrollPane createScrollableBox(String content) {
        TextArea textArea = new TextArea(content);
        textArea.setEditable(false);
        ScrollPane scrollPane = new ScrollPane(textArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        return scrollPane;
    }
}
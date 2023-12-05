package PantryPal;

import java.util.HashMap;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

class GPTResultsDisplay extends BorderPane{
    private Header header;
    private GPTFooter footer;

    private String instructions;
    private NavigationHandler nHandler;
    private RecipeHandler rHandler;
    private CreateHandler cHandler;
    private UIRecipe r;

    GPTResultsDisplay(NavigationHandler handler, CreateHandler cHandler) {
        this.nHandler = handler;
        this.cHandler = cHandler;
        // Initialise the header Object
        header = new Header(cHandler.getRecipe().getTitle());
        // Initialise the Footer Object
        footer = new GPTFooter();

        // Create a VBox in the center
        VBox centerBox = new VBox();
        centerBox.setSpacing(10); // Adjust the spacing between scrollable boxes

        // Create two scrollable boxes with text
        ScrollPane scrollPane1 = createScrollableBox("Ingredients: " +cHandler.getRecipe().getIngredients());
        ScrollPane scrollPane2 = createScrollableBox("Instructions: "+ cHandler.getRecipe().getInstructions());

        centerBox.getChildren().addAll(scrollPane1, scrollPane2);

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

    public void setTitle(String s){
        //called when displaying from handler, handler has blank one by default
        //access header settext
        header.setTitle(s);
    }

    public void setIngredients(String s){
        //called when displaying from handler, handler has blank one by default
        VBox v = (VBox)this.getCenter();
        //THIS SHOULD BE THE FIRST ELEMENT IF IT CHANGES THINGS WILL NOT BE GOOD
        ScrollPane scroll1 = (ScrollPane)v.getChildren().get(0);
        TextField textField = (TextField) scroll1.getContent();
        textField.setText(s);
    }

    public void setInstructions(String s){
        //called when displaying from handler, handler has blank one by default
        VBox v = (VBox)this.getCenter();
        //THIS SHOULD BE THE FIRST ELEMENT IF IT CHANGES THINGS WILL NOT BE GOOD
        ScrollPane scroll2 = (ScrollPane)v.getChildren().get(1);
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
            ((ScrollPane)vBox.getChildren().get(1)).setContent(createScrollableBox("Ingredients: " + recipe));
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
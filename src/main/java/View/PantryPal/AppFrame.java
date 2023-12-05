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

/**
 * Class Copied from Lab 1 for AppFrame
 */
class AppFrame extends BorderPane {
    private Header header;
    private ListFooter footer;
    private UIRecipeList recipeList;
    private Button newRecipeButton;
    private Button logOutButton;
    private NavigationHandler nHandler;
    private UserHandler uHandler;
    private RecipeList list;
    private RecipeHandler rHandler;
    //private String defaultTextFieldStyle;
    private int sortOrder;
    private ComboBox<String> sortBox;

    private String filterType;
    private ComboBox<String> filterBox;

    AppFrame(NavigationHandler handler, UserHandler uHandler) {
        this.filterType = "All";
        this.sortOrder = 0;
        this.nHandler = handler;
        // Initialise the header Object
        header = new Header("Recipe List");
        //create new recipelist and handler
        list = new RecipeList();
        System.out.println("refreshing");
        //TODO: get list based on backend not csv
        //list.refresh();
        rHandler = new RecipeHandler(list);
        rHandler.getRecipeList(uHandler.getUserName());
        //create ui recipe list to display recipes
        //in its initialization it will get from backend and display
        //can also call updatelist later to reget from backend
        recipeList = new UIRecipeList(rHandler,nHandler);
        // Initialise the recipelist footer Object
        footer = new ListFooter();

        ScrollPane Scroller = new ScrollPane(recipeList);
        Scroller.setFitToHeight(true);
        Scroller.setFitToWidth(true);
        // Add header to the top of the BorderPane
        this.setTop(header);
        // Add scroller to the centre of the BorderPane
        this.setCenter(Scroller);

        // Add footer to the bottom of the BorderPane
        this.setBottom(footer);
        // Initialise Button Variables through the getters in Footer
        newRecipeButton = footer.getNewRecipeButton();
        logOutButton = footer.getLogOutButton();
        sortBox = footer.getSortBox();
        filterBox = footer.getFilterBox();

        // Call Event Listeners for the Buttons
        addListeners();
    }

    public void addListeners()
    {
    newRecipeButton.setOnAction(e -> {

        //send to record page, and also add a recipe for test purposes
        CreateHandler createHandler = new CreateHandler();
        nHandler.recordMeal(createHandler);
    });

    logOutButton.setOnAction(e1 ->{
        //reset filter to default
        this.filterType = "All";

        //reset sort order and button
        this.sortOrder = 0;
        sortBox.setValue("Newest to Oldest");
        //on logout load details
        UserAccDisplay login = (UserAccDisplay)nHandler.getMap().get("UserSL").getRoot();
        ArrayList<String> details = AutoLogin.load();
        if(!details.isEmpty() && login.remembered()){
            String user = details.get(0);
            String pass = details.get(1);
            login.load(user, pass);
        }
        nHandler.userSL();
    });

    sortBox.setOnAction(e2->{
        String check = sortBox.getValue();
        if(check.equals("Newest to Oldest")){
            this.sortOrder = 0;
            System.out.println("SORTORDER IS " +sortOrder);
        } else if (check.equals("Oldest to Newest")){
            this.sortOrder = 1;
            System.out.println("SORTORDER IS " +sortOrder);
        } else if (check.equals("Alphabetical")){
            this.sortOrder = 2;
            System.out.println("SORTORDER IS " +sortOrder);
        } else if (check.equals("Reverse Alphabetical")){
            this.sortOrder = 3;
            System.out.println("SORTORDER IS " +sortOrder);
        } else {
            System.out.println("Unimplemented sorting method");
        }
        recipeList.updateList(nHandler);
    });


    filterBox.setOnAction(e3-> {
        String filter = filterBox.getValue();
        if(filter.equals("All")) {
            this.filterType = "All";
            System.out.println("FILTER TYPE IS " + filterType);
        }
        else if(filter.equals("Breakfast")) {
            this.filterType = "Breakfast";
            System.out.println("FILTER TYPE IS " + filterType);
        }
        else if(filter.equals("Lunch")) {
            this.filterType = "Lunch";
            System.out.println("FILTER TYPE IS " + filterType);
        }
        else if(filter.equals("Dinner")) {
            this.filterType = "Dinner";
            System.out.println("FILTER TYPE IS " + filterType);

        }
        recipeList.updateList(nHandler);
    });

    }

    // public void debugAddRecipe(String title, String meal, String ingredients, String recipeinstructions){
    //     // just dummy values for now, gotta get the tokens from Chat GPT and parse them and pass them into here
    //     UIRecipe recipe = new UIRecipe(new Text(title), new Text(meal),ingredients, recipeinstructions, this.nHandler);
    //     // Add recipe to recipelist
    //     recipeList.getChildren().add(recipe);
    //     recipeList.updateRecipeIndices();
    // }

    public NavigationHandler getNavHandler(){
        return this.nHandler;
    }

    public RecipeHandler getRecipeHandler(){
        return this.rHandler;
    }

    public UIRecipeList getRecipeList(){
        return this.recipeList;
    }

    public UserHandler getUserHandler() {
        return this.uHandler;
    }

    public void setUHandler(UserHandler uHandler) {
        this.uHandler = uHandler;
    }

    public void setSortOrder(int set){
        this.sortOrder = set;
    }

    public int getSortOrder(){
        return this.sortOrder;
    }

    public void setFilterType(String fil) {
        this.filterType = fil;
    }

    public String getFilterType() {
        return this.filterType;
    }

}
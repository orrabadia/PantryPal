package PantryPal;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

class UIRecipeList extends VBox { // extends HBox?
    //private RecipeList rList;
    private NavigationHandler nHandler;
    private RecipeHandler rHandler;
    private FilterHandler fHandler;


    //u handler parameter?
    // new RecipeList
    UIRecipeList(RecipeHandler rHandler, NavigationHandler nHandler) {
        //this.rList.setList(rHandler.getRecipeList());
        this.rHandler = rHandler;
        this.nHandler = nHandler;
        this.fHandler = fHandler;
        this.updateList(nHandler);
        // UI elements
        this.setSpacing(5); // sets spacing between tasks
        this.setPrefSize(500, 560);
        this.setStyle("-fx-background-color: #F0F8FF;");
    }

    //taken from lab 1
    public void updateRecipeIndices() {
        int index = 1;
        for (int i = 0; i < this.getChildren().size(); i++) {
            if (this.getChildren().get(i) instanceof UIRecipe) {
                ((UIRecipe) this.getChildren().get(i)).setRecipeIndex(index);
                index++;
            }
        }
    }

    /**
     *
     * Updates list based on what is currently in backend
     * takes behavior based on what style of sort you want to use-default is newest first(0)
     * later add more to have different sorting orders
     */
    public void updateList(NavigationHandler nHandler){

        //get sortorder from appframe
        int sortorder;
        //get filterType from appframe
        String filterType;
         try {
            AppFrame a = (AppFrame)nHandler.getMap().get("RecipeList").getRoot();
            sortorder = a.getSortOrder();
            filterType = a.getFilterType();

        } catch (NullPointerException e) {
            // if you get nullpointer, this means it hasn't been initialized yet(first call), set to 0
            sortorder = 0;
            filterType = "All";
        }

        //takes behavior based on what style of sort you want to use-default is revchron
        this.getChildren().clear();
        //replace current list with whats in backend

        // added .getList()
        ArrayList<Recipe> list = rHandler.getRecipeList(((UserAccDisplay)this.nHandler.getMap().get("UserSL").getRoot()).getUHandler().getUserName()).getList();
        System.out.println("LIST SIZE:" + list.size());
        //sort last to first

        if(sortorder == 0){
            //sort backwards if order 0
            list = SortHandler.sortRevChronological(list);
        } else if (sortorder == 1){
            //1 is oldest to newest
            list = SortHandler.sortChronological(list);
        } else if (sortorder == 2){
            //2 is alphabetical
            list = SortHandler.sortAlphabetical(list);
        } else if (sortorder == 3){
            //3 is reverse alphabetical
            list = SortHandler.sortRevAlphabetical(list);
        }

        //Calls on handler to filter the list
        list = FilterHandler.filterMealType(list, filterType);

        for(Recipe r : list){
            int index = r.getIndex();
            String title = r.getTitle();
            String mealType = r.getMealType();
            String ingredients = r.getIngredients();
            String instructions = r.getInstructions();
            UIRecipe uiR = new UIRecipe(new Text(title), new Text(mealType), ingredients, instructions, index, nHandler);
            this.getChildren().add(uiR);
            this.updateRecipeIndices();
        }
    }

    //add when delete implementing
    // public void updateRecipeIndices() {
    // int index = 1;
    // for (int i = 0; i < this.getChildren(i); i++) {
    // if (this.getChildren().get(i) instanceof Recipe) {
    // ((Recipe) this.getChildren().get(i)).setRecipeIndex(index);
    // index++;
    // }
    // }
    // }
}

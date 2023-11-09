package PantryPal;

public class RecipeHandler {
    private RecipeList list;
    RecipeHandler(RecipeList list){
        this.list = list;
    }

    public void addRecipe(Recipe r){
        //eventually these will call database methods, but right now we just call on the recipelist class
        //this would be a post request otherwise
        this.list.add(r);
    }

    public void deleteRecipe(String title){
        this.list.remove(title);
    }

    //add methods later for edit
}

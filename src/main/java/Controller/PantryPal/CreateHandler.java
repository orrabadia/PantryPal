package PantryPal;

public class CreateHandler {
    //class for handling passing on a created recipe
    private Recipe recipe;
    CreateHandler() {
        recipe = new Recipe();
    }
    public Recipe getRecipe(){
        return recipe;
    }
}

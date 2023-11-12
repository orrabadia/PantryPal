package PantryPal;

public class CreateHandler {
    private Recipe recipe;
    CreateHandler() {
        recipe = new Recipe();
    }
    public Recipe getRecipe(){
        return recipe;
    }
}

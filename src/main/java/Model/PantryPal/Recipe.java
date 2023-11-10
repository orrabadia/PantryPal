package PantryPal;

public class Recipe {
    private String title;
    private String mealType;
    private String ingredients;
    private String instructions;
    private RecipeHandler rHandler;
    Recipe(RecipeHandler rHandler, String title, String mealType, String ingredients, String instructions){
        this.rHandler = rHandler;
        this.title = title;
        this.mealType = mealType;
        this.ingredients = ingredients;
        this.instructions = instructions;
    }

    public RecipeHandler getRHandler(){
        return this.rHandler;
    }
    
    public String getTitle(){
        return this.title;
    }

    public String getMealType(){
        return this.mealType;
    }

    public String getIngredients(){
        return this.ingredients;
    }

    public String getInstructions(){
        return this.instructions;
    }
}

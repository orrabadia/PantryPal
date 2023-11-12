package PantryPal;

public class Recipe {
    private String title;
    private String mealType;
    private String ingredients;
    private String instructions;
    Recipe(String title, String mealType, String ingredients, String instructions){
        this.title = title;
        this.mealType = mealType;
        this.ingredients = ingredients;
        this.instructions = instructions;
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

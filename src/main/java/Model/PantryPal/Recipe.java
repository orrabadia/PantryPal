package PantryPal;

public class Recipe {
    private String title;
    private String mealType;
    private String ingredients;
    private String instructions;
    private int index;
    Recipe() {
        title="";
        mealType="";
        ingredients="";
        instructions="";
    }
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

    public void setTitle(String title) {
        this.title = title;
    }
    public void setMealType(String mealType) {
        this.mealType = mealType;
    }
    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }
    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
    public void setIndex(int i){
        this.index = i;
    }
    public int getIndex(){
        return this.index;
    }
}

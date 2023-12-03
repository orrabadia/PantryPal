package PantryPal;

public class HTMLBuilder {
    String title;
    String mealType;
    String ingredients;
    String instructions;
    HTMLBuilder(String title, String mealType, String ingredients, String instructions) {
        this.title = title;
        this.mealType = mealType;
        this.ingredients = ingredients;
        this.instructions = instructions;
    }
    public StringBuilder buildHTML() {
        StringBuilder htmlBuilder = new StringBuilder();
        htmlBuilder
              .append("<html>")
              .append("<body>")
              .append("<h1>")
              .append("Title: ")
              .append(title)
              .append("<br>")
              .append("Meal Type: ")
              .append(mealType)
              .append("<br>")
              .append("Ingredients: ")
              .append(ingredients)
              .append("<br>")
              .append("Instructions: ")
              .append(instructions)
              .append("<br>")
              .append("<img src=\"https://upload.wikimedia.org/wikipedia/commons/8/8a/Banana-Single.jpg\">")  // works, is giant banana
              .append("<br>")
              .append("</h1>")
              .append("</body>")
              .append("</html>");
        return htmlBuilder;
    }
}

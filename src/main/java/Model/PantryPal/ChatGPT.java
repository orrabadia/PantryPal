package PantryPal;

import java.io.IOException;
import java.net.URISyntaxException;

public interface ChatGPT {
    public String generateRecipe(String mealType, String ingredients) throws IOException, InterruptedException, URISyntaxException;
}

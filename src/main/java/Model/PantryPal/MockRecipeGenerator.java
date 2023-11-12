package PantryPal;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class MockRecipeGenerator implements ChatGPT {
   public String generateRecipe(String mealType, String ingredients) throws IOException, InterruptedException, URISyntaxException {
        String ret = "Maybe you should just go on a diet.";
        return ret;
    }
}

package PantryPal;

import java.io.IOException;
import java.net.URISyntaxException;

public interface DallE {
    public void generateRecipeImage(String title, String ingredients) throws IOException, InterruptedException, URISyntaxException;
}
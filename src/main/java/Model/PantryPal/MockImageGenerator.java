package PantryPal;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MockImageGenerator implements DallE {
   
    public void generateRecipeImage(String title, String ingredients) throws IOException, InterruptedException, URISyntaxException {
    // image of Pizza
    String imageURL = "https://assets.teenvogue.com/photos/5ab665d06d36ed4396878433/master/pass/GettyImages-519526540.jpg";
    try (
            InputStream in = new URI(imageURL).toURL().openStream())
        {
            Files.copy(in, Paths.get("Pizza.jpg"));
        }
    }
}

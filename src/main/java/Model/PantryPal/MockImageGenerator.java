package PantryPal;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MockImageGenerator implements DallE {
   
    public String generateRecipeImage(String title, String ingredients) throws IOException, InterruptedException, URISyntaxException {
    // image of Pizza
    String imageURL = "https://assets.teenvogue.com/photos/5ab665d06d36ed4396878433/master/pass/GettyImages-519526540.jpg";
    return imageURL;
    }
}

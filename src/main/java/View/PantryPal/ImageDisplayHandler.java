package PantryPal;

import java.util.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
/**handles image displaying and saving locally */
public class ImageDisplayHandler {
    //map where key is the index, and then value is a filepath containing thing for that image, defaults to null
    Map<Integer, String> images;
    
    //add an instance of this to navhandler, and a setter 
    //refactor uirecipe to have index
    //uirecipe needs access to add a new one when clicking display recipe
    //when you click displayrecipe, it calls navhandler display recipe(add a pass of the current index)
    //which sets field on recipedisplay
    public ImageDisplayHandler(){
        images = new HashMap<>();
    }

    //add a method to setimage(in rd), this is called in navhandler
    //navhandler will call method to check if exists, if not then get from dalle and store

    //then set

    //method to get url from dalle
    String getUrl(String title, String ingredients){
        DallEHandler d = new DallEHandler();
        return d.generate(title, ingredients);
    }

    //method to get filepath given index
    String getFilePath(Integer index){
        return images.get(index);
    }

    //method to create filepath and downlaod+store image(username/index)-given url string and index
    //note now filehandler must catch this
    void store(String url, String index, String username) throws IOException, InterruptedException, URISyntaxException{
        // create images directory in view
        Path imagesDir = Paths.get("images");
        if (!Files.exists(imagesDir)) {
            Files.createDirectory(imagesDir);
        }

        // download  image and save it as username/index.jpg in the 'images' directory
        try (InputStream in = new URI(url).toURL().openStream()) {
            Path imagePath = Paths.get("images", username + "/" + index + ".jpg");
            Files.copy(in, imagePath);
        }
    }

}

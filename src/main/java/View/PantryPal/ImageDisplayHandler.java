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
    //top level map maps users to their specific map of image links
    HashMap<String, HashMap<Integer, String>> images;
    HashMap<Integer, String> userMap;
    //navhandler has access to appframe, which has userhandler and current user
    
    //refactors:
    //add an instance of this to navhandler, and a setter/getter
    //refactor uirecipe to have index
    //add one of these to recipedisplay, and a setuser
    //on display recipe delete, call the delete method(already have index and user)

    //recipedisplay must have access to this
    //when you call the stuff for recipedisplay, have a setter method
    //that will set its imagedisplayhandler to this(and associate with user each time)

    //workflow:
    //in UI recipe, when you click displayrecipe, it calls navhandler display recipe(pass uirecipe)
    //in that method, it calls setuser(get userhandler from appframe) (do each time you display)
    //also do set IDHandler on the displayrecipepage
    //then check instance map w/indexto see if exists(getusermap)

    //if not exists in usermap, do getURl and call store

    //else get filepath from usermap

    //add a method to setimage(in rd), this is called in navhandler

    //on delete(recipedisplay) must delete it from map
    //should be associated from the last time you ran it, pass index+user

    public ImageDisplayHandler(){
        images = new HashMap<>();
        this.userMap = null;
    }

    /**associate this map with user, called every time you display*/
    void setUser(String user){
        this.userMap = images.get(user);
        if(this.userMap == null){
            this.userMap = new HashMap<>();
            images.put(user, this.userMap);
        }
    }

    /** method to get url from dalle */
    String getUrl(String title, String ingredients){
        DallEHandler d = new DallEHandler();
        return d.generate(title, ingredients);
    }

    /** method to get filepath given index-returns null if nonexistent*/
    String getFilePath(Integer index){
        return userMap.get(index);
    }

    //method to create filepath and downlaod+store image(username/index)-given url string and index
    //note now filehandler must catch this
    void store(String url, int index, String username) throws IOException, InterruptedException, URISyntaxException{
        // create images directory in view
        Path imagesDir = Paths.get("images");
        if (!Files.exists(imagesDir)) {
            Files.createDirectory(imagesDir);
        }

        // download  image and save it as username/index.jpg in the 'images' directory
        try (InputStream in = new URI(url).toURL().openStream()) {
            Path imagePath = Paths.get("images", username + "/" + index + ".jpg");
            Files.copy(in, imagePath);
            this.userMap.put(index, imagePath.toString());
        }

        //TODO: SET IN MAP
    }

     HashMap<Integer, String> getUserMap(){
        return this.userMap;
     }

    void delete(String user, int index){
        this.userMap.remove(index);    
    }

}

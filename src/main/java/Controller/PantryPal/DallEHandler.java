package PantryPal;

import java.io.IOException;
import java.net.URISyntaxException;

public class DallEHandler {
    private RequestHandler r;
    DallEHandler(){
        r = new RequestHandler();
    }

    /** return string of url, will download and save image locally */
    public String generate(String title, String user, String index, String ingredients){
        return r.performImageRequest("POST", title, user, index, ingredients);
    }

    /** delete image from server, used when showing temp image before saving recipe */
    public void delete(String user, String index){
        r.performImageRequest("DELETE", "", user, index, "");
    }
}

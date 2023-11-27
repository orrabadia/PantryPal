package PantryPal;

import java.io.IOException;
import java.net.URISyntaxException;

public class UserHandler {
    private RequestHandler reqHandler;
    //private MongoDB db;
    
    UserHandler(/*String user, String pass*/) {
        reqHandler = new RequestHandler();
        //db = new MongoDB()
        
    }

    public void putUser(String user, String pass) {
        String ret = reqHandler.performUserRequest("PUT", user, pass);
    }
    public String getUser(String user, String pass) {
        String ret = reqHandler.performUserRequest("GET", user, pass);
        return ret;
    }
}
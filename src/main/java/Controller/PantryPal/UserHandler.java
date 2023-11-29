package PantryPal;

import java.io.IOException;
import java.net.URISyntaxException;

public class UserHandler {
    private RequestHandler reqHandler;
    private String user;
    private String pass;

    private AppFrame appFrame;
    //private MongoDB db;
    
    UserHandler(/*String user, String pass*/) {
        reqHandler = new RequestHandler();
        //db = new MongoDB()
        
    }

    public void setAppFrame(AppFrame appFrame){
        this.appFrame = appFrame;
    }

    public void setUser(String user){
        this.user = user;
    }
    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getUserName(){
        return this.user;
    }

    public AppFrame getAppFrame() {
        return this.appFrame;
    }

    public void putUser(String user, String pass) {
        String ret = reqHandler.performUserRequest("PUT", user, pass);
    }
    public String getUser(String user, String pass) {
        String ret = reqHandler.performUserRequest("GET", user, pass);
        return ret;
    }
  

    
}
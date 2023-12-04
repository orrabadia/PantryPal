package PantryPal;

import java.io.IOException;
import java.net.URISyntaxException;

public class DallEHandler {
    private DallE d;
    DallEHandler(){
        d = new ImageGenerator();
    }
    
    DallEHandler(boolean mock){
        //if constructed with mock true, then mock it for testing purposes
        if (mock) {
            d = new MockImageGenerator();
        }
    }

    // don't know if it should be void return type
    public String generate(String title, String ingredients){
        String ret = "ERROR";
        try {
            d.generateRecipeImage(title, ingredients);
            ret = "SUCCESS";
        } catch (IOException e1) {
            System.out.println("IOEXCEPTION" + e1.toString());
        } catch (InterruptedException e2) {
            System.out.println("INTERRUPTEDEXCEPTION" + e2.toString());
        } catch (URISyntaxException e3){
            System.out.println("URISYNTAXEXCEPTION" + e3.toString());
        }
        return ret;
    }
}

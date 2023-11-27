package PantryPal;

import java.io.IOException;
import java.net.URISyntaxException;

public class GPTHandler {
    private ChatGPT c;
    GPTHandler(){
        c = new RecipeGenerator();
    }
    
    GPTHandler(boolean mock){
        //if constructed with mock true, then mock it for testing purposes
        if(mock){
            c = new MockRecipeGenerator();
        }
    }

    public String generate(String meal, String ingredients){
        String ret = "ERROR";
        try {
            ret = c.generateRecipe(meal, ingredients);
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

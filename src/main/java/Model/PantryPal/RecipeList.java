package PantryPal;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class RecipeList {
    private ArrayList<Recipe> list;
    RecipeList(){
        list = new ArrayList<>();
    }
    public ArrayList<Recipe> getList(){
        return this.list;
    }

    public void add(Recipe r){
        //add and update csv
        this.list.add(r);
        this.update();
    }

    public void remove(String title){
        Recipe remove = null;
        for(Recipe r: list){
            if(r.getTitle().equals(title)){
                remove = r;
            }
        }
        if(remove != null){
            this.list.remove(remove);
        }
        //remove and update csv
        this.update();
    }

    public void clear(){
        this.list.clear();
        this.update();
    }

    public Recipe get(String title){
        Recipe ret;
        for(Recipe r: list){
            if(r.getTitle().equals(title)){
                return r;
            }
        }
        ret = null;
        return ret;
    }

    public int size(){
        return this.list.size();
    }

    /**update the backend(csv in this case from the recipelist)
    */
    public void update(){
        try {
            ArrayList<Recipe> recipes = list;
            ArrayList<String> recipeWrite = new ArrayList<>();
            FileWriter f = new FileWriter("save.csv");
            for (Recipe i: recipes) {
                recipeWrite.add(i.getTitle());
                recipeWrite.add(i.getMealType());
                recipeWrite.add(i.getIngredients());
                recipeWrite.add(i.getInstructions());
            }
            // f.append("title");
            // f.append(",");
            // f.append("mealtype");
            // f.append(",");
            // f.append("ingredients");
            // f.append(",");
            // f.append("instructions");
            // f.append(",");
            // f.write("\n");
            for(int i = 0; i<recipeWrite.size();i++){
                String s = recipeWrite.get(i);
                if(i == recipeWrite.size()-1){
                    f.append(s);
                } else {
                    f.append(s);
                    f.append(",");                }
                if((i + 1) % 4 == 0){
                    //if multiple of 4, add a newline
                    f.write("\n");
                }
            }
            f.close();
        } catch (Exception e) {
            System.out.println("save error");
            e.printStackTrace();
        }
    }

    /**get list based on backend csv, also updates this current list
    */
    public void refresh(){
        ArrayList <Recipe> rlist = new ArrayList<>();
        String csvFile = "./save.csv";
        this.list.clear();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                // split for comma
                String[] fields = line.split(",");

                // get each field
                if (fields.length >= 4) {
                    String title = fields[0].trim();
                    String mealtype = fields[1].trim();
                    String ingredients = fields[2].trim();
                    String instructions = fields[3].trim();
                    Recipe r = new Recipe(title, mealtype, ingredients, instructions);
                    rlist.add(r);
                    // update list
                    // System.out.println("Title: " + title);
                    // System.out.println("Meal Type: " + mealtype);
                    // System.out.println("Ingredients: " + ingredients);
                    // System.out.println("Instructions: " + instructions);

                } 
            }
            //System.out.println(rlist.toString());
            this.list = rlist;
            //System.out.println(this.list.toString());
        } catch (IOException e) {
            System.out.println("No File found");
            e.printStackTrace();
        }
    }

    public void setList(ArrayList<Recipe> set){
        this.list = set;
    }

    /**return current csv list as string for http sending
    */
    public String stringify(){
        String csvFile = "./save.csv";
        this.list.clear();
        String ret = "";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                // split for comma
                String[] fields = line.split(",");

                // get each field
                if (fields.length >= 4) {
                    String title = fields[0].trim();
                    String mealtype = fields[1].trim();
                    String ingredients = fields[2].trim();
                    String instructions = fields[3].trim();
                    ret = ret + title + "," + mealtype + "," + ingredients + "," + instructions + "\n";
                    //add a new line of recipe info
                } 
            }
            //System.out.println(rlist.toString());
            return ret;
            //System.out.println(this.list.toString());
        } catch (IOException e) {
            System.out.println("No File found");
            e.printStackTrace();
        }
        return ret;
    }
}
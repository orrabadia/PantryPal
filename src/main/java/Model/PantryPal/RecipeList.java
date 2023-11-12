package PantryPal;

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
        this.list.add(r);
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
    }

    public void clear(){
        this.list.clear();
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
}
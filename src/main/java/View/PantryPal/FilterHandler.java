package PantryPal;

import java.util.ArrayList;

public class FilterHandler {
    public static ArrayList<Recipe> filterMealType(ArrayList<Recipe> inputList, String filter) {
        ArrayList<Recipe> filteredList = new ArrayList<Recipe>();
        for(Recipe r : inputList){
            if(filter.equals( "All")) {
                filteredList.add(r);
            }
            else if(filter.equals("Breakfast") && r.getMealType().contains("Breakfast")) {
                filteredList.add(r);
            }
            else if(filter.equals("Lunch") && r.getMealType().contains("Lunch")) {
                filteredList.add(r);
            }
            else if(filter.equals("Dinner") && r.getMealType().contains("Dinner")) {
                filteredList.add(r);
            }
        }
        return filteredList;
    }
}

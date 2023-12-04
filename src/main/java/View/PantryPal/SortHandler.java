package PantryPal;

import java.util.*;

public class SortHandler {

  public static ArrayList<Recipe> sortRevChronological(ArrayList<Recipe> input){
    revChronComparator comp = new revChronComparator();
    Collections.sort(input, comp);
    return input;
  }

  public static ArrayList<Recipe> sortChronological(ArrayList<Recipe> input) {
    chronComparator comp = new chronComparator();
    Collections.sort(input, comp);
    return input;
  }

  public static ArrayList<Recipe> sortAlphabetical(ArrayList<Recipe> input) {
    alphaComparator comp = new alphaComparator();
    Collections.sort(input, comp);
    return input;
  }

  public static ArrayList<Recipe> sortRevAlphabetical(ArrayList<Recipe> input) {
    revAlphaComparator comp = new revAlphaComparator();
    Collections.sort(input, comp);
    return input;
  }
}

class revChronComparator implements Comparator<Recipe> {

  @Override
  public int compare(Recipe r1, Recipe r2) {
    return Integer.compare(r2.getIndex(), r1.getIndex());
  }
}

class chronComparator implements Comparator<Recipe> {

  @Override
  public int compare(Recipe r1, Recipe r2) {
    return Integer.compare(r1.getIndex(), r2.getIndex());
  }
}

class alphaComparator implements Comparator<Recipe> {

  @Override
  public int compare(Recipe r1, Recipe r2) {
    return r1.getTitle().compareTo(r2.getTitle());
  }
}

class revAlphaComparator implements Comparator<Recipe> {

  @Override
  public int compare(Recipe r1, Recipe r2) {
    return r2.getTitle().compareTo(r1.getTitle());
  }
}

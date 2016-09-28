package ljuboandtedi.fridger.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by NoLight on 27.9.2016 г..
 */

public class Recipe {
    ArrayList<String> ingredientLines;
    HashMap<String,Double> flavors;
    HashMap<String,Double> nutritions;
    String name;
    String servings;
    String timeForPrepare;
    String bigPicUrl;
    double rating;

    public Recipe(ArrayList<String> ingredientLines, HashMap<String, Double> flavors, HashMap<String, Double> nutritions, String name, String servings, String timeForPrepare, double rating) {
        this.ingredientLines = ingredientLines;
        this.flavors = flavors;
        this.nutritions = nutritions;
        this.name = name;
        this.servings = servings;
        this.timeForPrepare = timeForPrepare;
        this.rating = rating;

    }

    public ArrayList<String> getIngredientLines() {
        return ingredientLines;
    }

    public HashMap<String, Double> getFlavors() {
        return flavors;
    }

    public HashMap<String, Double> getNutritions() {
        return nutritions;
    }

    public String getName() {
        return name;
    }

    public String getServings() {
        return servings;
    }

    public String getTimeForPrepare() {
        return timeForPrepare;
    }

    public String getBigPicUrl() {
        return bigPicUrl;
    }

    public double getRating() {
        return rating;
    }
}

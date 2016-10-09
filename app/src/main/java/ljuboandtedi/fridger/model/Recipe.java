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
    String id;
    String numberOfServings;
    double rating;

    public Recipe(ArrayList<String> ingredientLines, HashMap<String, Double> flavors, HashMap<String, Double> nutritions, String name, String servings, String timeForPrepare, double rating,String bigPicUrl,String id,String numberOfServings) {
        this.ingredientLines = ingredientLines;
        this.flavors = flavors;
        this.nutritions = nutritions;
        this.name = name;
        this.servings = servings;
        this.timeForPrepare = timeForPrepare;
        this.rating = rating;
        this.bigPicUrl = bigPicUrl;
        this.id = id;
        this.numberOfServings = numberOfServings;
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

    public String getId() {
        return id;
    }

    public String getNumberOfServings() {
        return numberOfServings;
    }
}

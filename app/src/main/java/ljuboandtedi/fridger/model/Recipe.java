package ljuboandtedi.fridger.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by NoLight on 27.9.2016 г..
 */

public class Recipe {
    ArrayList<String> ingredientLines;
    HashMap<String,Double> flavors;
    ArrayList<IngredientValues> nutritions;
    ArrayList<String> courses;
    String source;
    String name;
    String servings;
    String timeForPrepare;
    String creator;
    String bigPicUrl;
    String id;
    double fatKCAL;
    String numberOfServings;
    double rating;

    public Recipe(ArrayList<String> ingredientLines, HashMap<String, Double> flavors,  ArrayList<IngredientValues> nutritions, String name, String servings, String timeForPrepare, double rating,String bigPicUrl,String id,String numberOfServings,ArrayList<String> courses,String source, String creator,double fatKCAL) {
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
        this.courses = courses;
        this.source = source;
        this.creator = creator;
        this.fatKCAL = fatKCAL;
    }

    public String getSource() {
        return source;
    }

    public String getCreator() {
        return creator;
    }

    public ArrayList<String> getIngredientLines() {
        return ingredientLines;
    }

    public HashMap<String, Double> getFlavors() {
        return flavors;
    }

    public ArrayList<IngredientValues> getNutritions() {
        return nutritions;
    }

    public double getFatKCAL() {
        return fatKCAL;
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

    public ArrayList<String> getCourses() {
        return courses;
    }
}

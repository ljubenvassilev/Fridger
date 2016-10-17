package ljuboandtedi.fridger.model;

import java.util.HashMap;

/**
 * Created by NoLight on 2.10.2016 г..
 */
public class RecipeManager {
    private static RecipeManager ourInstance = new RecipeManager();
    public static HashMap<String, Recipe> recipes;
    public static RecipeManager getInstance() {
        return ourInstance;
    }
    private RecipeManager() {
        recipes = new HashMap<>();
    }
}
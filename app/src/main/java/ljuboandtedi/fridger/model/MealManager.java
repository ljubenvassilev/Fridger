package ljuboandtedi.fridger.model;

import java.util.HashMap;

/**
 * Created by NoLight on 28.9.2016 г..
 */
public class MealManager {
    private static MealManager ourInstance = new MealManager();
    public static HashMap<String,Meal> meals;
    public static MealManager getInstance() {
        return ourInstance;
    }

    private MealManager() {
        meals = new HashMap<>();
    }
}

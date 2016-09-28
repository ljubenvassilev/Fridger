package ljuboandtedi.fridger.model;

import java.util.ArrayList;

/**
 * Created by NoLight on 28.9.2016 г..
 */
public class ShoppingListForTestings {
    private static ShoppingListForTestings ourInstance = new ShoppingListForTestings();
    public static ArrayList<String> ingredients;
    public static ShoppingListForTestings getInstance() {
        return ourInstance;
    }

    private ShoppingListForTestings() {
        ingredients = new ArrayList<>();
    }
}

package ljuboandtedi.fridger.model;

/**
 * Created by NoLight on 25.9.2016 г..
 */
import android.graphics.Bitmap;
import android.media.Image;
import android.widget.ImageView;

import ljuboandtedi.fridger.model.Recipe;

/**
 * Created by NoLight on 25.9.2016 г..
 */

public class Meal {
    private String ingredients;
    private String flavors;
    private String rating;
    private String recipeName;
    private String id;
    private String url;
    private Recipe recipe;
    private boolean isFavourite;

    public Meal(String ingredients, String flavors, String rating, String recipeName, String id,String url) {
        this.ingredients = ingredients;
        this.flavors = flavors;
        this.rating = rating;
        this.recipeName = recipeName;
        this.id = id;
        this.url = url;
    }

    public String getIngredients() {
        return ingredients;
    }

    public String getFlavors() {
        return flavors;
    }

    public String getRating() {
        return rating;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public Recipe getRecipe() {
        return recipe;
    }

}

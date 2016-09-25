package ljuboandtedi.fridger;

/**
 * Created by NoLight on 25.9.2016 г..
 */
import android.graphics.Bitmap;
import android.media.Image;
import android.widget.ImageView;

/**
 * Created by NoLight on 25.9.2016 г..
 */

public class Meal {
    private String ingredients;
    private String flavors;
    private String rating;
    private String source;
    private String id;
    private String url;

    public Meal(String ingredients, String flavors, String rating, String source, String id,String url) {
        this.ingredients = ingredients;
        this.flavors = flavors;
        this.rating = rating;
        this.source = source;
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

    public String getSource() {
        return source;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }
}

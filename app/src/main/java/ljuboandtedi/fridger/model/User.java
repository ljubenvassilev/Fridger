package ljuboandtedi.fridger.model;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Ljuben Vassilev on 9/25/2016.
 */
public class User {

    User(String facebookID, String preferences) {
        fridge = new ArrayList<>();
        this.facebookID = facebookID;
        this.preferences = preferences;
    }

    private ArrayList <Meal> favouriteMeals;
    private String facebookID;
    private String preferences;
    private ArrayList <Ingredient> fridge;

    public void setFacebookID(String facebookID) { this.facebookID = facebookID;   }

    public String getFacebookID() { return facebookID; }

    public void setPreferences(String preferences){ this.preferences = preferences; }

    public String getPreferences(){return this.preferences;}

    public ArrayList<Ingredient> getFridge() {
        return (ArrayList<Ingredient>) Collections.unmodifiableList(fridge);
    }
}

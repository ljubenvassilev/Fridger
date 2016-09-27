package ljuboandtedi.fridger.model;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Ljuben Vassilev on 9/25/2016.
 */
public class User {

    private static User ourInstance=null;

    public static User getInstance() {
        if(ourInstance==null) {
            ourInstance = new User();
        }
        return ourInstance;
    }

    private User() {
        fridge = new ArrayList<>();
        preferences = "";
    }

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

package ljuboandtedi.fridger.model;

import android.app.Activity;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Ljuben Vassilev on 9/25/2016.
 */
public class User {

    User(String facebookID, String preferences, ArrayList<String> fridge, ArrayList<String> shoppingList, ArrayList<String> favouriteMeals){
        this.fridge = new ArrayList<>();
        this.fridge.addAll(fridge);
        this.shoppingList = new ArrayList<>();
        this.shoppingList.addAll(shoppingList);
        this.favouriteMeals = new ArrayList<>();
        this.favouriteMeals.addAll(favouriteMeals);
        this.facebookID = facebookID;
        this.preferences = preferences;
    }

    private String facebookID;
    private String preferences;
    private ArrayList <String> fridge;
    private ArrayList <String> shoppingList;
    private ArrayList <String> favouriteMeals;

    public void setFacebookID(String facebookID) { this.facebookID = facebookID;   }

    public String getFacebookID() { return facebookID; }

    public void setPreferences(String preferences){ this.preferences = preferences; }

    public String getPreferences(){return this.preferences;}

    public ArrayList<String> getFridge() {
        return (ArrayList<String>) Collections.unmodifiableList(fridge);
    }

    public ArrayList<String> getShoppingList() {
        return (ArrayList<String>) Collections.unmodifiableList(shoppingList);
    }

    public ArrayList<String> getFavouriteMeals() {
        return (ArrayList<String>) Collections.unmodifiableList(favouriteMeals);
    }

    public void setFridge(ArrayList<String> fridge) {
        this.fridge = fridge;
    }

    public void setShoppingList(ArrayList<String> shoppingList) {
        this.shoppingList = shoppingList;
    }

    public void setFavouriteMeals(ArrayList<String> favouriteMeals) {
        this.favouriteMeals = favouriteMeals;
    }
}

package ljuboandtedi.fridger.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;

import static com.facebook.FacebookSdk.getApplicationContext;

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

    void setFacebookID(String facebookID) { this.facebookID = facebookID;   }

    public String getFacebookID() { return facebookID; }

    void setPreferences(String preferences){ this.preferences = preferences; }

    public String getPreferences(){return this.preferences;}

    public ArrayList<String> getFridge() {
        return fridge;
    }

    public ArrayList<String> getShoppingList() {
        return shoppingList;
    }

    public ArrayList<String> getFavouriteMeals() {
        return favouriteMeals;
    }

    public void addToFridge(String ingredient) {
        fridge.add(ingredient);
        DatabaseHelper.getInstance(getApplicationContext()).addToFridge(ingredient);
    }

    public void addToShoppingList(String ingredient) {
        shoppingList.add(ingredient);
        DatabaseHelper.getInstance(getApplicationContext()).addToShoppingList(ingredient);
    }

    public void addToFavoriteMeeals(String meal) {
        shoppingList.add(meal);
        DatabaseHelper.getInstance(getApplicationContext()).addToFavoriteMeals(meal);
    }

    public void removeFromFridge(String ingredient) {
        fridge.remove(ingredient);
        DatabaseHelper.getInstance(getApplicationContext()).removeFromFridge(ingredient);
    }

    public void removeFromShoppingList(String ingredient) {
        shoppingList.remove(ingredient);
        DatabaseHelper.getInstance(getApplicationContext()).removeFromShoppingList(ingredient);
    }

    public void removeFromFavoriteMeeals(String meal) {
        shoppingList.remove(meal);
        DatabaseHelper.getInstance(getApplicationContext()).removeFromFavoriteMeals(meal);
    }


    void setFridge(ArrayList<String> fridge) {
        this.fridge = fridge;
    }

    void setShoppingList(ArrayList<String> shoppingList) {
        this.shoppingList = shoppingList;
    }

    void setFavouriteMeals(ArrayList<String> favouriteMeals) {
        this.favouriteMeals = favouriteMeals;
    }
}

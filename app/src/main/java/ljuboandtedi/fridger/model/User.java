package ljuboandtedi.fridger.model;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Ljuben Vassilev on 9/25/2016.
 */
public class User {

    private String facebookID;

    ArrayList<Ingredient> fridge;

    boolean likeSalty;

    boolean likeMeaty;

    boolean likePiquant;

    boolean likeBitter;

    boolean likeSour;

    boolean likeSweet;

    private static User ourInstance = new User();

    public static User getInstance() {
        return ourInstance;
    }

    private User() {
        fridge = new ArrayList<>();
    }

    public void setFacebookID(String facebookID) {
        this.facebookID = facebookID;
    }

    public void setLikeSalty(boolean likeSalty) {
        this.likeSalty = likeSalty;
    }

    public void setLikeMeaty(boolean likeMeaty) {
        this.likeMeaty = likeMeaty;
    }

    public void setLikePiquant(boolean likePiquant) {
        this.likePiquant = likePiquant;
    }

    public void setLikeBitter(boolean likeBitter) {
        this.likeBitter = likeBitter;
    }

    public void setLikeSour(boolean likeSour) {
        this.likeSour = likeSour;
    }

    public void setLikeSweet(boolean likeSweet) {
        this.likeSweet = likeSweet;
    }

    public ArrayList<Ingredient> getFridge() {
        return (ArrayList<Ingredient>) Collections.unmodifiableList(fridge);
    }

    public boolean isLikeSalty() {
        return likeSalty;
    }

    public boolean isLikeMeaty() {
        return likeMeaty;
    }

    public boolean isLikePiquant() {
        return likePiquant;
    }

    public boolean isLikeSour() {
        return likeSour;
    }

    public boolean isLikeBitter() {
        return likeBitter;
    }

    public boolean isLikeSweet() {
        return likeSweet;
    }
}

package ljuboandtedi.fridger.model;

/**
 * Created by Nicole Todorova on 9/25/2016.
 */
public class User {
    private static User ourInstance = new User();

    public static User getInstance() {
        return ourInstance;
    }

    private User() {
    }
}

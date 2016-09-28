package ljuboandtedi.fridger.model;

import java.util.ArrayList;

/**
 * Created by NoLight on 28.9.2016 г..
 */
public class SearchesForTesting {
    private static SearchesForTesting ourInstance = new SearchesForTesting();
    public static ArrayList<String> searches = new ArrayList<>();
    public static SearchesForTesting getInstance() {
        return ourInstance;
    }

    private SearchesForTesting() {
    }
}

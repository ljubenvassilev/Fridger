package ljuboandtedi.fridger.activties;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.animation.OvershootInterpolator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import ljuboandtedi.fridger.R;

public class ShowMealActivity extends DrawerActivity {

    private Bitmap mealPic;
    private RecyclerView listOfMeals;
    private static String bigPicUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.replaceContentLayout(R.layout.activity_recyclelist, super.CONTENT_LAYOUT_ID);

        listOfMeals = (RecyclerView) findViewById(R.id.recycleList);

        List<String> recipes = new ArrayList<>();
        String info = getIntent().getStringExtra("json");

        try {
            JSONObject json = new JSONObject(info);
            JSONArray matches = json.getJSONArray("matches");


            //Takes the first JOBJ in matches!!!
            //Atm it takes the first Soup found in the search of soups.
            //It will be in a recycle view and it will be in an array so we can take all the soups. This is sample for the first soup.


            //Lets try with array
            for (int i = 0; i < matches.length(); i++) {
                Log.i("matches", matches.length() + "");
                String ingredientsString = "";
                String recipeName = "";
                String flavorsString = "";
                String sourceOfInfo = "";
                String picUrl = "";
                int ratingInt = 0;
                String attributes = matches.getString(i);
                JSONObject attributesInJson = new JSONObject(attributes);

                //Takes json array for ingredients
                if (!attributesInJson.isNull("ingredients")) {
                    JSONArray ingredientsInJson = attributesInJson.getJSONArray("ingredients");
                    final int resultsLength = ingredientsInJson.length();
                    for (int j = 0; j < resultsLength; j++) {
                        //Appending String (Should be stringBuilder) to get info for all ingredients
                        ingredientsString += ingredientsInJson.getString(j) + ", ";

                    }
                }
                //recipeName in yummy for the meal
                if (!attributesInJson.isNull("recipeName")) {
                    recipeName = attributesInJson.getString("recipeName");
                }
                if (!attributesInJson.isNull("sourceDisplayName")) {
                    sourceOfInfo = attributesInJson.getString("sourceDisplayName");
                }
                //Rating of the recipe
                if (!attributesInJson.isNull("rating")) {
                    ratingInt = attributesInJson.getInt("rating");
                }
                Log.i("flavors", i + "");

                String crappyPrefix = "null";
                if (attributes.startsWith("null")) {
                    attributes = attributes.substring(crappyPrefix.length(), attributes.length());
                }

                if (!attributesInJson.isNull("flavors")) {

                    JSONObject flavorsInJson = attributesInJson.getJSONObject("flavors");
                    //Appending String (Should be stringBuilder) to get info for all flavors
                    flavorsString += "piquant: " + flavorsInJson.getDouble("piquant");
                    flavorsString += "\nmeaty: " + flavorsInJson.getDouble("meaty");
                    flavorsString += "\nbitter: " + flavorsInJson.getDouble("bitter");
                    flavorsString += "\nsweet: " + flavorsInJson.getDouble("sweet");
                    flavorsString += "\nsour: " + flavorsInJson.getDouble("sour");
                    flavorsString += "\nsalty: " + flavorsInJson.getDouble("salty");
                }
                String id = "";
                if (!attributesInJson.isNull("id")) {
                    //JSONObject aboutPicture = attributesInJson.getJSONObject("id");
                    id = attributesInJson.getString("id");

                    Log.e("ID", id);
                }

                recipes.add(id);
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }

        listOfMeals.setLayoutManager(new LinearLayoutManager(this));
        listOfMeals.setAdapter(new ljuboandtedi.fridger.adapters.MealRecyclerAdapter(this, recipes));
        listOfMeals.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f)));
    }

}


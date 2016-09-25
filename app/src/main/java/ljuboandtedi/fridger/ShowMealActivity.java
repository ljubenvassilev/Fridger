package ljuboandtedi.fridger;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ShowMealActivity extends AppCompatActivity {
    private Bitmap mealPic;
    private RecyclerView listOfMeals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclelist);

        listOfMeals = (RecyclerView) findViewById(R.id.recycleList);

        List<Meal> meals = new ArrayList<>();
//        TextView sourceDisplayName = (TextView) findViewById(R.id.showMeals_sourceDisplayName);
//        TextView recipeNameTV = (TextView) findViewById(R.id.showMeals_recipeName);
//        TextView rating = (TextView) findViewById(R.id.showMeals_rating);

        /// TextView ingredientsAndFlavorsTest = (TextView) findViewById(R.id.showMeals_testingIngredients);

        // mealPic = (ImageView) findViewById(R.id.showMeals_meal_picture);


        String info = getIntent().getStringExtra("json");

        try {
            JSONObject json = new JSONObject(info);
            JSONArray matches = json.getJSONArray("matches");


            //Takes the first JOBJ in matches!!!
            //Atm it takes the first Soup found in the search of soups.
            //It will be in a recycle view and it will be in an array so we can take all the soups. This is sample for the first soup.


            //Lets try with array
            for(int i = 0; i < matches.length(); i++) {
                Log.i("matches",matches.length() + "");
                String ingredientsString = "";
                String recipeName = "";
                String flavorsString = "";
                String sourceOfInfo = "";
                String picUrl = "";
                int ratingInt = 0;
                String attributes = matches.getString(i);
                JSONObject attributesInJson = new JSONObject(attributes);

                //Takes json array for ingredients
                if(!attributesInJson.isNull("ingredients")) {
                    JSONArray ingredientsInJson = attributesInJson.getJSONArray("ingredients");
                    final int resultsLength = ingredientsInJson.length();
                    for (int j = 0; j < resultsLength; j++) {
                        //Appending String (Should be stringBuilder) to get info for all ingredients
                        ingredientsString += ingredientsInJson.getString(j) + ", ";

                    }
                }
                //recipeName in yummy for the meal
                if(!attributesInJson.isNull("recipeName")) {
                    recipeName = attributesInJson.getString("recipeName");
                }
                if(!attributesInJson.isNull("sourceDisplayName")) {
                    sourceOfInfo = attributesInJson.getString("sourceDisplayName");
                }
                //Rating of the recipe
                if(!attributesInJson.isNull("rating")) {
                    ratingInt = attributesInJson.getInt("rating");
                }
                Log.i("flavors",i+"");

                String crappyPrefix = "null";
                if(attributes.startsWith("null")){
                    attributes =attributes.substring(crappyPrefix.length(),attributes.length());
                }

                if(!attributesInJson.isNull("flavors")) {

                    JSONObject flavorsInJson = attributesInJson.getJSONObject("flavors");
                    //Appending String (Should be stringBuilder) to get info for all flavors
                    flavorsString += "piquant: " + flavorsInJson.getDouble("piquant");
                    flavorsString += "\nmeaty: " + flavorsInJson.getDouble("meaty");
                    flavorsString += "\nbitter: " + flavorsInJson.getDouble("bitter");
                    flavorsString += "\nsweet: " + flavorsInJson.getDouble("sweet");
                    flavorsString += "\nsour: " + flavorsInJson.getDouble("sour");
                    flavorsString += "\nsalty: " + flavorsInJson.getDouble("salty");
                }
                //Pic url.
                if(!attributesInJson.isNull("imageUrlsBySize")) {
                    JSONObject aboutPicture = attributesInJson.getJSONObject("imageUrlsBySize");
                    picUrl = aboutPicture.getString("90");
                }

                Meal meal = new Meal(ingredientsString,flavorsString,ratingInt + "",sourceOfInfo,recipeName,picUrl);
                //new RequestTask(meal).execute(picUrl);

                meals.add(meal);
            }
            Log.i("meals",meals.toString());


        } catch (JSONException e) {
            e.printStackTrace();
        }

//        recipeNameTV.setText(recipeName);
//        sourceDisplayName.setText(sourceOfInfo);
//        rating.setText("Rating by yummly: " +ratingInt);
//        ingredientsAndFlavorsTest.setText("Flavors:\n"+flavorsString + "\n\n Ingredients: \n"  + ingredientsString);
//
        listOfMeals.setLayoutManager(new LinearLayoutManager(this));
        listOfMeals.setAdapter(new MealRecyclerAdapter(this, meals));
    }

}


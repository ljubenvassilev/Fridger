package ljuboandtedi.fridger;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ShowMealActivity extends AppCompatActivity {
    private ImageView mealPic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_meal);

        TextView sourceDisplayName = (TextView) findViewById(R.id.showMeals_sourceDisplayName);
        TextView recipeNameTV = (TextView) findViewById(R.id.showMeals_recipeName);
        TextView rating = (TextView) findViewById(R.id.showMeals_rating);

        TextView ingredientsAndFlavorsTest = (TextView) findViewById(R.id.showMeals_testingIngredients);

        mealPic = (ImageView) findViewById(R.id.showMeals_meal_picture);


        String info = getIntent().getStringExtra("json");
        String ingredientsString = "";
        String recipeName = "";
        String flavorsString = "";
        String sourceOfInfo = "";
        String picUrl = "";
        int ratingInt = 0;
        int counter = 0;
        try {

            JSONObject json = new JSONObject(info);
            JSONArray matches = json.getJSONArray("matches");


            //Takes the first JOBJ in matches!!!
            //Atm it takes the first Soup found in the search of soups.
            //It will be in a recycle view and it will be in an array so we can take all the soups. This is sample for the first soup.
            String attributes = matches.getString(0);
            JSONObject attributesInJson = new JSONObject(attributes);

            //Takes json array for ingredients
            JSONArray ingredientsInJson = attributesInJson.getJSONArray("ingredients");

            for(int i = 0; i < ingredientsInJson.length(); i++){
                //Appending String (Should be stringBuilder) to get info for all ingredients
                ingredientsString += ingredientsInJson.getString(i) + ", ";

            }
            //recipeName in yummy for the meal
            recipeName = attributesInJson.getString("recipeName");

            sourceOfInfo = attributesInJson.getString("sourceDisplayName");

            //Rating of the recipe
            ratingInt = attributesInJson.getInt("rating");

            JSONObject flavorsInJson = attributesInJson.getJSONObject("flavors");
            //Appending String (Should be stringBuilder) to get info for all flavors
            flavorsString += "piquant: " + flavorsInJson.getDouble("piquant");
            flavorsString += "\nmeaty: " + flavorsInJson.getDouble("meaty");
            flavorsString += "\nbitter: " + flavorsInJson.getDouble("bitter");
            flavorsString += "\nsweet: " + flavorsInJson.getDouble("sweet");
            flavorsString += "\nsour: " + flavorsInJson.getDouble("sour");
            flavorsString += "\nsalty: " + flavorsInJson.getDouble("salty");

            //Pic url.
            JSONObject aboutPicture = attributesInJson.getJSONObject("imageUrlsBySize");
            picUrl = aboutPicture.getString("90");

            new RequestTask().execute(picUrl);



        } catch (JSONException e) {
            e.printStackTrace();
        }

        recipeNameTV.setText(recipeName);
        sourceDisplayName.setText(sourceOfInfo);
        rating.setText("Rating by yummly: " +ratingInt);
        ingredientsAndFlavorsTest.setText("Flavors:\n"+flavorsString + "\n\n Ingredients: \n"  + ingredientsString);
//
    }

    class RequestTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String address = params[0];
            Bitmap bitmap = null;
            try {
                URL url = new URL(address);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                InputStream is = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(is);


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap image) {

            mealPic.setImageBitmap(image);

        }
    }

}

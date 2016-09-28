package ljuboandtedi.fridger;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ljuboandtedi.fridger.model.Meal;
import ljuboandtedi.fridger.model.MealManager;

public class ShowMealActivity extends AppCompatActivity {
    private Bitmap mealPic;
    private RecyclerView listOfMeals;
    private static String bigPicUrl = "";

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
                //Pic url.
                if (!attributesInJson.isNull("imageUrlsBySize")) {
                    JSONObject aboutPicture = attributesInJson.getJSONObject("imageUrlsBySize");
                    picUrl = aboutPicture.getString("90");
                }
                String id = "";
                if (!attributesInJson.isNull("id")) {
                    //JSONObject aboutPicture = attributesInJson.getJSONObject("id");
                    id = attributesInJson.getString("id");

                    Log.e("ID", id);
                }
                // new RequestTaskForRecipe().execute("http://api.yummly.com/v1/api/recipe/" + id + "?_app_id=19ff7314&_app_key=8bdb64c8c177c7e770c8ce0d000263fd");
                Meal meal = new Meal(ingredientsString, flavorsString, ratingInt + "", recipeName, id, "");


                meals.add(meal);
                MealManager.getInstance().meals.put(meal.getId(),meal);

            }
            Log.i("meals", meals.toString());


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
//    class RequestTaskForRecipe extends AsyncTask<String, Void, String> {
//        @Override
//        protected String doInBackground(String... params) {
//            String address = params[0];
//            String json = "";
//            try {
//                URL url = new URL(address);
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                connection.setRequestMethod("GET");
//                connection.connect();
//                Scanner sc = new Scanner(connection.getInputStream());
//                while(sc.hasNextLine()){
//                    json+=(sc.nextLine());
//                }
//
//
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            Log.e("link",address);
//            return json;
//        }
//
//        @Override
//        protected void onPostExecute(String json) {
//            try {
//                JSONObject object = new JSONObject(json);
//                JSONArray ingredientLines = object.getJSONArray("ingredientLines");
//                ArrayList<String> ingredientLinesArr = new ArrayList<>();
//
//                for(int i = 0; i<ingredientLines.length();i++){
//                    ingredientLinesArr.add(ingredientLines.getString(i));
//                }
//
//                if(!object.isNull("flavors")) {
//                    JSONObject flavors = object.getJSONObject("flavors");
//                    HashMap<String, Double> flavorsMap = new HashMap<>();
//                    if(!flavors.isNull("Salty")) {
//                        flavorsMap.put("Salty", flavors.getDouble("Salty"));
//                    }
//                    if(!flavors.isNull("Meaty")) {
//                        flavorsMap.put("Meaty", flavors.getDouble("Meaty"));
//                    }
//                    if(!flavors.isNull("Piquant")) {
//                        flavorsMap.put("Piquant", flavors.getDouble("Piquant"));
//                    }
//                    if(!flavors.isNull("Bitter")) {
//                        flavorsMap.put("Bitter", flavors.getDouble("Bitter"));
//                    }
//                    if(!flavors.isNull("Sour")) {
//                        flavorsMap.put("Sour", flavors.getDouble("Sour"));
//                    }
//                    if(!flavors.isNull("Sweet")) {
//                        flavorsMap.put("Sweet", flavors.getDouble("Sweet"));
//                    }
//                }
//                JSONArray nutritions = object.getJSONArray("nutritionEstimates");
//                HashMap<String,Double> nutritionsMap = new HashMap<>();
//                for (int i = 0; i<nutritions.length(); i++) {
//                    JSONObject nutrition = nutritions.getJSONObject(i);
//                    nutritionsMap.put(nutrition.getString("description"),nutrition.getDouble("value"));
//                }
//
//                String nameOfRecipe = object.getString("name");
//                String servings = object.getString("yield");
//                String totalTime = object.getString("totalTime");
//                Double rating  = object.getDouble("rating");
//
//                JSONArray images = object.getJSONArray("images");
//                String bigPicUrl = images.getJSONObject(0).getString("hostedLargeUrl");
//
//                Log.i("big pic url", bigPicUrl);
//
//                ShowMealActivity.bigPicUrl = bigPicUrl;
//                Log.i("TOVA E", ShowMealActivity.bigPicUrl);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//        }
//}}
}


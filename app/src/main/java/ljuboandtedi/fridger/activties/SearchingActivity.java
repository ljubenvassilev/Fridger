package ljuboandtedi.fridger.activties;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import ljuboandtedi.fridger.R;
import ljuboandtedi.fridger.adapters.MealRecyclerAdapter;
import ljuboandtedi.fridger.adapters.SearchingAdapter;
import ljuboandtedi.fridger.model.IngredientValues;
import ljuboandtedi.fridger.model.Recipe;
import ljuboandtedi.fridger.model.RecipeManager;
import ljuboandtedi.fridger.model.User;

public class SearchingActivity extends DrawerActivity {
    RecyclerView recListIngredients;
    SearchingAdapter searchingAdapter;
    EditText searchField;
    ArrayList<String> recipes;
    ArrayList<String> recipesSmallPics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.replaceContentLayout(R.layout.activity_searching, super.CONTENT_LAYOUT_ID);
        recListIngredients = (RecyclerView) findViewById(R.id.recycleListForSearchings);
        recipes = new ArrayList<>();
        recipesSmallPics = new ArrayList<>();
        recListIngredients.setLayoutManager(new LinearLayoutManager(this));
        searchingAdapter = new SearchingAdapter(recipes,recipesSmallPics,this);
        recListIngredients.setAdapter(searchingAdapter);
        searchField = (EditText) findViewById(R.id.searching_ET);
        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String edittedText = searchField.getText().toString();
                edittedText = edittedText.trim().replace(" ", "+");
                recipes.clear();
                searchingAdapter.notifyDataSetChanged();
                recipesSmallPics.clear();
                searchingAdapter.notifyDataSetChanged();
                new RequestTask().execute("http://api.yummly.com/v1/api/recipes?_app_id=19ff7314&_app_key=8bdb64c8c177c7e770c8ce0d000263fd&q=" + edittedText + "&maxResult=5&start=5");
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private class RequestTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String address = params[0];
            String json = "";
            try {
                URL url = new URL(address);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                Scanner sc = new Scanner(connection.getInputStream());
                while (sc.hasNextLine()) {
                    json += (sc.nextLine());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return json;
        }

        @Override
        protected void onPostExecute(String jsonX) {

            Log.i("JSON", jsonX);
            try {
                JSONObject json = new JSONObject(jsonX);
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
                    searchingAdapter.notifyDataSetChanged();
                    new RequestTaskForRecipe().execute("http://api.yummly.com/v1/api/recipe/" + id + "?_app_id="+ User.ID+"&_app_key="+User.KEY);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    class RequestTaskForRecipe extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            Log.e("params", params[0]);
            String address = params[0];
            String json = "";

            try {
                URL url = new URL(address);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                Scanner sc = new Scanner(connection.getInputStream());
                while (sc.hasNextLine()) {
                    json += (sc.nextLine());
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
            return json;
        }

        @Override
        protected void onPostExecute(String json) {
            try {
                JSONObject object = new JSONObject(json);
                JSONArray ingredientLines = object.getJSONArray("ingredientLines");
                ArrayList<String> ingredientLinesArr = new ArrayList<>();

                for (int i = 0; i < ingredientLines.length(); i++) {
                    ingredientLinesArr.add(ingredientLines.getString(i));
                }
                HashMap<String, Double> flavorsMap = new HashMap<>();
                if (!object.isNull("flavors")) {
                    JSONObject flavors = object.getJSONObject("flavors");
                    flavorsMap = new HashMap<>();
                    if (!flavors.isNull("Salty")) {
                        flavorsMap.put("Salty", flavors.getDouble("Salty"));
                    }
                    if (!flavors.isNull("Meaty")) {
                        flavorsMap.put("Meaty", flavors.getDouble("Meaty"));
                    }
                    if (!flavors.isNull("Piquant")) {
                        flavorsMap.put("Piquant", flavors.getDouble("Piquant"));
                    }
                    if (!flavors.isNull("Bitter")) {
                        flavorsMap.put("Bitter", flavors.getDouble("Bitter"));
                    }
                    if (!flavors.isNull("Sour")) {
                        flavorsMap.put("Sour", flavors.getDouble("Sour"));
                    }
                    if (!flavors.isNull("Sweet")) {
                        flavorsMap.put("Sweet", flavors.getDouble("Sweet"));
                    }
                }
                JSONArray nutritions = object.getJSONArray("nutritionEstimates");
                ArrayList<IngredientValues> nutritionsValues = new ArrayList<>();
                double fatKCAL = 0.0;
                for (int i = 0; i < nutritions.length(); i++) {
                    JSONObject nutrition = nutritions.getJSONObject(i);
                    if (nutrition.getString("attribute").equals("FAT_KCAL")) {
                        fatKCAL = nutrition.getDouble("value");
                    } else {
                        IngredientValues ingrValue = new IngredientValues(nutrition.getString("description"), nutrition.getDouble("value"));
                        nutritionsValues.add(ingrValue);
                    }
                }

                String nameOfRecipe = object.getString("name");
                String servings = object.getString("yield");
                String totalTime = object.getString("totalTime");
                Double rating = object.getDouble("rating");
                JSONArray images = object.getJSONArray("images");
                String bigPicUrl = images.getJSONObject(0).getString("hostedLargeUrl");
                String smallPicUrl = images.getJSONObject(0).getString("hostedSmallUrl");
                String id = object.getString("id");
                String numberOfServings = object.getString("numberOfServings");
                ArrayList<String> coursesForTheRecipe = new ArrayList<>();
                JSONObject sources = object.getJSONObject("source");
                String source = sources.getString("sourceRecipeUrl");
                String creator = sources.getString("sourceDisplayName");
                JSONObject course = object.getJSONObject("attributes");

                if (!course.isNull("course")) {
                    JSONArray courses = course.getJSONArray("course");
                    for (int i = 0; i < courses.length(); i++) {
                        coursesForTheRecipe.add(courses.getString(i));
                    }
                }
                Recipe recipe = new Recipe(ingredientLinesArr, flavorsMap, nutritionsValues, nameOfRecipe, servings, totalTime, rating, bigPicUrl, id, numberOfServings, coursesForTheRecipe, source, creator, fatKCAL,smallPicUrl);
                RecipeManager.recipes.put(recipe.getName(),recipe);

                recipes.add(recipe.getName());
                recipesSmallPics.add(recipe.getSmallPicUrl());
                searchingAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

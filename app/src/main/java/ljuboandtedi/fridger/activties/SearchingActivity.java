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

import static com.facebook.FacebookSdk.getApplicationContext;

public class SearchingActivity extends DrawerActivity {
    private RecyclerView recListIngredients;
    private SearchingAdapter searchingAdapter;
    private EditText searchField;
    private ArrayList<String> recipes;
    private ArrayList<String> recipesSmallPics;

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
            try {
                JSONObject json = new JSONObject(jsonX);
                JSONArray matches = json.getJSONArray("matches");
                for (int i = 0; i < matches.length(); i++) {
                    StringBuilder mealFlavors = new StringBuilder();
                    Log.i("matches", matches.length() + "");
                    String attributes = matches.getString(i);
                    JSONObject attributesInJson = new JSONObject(attributes);
                    if (!attributesInJson.isNull("flavors")) {
                        JSONObject flavorsInJson = attributesInJson.getJSONObject("flavors");

                        mealFlavors.append("piquant: ").append(flavorsInJson.getDouble("piquant"));
                        mealFlavors.append("\nmeaty: ").append(flavorsInJson.getDouble("meaty"));
                        mealFlavors.append("\nbitter: ").append(flavorsInJson.getDouble("bitter"));
                        mealFlavors.append("\nsweet: ").append(flavorsInJson.getDouble("sweet"));
                        mealFlavors.append("\nsour: " ).append(flavorsInJson.getDouble("sour"));
                        mealFlavors.append("\nsalty: ").append(flavorsInJson.getDouble("salty"));
                    }
                    String id = "";
                    if (!attributesInJson.isNull("id")) {
                        id = attributesInJson.getString("id");
                    }
                    searchingAdapter.notifyDataSetChanged();
                    new RequestTaskForRecipe().execute("http://api.yummly.com/v1/api/recipe/" + id + "?_"+getResources().getString(R.string.api));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class RequestTaskForRecipe extends AsyncTask<String, Void, String> {

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

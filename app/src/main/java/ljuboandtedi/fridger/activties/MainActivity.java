package ljuboandtedi.fridger.activties;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;

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
import ljuboandtedi.fridger.adapters.MealAdapter;
import ljuboandtedi.fridger.adapters.MealRecyclerAdapter;
import ljuboandtedi.fridger.model.IngredientValues;
import ljuboandtedi.fridger.model.Recipe;

import static ljuboandtedi.fridger.model.RecipeManager.recipes;

public class MainActivity extends DrawerActivity {

    Button favMealsButton;
    Button shoppingListButton;
    Button myFridgeButton;
    Button searchMenuButton;
    ArrayList<String> recipesMAIN;
    SwipeFlingAdapterView flingContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.replaceContentLayout(R.layout.activity_main, super.CONTENT_LAYOUT_ID);

        favMealsButton = (Button) findViewById(R.id.main_favMealsButton);
        shoppingListButton = (Button) findViewById(R.id.main_shopListButton);
        myFridgeButton = (Button) findViewById(R.id.main_fridgeButton);
        searchMenuButton = (Button) findViewById(R.id.main_SearchMenuButton);

        favMealsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FavouriteMealsActivity.class);
                startActivity(intent);
            }
        });
        myFridgeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, YourFridgeActivity.class);
                startActivity(intent);
            }
        });
        shoppingListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ShoppingListActivity.class);
                startActivity(intent);
            }
        });
        searchMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchMealsActivity.class);
                startActivity(intent);
            }
        });
        flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);
        recipesMAIN = new ArrayList<>();
        //adpterForRelatedMeals = new MealRecyclerAdapter(MainActivity.this, recipes);
        new RequestTaskForRelatedMeals().execute("http://api.yummly.com/v1/api/recipes?_app_id=19ff7314&_app_key=8bdb64c8c177c7e770c8ce0d000263fd&q=&maxResult=10&start=10");
//        al = new ArrayList<>();
//        al.add("php");
//        al.add("c");
//        al.add("python");
//        al.add("java");
//        al.add("html");
//        al.add("c++");
//        al.add("css");
//        al.add("javascript");
//
//        arrayAdapter = new ArrayAdapter<>(this, R.layout.item, R.id.helloText, al);
    }

    class RequestTaskForRelatedMeals extends AsyncTask<String, Void, String> {

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
                    recipesMAIN.add(id);
                }
                    Log.e("tiqRecepti",recipesMAIN.toString());
                    Log.e("puskamAdapter","puskamGO");

                    final  MealAdapter mealAdapter = new MealAdapter(MainActivity.this, recipesMAIN);
                    flingContainer.setAdapter(mealAdapter);
                    mealAdapter.notifyDataSetChanged();
                     flingContainer.setVisibility(View.VISIBLE);
                    //flingContainer.performClick();
                    //flingContainer.getTopCardListener().selectLeft();
                    flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
                        @Override
                        public void removeFirstObjectInAdapter() {
                            // this is the simplest way to delete an object from the Adapter (/AdapterView)
                            Log.d("LIST", "removed object!");
                            mealAdapter.remove(recipesMAIN.get(0));
                            mealAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onLeftCardExit(Object dataObject) {
                            //Do something on the left!
                            //You also have access to the original object.
                            //If you want to use it just cast it (String) dataObject
                        }

                        @Override
                        public void onRightCardExit(Object dataObject) {

                        }

                        @Override
                        public void onAdapterAboutToEmpty(int itemsInAdapter) {
                            // Ask for more data here
//                            al.add("XML ".concat(String.valueOf(i)));
//                            arrayAdapter.notifyDataSetChanged();
//                            i++;
                        }

                        @Override
                        public void onScroll(float scrollProgressPercent) {
                            View view = flingContainer.getSelectedView();
                            view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                            view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
                        }
                    });

                    Log.e("receptite", recipes.toString());



            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}




package ljuboandtedi.fridger.activties;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import ljuboandtedi.fridger.R;
import ljuboandtedi.fridger.adapters.IngredientsRecyclerAdapter;
import ljuboandtedi.fridger.adapters.MealRecyclerAdapter;
import ljuboandtedi.fridger.model.DatabaseHelper;
import ljuboandtedi.fridger.model.Meal;
import ljuboandtedi.fridger.model.MealManager;
import ljuboandtedi.fridger.model.Recipe;
import ljuboandtedi.fridger.model.RecipeManager;

public class RecipeInfoActivity extends DrawerActivity {

    ImageView iv;
    TextView caloriesTV;
    TextView totalTime;
    TextView ingredients;
    TextView servingsForTheIngredients;
    TextView itb;
    TextView course1TV;
    TextView course2TV;
    TextView course3TV;
    CheckBox cbFavourite;
    Button buyCheckedButton;
    Button buyALlButton;
    Button viewDirections;
    Button viewNutritions;
    Button hideIngredients;
    Button showHideIngredients;
    RecyclerView ingredientsList;
    IngredientsRecyclerAdapter adapter;
    TextView continueExploring;
    RecyclerView relatedMeals;
    private SlidingUpPanelLayout slidingLayout;

    Recipe recipe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.replaceContentLayout(R.layout.activity_recipe_info_slidingbar, super.CONTENT_LAYOUT_ID);
        iv = (ImageView) findViewById(R.id.recipe_info_Image);

        relatedMeals = (RecyclerView) findViewById(R.id.recycleListForRelatedMeals);
        relatedMeals.setLayoutManager(new LinearLayoutManager(RecipeInfoActivity.this,LinearLayoutManager.HORIZONTAL,false));
        new RequestTaskForRelatedMeals().execute("http://api.yummly.com/v1/api/recipes?_app_id=19ff7314&_app_key=8bdb64c8c177c7e770c8ce0d000263fd&q=&maxResult=10&start=10");

        course1TV = (TextView) findViewById(R.id.recipe_info_course1);
        course2TV = (TextView) findViewById(R.id.recipe_info_course2);
        course3TV = (TextView) findViewById(R.id.recipe_info_course3);
        continueExploring = (TextView) findViewById(R.id.recipe_info_continueExploring);

        //set layout slide listener
        slidingLayout = (SlidingUpPanelLayout)findViewById(R.id.sliding_layout);
        slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);

        caloriesTV = (TextView) findViewById(R.id.recipe_info_Calories);
        totalTime = (TextView) findViewById(R.id.recipe_info_TotalTime);
        ingredients = (TextView) findViewById(R.id.recipe_info_Ingredients);
        cbFavourite = (CheckBox) findViewById(R.id.recipe_info_FavouriteButton);
        servingsForTheIngredients = (TextView) findViewById(R.id.recipe_info_servingsForTheIngredients);

       /// itb = (TextView) findViewById(R.id.recipe_info_itemsToBeBought);

        buyALlButton = (Button) findViewById(R.id.recipe_info_buyALlButton);
        viewDirections = (Button) findViewById(R.id.recipe_info_viewDirections);
        viewNutritions = (Button) findViewById(R.id.recipe_info_viewNutritions);
        showHideIngredients = (Button) findViewById(R.id.recipe_info_ShowIngredients);
        buyCheckedButton = (Button) findViewById(R.id.recipe_info_buyCheckedButton);

        showHideIngredients.setOnClickListener(onShowListener());
        showHideIngredients.setVisibility(View.GONE);
        hideIngredients = (Button) findViewById(R.id.recipe_info_HideButton);
        hideIngredients.setOnClickListener(onHideListener());

        recipe = RecipeManager.recipes.get(getIntent().getStringExtra("recipe"));
        new RequestTask().execute(recipe.getBigPicUrl());

        caloriesTV.setText("Calories: " + recipe.getFatKCAL());
        if (recipe.getTimeForPrepare() == null || recipe.getTimeForPrepare().equals("null")) {
            totalTime.setText("Fast & Easy");
        } else {
            totalTime.setText(recipe.getTimeForPrepare());
        }
       // itb.setText("ITB:" + DatabaseHelper.getInstance(this).getUserShoppingList(DatabaseHelper.getInstance(this).getCurrentUser().getFacebookID()).size());

        viewDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebView webview = new WebView(RecipeInfoActivity.this);
                webview.loadUrl(recipe.getSource());
                setContentView(webview);
            }
        });
        viewNutritions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecipeInfoActivity.this,NutritionsActivity.class);
                intent.putExtra("recipe",recipe.getName());
                startActivity(intent);
            }
        });
        if(recipe.getCourses().size() == 1){
            course1TV.setText(recipe.getCourses().get(0));
            course2TV.setVisibility(View.GONE);
            course3TV.setVisibility(View.GONE);
        }
        else if (recipe.getCourses().size() == 0){
            course1TV.setVisibility(View.GONE);
            course2TV.setVisibility(View.GONE);
            course3TV.setVisibility(View.GONE);
            continueExploring.setVisibility(View.GONE);
        }
        else if(recipe.getCourses().size() == 2){
            course1TV.setText(recipe.getCourses().get(0));
            course2TV.setText(recipe.getCourses().get(1));
            course3TV.setVisibility(View.GONE);
        }
        else{
            course1TV.setText(recipe.getCourses().get(0));
            course2TV.setText(recipe.getCourses().get(1));
            course3TV.setText(recipe.getCourses().get(2));
        }
        final String courseOfTV1 = "&allowedCourse[]=course^course-" + course1TV.getText().toString().trim().replace(" ","+");
        final String courseOfTV2 = "&allowedCourse[]=course^course-" + course2TV.getText().toString().trim().replace(" ","+");
        final String courseOfTV3 = "&allowedCourse[]=course^course-" + course3TV.getText().toString().trim().replace(" ","+");


        course1TV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("allowed course","http://api.yummly.com/v1/api/recipes?_app_id=19ff7314&_app_key=8bdb64c8c177c7e770c8ce0d000263fd&q="+ courseOfTV1 + "&maxResult=40&start=10");

                new RequestTaskForTheCourses().execute("http://api.yummly.com/v1/api/recipes?_app_id=19ff7314&_app_key=8bdb64c8c177c7e770c8ce0d000263fd&q=" + courseOfTV1 + "&maxResult=40&start=10");

            }
        });
        course2TV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("allowed course","http://api.yummly.com/v1/api/recipes?_app_id=19ff7314&_app_key=8bdb64c8c177c7e770c8ce0d000263fd&q="+ courseOfTV2 + "&maxResult=40&start=10");

                new RequestTaskForTheCourses().execute("http://api.yummly.com/v1/api/recipes?_app_id=19ff7314&_app_key=8bdb64c8c177c7e770c8ce0d000263fd&q=" + courseOfTV2 + "&maxResult=40&start=10");

            }
        });
        course3TV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("allowed course","http://api.yummly.com/v1/api/recipes?_app_id=19ff7314&_app_key=8bdb64c8c177c7e770c8ce0d000263fd&q="+ courseOfTV3 + "&maxResult=40&start=10");

                new RequestTaskForTheCourses().execute("http://api.yummly.com/v1/api/recipes?_app_id=19ff7314&_app_key=8bdb64c8c177c7e770c8ce0d000263fd&q=" + courseOfTV3 + "&maxResult=40&start=10");

            }
        });

        servingsForTheIngredients.setText(recipe.getNumberOfServings() + "");
        ingredients.setText(recipe.getIngredientLines().size() + " Ingredients");


        if (DatabaseHelper.getInstance(this).getUserFavoriteMeals(DatabaseHelper.getInstance(this).getCurrentUser().getFacebookID()).contains(recipe.getId())) {
            cbFavourite.setChecked(true);
        }
        cbFavourite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    DatabaseHelper.getInstance(RecipeInfoActivity.this).addToFavoriteMeals(recipe.getId());
                    RecipeManager.test.add(recipe.getId());
                } else if (DatabaseHelper.getInstance(RecipeInfoActivity.this).getUserFavoriteMeals(DatabaseHelper.getInstance(RecipeInfoActivity.this).getCurrentUser().getFacebookID()).contains(recipe.getId())) {
                    DatabaseHelper.getInstance(RecipeInfoActivity.this).removeFromFavoriteMeals(recipe.getId());
                }
            }
        });


        buyALlButton.setText("Buy All");
        adapter = new IngredientsRecyclerAdapter(this,recipe.getIngredientLines());

        buyALlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                adapter.removeAll();
                Toast.makeText(RecipeInfoActivity.this, "Everything was added", Toast.LENGTH_SHORT).show();
              //  itb.setText("ITB:" + DatabaseHelper.getInstance(RecipeInfoActivity.this).getUserShoppingList(DatabaseHelper.getInstance(RecipeInfoActivity.this).getCurrentUser().getFacebookID()).size());
                buyALlButton.setVisibility(View.GONE);
                buyCheckedButton.setVisibility(View.GONE);

            }
        });
        buyCheckedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int numberOfPurchases = adapter.removeSelectedProducts();
                Log.e("kolko",numberOfPurchases+"");
                if (numberOfPurchases > 0) {
                    Toast.makeText(RecipeInfoActivity.this,numberOfPurchases + "Ingr. added to your SList", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RecipeInfoActivity.this, "Nothing selected", Toast.LENGTH_SHORT).show();
                }

//                itb.setText("ITB:" + DatabaseHelper.getInstance(RecipeInfoActivity.this).getUserShoppingList(DatabaseHelper.getInstance(RecipeInfoActivity.this).getCurrentUser().getFacebookID()).size());

            }
        });


        ingredientsList = (RecyclerView) findViewById(R.id.recycleListForIngredients);
        ingredientsList.setLayoutManager(new LinearLayoutManager(this));
        ingredientsList.setAdapter(adapter);

    }
    private View.OnClickListener onShowListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show sliding layout in bottom of screen (not expand it)
                slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                showHideIngredients.setVisibility(View.GONE);
            }
        };
    }
    private View.OnClickListener onHideListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hide sliding layout
                slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                showHideIngredients.setVisibility(View.VISIBLE);
            }
        };
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


            } catch (IOException e) {
                e.printStackTrace();
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap image) {
            iv.setImageBitmap(image);
        }
    }
    class RequestTaskForTheCourses extends AsyncTask<String, Void, String> {

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
                while(sc.hasNextLine()){
                    json+=(sc.nextLine());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return json;
        }

        @Override
        protected void onPostExecute(String json) {

            Intent intent = new Intent(RecipeInfoActivity.this, ShowMealActivity.class);
            intent.putExtra("json", json);
            startActivity(intent);
            finish();
            Log.i("JSON",json);

        }
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
                while(sc.hasNextLine()){
                    json+=(sc.nextLine());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return json;
        }

        @Override
        protected void onPostExecute(String jsonX) {
            List<String> recipes = new ArrayList<>();
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

                    recipes.add(id);
                Log.e("receptite",recipes.toString());
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            relatedMeals.setAdapter(new MealRecyclerAdapter(RecipeInfoActivity.this,recipes));
            Log.e("bqhtuk","123");

        }

    }
}

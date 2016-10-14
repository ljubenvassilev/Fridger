package ljuboandtedi.fridger.activties;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
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
import ljuboandtedi.fridger.model.RecipeManager;
import ljuboandtedi.fridger.model.User;

import static ljuboandtedi.fridger.model.RecipeManager.recipes;

public class MainActivity extends DrawerActivity {

    private ProgressDialog myProgressDialog;
    private Button searchActivityButton;
    private ArrayList<Bitmap> bitmaps2;
    //private ArrayList<Bitmap> bitmaps;
    private ArrayList<String> recipesByName2;
    //private ArrayList<String> recipesByName;
    //private SwipeFlingAdapterView flingContainer;
    private SwipeFlingAdapterView flingContainer2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.replaceContentLayout(R.layout.activity_main, super.CONTENT_LAYOUT_ID);

        searchActivityButton = (Button) findViewById(R.id.intenting);
        searchActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SearchingActivity.class);
                startActivity(intent);
            }
        });
        myProgressDialog = new ProgressDialog(this);

        //bitmaps = new ArrayList<>();
        bitmaps2 = new ArrayList<>();
        //flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame1);
        flingContainer2 = (SwipeFlingAdapterView) findViewById(R.id.frame2);
        //recipesByName = new ArrayList<>();
        recipesByName2 = new ArrayList<>();
        //new RequestTaskForRelatedMeals(recipesByName,bitmaps,flingContainer).execute("http://api.yummly.com/v1/api/recipes?_app_id=19ff7314&_app_key=8bdb64c8c177c7e770c8ce0d000263fd&=qpizza&maxResult=10&start=10");
        new RequestTaskForRelatedMeals(recipesByName2,bitmaps2,flingContainer2).execute("http://api.yummly.com/v1/api/recipes?_"+getResources().getString(R.string.api)+"&q=pizza&maxResult=20&start=10");        Log.d("apito", getResources().getString(R.string.api));
    }

   private class RequestTaskForRelatedMeals extends AsyncTask<String, Void, String> {
        private ArrayList<String> recipes;
        private ArrayList<Bitmap> bitmaps;
        private SwipeFlingAdapterView flingContainer;

        RequestTaskForRelatedMeals(ArrayList<String> recipes, ArrayList<Bitmap> bitmaps ,SwipeFlingAdapterView flingContainer){
            this.recipes = recipes;
            this.bitmaps = bitmaps;
            this.flingContainer = flingContainer;
        }

        @Override
        protected void onPreExecute() {
            showProgressDialog();
        }

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
                    new RequestTaskForRecipe(recipes,bitmaps,flingContainer).execute("http://api.yummly.com/v1/api/recipe/" +id+ "?_"+getResources().getString(R.string.api));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    private class RequestTaskForRecipe extends AsyncTask<String, Void, String> {
        private ArrayList<String> recipes;
        private SwipeFlingAdapterView flingContainer;
        private ArrayList<Bitmap> bitmaps;

        RequestTaskForRecipe( ArrayList<String> recipes, ArrayList<Bitmap> bitmaps ,SwipeFlingAdapterView flingContainer){
            this.recipes = recipes;
            this.bitmaps = bitmaps;
            this.flingContainer = flingContainer;
        }

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

                new RequestTask(recipes,bitmaps,flingContainer).execute(recipe.getBigPicUrl());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

       private class RequestTask extends AsyncTask<String, Void, Bitmap> {
           private ArrayList<String> recipes;
           private SwipeFlingAdapterView flingContainer;
           private ArrayList<Bitmap> bitmaps;

            RequestTask( ArrayList<String> recipes, ArrayList<Bitmap> bitmaps ,SwipeFlingAdapterView flingContainer){
                this.recipes = recipes;
                this.bitmaps = bitmaps;
                this.flingContainer = flingContainer;
            }
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
                bitmaps.add(image);
                if(bitmaps.size() == 10){
                    hideProgressDialog();

                    final  MealAdapter mealAdapter = new MealAdapter(MainActivity.this, bitmaps);
                    flingContainer.setAdapter(mealAdapter);
                    mealAdapter.notifyDataSetChanged();

                    flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
                        @Override
                        public void removeFirstObjectInAdapter() {
                            Log.d("LIST", "removed object!");
                            mealAdapter.remove(bitmaps.get(0));
                            recipes.remove(0);
                            mealAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onLeftCardExit(Object dataObject) {
                        }

                        @Override
                        public void onRightCardExit(Object dataObject) {
                        }

                        @Override
                        public void onAdapterAboutToEmpty(int itemsInAdapter) {
                        }

                        @Override
                        public void onScroll(float scrollProgressPercent) {
                            View view = flingContainer.getSelectedView();
                            view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                            view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
                        }
                    });
                    flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClicked(int itemPosition, Object dataObject) {
                            String recipeName = recipes.get(itemPosition);
                            Intent intent = new Intent(MainActivity.this,RecipeInfoActivity.class);
                            intent.putExtra("recipe",recipeName);
                            startActivity(intent);
                        }
                    });
                }
            }
        }
    }
    private void showProgressDialog() {
        if (myProgressDialog == null) {
            myProgressDialog = new ProgressDialog(this);
            myProgressDialog.setMessage("Loading...");
            myProgressDialog.setIndeterminate(true);
        }
        myProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (myProgressDialog != null && myProgressDialog.isShowing()) {
            myProgressDialog.hide();
        }
    }

}




package ljuboandtedi.fridger.adapters;

/**
 * Created by NoLight on 25.9.2016 г..
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import ljuboandtedi.fridger.R;
import ljuboandtedi.fridger.activties.RecipeInfoActivity;
import ljuboandtedi.fridger.model.Meal;
import ljuboandtedi.fridger.model.Recipe;


/**
 * Created by NoLight on 25.9.2016 г..
 */

public class MealRecyclerAdapter  extends  RecyclerView.Adapter<MealRecyclerAdapter.MyMealViewHolder> {

    private List<Meal> meals;
    private Activity activity;
    public String info;

    public MealRecyclerAdapter(Activity activity, List meals) {
        this.meals = meals;
        this.activity = activity;
    }


    @Override
    public MealRecyclerAdapter.MyMealViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View row = inflater.inflate(R.layout.activity_searchpic, parent, false);
        MyMealViewHolder vh = new MyMealViewHolder(row);
        return vh;
    }

    @Override
   synchronized public void onBindViewHolder(MealRecyclerAdapter.MyMealViewHolder holder, int position) {
        final Meal meal = meals.get(position);
        Log.e("url",meal.getUrl());
        new RequestTaskForRecipe(holder,meal).execute("http://api.yummly.com/v1/api/recipe/" +meal.getId()+ "?_app_id=19ff7314&_app_key=8bdb64c8c177c7e770c8ce0d000263fd" );
        Log.e("urlaa",meal.getUrl() + "ASD");


        holder.recipeNameTV.setText(meal.getId());
       // holder.rating.setText(meal.getRating());
        //holder.ingredientsAndFlavorsTest.setText("Flavors:\n"+meal.getFlavors() + "\n\n Ingredients: \n"  + meal.getIngredients());
        //holder.sourceDisplayName.setText(meal.getSource());

    }

    @Override
    public int getItemCount() {
        return meals.size();
    }
    class MyMealViewHolder extends RecyclerView.ViewHolder {
        TextView sourceDisplayName;
        TextView recipeNameTV;
        TextView rating;
        TextView ingredientsAndFlavorsTest;
        ImageView mealPic;

        MyMealViewHolder(View row){
            super(row);
            //sourceDisplayName = (TextView) row.findViewById(R.id.searchpic_NameOfTheRecipe);
            recipeNameTV = (TextView) row.findViewById(R.id.searchpic_NameOfTheRecipe);
            //rating = (TextView) row.findViewById(R.id.showMeals_rating);
            //ingredientsAndFlavorsTest = (TextView) row.findViewById(R.id.showMeals_testingIngredients);
            mealPic = (ImageView) row.findViewById(R.id.searchpic_Image);
        }
    }
    class RequestTaskForRecipe extends AsyncTask<String, Void, String> {
        Meal meal;
        MyMealViewHolder holder;
        RequestTaskForRecipe( MyMealViewHolder holder, Meal meal){
            this.meal = meal;
            this.holder = holder;
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
                while(sc.hasNextLine()){
                    json+=(sc.nextLine());
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
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
                HashMap<String, Double> nutritionsMap = new HashMap<>();
                for (int i = 0; i < nutritions.length(); i++) {
                    JSONObject nutrition = nutritions.getJSONObject(i);
                    nutritionsMap.put(nutrition.getString("description"), nutrition.getDouble("value"));
                }

                String nameOfRecipe = object.getString("name");
                String servings = object.getString("yield");
                String totalTime = object.getString("totalTime");
                Double rating = object.getDouble("rating");

                JSONArray images = object.getJSONArray("images");
                String bigPicUrl = images.getJSONObject(0).getString("hostedLargeUrl");

                Log.i("big pic url", bigPicUrl);
                meal.setUrl(bigPicUrl);
                Log.i("meal url", meal.getUrl());
                Recipe recipe = new Recipe(ingredientLinesArr, flavorsMap, nutritionsMap, nameOfRecipe, servings, totalTime, rating);
                meal.setRecipe(recipe);
                new RequestTask(holder, recipe).execute(meal.getUrl());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
            class RequestTask extends AsyncTask<String, Void, Bitmap> {

                MyMealViewHolder holder;
                Recipe recipe;
                RequestTask( MyMealViewHolder holder, Recipe recipe){
                    this.holder = holder;
                    this.recipe = recipe;
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
                    holder.mealPic.setImageBitmap(image);
                    holder.mealPic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(activity,RecipeInfoActivity.class);
                            intent.putExtra("meal",meal.getId());
                            activity.startActivity(intent);
                        }
                    });
                }

        }
    }


}


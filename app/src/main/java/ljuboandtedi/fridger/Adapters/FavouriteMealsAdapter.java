package ljuboandtedi.fridger.adapters;

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
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import ljuboandtedi.fridger.R;
import ljuboandtedi.fridger.activties.RecipeInfoActivity;
import ljuboandtedi.fridger.model.Recipe;
import ljuboandtedi.fridger.model.RecipeManager;

/**
 * Created by NoLight on 2.10.2016 г..
 */

public class FavouriteMealsAdapter extends RecyclerView.Adapter<FavouriteMealsAdapter.MyFavouriteMealHolder> {

    private List<String> recipes;
    private Activity activity;
    public String info;

    public FavouriteMealsAdapter(Activity activity, List recipes) {
        this.recipes = recipes;
        this.activity = activity;
    }


    @Override
    public FavouriteMealsAdapter.MyFavouriteMealHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View row = inflater.inflate(R.layout.activity_searchpic, parent, false);
        FavouriteMealsAdapter.MyFavouriteMealHolder vh = new FavouriteMealsAdapter.MyFavouriteMealHolder(row);
        return vh;
    }



    @Override
    public void onBindViewHolder(MyFavouriteMealHolder holder, int position) {
        final String recipeId = recipes.get(position);

        new FavouriteMealsAdapter.RequestTaskForRecipe(holder).execute("http://api.yummly.com/v1/api/recipe/" +recipeId+ "?_app_id=19ff7314&_app_key=8bdb64c8c177c7e770c8ce0d000263fd" );


    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }
    class MyFavouriteMealHolder extends RecyclerView.ViewHolder {
        TextView recipeNameTV;
        ImageView mealPic;

        MyFavouriteMealHolder(View row){
            super(row);
            recipeNameTV = (TextView) row.findViewById(R.id.searchpic_NameOfTheRecipe);
            mealPic = (ImageView) row.findViewById(R.id.searchpic_Image);
        }
    }
    private class RequestTaskForRecipe extends AsyncTask<String, Void, String> {

        MyFavouriteMealHolder holder;
        RequestTaskForRecipe(MyFavouriteMealHolder holder){
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
                String id = object.getString("id");
                String numberOfServings = object.getString("numberOfServings");
                JSONObject course = object.getJSONObject("attributes");
                JSONArray courses = course.getJSONArray("course");
                ArrayList<String> coursesForTheRecipe = new ArrayList<>();
                for(int i = 0; i < courses.length(); i++){
                    coursesForTheRecipe.add(courses.getString(i));
                }
                Log.e("ccourses",coursesForTheRecipe.toString());
                //Recipe recipe = new Recipe(ingredientLinesArr, flavorsMap, nutritionsMap, nameOfRecipe, servings, totalTime, rating,bigPicUrl,id,numberOfServings,coursesForTheRecipe);
                //new FavouriteMealsAdapter.RequestTaskForRecipe.RequestTask(holder, recipe).execute(bigPicUrl);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        class RequestTask extends AsyncTask<String, Void, Bitmap> {

            MyFavouriteMealHolder holder;
            Recipe recipe;
            RequestTask(MyFavouriteMealHolder holder, Recipe recipe){
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
                RecipeManager.recipes.put(recipe.getName(),recipe);
                holder.mealPic.setImageBitmap(image);
                holder.mealPic.setHorizontalFadingEdgeEnabled(true);
                holder.mealPic.setFadingEdgeLength(40);
                holder.mealPic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(activity,RecipeInfoActivity.class);
                        intent.putExtra("recipe",recipe.getName());
                        activity.startActivity(intent);
                    }
                });
            }

        }
    }

}

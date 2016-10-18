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
import android.widget.ImageButton;
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
import ljuboandtedi.fridger.activties.FavouriteMealsActivity;
import ljuboandtedi.fridger.activties.RecipeInfoActivity;
import ljuboandtedi.fridger.model.DatabaseHelper;
import ljuboandtedi.fridger.model.IngredientValues;
import ljuboandtedi.fridger.model.Recipe;
import ljuboandtedi.fridger.model.RecipeManager;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by NoLight on 16.10.2016 г..
 */

public class FavouriteMealsAdapter extends  RecyclerView.Adapter<FavouriteMealsAdapter.MyFavouriteMealViewHolder> {

    private List<String> recipes;
    private Activity activity;

    public FavouriteMealsAdapter(Activity activity, List recipes) {
        this.recipes = recipes;
        this.activity = activity;
    }
    public void add(List<String> recipes){
        List<String> newRecipes = new ArrayList<>();
        newRecipes.addAll(recipes);
        this.recipes.addAll(newRecipes);
    }

    @Override
    public FavouriteMealsAdapter.MyFavouriteMealViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View row = inflater.inflate(R.layout.favouritemeal_recycle_row, parent, false);
        FavouriteMealsAdapter.MyFavouriteMealViewHolder vh = new FavouriteMealsAdapter.MyFavouriteMealViewHolder(row);
        return vh;
    }

    @Override
    public void onBindViewHolder(FavouriteMealsAdapter.MyFavouriteMealViewHolder holder, int position) {
        final String recipe = recipes.get(position);
        holder.mealPic.setImageDrawable(null);

        new FavouriteMealsAdapter.RequestTaskForRecipe(holder).execute("http://api.yummly.com/v1/api/recipe/"
                +recipe+ "?_"+getApplicationContext().getResources().getString(R.string.api));
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }
    class MyFavouriteMealViewHolder extends RecyclerView.ViewHolder {
        private TextView recipeNameTV;
        private TextView recipeCreator;
        private ImageView mealPic;
        private ImageView overlay;
        private ImageButton remover;

        MyFavouriteMealViewHolder(View row){
            super(row);
            recipeNameTV = (TextView) row.findViewById(R.id.searchpic_NameOfTheRecipe);
            mealPic = (ImageView) row.findViewById(R.id.searchpic_Image);
            recipeCreator = (TextView) row.findViewById(R.id.searchpic_creator);
            overlay = (ImageView) row.findViewById(R.id.overlay);
            remover = (ImageButton) row.findViewById(R.id.favouritemeal_recycle_row_remover);
        }
    }
    class RequestTaskForRecipe extends AsyncTask<String, Void, String> {
        MyFavouriteMealViewHolder holder;
        RequestTaskForRecipe(MyFavouriteMealViewHolder holder){
            this.holder = holder;
        }

        @Override
        protected String doInBackground(String... params) {
            Log.e("params",params[0]);
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
                ArrayList<IngredientValues> nutritionsValues = new ArrayList<>();
                double fatKCAL = 0.0;
                for (int i = 0; i < nutritions.length(); i++) {
                    JSONObject nutrition = nutritions.getJSONObject(i);
                    if(nutrition.getString("attribute").equals("Energy")){
                        fatKCAL = nutrition.getDouble("value");
                    }
                    else if(!((nutrition.getString("description").startsWith("0"))
                            ||!(nutrition.getString("description").startsWith("1"))
                            ||!(nutrition.getString("description").startsWith("2"))
                            ||!(nutrition.getString("description").startsWith("3"))
                            ||!(nutrition.getString("description").startsWith("4"))
                            ||!(nutrition.getString("description").startsWith("5"))
                            ||!(nutrition.getString("description").startsWith("6"))
                            ||!(nutrition.getString("description").startsWith("7"))
                            ||!(nutrition.getString("description").startsWith("8"))
                            ||!(nutrition.getString("description").startsWith("9")))){
                        IngredientValues ingrValue = new IngredientValues(nutrition
                                .getString("description"),nutrition.getDouble("value"));
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
                Log.e("ccourses", coursesForTheRecipe.toString());
                Recipe recipe = new Recipe(ingredientLinesArr, flavorsMap, nutritionsValues,
                        nameOfRecipe, servings, totalTime, rating, bigPicUrl, id, numberOfServings,
                        coursesForTheRecipe, source, creator, fatKCAL,smallPicUrl);
                holder.recipeNameTV.setText(recipe.getName());
                holder.recipeCreator.setText(recipe.getCreator());
                new FavouriteMealsAdapter.RequestTaskForRecipe.RequestTask(holder, recipe).execute(bigPicUrl);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        private class RequestTask extends AsyncTask<String, Void, Bitmap> {

            private MyFavouriteMealViewHolder holder;
            private Recipe recipe;
            RequestTask(MyFavouriteMealViewHolder holder, Recipe recipe){
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
                holder.remover.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatabaseHelper.getInstance(activity).removeFromFavoriteMeals(recipe.getId());
                        DatabaseHelper.getInstance(activity).getUserFavoriteMeals(DatabaseHelper
                                .getInstance(activity).getCurrentUser().getFacebookID());
                        recipes.remove(recipe.getId());
                        notifyDataSetChanged();
                    }
                });
                holder.mealPic.setImageBitmap(image);
                holder.overlay.setBackgroundResource(R.color.transparent);
                holder.overlay.setImageResource(R.drawable.gradient);
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

package ljuboandtedi.fridger.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
import ljuboandtedi.fridger.model.IngredientValues;
import ljuboandtedi.fridger.model.MealForTheLibrary;
import ljuboandtedi.fridger.model.Recipe;
import ljuboandtedi.fridger.model.RecipeManager;

/**
 * Created by NoLight on 12.10.2016 г..
 */

public class MealAdapter extends ArrayAdapter {

    private List<String> recipes;
    private Activity activity;
    public MealAdapter(Activity activity, List recipes) {
        super(activity,R.layout.meal_row, recipes);
        this.recipes = recipes;
        this.activity =activity;
    }
    class MealViewHolder{
        ImageView image;
        TextView name;
        TextView attrs;

        MealViewHolder(View row){
            image = (ImageView)  row.findViewById(R.id.helloText);
            name = (TextView)    row.findViewById(R.id.meal_creator);
            attrs = (TextView)   row.findViewById(R.id.meal_name);
        }
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.e("blaaaaa","vika se");
        LayoutInflater inflater = activity.getLayoutInflater();
        View row;
        MealViewHolder vh;
        String recipe = recipes.get(position);

        if (convertView != null) {
            row = convertView;
            vh = (MealViewHolder) row.getTag();
        } else {
            row = inflater.inflate(R.layout.meal_row, parent, false);
            vh = new MealViewHolder(row);
            row.setTag(vh);
        }

        Log.e("doTukSym", "a");
        class RequestTaskForRecipe extends AsyncTask<String, Void, String> {
            MealViewHolder viewHolder;

            RequestTaskForRecipe(MealViewHolder viewHolder) {
                this.viewHolder = viewHolder;
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
                    Log.e("ccoursesADAPTER", coursesForTheRecipe.toString());
                    Recipe recipe = new Recipe(ingredientLinesArr, flavorsMap, nutritionsValues, nameOfRecipe, servings, totalTime, rating, bigPicUrl, id, numberOfServings, coursesForTheRecipe, source, creator, fatKCAL);
                    viewHolder.name.setText(recipe.getName());
                    viewHolder.attrs.setText(recipe.getCreator());
                    new RequestTask(viewHolder, recipe).execute(recipe.getBigPicUrl());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            class RequestTask extends AsyncTask<String, Void, Bitmap> {

                MealViewHolder holder;
                Recipe recipe;

                RequestTask(MealViewHolder holder, Recipe recipe) {
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
                    RecipeManager.recipes.put(recipe.getName(), recipe);
                    holder.image.setImageBitmap(image);

                    Log.e("tukaSym", "a");
                    holder.image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(activity, RecipeInfoActivity.class);
                            intent.putExtra("recipe", recipe.getName());
                            activity.startActivity(intent);
                        }
                    });
                }
            }
        }
        new RequestTaskForRecipe(vh).execute("http://api.yummly.com/v1/api/recipe/" +recipe+ "?_app_id=19ff7314&_app_key=8bdb64c8c177c7e770c8ce0d000263fd");




        return row;
    }
    @Override
    public String getItem(int position) {
        // TODO Auto-generated method stub
        return recipes.get(position);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return recipes.size();
    }
}

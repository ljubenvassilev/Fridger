package ljuboandtedi.fridger.activties;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import java.util.Scanner;

import ljuboandtedi.fridger.R;
import ljuboandtedi.fridger.adapters.IngredientListAdapter;
import ljuboandtedi.fridger.adapters.IngredientsRecyclerAdapter;
import ljuboandtedi.fridger.adapters.MealRecyclerAdapter;
import ljuboandtedi.fridger.model.DatabaseHelper;
import ljuboandtedi.fridger.model.Recipe;
import ljuboandtedi.fridger.model.RecipeManager;
import ljuboandtedi.fridger.model.User;

public class RecipeInfoActivity extends DrawerActivity {
    private boolean isItFavourite;
    private ImageView iv;
    private TextView caloriesTV;
    private TextView totalTime;
    private TextView ingredients;
    private Button course1TV;
    private Button course2TV;
    private Button course3TV;
    private ImageButton favouriteImageButton;
    private Button ingridientsInNewActivity;
    private Button viewDirections;
    private Button viewNutritions;
    private ImageButton hideIngredients;
    private ImageButton showHideIngredients;
    private MealRecyclerAdapter adpterForRelatedMeals;
    private RecyclerView relatedMeals;
    private SlidingUpPanelLayout slidingLayout;
    private Recipe recipe;
    private String searchInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.replaceContentLayout(R.layout.activity_recipe_info_slidingbar, super.CONTENT_LAYOUT_ID);

        iv = (ImageView) findViewById(R.id.recipe_info_Image);
        recipe = RecipeManager.recipes.get(getIntent().getStringExtra("recipe"));
        new RequestTask().execute(recipe.getBigPicUrl());
        relatedMeals = (RecyclerView) findViewById(R.id.recycleListForRelatedMeals);
        searchInfo = getIntent().getStringExtra("search");
        new RequestTaskForRelatedMeals().execute("http://api.yummly.com/v1/api/recipes?_"
                +getResources().getString(R.string.api)+"&q="
                +searchInfo+"&maxResult=15&start=10");
        course1TV = (Button) findViewById(R.id.recipe_info_course1);
        course2TV = (Button) findViewById(R.id.recipe_info_course2);
        course3TV = (Button) findViewById(R.id.recipe_info_course3);
        if(recipe.getCourses().size() == 1){
            course1TV.setText(recipe.getCourses().get(0));
            course2TV.setVisibility(View.GONE);
            course3TV.setVisibility(View.GONE);
        }
        else if (recipe.getCourses().size() == 0){
            course1TV.setVisibility(View.GONE);
            course2TV.setVisibility(View.GONE);
            course3TV.setVisibility(View.GONE);
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
        final String courseOfTV1 = "&allowedCourse[]=course^course-" + course1TV.getText()
                .toString().trim().replace(" ","+");
        final String courseOfTV2 = "&allowedCourse[]=course^course-" + course2TV.getText()
                .toString().trim().replace(" ","+");
        final String courseOfTV3 = "&allowedCourse[]=course^course-" + course3TV.getText()
                .toString().trim().replace(" ","+");

        String query = getSharedPreferences("Fridger", Context.MODE_PRIVATE).getString("lastSearch","");
        if(query.isEmpty()){
            new RequestTaskForRelatedMeals().execute("http://api.yummly.com/v1/api/recipes?_"+
                    getResources().getString(R.string.api)+"&q=&maxResult=15&start=10");
        } else {
            new RequestTaskForRelatedMeals().execute(query);
        }

        slidingLayout = (SlidingUpPanelLayout)findViewById(R.id.sliding_layout);
        slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        ingridientsInNewActivity= (Button) findViewById(R.id.recipe_info_showIngredientsActivity);
        ingridientsInNewActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecipeInfoActivity.this,RecipeIngredientsInfo.class);
                intent.putExtra("recipe",recipe.getName());
                startActivity(intent);
            }
        });
        caloriesTV = (TextView) findViewById(R.id.recipe_info_Calories);
        totalTime = (TextView) findViewById(R.id.recipe_info_TotalTime);
        ingredients = (TextView) findViewById(R.id.recipe_info_Ingredients);
        favouriteImageButton = (ImageButton) findViewById(R.id.recipe_info_FavouriteButton);

        viewDirections = (Button) findViewById(R.id.recipe_info_viewDirections);
        viewNutritions = (Button) findViewById(R.id.recipe_info_viewNutritions);
        showHideIngredients = (ImageButton) findViewById(R.id.recipe_info_ShowIngredients);
        showHideIngredients.setOnClickListener(onShowListener());
        showHideIngredients.setVisibility(View.VISIBLE);

        hideIngredients = (ImageButton) findViewById(R.id.recipe_info_HideButton);
        hideIngredients.setOnClickListener(onHideListener());
        if(recipe.getFatKCAL() == 0.0){
            caloriesTV.setText("Calories: " + "n/a");
        }
        else
            caloriesTV.setText("Calories: " + recipe.getFatKCAL());

        if (recipe.getTimeForPrepare() == null || recipe.getTimeForPrepare().equals("null")) {
            totalTime.setText("Fast & Easy");
        } else {
            totalTime.setText(recipe.getTimeForPrepare());
        }

        viewDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RecipeInfoActivity.this,DirectionsActivity.class).
                        putExtra("directions",recipe.getSource()));
            }
        });
        if(recipe.getNutritions().size() == 0){
            viewNutritions.setVisibility(View.GONE);
        }
        viewNutritions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecipeInfoActivity.this,NutritionsActivity.class);
                intent.putExtra("recipe",recipe.getName());
                startActivity(intent);
            }
        });

        course1TV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RequestTaskForTheCourses().execute("http://api.yummly.com/v1/api/recipes?_"
                        +getResources().getString(R.string.api)+"&q="
                        + courseOfTV1 + "&maxResult=40&start=10");
            }
        });
        course2TV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RequestTaskForTheCourses().execute("http://api.yummly.com/v1/api/recipes?_"
                        +getResources().getString(R.string.api)+"&q="
                        + courseOfTV2 + "&maxResult=40&start=10");
            }
        });
        course3TV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RequestTaskForTheCourses().execute("http://api.yummly.com/v1/api/recipes?_"
                        +getResources().getString(R.string.api)+"&q="
                        + courseOfTV3 + "&maxResult=40&start=10");
            }
        });


        ingredients.setText(recipe.getIngredientLines().size() + " Ingredients");
        isItFavourite = DatabaseHelper.getInstance(RecipeInfoActivity.this)
                .getUserFavoriteMeals(DatabaseHelper.getInstance(RecipeInfoActivity.this)
                        .getCurrentUser().getFacebookID()).contains(recipe.getId());
        if(isItFavourite){ favouriteImageButton.setImageResource(R.drawable.star_light); }
        favouriteImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isItFavourite){
                    favouriteImageButton.setImageResource(R.drawable.star_dark);
                    DatabaseHelper.getInstance(RecipeInfoActivity.this)
                            .removeFromFavoriteMeals(recipe.getId());
                    isItFavourite = false;
                    Toast.makeText(RecipeInfoActivity.this, "Removed from MyMeals",
                            Toast.LENGTH_SHORT).show();
                }
                 if (DatabaseHelper.getInstance(RecipeInfoActivity.this)
                         .getUserFavoriteMeals(DatabaseHelper.getInstance(RecipeInfoActivity.this)
                                 .getCurrentUser().getFacebookID()).contains(recipe.getId())) {
                    DatabaseHelper.getInstance(RecipeInfoActivity.this)
                            .removeFromFavoriteMeals(recipe.getId());
                     favouriteImageButton.setImageResource(R.drawable.star_dark);
                     Toast.makeText(RecipeInfoActivity.this, "Removed from MyMeals",
                             Toast.LENGTH_SHORT).show();

                 }
                else{
                     Toast.makeText(RecipeInfoActivity.this, "Added to MyMeals",
                             Toast.LENGTH_SHORT).show();
                     favouriteImageButton.setImageResource(R.drawable.star_light);
                     DatabaseHelper.getInstance(RecipeInfoActivity.this)
                             .addToFavoriteMeals(recipe.getId());
                 }
            }
        });

    }

    private View.OnClickListener onShowListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                showHideIngredients.setVisibility(View.GONE);
            }
        };
    }
    private View.OnClickListener onHideListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                showHideIngredients.setVisibility(View.VISIBLE);
            }
        };
    }
    private class RequestTask extends AsyncTask<String, Void, Bitmap> {

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
    private class RequestTaskForTheCourses extends AsyncTask<String, Void, String> {

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
    private class RequestTaskForRelatedMeals extends AsyncTask<String, Void, String> {

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
            try {
                JSONObject json = new JSONObject(jsonX);
                JSONArray matches = json.getJSONArray("matches");
                ArrayList<String> recipes = new ArrayList<>();

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
                    if (!attributesInJson.isNull("id")) { id = attributesInJson.getString("id"); }
                    recipes.add(id);
                }
                adpterForRelatedMeals = new MealRecyclerAdapter(RecipeInfoActivity.this,recipes);
                adpterForRelatedMeals.notifyDataSetChanged();
                relatedMeals.setLayoutManager(new LinearLayoutManager(RecipeInfoActivity.this,
                        LinearLayoutManager.HORIZONTAL,false));
                relatedMeals.setAdapter(adpterForRelatedMeals);
                adpterForRelatedMeals.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

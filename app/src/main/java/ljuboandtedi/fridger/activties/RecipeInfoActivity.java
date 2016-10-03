package ljuboandtedi.fridger.activties;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import ljuboandtedi.fridger.R;
import ljuboandtedi.fridger.model.DatabaseHelper;
import ljuboandtedi.fridger.model.Meal;
import ljuboandtedi.fridger.model.MealManager;
import ljuboandtedi.fridger.model.Recipe;
import ljuboandtedi.fridger.model.RecipeManager;

public class RecipeInfoActivity extends DrawerActivity {
    ImageView iv;
    TextView caloriesTV;
    TextView servingsTV;
    TextView ingredients;
    CheckBox cbFavourite;
    Button addToShoppingListButton;
    Meal meal;
    Recipe recipe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.replaceContentLayout(R.layout.activity_recipe_info, super.CONTENT_LAYOUT_ID);
        iv = (ImageView) findViewById(R.id.recipe_info_Image);
        caloriesTV = (TextView) findViewById(R.id.recipe_info_Calories);
        servingsTV = (TextView) findViewById(R.id.recipe_info_Servings);
        ingredients = (TextView) findViewById(R.id.recipe_info_Ingredients);
        cbFavourite = (CheckBox) findViewById(R.id.recipe_info_FavouriteButton);
        addToShoppingListButton = (Button) findViewById(R.id.recipe_info_addToShopList);

        if(MealManager.meals.get(getIntent().getStringExtra("meal")) != null){
            meal = MealManager.meals.get(getIntent().getStringExtra("meal"));
            recipe = meal.getRecipe();
        }
        else
            recipe = RecipeManager.recipes.get(getIntent().getStringExtra("recipe"));
        new RequestTask().execute(recipe.getBigPicUrl());
        if(recipe.getNutritions().get("FAT") == null){
            caloriesTV.setText("Calories: "+0);
        }
        else{
            caloriesTV.setText("Calories: "+recipe.getNutritions().get("FAT"));
        }
        if(recipe.getServings() == null || recipe.getServings().equals("null")){
            servingsTV.setText("Servings: " + 1);
        }
        else{
            servingsTV.setText("Servings: " + recipe.getServings());
        }
        ingredients.setText(recipe.getIngredientLines().size() + " Ingredients");
        if(DatabaseHelper.getInstance(this).getUserFavoriteMeals(DatabaseHelper.getInstance(this).getCurrentUser().getFacebookID()).contains(recipe.getId())){
            cbFavourite.setChecked(true);
        }
        cbFavourite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    DatabaseHelper.getInstance(RecipeInfoActivity.this).addToFavoriteMeals(recipe.getId());
                    RecipeManager.test.add(recipe.getId());
                }
                else
                    if(DatabaseHelper.getInstance(RecipeInfoActivity.this).getUserFavoriteMeals(DatabaseHelper.getInstance(RecipeInfoActivity.this).getCurrentUser().getFacebookID()).contains(recipe.getId())){
                        DatabaseHelper.getInstance(RecipeInfoActivity.this).removeFromFavoriteMeals(recipe.getId());
                    }
//                if(RecipeManager.test.contains(recipe.getId())){
//                    RecipeManager.test.remove(recipe.getId());
//                }

            }
        });
        addToShoppingListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecipeInfoActivity.this,BuyingIngredientsActivity.class);
                intent.putExtra("recipe",recipe.getName());
                startActivity(intent);
                finish();
            }
        });

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
}

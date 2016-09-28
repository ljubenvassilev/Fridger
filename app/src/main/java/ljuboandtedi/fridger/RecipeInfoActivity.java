package ljuboandtedi.fridger;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

import ljuboandtedi.fridger.model.Meal;
import ljuboandtedi.fridger.model.MealManager;
import ljuboandtedi.fridger.model.Recipe;

public class RecipeInfoActivity extends AppCompatActivity {
    ImageView iv;
    TextView caloriesTV;
    TextView servingsTV;
    TextView ingredients;
    CheckBox cbFavourite;
    Button addToShoppingListButton;
    Meal meal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_info);
        iv = (ImageView) findViewById(R.id.recipe_info_Image);
        caloriesTV = (TextView) findViewById(R.id.recipe_info_Calories);
        servingsTV = (TextView) findViewById(R.id.recipe_info_Servings);
        ingredients = (TextView) findViewById(R.id.recipe_info_Ingredients);
        cbFavourite = (CheckBox) findViewById(R.id.recipe_info_FavouriteButton);
        addToShoppingListButton = (Button) findViewById(R.id.recipe_info_addToShopList);


        meal = MealManager.meals.get(getIntent().getStringExtra("meal"));
        new RequestTask().execute(meal.getUrl());
        if(meal.getRecipe().getNutritions().get("FAT") == null){
            caloriesTV.setText("Calories: "+0);
        }
        else{
            caloriesTV.setText("Calories: "+meal.getRecipe().getNutritions().get("FAT"));
        }
        if(meal.getRecipe().getServings() == null){
            servingsTV.setText("Servings: " + 1);
        }
        else{
            servingsTV.setText("Servings: " + meal.getRecipe().getServings());
        }
        ingredients.setText(meal.getRecipe().getIngredientLines().size() + " Ingredients");
        cbFavourite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //put meal in user favourites.
                }
                else{
                    //remove meal from user favourites.
                }
            }
        });
        addToShoppingListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecipeInfoActivity.this,BuyingIngredientsActivity.class);
                intent.putExtra("meal",meal.getId());
                startActivity(intent);
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


            } catch (MalformedURLException e) {
                e.printStackTrace();
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

package ljuboandtedi.fridger.activties;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import ljuboandtedi.fridger.R;
import ljuboandtedi.fridger.adapters.IngredientsRecyclerAdapter;
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
    CheckBox cbFavourite;
    Button buyALlButton;
    RecyclerView ingredientsList;
    Meal meal;
    Recipe recipe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.replaceContentLayout(R.layout.activity_recipe_info, super.CONTENT_LAYOUT_ID);
        iv = (ImageView) findViewById(R.id.recipe_info_Image);
        caloriesTV = (TextView) findViewById(R.id.recipe_info_Calories);
        totalTime = (TextView) findViewById(R.id.recipe_info_TotalTime);
        ingredients = (TextView) findViewById(R.id.recipe_info_Ingredients);
        cbFavourite = (CheckBox) findViewById(R.id.recipe_info_FavouriteButton);
        servingsForTheIngredients = (TextView) findViewById(R.id.recipe_info_servingsForTheIngredients);
        itb = (TextView) findViewById(R.id.recipe_info_itemsToBeBought);
        buyALlButton = (Button) findViewById(R.id.recipe_info_buyALlButton);
        if(MealManager.meals.get(getIntent().getStringExtra("meal")) != null){
            meal = MealManager.meals.get(getIntent().getStringExtra("meal"));
            recipe = meal.getRecipe();
        }
        else
            recipe = RecipeManager.recipes.get(getIntent().getStringExtra("recipe"));
        new RequestTask().execute(recipe.getBigPicUrl());
        if(recipe.getNutritions().get("ENERC_KCAL") == null){
            caloriesTV.setText("Calories: " + 0);
        }
        else{
            caloriesTV.setText("Calories: "+recipe.getNutritions().get("FAT"));
        }
        if(recipe.getTimeForPrepare() == null ||recipe.getTimeForPrepare().equals("null")){
            totalTime.setText("Fast & Easy");
        }
        else{
            totalTime.setText(recipe.getTimeForPrepare());
        }
        itb.setText("ITB:" + DatabaseHelper.getInstance(this).getUserFridge(DatabaseHelper.getInstance(this).getCurrentUser().getFacebookID()).size());
        servingsForTheIngredients.setText(recipe.getNumberOfServings() + "");
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
            }
        });
        buyALlButton.setText("Buy Checked");
        final int sizeOfSList = DatabaseHelper.getInstance(RecipeInfoActivity.this).getUserShoppingList(DatabaseHelper.getInstance(RecipeInfoActivity.this).getCurrentUser().getFacebookID()).size();
        buyALlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sizeOfSList< DatabaseHelper.getInstance(RecipeInfoActivity.this).getUserShoppingList(DatabaseHelper.getInstance(RecipeInfoActivity.this).getCurrentUser().getFacebookID()).size()){
                    Toast.makeText(RecipeInfoActivity.this, "Ingr. added to your SList", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    Toast.makeText(RecipeInfoActivity.this, "Nothing selected", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ingredientsList = (RecyclerView) findViewById(R.id.recycleListForIngredients);
        ingredientsList.setLayoutManager(new LinearLayoutManager(this));
        ingredientsList.setAdapter(new IngredientsRecyclerAdapter(this ,recipe.getIngredientLines()));
         class IngredientsRecyclerAdapter extends  RecyclerView.Adapter<IngredientsRecyclerAdapter.MyIngredientViewHolder>{

            private List<String> ingredients;
            private HashMap<String,Boolean> ingredientsChecker;
            private Activity activity;

            public IngredientsRecyclerAdapter(Activity activity, List<String> ingredients){
                this.ingredients = ingredients;
                ingredientsChecker = new HashMap<>();
                for(String s: ingredients){
                    ingredientsChecker.put(s,false);
                }
                this.activity = activity;

            }

            @Override
            public IngredientsRecyclerAdapter.MyIngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                //inflate xml
                LayoutInflater inflater = activity.getLayoutInflater();
                View row = inflater.inflate(R.layout.ingredients_info_activity, parent, false);
                //create vh
                IngredientsRecyclerAdapter.MyIngredientViewHolder vh = new MyIngredientViewHolder(row);
                //return vh
                return vh;
            }

            @Override
            public int getItemCount() {
                return ingredients.size();
            }

            @Override
            public void onBindViewHolder(final IngredientsRecyclerAdapter.MyIngredientViewHolder holder, int position) {
                //get obj on position
                final String ingredient = ingredients.get(position);

                //fill data of the VH with the data of the object
//       int cbHeight = holder.cb.getHeight();
//        ViewGroup.LayoutParams params =  holder.cb.getLayoutParams();
//        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
//        Display display = wm.getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//        double width = size.x;
//        Log.e("widthScreen",width + "");
//        double height = size.y;
//        Log.e("heightScreen",height + " ");
//        double scale = params.height/height;
//        Log.e("scale",scale + "");
//        Log.e("height",params.height + "");
//        double newWidth = width*scale;
//        Log.e("new width",""+newWidth);
//        params.width =(int) newWidth;
//
//        holder.cb.setLayoutParams(params);
//        Log.e("ingredient",ingredient);
//        Log.e("selected",isitSelected + "");



                boolean isitSelected = ingredientsChecker.get(ingredient);
                if(isitSelected){
                    holder.cb.setChecked(true);
                }
                if(!isitSelected){
                    holder.cb.setChecked(false);
                }
                holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            holder.cb.setBackgroundColor(Color.GRAY);
                            holder.ingredient.setBackgroundColor(Color.GRAY);
                            ingredientsChecker.put(ingredient,true);
                            DatabaseHelper.getInstance(activity).addToShoppingList(ingredient);
                        }
                        if(!isChecked){
                            holder.cb.setBackgroundColor(Color.WHITE);
                            holder.ingredient.setBackgroundColor(Color.WHITE);
                            if(DatabaseHelper.getInstance(activity).getUserShoppingList(DatabaseHelper.getInstance(activity).getCurrentUser().getFacebookID()).contains(ingredient)){
                                DatabaseHelper.getInstance(activity).removeFromShoppingList(ingredient);
                                ingredientsChecker.put(ingredient,false);
                            }
                        }
                    }
                });

                holder.ingredient.setText(ingredient);

            }

              class MyIngredientViewHolder extends RecyclerView.ViewHolder{
                TextView ingredient;
                CheckBox cb;
                MyIngredientViewHolder(View row){
                    super(row);
                    this.setIsRecyclable(false);
                    cb = (CheckBox) row.findViewById(R.id.buyingIngredientChecked);
                    ingredient = (TextView)    row.findViewById(R.id.buyingIngredient);

                }
            }
        }
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

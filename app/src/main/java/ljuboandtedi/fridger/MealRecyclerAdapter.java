package ljuboandtedi.fridger;

/**
 * Created by NoLight on 25.9.2016 г..
 */

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;


/**
 * Created by NoLight on 25.9.2016 г..
 */

public class MealRecyclerAdapter  extends  RecyclerView.Adapter<MealRecyclerAdapter.MyMealViewHolder> {

    private List<Meal> meals;
    private Activity activity;

    public MealRecyclerAdapter(Activity activity, List meals) {
        this.meals = meals;
        this.activity = activity;
    }


    @Override
    public MealRecyclerAdapter.MyMealViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View row = inflater.inflate(R.layout.activity_show_meal, parent, false);
        MyMealViewHolder vh = new MyMealViewHolder(row);
        return vh;
    }

    @Override
    public void onBindViewHolder(MealRecyclerAdapter.MyMealViewHolder holder, int position) {
        Meal meal = meals.get(position);
        new RequestTask(holder).execute(meal.getUrl());
        holder.recipeNameTV.setText(meal.getId());
        holder.rating.setText(meal.getRating());
        holder.ingredientsAndFlavorsTest.setText("Flavors:\n"+meal.getFlavors() + "\n\n Ingredients: \n"  + meal.getIngredients());
        holder.sourceDisplayName.setText(meal.getSource());
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
            sourceDisplayName = (TextView) row.findViewById(R.id.showMeals_sourceDisplayName);
            recipeNameTV = (TextView) row.findViewById(R.id.showMeals_recipeName);
            rating = (TextView) row.findViewById(R.id.showMeals_rating);
            ingredientsAndFlavorsTest = (TextView) row.findViewById(R.id.showMeals_testingIngredients);
            mealPic = (ImageView) row.findViewById(R.id.showMeals_meal_picture);
        }
    }

    class RequestTask extends AsyncTask<String, Void, Bitmap> {

        MyMealViewHolder holder;
        RequestTask( MyMealViewHolder holder){
            this.holder = holder;
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


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap image) {
            holder.mealPic.setImageBitmap(image);
    }
}
}

package ljuboandtedi.fridger;

/**
 * Created by NoLight on 25.9.2016 г..
 */

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
        //holder.mealPic.setImageBitmap(meal.getImage());
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
        //ImageView mealPic;

        MyMealViewHolder(View row){
            super(row);
            sourceDisplayName = (TextView) row.findViewById(R.id.showMeals_sourceDisplayName);
            recipeNameTV = (TextView) row.findViewById(R.id.showMeals_recipeName);
            rating = (TextView) row.findViewById(R.id.showMeals_rating);
            ingredientsAndFlavorsTest = (TextView) row.findViewById(R.id.showMeals_testingIngredients);
            //mealPic = (ImageView) row.findViewById(R.id.showMeals_meal_picture);
        }
    }
}

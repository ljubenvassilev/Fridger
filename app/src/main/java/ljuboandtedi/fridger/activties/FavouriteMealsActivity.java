package ljuboandtedi.fridger.activties;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


import ljuboandtedi.fridger.R;
import ljuboandtedi.fridger.model.DatabaseHelper;
import ljuboandtedi.fridger.model.Meal;
import ljuboandtedi.fridger.model.RecipeManager;

public class FavouriteMealsActivity extends AppCompatActivity {
    private RecyclerView listOfMeals;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclelist);
        listOfMeals = (RecyclerView) findViewById(R.id.recycleList);
        //List<String> favMeals = DatabaseHelper.getInstance(this).getUserFavoriteMeals(DatabaseHelper.getInstance(this).getCurrentUser().getFacebookID());
        List<String> favMeals = RecipeManager.test;

        listOfMeals.setLayoutManager(new LinearLayoutManager(this));
        listOfMeals.setAdapter(new ljuboandtedi.fridger.Adapters.MealRecyclerAdapter(this, DatabaseHelper.getInstance(this).getUserFavoriteMeals(DatabaseHelper.getInstance(this).getCurrentUser().getFacebookID())));
    }
}

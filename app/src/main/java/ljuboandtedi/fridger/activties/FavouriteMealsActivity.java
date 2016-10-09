package ljuboandtedi.fridger.activties;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;


import ljuboandtedi.fridger.R;
import ljuboandtedi.fridger.model.DatabaseHelper;
import ljuboandtedi.fridger.model.RecipeManager;

public class FavouriteMealsActivity extends DrawerActivity {
    private RecyclerView listOfMeals;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.replaceContentLayout(R.layout.activity_search_meals, super.CONTENT_LAYOUT_ID);
        listOfMeals = (RecyclerView) findViewById(R.id.recycleList);

        listOfMeals.setLayoutManager(new LinearLayoutManager(this));
        listOfMeals.setAdapter(new ljuboandtedi.fridger.adapters.MealRecyclerAdapter(FavouriteMealsActivity.this, DatabaseHelper.getInstance(FavouriteMealsActivity.this).getUserFavoriteMeals(DatabaseHelper.getInstance(FavouriteMealsActivity.this).getCurrentUser().getFacebookID())));
    }
}

package ljuboandtedi.fridger.activties;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;


import ljuboandtedi.fridger.R;
import ljuboandtedi.fridger.adapters.FavouriteMealsAdapter;
import ljuboandtedi.fridger.model.DatabaseHelper;
import ljuboandtedi.fridger.model.RecipeManager;

public class FavouriteMealsActivity extends DrawerActivity {
    private RecyclerView listOfMeals;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.replaceContentLayout(R.layout.activity_recyclelist, super.CONTENT_LAYOUT_ID);
        listOfMeals = (RecyclerView) findViewById(R.id.recycleList);

        listOfMeals.setLayoutManager(new LinearLayoutManager(this));
        listOfMeals.setAdapter(new FavouriteMealsAdapter(FavouriteMealsActivity.this, DatabaseHelper.getInstance(FavouriteMealsActivity.this).getUserFavoriteMeals(DatabaseHelper.getInstance(FavouriteMealsActivity.this).getCurrentUser().getFacebookID())));
        if(DatabaseHelper.getInstance(FavouriteMealsActivity.this).getUserFavoriteMeals(DatabaseHelper.getInstance(FavouriteMealsActivity.this).getCurrentUser().getFacebookID()).size() <1){
            
        }
    }
}

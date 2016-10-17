package ljuboandtedi.fridger.activties;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import ljuboandtedi.fridger.R;
import ljuboandtedi.fridger.adapters.FavouriteMealsAdapter;
import ljuboandtedi.fridger.model.DatabaseHelper;

public class FavouriteMealsActivity extends DrawerActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.replaceContentLayout(R.layout.activity_recyclelist, super.CONTENT_LAYOUT_ID);
        RecyclerView listOfMeals = (RecyclerView) findViewById(R.id.recycleList);
        listOfMeals.setLayoutManager(new LinearLayoutManager(this));
        listOfMeals.setAdapter(new FavouriteMealsAdapter(FavouriteMealsActivity.this, DatabaseHelper.getInstance(FavouriteMealsActivity.this).getUserFavoriteMeals(DatabaseHelper.getInstance(FavouriteMealsActivity.this).getCurrentUser().getFacebookID())));

    }
}

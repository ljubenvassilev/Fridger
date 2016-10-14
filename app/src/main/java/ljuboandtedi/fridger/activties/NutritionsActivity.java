package ljuboandtedi.fridger.activties;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import ljuboandtedi.fridger.R;
import ljuboandtedi.fridger.adapters.IngridientValuesAdapter;
import ljuboandtedi.fridger.model.Recipe;
import ljuboandtedi.fridger.model.RecipeManager;

public class NutritionsActivity extends AppCompatActivity {
    private RecyclerView listOfIngredients;
    private Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutritions);
        recipe = RecipeManager.recipes.get(getIntent().getStringExtra("recipe"));

        listOfIngredients = (RecyclerView) findViewById(R.id.nutritions_recycleList);
        listOfIngredients.setLayoutManager(new LinearLayoutManager(this));
        listOfIngredients.setAdapter(new IngridientValuesAdapter(recipe.getNutritions(),this));
    }
}

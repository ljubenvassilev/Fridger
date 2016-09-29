package ljuboandtedi.fridger.activties;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import ljuboandtedi.fridger.R;
import ljuboandtedi.fridger.adapters.IngredientsRecyclerAdapter;
import ljuboandtedi.fridger.model.ShoppingListForTestings;

public class YourIngredientsActivity extends AppCompatActivity {
    private RecyclerView listOfIngredients;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_ingredients);
        listOfIngredients = (RecyclerView) findViewById(R.id.recycleListForIngredients);
        listOfIngredients.setLayoutManager(new LinearLayoutManager(this));
        listOfIngredients.setAdapter(new IngredientsRecyclerAdapter(this, ShoppingListForTestings.ingredients));
    }
}

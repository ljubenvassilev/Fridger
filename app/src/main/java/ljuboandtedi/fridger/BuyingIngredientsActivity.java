package ljuboandtedi.fridger;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ljuboandtedi.fridger.model.Meal;
import ljuboandtedi.fridger.model.MealManager;
import ljuboandtedi.fridger.model.SearchesForTesting;

public class BuyingIngredientsActivity extends AppCompatActivity {
    Meal meal;
    private RecyclerView listOfIngredients;
    TextView tv;
    Button addButton;
    Button shopListButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buying_ingredients);
        listOfIngredients = (RecyclerView) findViewById(R.id.recycleListForIngredients);
        tv = (TextView) findViewById(R.id.buyingingredients_tv);
        addButton = (Button) findViewById(R.id.buyingingredients_DoneButton);
        shopListButton = (Button) findViewById(R.id.buyingingredients_ListButton);
        meal = MealManager.meals.get(getIntent().getStringExtra("meal"));

        ArrayList<String> ingredients = new ArrayList<>();
        ingredients = meal.getRecipe().getIngredientLines();

        listOfIngredients.setLayoutManager(new LinearLayoutManager(this));
        listOfIngredients.setAdapter(new IngredientsRecyclerAdapter(this, ingredients));

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BuyingIngredientsActivity.this,MainActivity.class);
                Toast.makeText(BuyingIngredientsActivity.this, "Added to shopping list", Toast.LENGTH_SHORT).show();
                intent.putExtra("json", SearchesForTesting.searches.get(SearchesForTesting.searches.size()-1));
                startActivity(intent);
            }
        });
        shopListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BuyingIngredientsActivity.this,YourIngredientsActivity.class);
                startActivity(intent);
            }
        });
    }
}

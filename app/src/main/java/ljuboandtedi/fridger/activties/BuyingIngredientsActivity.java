package ljuboandtedi.fridger.activties;

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

import ljuboandtedi.fridger.R;
import ljuboandtedi.fridger.adapters.*;
import ljuboandtedi.fridger.model.Recipe;
import ljuboandtedi.fridger.model.RecipeManager;

public class BuyingIngredientsActivity extends DrawerActivity {
    Recipe recipe;
    private RecyclerView listOfIngredients;
    TextView tv;
    Button addButton;
    Button shopListButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.replaceContentLayout(R.layout.activity_buying_ingredients, super.CONTENT_LAYOUT_ID);
        listOfIngredients = (RecyclerView) findViewById(R.id.recycleListForIngredients);
        tv = (TextView) findViewById(R.id.buyingingredients_tv);
        addButton = (Button) findViewById(R.id.buyingingredients_DoneButton);
        shopListButton = (Button) findViewById(R.id.buyingingredients_ListButton);
        recipe = RecipeManager.recipes.get(getIntent().getStringExtra("recipe"));

        ArrayList<String> ingredients = new ArrayList<>();
        ingredients = recipe.getIngredientLines();

        listOfIngredients.setLayoutManager(new LinearLayoutManager(this));
        listOfIngredients.setAdapter(new IngredientsRecyclerAdapter(this, ingredients));

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BuyingIngredientsActivity.this, "Added to shopping list", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        shopListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BuyingIngredientsActivity.this,ShoppingListActivity.class);
                startActivity(intent);
            }
        });
    }
}

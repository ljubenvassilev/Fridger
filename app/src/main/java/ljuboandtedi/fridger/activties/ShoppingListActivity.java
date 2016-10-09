package ljuboandtedi.fridger.activties;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import ljuboandtedi.fridger.adapters.IngredientsInShoppingListAdapter;
import ljuboandtedi.fridger.R;
import ljuboandtedi.fridger.adapters.IngredientsRecyclerAdapter;
import ljuboandtedi.fridger.model.DatabaseHelper;

public class ShoppingListActivity extends DrawerActivity {
    private RecyclerView listOfIngredients;
    Button addToFridgeButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.replaceContentLayout(R.layout.activity_your_ingredients, super.CONTENT_LAYOUT_ID);
        listOfIngredients = (RecyclerView) findViewById(R.id.recycleListForIngredients);
        addToFridgeButton = (Button) findViewById(R.id.yourIngredients_addToFridgeButton);

        listOfIngredients.setLayoutManager(new LinearLayoutManager(this));
        listOfIngredients.setAdapter(new IngredientsInShoppingListAdapter(this,DatabaseHelper.getInstance(this).getUserShoppingList(DatabaseHelper.getInstance(this).getCurrentUser().getFacebookID())));

        addToFridgeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ShoppingListActivity.this, "Selected are now in your fridge", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}

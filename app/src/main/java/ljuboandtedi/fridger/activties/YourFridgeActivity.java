package ljuboandtedi.fridger.activties;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import ljuboandtedi.fridger.adapters.MyFridgeMealsAdapter;
import ljuboandtedi.fridger.R;
import ljuboandtedi.fridger.model.DatabaseHelper;

public class YourFridgeActivity extends DrawerActivity {
    private RecyclerView listOfIngredients;
    private Button removeSelectedFromFridgeButton;
    private Button removeAll;
    private MyFridgeMealsAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.replaceContentLayout(R.layout.activity_your_fridge, super.CONTENT_LAYOUT_ID);

        listOfIngredients = (RecyclerView) findViewById(R.id.recycleListForIngredients);
        removeSelectedFromFridgeButton = (Button) findViewById(R.id.yourFridge_removeFromFridgeButton);
        removeAll = (Button) findViewById(R.id.yourFridge_removeAllToFridgeButton);
        removeAll.setText("Eat All");
        removeSelectedFromFridgeButton.setText("Eat Selected");

        listOfIngredients.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyFridgeMealsAdapter(this, DatabaseHelper.getInstance(this).getUserFridge(DatabaseHelper.getInstance(this).getCurrentUser().getFacebookID()));
        listOfIngredients.setAdapter(adapter);

        int sizeOfFridgeProducts = DatabaseHelper.getInstance(YourFridgeActivity.this).getUserFridge(DatabaseHelper.getInstance(YourFridgeActivity.this).getCurrentUser().getFacebookID()).size();
        if (sizeOfFridgeProducts == 0) {
            removeSelectedFromFridgeButton.setVisibility(View.GONE);
            removeAll.setVisibility(View.GONE);
        }

        removeSelectedFromFridgeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int sizeOfFridgeProducts = DatabaseHelper.getInstance(YourFridgeActivity.this).getUserFridge(DatabaseHelper.getInstance(YourFridgeActivity.this).getCurrentUser().getFacebookID()).size();
                int numberOfRemovedFromFridgeProducts = adapter.removeSelectedProducts();
                if (numberOfRemovedFromFridgeProducts == sizeOfFridgeProducts) {
                    Toast.makeText(YourFridgeActivity.this, "Empty Fridge.", Toast.LENGTH_SHORT).show();
                    finish();
                } else if (numberOfRemovedFromFridgeProducts > 0) {
                    Toast.makeText(YourFridgeActivity.this, "Removed some ingr.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(YourFridgeActivity.this, "Nothing selected", Toast.LENGTH_SHORT).show();
                }

            }
        });
        removeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.removeAll();
                Toast.makeText(YourFridgeActivity.this, "Everything was removed to your fridge, it is empty.", Toast.LENGTH_SHORT).show();
                removeSelectedFromFridgeButton.setVisibility(View.GONE);
                removeAll.setVisibility(View.GONE);
            }
        });
    }
}

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
    Button removeSelectedFromFridgeButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.replaceContentLayout(R.layout.activity_your_fridge, super.CONTENT_LAYOUT_ID);
        listOfIngredients = (RecyclerView) findViewById(R.id.recycleListForIngredients);
        removeSelectedFromFridgeButton = (Button) findViewById(R.id.fridge_removeButton);
        final int sizeOfFridgeIngrediants = DatabaseHelper.getInstance(this).getUserFridge(DatabaseHelper.getInstance(this).getCurrentUser().getFacebookID()).size();
        listOfIngredients.setLayoutManager(new LinearLayoutManager(this));
        listOfIngredients.setAdapter(new MyFridgeMealsAdapter(this, DatabaseHelper.getInstance(this).getUserFridge(DatabaseHelper.getInstance(this).getCurrentUser().getFacebookID())));
        removeSelectedFromFridgeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int sizeOfFridgeIngredientsAfterReview = DatabaseHelper.getInstance(YourFridgeActivity.this).getUserFridge(DatabaseHelper.getInstance(YourFridgeActivity.this).getCurrentUser().getFacebookID()).size();
                if(sizeOfFridgeIngrediants == sizeOfFridgeIngredientsAfterReview){
                    Toast.makeText(YourFridgeActivity.this, "Nothing was eaten", Toast.LENGTH_SHORT).show();
                }
                if(sizeOfFridgeIngrediants > sizeOfFridgeIngredientsAfterReview){
                    Toast.makeText(YourFridgeActivity.this, "You eat them", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });
    }
}

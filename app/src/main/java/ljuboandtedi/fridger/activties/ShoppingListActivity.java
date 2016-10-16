package ljuboandtedi.fridger.activties;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ljuboandtedi.fridger.widget.Widget;
import ljuboandtedi.fridger.adapters.IngredientsInShoppingListAdapter;
import ljuboandtedi.fridger.R;
import ljuboandtedi.fridger.model.DatabaseHelper;

public class ShoppingListActivity extends DrawerActivity {
    private RecyclerView listOfIngredients;
    private Button addToFridgeButton;
    private Button removeAllButton;
    private Button addSpecIngredient;
    private EditText specialIngredient;
    private IngredientsInShoppingListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.replaceContentLayout(R.layout.activity_your_ingredients, super.CONTENT_LAYOUT_ID);
        listOfIngredients = (RecyclerView) findViewById(R.id.recycleListForIngredients);
        addToFridgeButton = (Button) findViewById(R.id.yourIngredients_addToFridgeButton);
        removeAllButton = (Button) findViewById(R.id.yourIngredients_addAllToFridgeButton);
        addSpecIngredient = (Button) findViewById(R.id.shoppingList_AddSpecIngredientButton);
        specialIngredient = (EditText) findViewById(R.id.shoppingList_SpecificIngredient);
        listOfIngredients.setLayoutManager(new LinearLayoutManager(this));
        adapter = new IngredientsInShoppingListAdapter(this,DatabaseHelper.getInstance(this).getUserShoppingList(DatabaseHelper.getInstance(this).getCurrentUser().getFacebookID()));
        listOfIngredients.setAdapter(adapter);

        final int sizeOfShoppingList = DatabaseHelper.getInstance(ShoppingListActivity.this).getUserShoppingList(DatabaseHelper.getInstance(ShoppingListActivity.this).getCurrentUser().getFacebookID()).size();
        if(sizeOfShoppingList == 0){
            addToFridgeButton.setVisibility(View.GONE);
            removeAllButton.setVisibility(View.GONE);
        }
        addToFridgeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int numberOfAddedToFridgeProducts = adapter.removeSelectedProducts();
                if(numberOfAddedToFridgeProducts == sizeOfShoppingList){
                    Toast.makeText(ShoppingListActivity.this, "Everything was added", Toast.LENGTH_SHORT).show();
                    updateWidget();
                    finish();
                }
                else if(numberOfAddedToFridgeProducts > 0){
                    Toast.makeText(ShoppingListActivity.this, "Added some ingr. to your fridge.", Toast.LENGTH_SHORT).show();
                    updateWidget();
                }
                else{
                    Toast.makeText(ShoppingListActivity.this, "Nothing selected", Toast.LENGTH_SHORT).show();
                }
                
            }
        });
        removeAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.removeAll();
                Toast.makeText(ShoppingListActivity.this, "Everything was added to your fridge", Toast.LENGTH_SHORT).show();
                updateWidget();
                finish();
            }
        });
        addSpecIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String specificIngredient = specialIngredient.getText().toString();
                if(!specificIngredient.isEmpty() || !(specificIngredient.length() < 3)){
                    if(!adapter.isItAdded(specificIngredient)){
                        Toast.makeText(ShoppingListActivity.this, "Invalid input", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(ShoppingListActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void updateWidget(){
//        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
//        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(getApplicationContext(), Widget.class));
//        if (appWidgetIds.length > 0) {
//            new Widget().onEnabled(getApplicationContext());
//        }
        Intent intent = new Intent(this,Widget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = {getSharedPreferences("Fridger",MODE_PRIVATE).getInt("widget",0)};
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
        sendBroadcast(intent);
    }
}

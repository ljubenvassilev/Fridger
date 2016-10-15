package ljuboandtedi.fridger.activties;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import ljuboandtedi.fridger.widget.CollectionWidget;
import ljuboandtedi.fridger.adapters.IngredientsInShoppingListAdapter;
import ljuboandtedi.fridger.R;
import ljuboandtedi.fridger.model.DatabaseHelper;

public class ShoppingListActivity extends DrawerActivity {
    private RecyclerView listOfIngredients;
    private Button addToFridgeButton;
    private Button removeAllButton;
    private IngredientsInShoppingListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.replaceContentLayout(R.layout.activity_your_ingredients, super.CONTENT_LAYOUT_ID);
        listOfIngredients = (RecyclerView) findViewById(R.id.recycleListForIngredients);
        addToFridgeButton = (Button) findViewById(R.id.yourIngredients_addToFridgeButton);
        removeAllButton = (Button) findViewById(R.id.yourIngredients_addAllToFridgeButton);

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
                    finish();
                }
                else if(numberOfAddedToFridgeProducts > 0){
                    Toast.makeText(ShoppingListActivity.this, "Added some ingr. to your fridge.", Toast.LENGTH_SHORT).show();
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
                updateWidgets(getApplicationContext());
                finish();
            }
        });
    }

    public void updateWidgets(Context context) {
        Intent intent = new Intent(context.getApplicationContext(), CollectionWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
        int[] ids = widgetManager.getAppWidgetIds(new ComponentName(context, CollectionWidget.class));
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            widgetManager.notifyAppWidgetViewDataChanged(ids, android.R.id.list);
        }
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        intent.putExtra("array",user.getShoppingList());
        context.sendBroadcast(intent);
    }
}

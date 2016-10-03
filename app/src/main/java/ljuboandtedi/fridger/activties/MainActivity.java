package ljuboandtedi.fridger.activties;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import ljuboandtedi.fridger.Adapters.MyFridgeMealsAdapter;
import ljuboandtedi.fridger.R;
import ljuboandtedi.fridger.model.SearchesForTesting;
import ljuboandtedi.fridger.model.ShoppingListForTestings;

public class MainActivity extends DrawerActivity {

    private Button apiTestInfoButton;
    CheckBox cbIngrButter;
    CheckBox cbIngrCarrot;
    CheckBox cbIngrGarlic;
    CheckBox cbIngrCheese;
    Button favMealsButton;
    Button shoppingListButton;
    Button myFridgeButton;
    Button searchMenuButton;
    EditText etMeal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        super.replaceContentLayout(R.layout.activity_main, super.CONTENT_LAYOUT_ID);
        favMealsButton = (Button) findViewById(R.id.main_favMealsButton);
        shoppingListButton = (Button) findViewById(R.id.main_shopListButton);
        myFridgeButton = (Button) findViewById(R.id.main_fridgeButton);
        searchMenuButton = (Button) findViewById(R.id.main_SearchMenuButton);

        favMealsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,FavouriteMealsActivity.class);
                startActivity(intent);
            }
        });
        myFridgeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, YourFridgeActivity.class);
                startActivity(intent);
            }
        });
        shoppingListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ShoppingListActivity.class);
                startActivity(intent);
            }
        });
        searchMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SearchMealsActivity.class);
                startActivity(intent);
            }
        });
    }



}

package ljuboandtedi.fridger;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import ljuboandtedi.fridger.model.User;

public class MainActivity extends BasicActivity {

    User user;
    DatabaseHelper db;
    private Button apiTestInfoButton;
    CheckBox cbIngrButter;
    CheckBox cbIngrCarrot;
    CheckBox cbIngrGarlic;
    CheckBox cbIngrCheese;
    EditText etMeal;
    Button profileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userID=getIntent().getStringExtra("userID");
        initUser(userID);
        user = User.getInstance();
        profileButton = (Button) findViewById(R.id.main_profile_button);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ProfileActivity.class).putExtra("userID", userID));
            }
        });
        cbIngrButter = (CheckBox) findViewById(R.id.main_checkBoxButter);
        cbIngrCarrot = (CheckBox) findViewById(R.id.main_checkBoxCarrots);
        cbIngrCheese = (CheckBox) findViewById(R.id.main_checkBoxCheese);
        cbIngrGarlic = (CheckBox) findViewById(R.id.main_checkBoxGarlic);
        etMeal = (EditText) findViewById(R.id.main_etMeal);
        //etSpecialIngr = (EditText) findViewById(R.id.welcomeActivity_etSpecialIngredient);
        //final String specialIngr = "&allowedIngredient[]=" + etSpecialIngr.getText().toString().trim();
        //Testing button
        apiTestInfoButton = (Button) findViewById(R.id.main_mealInfoButton);
        apiTestInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Request for Soup atm you can try with different stuff
                String whatToSearch = etMeal.getText().toString().trim();
                String ingridients = "";
                if(cbIngrButter.isChecked()){
                    ingridients += "&allowedIngredient[]=butter";
                }
                if(cbIngrCarrot.isChecked()){
                    ingridients += "&allowedIngredient[]=carrot";
                }
                if(cbIngrCheese.isChecked()){
                    ingridients += "&allowedIngredient[]=cheese";
                }
                if(cbIngrGarlic.isChecked()){
                    ingridients += "&allowedIngredient[]=garlic";
                }
                Log.i("ingredients",ingridients+"");

                new MainActivity.RequestTask().execute("http://api.yummly.com/v1/api/recipes?_app_id=19ff7314&_app_key=8bdb64c8c177c7e770c8ce0d000263fd&q=" +whatToSearch + ingridients + "&maxResult=40&start=10");

            }
        });
    }


    void initUser (String userID){
        db = new DatabaseHelper(MainActivity.this);
        if(db.userExists(userID)) {
            Cursor rs = db.getUser(userID);
            rs.moveToFirst();
            user.getInstance().setFacebookID(rs.getString(0));
            user.getInstance().setPreferences(db.getUserPreferences(userID));
            if (!rs.isClosed()) {
                rs.close();
            }
        }
        else{
            db.addUser(userID);
        }
    }
    class RequestTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String address = params[0];
            String json = "";
            try {
                URL url = new URL(address);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                Scanner sc = new Scanner(connection.getInputStream());
                while(sc.hasNextLine()){
                    json+=(sc.nextLine());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return json;
        }

        @Override
        protected void onPostExecute(String json) {
            Intent intent = new Intent(MainActivity.this, ShowMealActivity.class);
            intent.putExtra("json", json);
            startActivity(intent);

            Log.i("JSON",json);
        }
    }


}

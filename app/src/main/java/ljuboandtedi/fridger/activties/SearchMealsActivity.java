package ljuboandtedi.fridger.activties;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.yahoo.mobile.client.android.util.rangeseekbar.RangeSeekBar;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import ljuboandtedi.fridger.R;
import ljuboandtedi.fridger.model.User;

public class SearchMealsActivity extends DrawerActivity {
    private Spinner courseSpinner;
    private Spinner holidaySpinner;
    private Button searchButton;
    private EditText mealET;
    private RangeSeekBar<Float> seekBarSweet;
    private RangeSeekBar<Float> seekBarMeaty;
    private RangeSeekBar<Float> seekBarSour;
    private RangeSeekBar<Float> seekBarBitter;
    private RangeSeekBar<Float> seekBarPiquant;
    private final HashMap<String,String> searchOptions = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.replaceContentLayout(R.layout.activity_search_meals, super.CONTENT_LAYOUT_ID);
        courseSpinner = (Spinner) findViewById(R.id.courseSpinner);
        holidaySpinner = (Spinner) findViewById(R.id.holidaySpinner);
        searchButton = (Button) findViewById(R.id.searchMeal_SearchButton);
        mealET = (EditText) findViewById(R.id.seachMeals_mealToSearch);
        seekBarSweet = (RangeSeekBar<Float>) findViewById(R.id.rangeSeekbarSweet);
        seekBarMeaty = (RangeSeekBar<Float>) findViewById(R.id.rangeSeekbarMeaty);
        seekBarPiquant = (RangeSeekBar<Float>) findViewById(R.id.rangeSeekbarPiquant);
        seekBarSour = (RangeSeekBar<Float>) findViewById(R.id.rangeSeekbarSour);
        seekBarBitter = (RangeSeekBar<Float>) findViewById(R.id.rangeSeekbarBitter);

        seekBarSweet.setRangeValues(0.0f, 1.0f);
        seekBarMeaty.setRangeValues(0.0f, 1.0f);
        seekBarSour.setRangeValues(0.0f, 1.0f);
        seekBarPiquant.setRangeValues(0.0f, 1.0f);
        seekBarBitter.setRangeValues(0.0f, 1.0f);

        ArrayList<String> courses = new ArrayList<>();
        courses.add("Course");
        courses.add("Main Dishes");
        courses.add("Desserts");
        courses.add("Salads");
        courses.add("Breakfast and Brunch");
        courses.add("Cocktails");

        ArrayList<String> holidays = new ArrayList<>();
        holidays.add("Holiday");
        holidays.add("Christmas");
        holidays.add("Thanksgiving");
        holidays.add("Hanukkah");
        holidays.add("Halloween");
        holidays.add("4th of July");

        ArrayAdapter<String> adapterCourses = new ArrayAdapter<>(this, R.layout.spinner_item, courses);
        ArrayAdapter<String> adapterHolidays = new ArrayAdapter<>(this, R.layout.spinner_item, holidays);

        adapterHolidays.setDropDownViewResource(R.layout.spinner_item);
        adapterCourses.setDropDownViewResource(R.layout.spinner_item);

        courseSpinner.setAdapter(adapterCourses);
        holidaySpinner.setAdapter(adapterHolidays);
        courseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                searchOptions.put("course", parent.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        holidaySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                searchOptions.put("holiday", parent.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String holiday = searchOptions.get("holiday");
                String course = searchOptions.get("course");
                if (holiday.equals("Holiday")) {
                    holiday = "";
                } else {
                    holiday = "&allowedHoliday[]=holiday^holiday-" + holiday.trim().replace(" ", "+");
                }
                if (course.equals("Course")) {
                    course = "";
                } else {
                    course = "&allowedCourse[]=course^course-" + course.trim().replace(" ", "+");
                }
                String bitterness = "&flavor.bitter.min="+Math.floor(seekBarBitter.getSelectedMinValue() * 100) / 1000 + "&flavor.bitter.max=" + Math.floor(seekBarBitter.getSelectedMaxValue() * 100) / 100 ;
                Log.e("floatite",bitterness);
                String sweetness = "&flavor.sweet.min="+Math.floor(seekBarSweet.getSelectedMinValue() * 100) / 10 + "&flavor.sweet.max=" + Math.floor(seekBarSweet.getSelectedMaxValue() * 100) / 10;
                Log.e("floatite",sweetness);
                String sourness = "&flavor.sour.min="+Math.floor(seekBarSour.getSelectedMinValue() * 100) / 10 + "&flavor.sour.max=" + Math.floor(seekBarSour.getSelectedMinValue() * 100) / 10;
                String piquantness = "&flavor.piquant.min="+seekBarPiquant.getSelectedMinValue() + "&flavor.piquant.max=" + seekBarPiquant.getSelectedMinValue();
                String meatyness = "&flavor.meaty.min="+seekBarMeaty.getSelectedMinValue() + "&flavor.meaty.max=" + seekBarMeaty.getSelectedMinValue();

                final String whatToSearch = mealET.getText().toString().trim().replace(" ", "+");
                new SearchMealsActivity.RequestTask().execute("http://api.yummly.com/v1/api/recipes?_"+getResources().getString(R.string.api)+"&q=" + whatToSearch + course + holiday + user.getPreferences()+"&maxResult=40&start=10");
            }
        });
    }

    private class RequestTask extends AsyncTask<String, Void, String> {

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
            Log.e("tupiqJson",json + "blaa");
            if(json.startsWith("No such recipe:")){
                Intent intent = new Intent(SearchMealsActivity.this,ShoppingListActivity.class);
                startActivity(intent);
                finish();
            }
            Intent intent = new Intent(SearchMealsActivity.this, ShowMealActivity.class);
            intent.putExtra("json", json);
            startActivity(intent);
        }
    }
}

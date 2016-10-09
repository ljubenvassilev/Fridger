package ljuboandtedi.fridger.activties;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.yahoo.mobile.client.android.util.rangeseekbar.RangeSeekBar;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import ljuboandtedi.fridger.R;

public class SearchMealsActivity extends DrawerActivity {
    Spinner courseSpinner;
    Spinner holidaySpinner;

    Button searchButton;
    EditText mealET;
    RangeSeekBar<Float> seekBarSweet;
    RangeSeekBar<Float> seekBarMeaty;
    RangeSeekBar<Float> seekBarSour;
    RangeSeekBar<Float> seekBarBitter;
    RangeSeekBar<Float> seekBarPiquant;
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

        seekBarSweet.setRangeValues(0.00f, 1.00f);
        seekBarMeaty.setRangeValues(0.00f, 1.00f);
        seekBarSour.setRangeValues(0.00f, 1.00f);
        seekBarPiquant.setRangeValues(0.00f, 1.00f);
        seekBarBitter.setRangeValues(0.00f, 1.00f);



        ArrayList<String> courses = new ArrayList<>();
        courses.add("Choose course");
        courses.add("Main Dishes");
        courses.add("Desserts");
        courses.add("Salads");
        courses.add("Breakfast and Brunch");
        courses.add("Lunch and Snacks");
        courses.add("Cocktails");

        final ArrayList<String> holidays = new ArrayList<>();
        holidays.add("Choose holiday");
        holidays.add("Christmas");
        holidays.add("Thanksgiving");
        holidays.add("Hanukkah");
        holidays.add("Halloween");
        holidays.add("4th of July");
        final HashMap<String,String> searchOptions = new HashMap<>();
        ArrayAdapter adapterCourses = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,courses);
        ArrayAdapter adapterHolidays = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,holidays);
        adapterHolidays.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseSpinner.setAdapter(adapterCourses);
        holidaySpinner.setAdapter(adapterHolidays);
        courseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                searchOptions.put("course",parent.getSelectedItem().toString());
                //&allowedCourse[]=course^course-Appetizers
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        holidaySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                searchOptions.put("holiday",parent.getSelectedItem().toString());
                //&allowedHoliday[]=holiday^holiday-thanksgiving
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

                if(holiday.equals("Choose holiday")){
                    holiday = "";
                }
                else{
                    holiday = "&allowedHoliday[]=holiday^holiday-" + holiday.trim().replace(" ","+");
                }
                if(course.equals("Choose course")){
                    course = "";
                }
                else{
                    course = "&allowedCourse[]=course^course-"+course.trim().replace(" ","+");
                }
                seekBarBitter.getSelectedMaxValue();
                seekBarBitter.getSelectedMinValue();
               final String whatToSearch = mealET.getText().toString();
                new SearchMealsActivity.RequestTask().execute("http://api.yummly.com/v1/api/recipes?_app_id=19ff7314&_app_key=8bdb64c8c177c7e770c8ce0d000263fd&q=" + whatToSearch + course + holiday +"&maxResult=40&start=10");

            }
        });

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
            Intent intent = new Intent(SearchMealsActivity.this, ShowMealActivity.class);
            intent.putExtra("json", json);
            startActivity(intent);

            Log.i("JSON",json);
        }
    }


}

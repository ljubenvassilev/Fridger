package ljuboandtedi.fridger.activties;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
    private RangeSeekBar<Integer> seekBarSweet;
    private RangeSeekBar<Integer> seekBarMeaty;
    private RangeSeekBar<Integer> seekBarSour;
    private RangeSeekBar<Integer> seekBarBitter;
    private RangeSeekBar<Integer> seekBarPiquant;
    private final HashMap<String,String> searchOptions = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.replaceContentLayout(R.layout.activity_search_meals, super.CONTENT_LAYOUT_ID);
        courseSpinner = (Spinner) findViewById(R.id.courseSpinner);
        holidaySpinner = (Spinner) findViewById(R.id.holidaySpinner);
        searchButton = (Button) findViewById(R.id.searchMeal_SearchButton);
        mealET = (EditText) findViewById(R.id.seachMeals_mealToSearch);
        seekBarSweet = (RangeSeekBar<Integer>) findViewById(R.id.rangeSeekbarSweet);
        seekBarMeaty = (RangeSeekBar<Integer>) findViewById(R.id.rangeSeekbarMeaty);
        seekBarPiquant = (RangeSeekBar<Integer>) findViewById(R.id.rangeSeekbarPiquant);
        seekBarSour = (RangeSeekBar<Integer>) findViewById(R.id.rangeSeekbarSour);
        seekBarBitter = (RangeSeekBar<Integer>) findViewById(R.id.rangeSeekbarBitter);

        seekBarSweet.setRangeValues(0, 10);
        seekBarMeaty.setRangeValues(0, 10);
        seekBarSour.setRangeValues(0, 10);
        seekBarPiquant.setRangeValues(0, 10);
        seekBarBitter.setRangeValues(0, 10);

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
                String bitterness = "&flavor.bitter.min=0."+seekBarBitter.getSelectedMinValue().toString()+ "&flavor.bitter.max=0."+seekBarBitter.getSelectedMinValue().toString();
                Log.e("floatite",bitterness);

                String sweetness = "&flavor.sweet.min="+Math.floor(seekBarSweet.getSelectedMinValue() * 100) / 10 + "&flavor.sweet.max=" + Math.floor(seekBarSweet.getSelectedMaxValue() * 100) / 10;
                Log.e("floatite",sweetness);

                String sourness = "&flavor.sour.min="+Math.floor(seekBarSour.getSelectedMinValue() * 100) / 10 + "&flavor.sour.max=" + Math.floor(seekBarSour.getSelectedMinValue() * 100) / 10;
                Log.e("floatite",sweetness);

                String piquantness = "&flavor.piquant.min="+seekBarPiquant.getSelectedMinValue() + "&flavor.piquant.max=" + seekBarPiquant.getSelectedMinValue();
                Log.e("floatite",piquantness);
                String meatyness = "&flavor.meaty.min="+seekBarMeaty.getSelectedMinValue() + "&flavor.meaty.max=" + seekBarMeaty.getSelectedMinValue();

                final String whatToSearch = mealET.getText().toString().trim().replace(" ", "+");
                final String searchQuery = "http://api.yummly.com/v1/api/recipes?_"+getResources().getString(R.string.api)+"&q=" + whatToSearch + course + holiday + user.getPreferences()+"&maxResult=40&start=10";
                new SearchMealsActivity.RequestTask().execute(searchQuery);

                final SharedPreferences.Editor editor = SearchMealsActivity.this.
                        getSharedPreferences("Fridger", Context.MODE_PRIVATE).edit();
                editor.putString("lastSearch", searchQuery).apply();
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
            Intent intent = new Intent(SearchMealsActivity.this, ShowMealActivity.class);
            intent.putExtra("json", json);
            intent.putExtra("search",searchedInfo);
            startActivity(intent);
        }
    }
}

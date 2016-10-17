package ljuboandtedi.fridger.activties;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import ljuboandtedi.fridger.R;
import ljuboandtedi.fridger.adapters.MealRecyclerAdapter;

public class ShowMealActivity extends DrawerActivity {

    private RecyclerView listOfMeals;
    private MealRecyclerAdapter adapter;
    private RelativeLayout rl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.replaceContentLayout(R.layout.activity_recyclelist, super.CONTENT_LAYOUT_ID);

        listOfMeals = (RecyclerView) findViewById(R.id.recycleList);
        rl = (RelativeLayout) findViewById(R.id.recycle_list_blabla);
        List<String> recipes = new ArrayList<>();
        String info = getIntent().getStringExtra("json");

        try {
            JSONObject json = new JSONObject(info);
            JSONArray matches = json.getJSONArray("matches");

            for (int i = 0; i < matches.length(); i++) {
                StringBuilder mealFlavors = new StringBuilder();
                Log.i("matches", matches.length() + "");
                String attributes = matches.getString(i);
                JSONObject attributesInJson = new JSONObject(attributes);

                if (!attributesInJson.isNull("flavors")) {
                    JSONObject flavorsInJson = attributesInJson.getJSONObject("flavors");
                    mealFlavors.append("piquant: ").append(flavorsInJson.getDouble("piquant"));
                    mealFlavors.append("\nmeaty: ").append(flavorsInJson.getDouble("meaty"));
                    mealFlavors.append("\nbitter: ").append(flavorsInJson.getDouble("bitter"));
                    mealFlavors.append("\nsweet: ").append(flavorsInJson.getDouble("sweet"));
                    mealFlavors.append("\nsour: " ).append(flavorsInJson.getDouble("sour"));
                    mealFlavors.append("\nsalty: ").append(flavorsInJson.getDouble("salty"));
                }

                String id = "";
                if (!attributesInJson.isNull("id")) {
                    id = attributesInJson.getString("id");
                }
                recipes.add(id);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapter = new ljuboandtedi.fridger.adapters.MealRecyclerAdapter(this, recipes);
        listOfMeals.setLayoutManager(new LinearLayoutManager(this));
        listOfMeals.setAdapter(adapter);

        adapter.notifyDataSetChanged();
        if(adapter.getItemCount() == 0){

        }
    }
}


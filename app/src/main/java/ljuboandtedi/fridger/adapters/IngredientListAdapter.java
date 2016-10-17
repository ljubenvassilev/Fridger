package ljuboandtedi.fridger.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ljuboandtedi.fridger.R;
import ljuboandtedi.fridger.model.DatabaseHelper;

/**
 * Created by NoLight on 16.10.2016 г..
 */

public class IngredientListAdapter extends ArrayAdapter {

    private List<String> ingredients;
    private HashMap<String,Boolean> ingredientsChecker;
    private Activity activity;
    public IngredientListAdapter(Activity context, List objects) {
        super(context, R.layout.ingredient_row, objects);
        this.ingredients = objects;
        this.activity = context;
        ingredientsChecker = new HashMap<>();
        for (String s : ingredients) {
            ingredientsChecker.put(s, false);
        }
    }
    class IngredientViewHolder{
        TextView ingredient;
        CheckBox checkBox;
        IngredientViewHolder(View row){
            ingredient = (TextView) row.findViewById(R.id.buyingIngredient);
            checkBox = (CheckBox) row.findViewById(R.id.buyingIngredientChecked);
        }
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View row;
        IngredientViewHolder vh;
        final String ingredient = ingredients.get(position);
        if (convertView != null) {
            row = convertView;
            vh = (IngredientViewHolder) row.getTag();
        } else {
            row = inflater.inflate(R.layout.ingredient_row, parent, false);
            vh = new IngredientViewHolder(row);
            row.setTag(vh);
        }
        boolean isitSelected = ingredientsChecker.get(ingredient);

        if(isitSelected){
            vh.checkBox.setChecked(true);
        }
        if(!isitSelected){
            vh.checkBox.setChecked(false);
        }
        vh.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    ingredientsChecker.put(ingredient,true);
                }
                if(!isChecked){
                    ingredientsChecker.put(ingredient,false);
                }
            }
        });
        vh.ingredient.setText(ingredient);
        return row;
    }
}

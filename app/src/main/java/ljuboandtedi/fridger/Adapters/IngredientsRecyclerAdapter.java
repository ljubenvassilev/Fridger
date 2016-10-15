package ljuboandtedi.fridger.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;


import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ljuboandtedi.fridger.R;
import ljuboandtedi.fridger.model.DatabaseHelper;

/**
 * Created by NoLight on 28.9.2016 г..
 */

public class IngredientsRecyclerAdapter extends  RecyclerView.Adapter<IngredientsRecyclerAdapter.MyIngredientViewHolder>{

    private List<String> ingredients;
    private HashMap<String,Boolean> ingredientsChecker;
    private Activity activity;

    public IngredientsRecyclerAdapter(Activity activity, List<String> ingredients) {
        this.ingredients = ingredients;
        ingredientsChecker = new HashMap<>();
        for (String s : ingredients) {
            ingredientsChecker.put(s, false);
        }
        this.activity = activity;
    }

    @Override
    public MyIngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View row = inflater.inflate(R.layout.ingredient_row, parent, false);
        return new MyIngredientViewHolder(row);
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    @Override
    public void onBindViewHolder(final MyIngredientViewHolder holder, int position) {
        final String ingredient = ingredients.get(position);
        boolean isitSelected = ingredientsChecker.get(ingredient);

        if(isitSelected){
            holder.cb.setChecked(true);
        }
        if(!isitSelected){
            holder.cb.setChecked(false);
        }
        holder.cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
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
        holder.ingredient.setText(ingredient);
    }

    class MyIngredientViewHolder extends RecyclerView.ViewHolder{
        private TextView ingredient;
        private CheckBox cb;
        MyIngredientViewHolder(View row){
            super(row);
            cb = (CheckBox) row.findViewById(R.id.buyingIngredientChecked);
            ingredient = (TextView)    row.findViewById(R.id.buyingIngredient);
            ingredient.setSelected(true);
        }
    }
    public int removeSelectedProducts(){
        int counter = 0;
        Iterator<Map.Entry<String,Boolean>> iter = ingredientsChecker.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String,Boolean> entry = iter.next();
            if(entry.getValue()){
                DatabaseHelper.getInstance(activity).addToShoppingList(entry.getKey());
                iter.remove();
                ingredients.remove(entry.getKey());
                counter++;
            }
        }
        notifyDataSetChanged();
        return counter;
    }
    public void removeAll(){
        Iterator<Map.Entry<String,Boolean>> iter = ingredientsChecker.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String,Boolean> entry = iter.next();
            DatabaseHelper.getInstance(activity).addToShoppingList(entry.getKey());
            iter.remove();
            ingredients.remove(entry.getKey());
        }
        notifyDataSetChanged();
    }

}



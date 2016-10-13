package ljuboandtedi.fridger.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.uniquestudio.library.CircleCheckBox;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ljuboandtedi.fridger.R;
import ljuboandtedi.fridger.model.DatabaseHelper;

/**
 * Created by NoLight on 28.9.2016 г..
 */

public class MyFridgeMealsAdapter extends  RecyclerView.Adapter<MyFridgeMealsAdapter.MyIngredientViewHolder>{

    private List<String> ingredients;
    //private List<Boolean[]> checked;
    private HashMap<String,Boolean> ingredientsChecker;
    private Activity activity;

    public MyFridgeMealsAdapter(Activity activity, List<String> ingredients){
        this.ingredients = ingredients;
        this.activity = activity;
        ingredientsChecker = new HashMap<>();
        for(String s: ingredients){
            ingredientsChecker.put(s,false);
        }

    }

    @Override
    public MyIngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate xml
        LayoutInflater inflater = activity.getLayoutInflater();
        View row = inflater.inflate(R.layout.ingredients_info_activity, parent, false);
        //create vh
        MyIngredientViewHolder vh = new MyIngredientViewHolder(row);
        //return vh
        return vh;
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    @Override
    public void onBindViewHolder(MyIngredientViewHolder holder, int position) {
        final String ingredient = ingredients.get(position);
        boolean isitSelected = ingredientsChecker.get(ingredient);


        Log.e("ingredient",ingredient);
        Log.e("selected",isitSelected + "");
        if(isitSelected){
            holder.cb.setChecked(true);
        }
        if(!isitSelected){
            holder.cb.setChecked(false);
        }
        holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    ingredientsChecker.put(ingredient,true);
                }
                if(!isChecked){
                    if(DatabaseHelper.getInstance(activity).getUserFridge(DatabaseHelper.getInstance(activity).getCurrentUser().getFacebookID()).contains(ingredient)){

                        ingredientsChecker.put(ingredient,false);
                    }
                }

            }

        });
        holder.ingredient.setText(ingredient);

    }

    class MyIngredientViewHolder extends RecyclerView.ViewHolder{
        TextView ingredient;
        CheckBox cb;
        MyIngredientViewHolder(View row){
            super(row);
            cb = (CheckBox) row.findViewById(R.id.buyingIngredientChecked);
            ingredient = (TextView)    row.findViewById(R.id.buyingIngredient);

        }
    }
    public int removeSelectedProducts(){
        int counter = 0;
        Iterator<Map.Entry<String,Boolean>> iter = ingredientsChecker.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String,Boolean> entry = iter.next();
            if(entry.getValue()){
                DatabaseHelper.getInstance(activity).removeFromFridge(entry.getKey());
                //DatabaseHelper.getInstance(activity).addToFridge(entry.getKey());
                iter.remove();
                ingredients.remove(entry.getKey());
               // ingredientsChecker.remove(entry.getKey());
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
            //DatabaseHelper.getInstance(activity).addToFridge(entry.getKey());
            DatabaseHelper.getInstance(activity).removeFromFridge(entry.getKey());
            ingredients.remove(entry.getKey());
            iter.remove();

           // ingredientsChecker.remove(entry.getKey());
        }
        ingredients.clear();
        ingredientsChecker.clear();
        notifyDataSetChanged();
    }


}


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

public class IngredientsInShoppingListAdapter extends  RecyclerView.Adapter<IngredientsInShoppingListAdapter.MyIngredientViewHolder>{

    private List<String> ingredients;
    private HashMap<String,Boolean> ingredientsChecker;
    private Activity activity;

    public IngredientsInShoppingListAdapter(Activity activity, List<String> ingredients){
        this.ingredients = ingredients;
        ingredientsChecker = new HashMap<>();
        for(String s: ingredients){
            ingredientsChecker.put(s,false);
        }
        this.activity = activity;
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
        //fill data of the VH with the data of the object
        int cbHeight = holder.cb.getHeight();
        ViewGroup.LayoutParams params =  holder.cb.getLayoutParams();
        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        double width = size.x;
        Log.e("widthScreen",width + "");
        double height = size.y;
        Log.e("heightScreen",height + " ");
        double scale = width/height;
        Log.e("scale",scale + "");
        Log.e("height",params.height + "");
        double newWidth = (params.height*scale);
        Log.e("new width",""+newWidth);
        params.width =(int) newWidth + 1;

        holder.cb.setLayoutParams(params);
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
                    DatabaseHelper.getInstance(activity).addToFridge(ingredient);
                    DatabaseHelper.getInstance(activity).removeFromShoppingList(ingredient);
                }
                if(!isChecked){
                    if(DatabaseHelper.getInstance(activity).getUserFridge(DatabaseHelper.getInstance(activity).getCurrentUser().getFacebookID()).contains(ingredient)){
                        ingredientsChecker.put(ingredient,false);
                        DatabaseHelper.getInstance(activity).removeFromFridge(ingredient);
                        DatabaseHelper.getInstance(activity).addToShoppingList(ingredient);
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
            this.setIsRecyclable(false);
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

                Log.e("entryKey",entry.getKey());
                DatabaseHelper.getInstance(activity).addToFridge(entry.getKey());
                Log.e("addedToFridgeInAdapter",DatabaseHelper.getInstance(activity).getUserShoppingList(DatabaseHelper.getInstance(activity).getCurrentUser().getFacebookID()).toString());
                iter.remove();

                ingredients.remove(entry.getKey());
                //ingredientsChecker.remove(entry.getKey());
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
            DatabaseHelper.getInstance(activity).addToFridge(entry.getKey());
            iter.remove();
            ingredients.remove(entry.getKey());
           // ingredientsChecker.remove(entry.getKey());
        }
        notifyDataSetChanged();
    }

}


package ljuboandtedi.fridger.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.uniquestudio.library.CircleCheckBox;


import java.util.List;

import ljuboandtedi.fridger.R;
import ljuboandtedi.fridger.model.DatabaseHelper;

/**
 * Created by NoLight on 28.9.2016 г..
 */

public class IngredientsRecyclerAdapter extends  RecyclerView.Adapter<IngredientsRecyclerAdapter.MyIngredientViewHolder>{

    private List<String> ingredients;

    private Activity activity;

    public IngredientsRecyclerAdapter(Activity activity, List<String> ingredients){
        this.ingredients = ingredients;
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
        //get obj on position
        final String ingredient = ingredients.get(position);

        //fill data of the VH with the data of the object

        holder.cb.setListener(new CircleCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if(isChecked){

                    DatabaseHelper.getInstance(activity).addToShoppingList(ingredient);
                }
               if(!isChecked){
                       if(DatabaseHelper.getInstance(activity).getUserShoppingList(DatabaseHelper.getInstance(activity).getCurrentUser().getFacebookID()).contains(ingredient)){
                           DatabaseHelper.getInstance(activity).removeFromShoppingList(ingredient);
                       }
                   }

            }
        });
        holder.ingredient.setText(ingredient);

    }

    class MyIngredientViewHolder extends RecyclerView.ViewHolder{
        TextView ingredient;
        CircleCheckBox cb;
        MyIngredientViewHolder(View row){
            super(row);
            cb = (CircleCheckBox) row.findViewById(R.id.buyingIngredientChecked);
            ingredient = (TextView)    row.findViewById(R.id.buyingIngredient);

        }
    }
}


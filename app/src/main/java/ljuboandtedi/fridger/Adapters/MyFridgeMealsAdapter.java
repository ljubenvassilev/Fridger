package ljuboandtedi.fridger.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

import ljuboandtedi.fridger.R;
import ljuboandtedi.fridger.model.DatabaseHelper;

/**
 * Created by NoLight on 28.9.2016 г..
 */

public class MyFridgeMealsAdapter extends  RecyclerView.Adapter<MyFridgeMealsAdapter.MyIngredientViewHolder>{

    private List<String> ingredients;
    //private List<Boolean[]> checked;
    private Activity activity;

    public MyFridgeMealsAdapter(Activity activity, List<String> ingredients){
        this.ingredients = ingredients;
        this.activity = activity;
//        checked = new ArrayList<>();
//        for(int i = 0; i < ingredients.size(); i++){
//            checked.add(new Boolean[1]);
//        }
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
       // final Boolean[] isSelected = checked.get(position);
        //fill data of the VH with the data of the object
       // holder.cb.setOnCheckedChangeListener(null);
        //holder.cb.setChecked(isSelected[0]);
        holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //isSelected[0] = true;
                    DatabaseHelper.getInstance(activity).removeFromFridge(ingredient);
                }
                if(!isChecked){
                    if(DatabaseHelper.getInstance(activity).getUserFridge(DatabaseHelper.getInstance(activity).getCurrentUser().getFacebookID()).contains(ingredient)){
                        DatabaseHelper.getInstance(activity).addToFridge(ingredient);
                        //isSelected[0] = false;
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
}


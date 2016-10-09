package ljuboandtedi.fridger.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import ljuboandtedi.fridger.R;
import ljuboandtedi.fridger.model.IngredientValues;

/**
 * Created by NoLight on 10.10.2016 г..
 */

public class IngridientValuesAdapter extends RecyclerView.Adapter<IngridientValuesAdapter.MyIngredientViewHolder> {
    private List<IngredientValues> ingridients;
    private Activity activity;

    public IngridientValuesAdapter(List<IngredientValues> ingridients, Activity activity) {
        this.ingridients = ingridients;
        this.activity = activity;
    }

    @Override
    public MyIngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate xml
        LayoutInflater inflater = activity.getLayoutInflater();
        View row = inflater.inflate(R.layout.activity_nutritions_row, parent, false);
        //create vh
        IngridientValuesAdapter.MyIngredientViewHolder vh = new IngridientValuesAdapter.MyIngredientViewHolder(row);
        //return vh
        return vh;
    }

    @Override
    public void onBindViewHolder(MyIngredientViewHolder holder, int position) {
        IngredientValues ingr = ingridients.get(position);
        holder.ingredientName.setText(ingr.getType());
        holder.ingredientValue.setText(ingr.getValue() + "");

    }


    @Override
    public int getItemCount() {
        return ingridients.size();
    }

    class MyIngredientViewHolder extends RecyclerView.ViewHolder{
        TextView ingredientName;
        TextView ingredientValue;
        MyIngredientViewHolder(View row){
            super(row);
            ingredientName = (TextView) row.findViewById(R.id.nutritions_ingrName);
            ingredientValue = (TextView)    row.findViewById(R.id.nutritions_ingrValue);
        }
    }
}

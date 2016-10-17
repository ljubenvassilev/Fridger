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

import static com.facebook.FacebookSdk.getApplicationContext;

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
        LayoutInflater inflater = activity.getLayoutInflater();
        View row = inflater.inflate(R.layout.activity_nutritions_row, parent, false);
        return new MyIngredientViewHolder(row);
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
        private TextView ingredientName;
        private TextView ingredientValue;
        MyIngredientViewHolder(View row){
            super(row);
            ingredientName = (TextView) row.findViewById(R.id.nutritions_ingrName);
            ingredientName.setTextColor(getApplicationContext().getResources().getColor(R.color.white));
            ingredientName.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.darkBlue));
            ingredientName.setTextSize(20);
            ingredientValue = (TextView)    row.findViewById(R.id.nutritions_ingrValue);
            ingredientValue.setTextColor(getApplicationContext().getResources().getColor(R.color.white));
            ingredientValue.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.darkBlue));
            ingredientValue.setTextSize(20);
        }
    }
}

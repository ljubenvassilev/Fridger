package ljuboandtedi.fridger.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import ljuboandtedi.fridger.R;
import ljuboandtedi.fridger.activties.RecipeInfoActivity;
import ljuboandtedi.fridger.model.IngredientValues;
import ljuboandtedi.fridger.model.Recipe;
import ljuboandtedi.fridger.model.RecipeManager;

/**
 * Created by NoLight on 12.10.2016 г..
 */

public class SearchingAdapter extends  RecyclerView.Adapter<SearchingAdapter.MyIngredientViewHolder> {
    private List<String> ingridients;
    private List<String> ingridientsPic;

    private Activity activity;

    public SearchingAdapter(List<String> ingridients,List<String> ingridientsPic, Activity activity) {
        this.ingridients = ingridients;
        this.ingridientsPic = ingridientsPic;
        this.activity = activity;

    }

    @Override
    public SearchingAdapter.MyIngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate xml
        LayoutInflater inflater = activity.getLayoutInflater();
        View row = inflater.inflate(R.layout.search_row_recycle, parent, false);
        //create vh
        MyIngredientViewHolder vh = new MyIngredientViewHolder(row);
        //return vh
        return vh;
    }

    @Override
    public void onBindViewHolder(MyIngredientViewHolder holder, int position) {
        String ingr = ingridients.get(position);
        String ingrPic = ingridientsPic.get(position);

        holder.ingredientValue.setText(ingr);
        holder.ingridientSmallIV.setImageResource(R.drawable.below_shadow);
        new  RequestTask(holder,ingr).execute(ingrPic);

    }


    @Override
    public int getItemCount() {
        return ingridients.size();
    }

    class MyIngredientViewHolder extends RecyclerView.ViewHolder{
        TextView ingredientValue;
        ImageView ingridientSmallIV;

        MyIngredientViewHolder(View row){
            super(row);

            ingredientValue = (TextView)    row.findViewById(R.id.searching_TV);
            ingridientSmallIV = (ImageView) row.findViewById(R.id.searching_IV);

        }
    }
    private class RequestTask extends AsyncTask<String, Void, Bitmap> {

        MyIngredientViewHolder holder;
        String recipeName;
        RequestTask(MyIngredientViewHolder holder, String recipeName){
            this.holder = holder;
            this.recipeName = recipeName;
        }
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String address = params[0];
            Bitmap bitmap = null;

            try {
                URL url = new URL(address);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                InputStream is = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(is);


            } catch (IOException e) {
                e.printStackTrace();
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap image) {

            holder.ingridientSmallIV.setImageBitmap(image);
//            holder.overlay.setBackgroundResource(R.color.transparent);
//            holder.overlay.setImageResource(R.drawable.gradient);
            holder.ingridientSmallIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity,RecipeInfoActivity.class);
                    intent.putExtra("recipe",recipeName);
                    activity.startActivity(intent);
                }
            });
        }

    }
}

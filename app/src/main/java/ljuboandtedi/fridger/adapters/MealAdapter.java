package ljuboandtedi.fridger.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import ljuboandtedi.fridger.R;
import ljuboandtedi.fridger.activties.RecipeInfoActivity;
import ljuboandtedi.fridger.model.IngredientValues;
import ljuboandtedi.fridger.model.Recipe;
import ljuboandtedi.fridger.model.RecipeManager;

/**
 * Created by NoLight on 12.10.2016 г..
 */

public class MealAdapter extends ArrayAdapter {

    private List<Bitmap> bitmaps;
    private Activity activity;
    public MealAdapter(Activity activity, List bitmaps) {
        super(activity,R.layout.meal_row, bitmaps);
        this.bitmaps = bitmaps;
        this.activity =activity;
    }
    class MealViewHolder{
        ImageView image;


        MealViewHolder(View row){
            image = (ImageView)  row.findViewById(R.id.helloText);

        }
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.e("blaaaaa","vika se");
        LayoutInflater inflater = activity.getLayoutInflater();
        View row;
        MealViewHolder vh;
        Bitmap bitmap = bitmaps.get(position);

        if (convertView != null) {
            row = convertView;
            vh = (MealViewHolder) row.getTag();
        } else {
            row = inflater.inflate(R.layout.meal_row, parent, false);
            vh = new MealViewHolder(row);
            row.setTag(vh);
        }

        Log.e("doTukSym", "a");

        vh.image.setImageBitmap(bitmap);
        //vh.image.setScaleType(ImageView.ScaleType.FIT_XY);


        return row;
    }
    @Override
    public Bitmap getItem(int position) {
        // TODO Auto-generated method stub
        return bitmaps.get(position);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return bitmaps.size();
    }

    @Override
    public void remove(Object object) {
        super.remove(object);
    }

    @Override
    public void addAll(Collection collection) {
        super.addAll(collection);
    }

    @Override
    public void clear() {
        super.clear();
    }
}

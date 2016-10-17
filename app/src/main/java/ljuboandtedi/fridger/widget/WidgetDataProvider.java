package ljuboandtedi.fridger.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.facebook.FacebookSdk;

import java.util.ArrayList;
import java.util.List;

import ljuboandtedi.fridger.model.DatabaseHelper;

import static com.facebook.FacebookSdk.getApplicationContext;

class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {

    List<String> mCollection = new ArrayList<>();
    Context mContext = null;

    WidgetDataProvider(Context context, Intent intent) { mContext = context; }

    private void initData() {
        mCollection=new ArrayList<>();
        FacebookSdk.sdkInitialize(mContext);
        SharedPreferences sharedPreferences = getApplicationContext().
                getSharedPreferences("Fridger",Context.MODE_PRIVATE);
        String user = sharedPreferences.getString("current","");
        Log.d("widget",user);
        if(user.length()>0){
            DatabaseHelper.getInstance(mContext).initUsers(user);
            List<String> list = DatabaseHelper.getInstance(mContext).getCurrentUser()
                    .getShoppingList();
            mCollection.addAll(list);
        }
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews view = new RemoteViews(mContext.getPackageName(),
                android.R.layout.simple_list_item_1);
        view.setTextViewText(android.R.id.text1, mCollection.get(position));
        return view;
    }

    @Override
    public void onCreate() { initData(); }

    @Override
    public void onDataSetChanged() { initData(); }

    @Override
    public void onDestroy() {}

    @Override
    public int getCount() {
        return mCollection.size();
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}

package ljuboandtedi.fridger.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import ljuboandtedi.fridger.R;
import ljuboandtedi.fridger.activties.WelcomeActivity;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Implementation of App Widget functionality.
 */
public class CollectionWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d("widget",String.valueOf(appWidgetIds[0]));
        SharedPreferences.Editor editor =  getApplicationContext().
                getSharedPreferences("Fridger", Context.MODE_PRIVATE).edit();
        editor.putInt("widget", appWidgetIds[0]).commit();
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.collection_widget);
        views.setRemoteAdapter(R.id.widget_list, new Intent(context, WidgetService.class));
        Intent intent = new Intent(context,WelcomeActivity.class);
        views.setOnClickPendingIntent(R.id.widget,PendingIntent
                .getActivity(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT));
        ComponentName thisWidget = new ComponentName(context, CollectionWidget.class);
        AppWidgetManager.getInstance(context).updateAppWidget(thisWidget, views);
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("widget", "receive");
        String action = intent.getAction();
        if (AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(action)) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                int[] appWidgetIds = extras.getIntArray(AppWidgetManager.EXTRA_APPWIDGET_IDS);
                if (appWidgetIds != null && appWidgetIds.length > 0) {
                    this.onUpdate(context, AppWidgetManager.getInstance(context), appWidgetIds);
                }
            }
        }
    }
}
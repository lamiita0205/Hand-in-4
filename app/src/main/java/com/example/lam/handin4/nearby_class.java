package com.example.lam.handin4;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Dan Iorga on 10/7/2015.
 */
public class nearby_class extends AppWidgetProvider {

    public static final String WIDGET_IDS_KEY ="mywidgetproviderwidgetids";
    public static final String WIDGET_DATA_KEY ="mywidgetproviderwidgetdata";


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.hasExtra(WIDGET_IDS_KEY)) {
            int[] ids = intent.getExtras().getIntArray(WIDGET_IDS_KEY);
            if (intent.hasExtra(WIDGET_DATA_KEY)) {
                Object data = intent.getExtras().getParcelable(WIDGET_DATA_KEY);
                this.update(context, AppWidgetManager.getInstance(context), ids, data);
            } else {
                this.onUpdate(context, AppWidgetManager.getInstance(context), ids);
            }
        } else super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        update(context, appWidgetManager, appWidgetIds, null);
    }

    public void update(Context context, AppWidgetManager manager, int[] ids, Object data)
    {
        //I suppose put the image there, or whatever i can do?!
        for(int i=0;i<ids.length;i++) {
            int currentWidgetId = ids[i];

            Intent intent = new Intent(context.getApplicationContext(),
                    UpdateWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);

            // Update the widgets via the service
            context.startService(intent);
        }
    }
}

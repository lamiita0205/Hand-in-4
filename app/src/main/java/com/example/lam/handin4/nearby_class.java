package com.example.lam.handin4;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Dan Iorga on 10/7/2015.
 */
public class nearby_class extends AppWidgetProvider {

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidhetIds)
    {
        //I suppose put the image there, or whateber i can do?!
        for(int i=0;i<appWidhetIds.length;i++) {
            int currentWidgetId = appWidhetIds[i];

            Intent intent = new Intent(context.getApplicationContext(),
                    UpdateWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidhetIds);

            // Update the widgets via the service
            context.startService(intent);
        }
    }
}

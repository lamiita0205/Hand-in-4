package com.example.lam.handin4;

/**
 * Created by Dan Iorga on 10/7/2015.
 */
import java.util.Random;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

public class UpdateWidgetService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startID)
    {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this
                    .getApplicationContext());
            int[] allWidgetIds = intent
                    .getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);

            RemoteViews remoteViews = new RemoteViews(this
                .getApplicationContext().getPackageName(),
                R.layout.nearby_layout);
            // Register an onClickListener
            Intent clickIntent = new Intent(this.getApplicationContext(),
                nearby_class.class);

            clickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,
                allWidgetIds);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, clickIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.btn, pendingIntent);

            for (int widgetId : allWidgetIds) {

                // Set the text
                remoteViews.setTextViewText(R.id.update,
                        "Current: " + String.valueOf(getPicturesNearby()));
                appWidgetManager.updateAppWidget(widgetId, remoteViews);

        }
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    private int getPicturesNearby()
    {
        return (int)System.currentTimeMillis();
    }
}
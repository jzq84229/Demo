package com.zhang.bdaywidget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;


/**
 * Implementation of App Widget functionality.
 */
public class BDayWidgetProvider extends AppWidgetProvider {
    private static final String tag = "BDayWidgetProvider";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(tag, "====onUpdate====");
        // There may be multiple widgets active, so update all of them
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
//        super.onDeleted(context, appWidgetIds);
        Log.d(tag, "====onDeleted====");
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            BDayWidgetModel bwm = BDayWidgetModel.retrieveModel(context, appWidgetIds[i]);
            bwm.removePrefs(context);
        }
    }

    @Override
    public void onEnabled(Context context) {
        Log.d(tag, "====onEnabled====");
        // Enter relevant functionality for when the first widget is created
        BDayWidgetModel.clearAllPreferences(context);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(new ComponentName(context, BDayWidgetProvider.class),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    @Override
    public void onDisabled(Context context) {
        Log.d(tag, "====onDisabled====");
        // Enter relevant functionality for when the last widget is disabled
        BDayWidgetModel.clearAllPreferences(context);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(new ComponentName(context, BDayWidgetProvider.class),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        BDayWidgetModel bwm = BDayWidgetModel.retrieveModel(context, appWidgetId);
        if (bwm == null) {
            return;
        }
        ConfigureBDayWidgetActivity.updateAppWidget(context, appWidgetManager, bwm);

//        CharSequence widgetText = context.getString(R.string.appwidget_text);
//        // Construct the RemoteViews object
//        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.bday_widget_provider);
//        views.setTextViewText(R.id.bdw_w_name, widgetText);
//
//        // Instruct the widget manager to update the widget
//        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}



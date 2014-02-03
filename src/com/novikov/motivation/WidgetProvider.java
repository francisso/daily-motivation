package com.novikov.motivation;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.TypedValue;
import android.widget.RemoteViews;

public class WidgetProvider extends AppWidgetProvider {
    public static final String TAG = "daily_motivation";


    @Override
    public void onReceive(final Context context, final Intent intent) {
        super.onReceive(context, intent);
        if ("WIDGET_CONFIGURED".equals(intent.getAction())) {
            Log.d(TAG, "One widget update called");
            updateIt(context, intent);
        }
    }

    /**
     * Обновление виджета событием "WIDGET_CONFIGURED",
     * пришедшим из меню настроек     *
     *
     * @param context
     * @param updateIntent Намерение, при получении "WIDGET_CONFIGURED"
     */
    private void updateIt(Context context, Intent updateIntent) {
        //Получение настроек
        int widgetId = updateIntent.getIntExtra("widget_id", 0);
        int textSize = updateIntent.getIntExtra("text_size", 20);
        int backColor = updateIntent.getIntExtra("back_color", 0);
        int textColor = updateIntent.getIntExtra("text_color", 0);

        //Создание намерения для запуска страницы настроек
        Intent intent = new Intent(context, Settings.class);
        intent.setAction("LAUNCH_SETTINGS" + widgetId);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        //Обновление виджета
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        views.setTextViewTextSize(R.id.large_text_id, TypedValue.COMPLEX_UNIT_DIP, textSize);
        views.setInt(R.id.background, "setBackgroundColor", backColor);
        views.setTextColor(R.id.large_text_id, textColor);
        views.setOnClickPendingIntent(R.id.settings_icon, pendingIntent);
        AppWidgetManager.getInstance(context).updateAppWidget(widgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager manager, int[] ids) {
        for (int id : ids) {
            Intent intent = new Intent(context, Settings.class);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            intent.setAction("LAUNCH_SETTINGS" + id);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            views.setOnClickPendingIntent(R.id.settings_icon, pendingIntent);
            manager.updateAppWidget(id, views);
        }
        Log.d(TAG, "Widgets were updated");
    }
}

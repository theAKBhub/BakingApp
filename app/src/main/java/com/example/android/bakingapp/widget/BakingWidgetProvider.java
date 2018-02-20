package com.example.android.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.activities.WidgetActivity;
import com.example.android.bakingapp.utils.Config;

/**
 * Implementation of App Widget functionality.
 */
public class BakingWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId, String[] recipe) {

        // Create an Intent to launch Activity when clicked
        Intent intent = new Intent(context, WidgetActivity.class);
        intent.putExtra(Config.INTENT_KEY_WIDGET_RECIPE, recipe);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_widget);

        // Update View
        if (!recipe[0].equals("0")) { // Check if a recipe ID is available, in which case update UI
            views.setTextViewText(R.id.appwidget_header, recipe[1]);
            views.setTextViewText(R.id.appwidget_text, recipe[2]);
        } else { // Update UI with default text if no recipe ID is available
            views.setTextViewText(R.id.appwidget_header, "");
            views.setTextViewText(R.id.appwidget_text, context.getString(R.string.appwidget_text));
        }

        // Widgets allow click handlers to only launch pending intents
        views.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        String [] recipe = new String[3];
        recipe[0] = "0";
        recipe[1] = "";
        recipe[2] = context.getString(R.string.appwidget_text);
        updateBakingWidgets(context, appWidgetManager, appWidgetIds, recipe);
    }

    public static void updateBakingWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds,
            String[] recipe) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, recipe);
        }
    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }
}


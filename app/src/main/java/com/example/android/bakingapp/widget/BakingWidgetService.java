package com.example.android.bakingapp.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import com.example.android.bakingapp.utils.Config;

/**
 * Created by aditibhattacharya on 06/02/2018.
 */

public class BakingWidgetService extends IntentService {

    private static final String ACTION_UPDATE_WIDGET = "com.example.android.bakingapp.action.update_widget";


    /**
     * Creates an IntentService
     */
    public BakingWidgetService() {
        super("BakingWidgetService");
    }

    public static void startActionUpdateWidget(Context context, String[] recipe) {
        Intent intent = new Intent(context, BakingWidgetService.class);
        intent.setAction(ACTION_UPDATE_WIDGET);
        intent.putExtra(Config.INTENT_KEY_WIDGET_INGREDIENTS, recipe);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            final String[] extra = intent.getStringArrayExtra(Config.INTENT_KEY_WIDGET_INGREDIENTS);
            if (ACTION_UPDATE_WIDGET.equals(action)) {
                handleActionUpdateWidget(extra);
            }
        }
    }

    private void handleActionUpdateWidget(String[] arg) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingWidgetProvider.class));

        // Update widgets
        BakingWidgetProvider.updateBakingWidgets(this, appWidgetManager, appWidgetIds, arg);
    }
}

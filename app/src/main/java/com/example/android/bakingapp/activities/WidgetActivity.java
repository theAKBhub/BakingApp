package com.example.android.bakingapp.activities;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import butterknife.BindString;
import butterknife.ButterKnife;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.models.Ingredient;
import com.example.android.bakingapp.models.Recipe;
import com.example.android.bakingapp.utils.Config;
import com.example.android.bakingapp.utils.Utils;
import com.example.android.bakingapp.widget.BakingWidgetService;
import java.util.ArrayList;
import java.util.List;

public class WidgetActivity extends AppCompatActivity {

    final Context mContext = this;
    private RadioButton mRadioButton;
    private RadioButton [] mRadioButtons;
    private RadioGroup mRadioGroupRecipeOptions;
    private Button mButton;
    private AppWidgetManager mAppWidgetManager;
    private Toast mToast;
    private int mAppWidgetId;
    private RadioGroup.LayoutParams mLayoutParams;
    public ArrayList<Recipe> mRecipeList;
    private String [] mWidgetRecipe;
    int mPrevRecipeId;
    private List<Ingredient> mIngredients;

    // ButterKinfe resource binding
    @BindString(R.string.display_ingredient)        String mDisplayIngredient;
    @BindString(R.string.appwidget_text)            String mWidgetDefaultText;
    @BindString(R.string.info_ingredients_saved)    String mInfoIngredientSaved;
    @BindString(R.string.info_no_chosen_recipe)     String mInfoNoChosenRecipe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(RESULT_CANCELED);
        setContentView(R.layout.activity_widget);
        ButterKnife.bind(this);

        if (MainActivity.sRecipeList == null) {
            startActivity(new Intent(this, MainActivity.class));
            Utils.showToastMessage(mContext, mToast, getString(R.string.alert_app_launch)).show();
            finish();
        }

        mRecipeList = MainActivity.sRecipeList;
        mWidgetRecipe = new String[3];

        // Retrieve intent extras
        processIntentExtras();

        // Populate the Radio Options
        displayRecipeOptions();

        // Processing for widget
        mAppWidgetManager = AppWidgetManager.getInstance(mContext);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }


        mButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                processWidgetRecipe();
            }
        });
    }

    /**
     * Method to display recipe options to choose from in form of radio buttons
     */
    public void displayRecipeOptions() {
        mRadioGroupRecipeOptions = findViewById(R.id.radiogroup_recipe_options);
        mButton = findViewById(R.id.button_choose_recipe);
        mLayoutParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        mRadioButtons = new RadioButton[mRecipeList.size()];

        int i = 0;
        for (Recipe recipe : mRecipeList) {
            mRadioButtons[i] = new RadioButton(this);
            mRadioGroupRecipeOptions.addView(mRadioButtons[i]);
            mRadioButtons[i].setText(recipe.getRecipeName());
            mRadioButtons[i].setTag(recipe.getRecipeId());
            mLayoutParams.setMargins(20, 20, 20, 20);
            mRadioButtons[i].setLayoutParams(mLayoutParams);
            mRadioButtons[i].setPadding(40, 0, 0, 0);

            if (mPrevRecipeId != 0) {
                if (mPrevRecipeId == recipe.getRecipeId()) {
                    mRadioButtons[i].setChecked(true);
                }
            }
        }
    }

    /**
     * Method to process the selected recipe ingredients to be displayed on widget
     */
    public void processWidgetRecipe() {
        int selectId = mRadioGroupRecipeOptions.getCheckedRadioButtonId();
        mRadioButton = findViewById(selectId);

        if (mRadioButton != null) {
            // Recipe id
            mWidgetRecipe[0] = mRadioButton.getTag().toString();

            // Recipe name
            mWidgetRecipe[1] = mRadioButton.getText().toString();

            // Recipe ingredients
            int id = Integer.valueOf(mWidgetRecipe[0]) - 1;
            mIngredients = mRecipeList.get(id).getRecipeIngredients();

            StringBuilder ingredientDisplayString = new StringBuilder();

            for (Ingredient ingredient : mIngredients) {
                ingredientDisplayString.append(
                        String.format(
                                mDisplayIngredient,
                                Utils.convertStringToFirstCapital(ingredient.getIngredient()),
                                Double.toString(ingredient.getIngredientQuantity()),
                                ingredient.getIngredientMeasure().toLowerCase()
                        )
                );
            }
            mWidgetRecipe[2] = ingredientDisplayString.toString();

            Utils.showToastMessage(this, mToast, mInfoIngredientSaved).show();

            BakingWidgetService.startActionUpdateWidget(mContext, mWidgetRecipe);
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();

        } else {
            mWidgetRecipe[0] = "0";
            mWidgetRecipe[1] = "";
            mWidgetRecipe[2] = mWidgetDefaultText;

            Utils.showToastMessage(this, mToast, mInfoNoChosenRecipe).show();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        processIntentExtras();
    }

    /**
     * Method to receive Intent Extras passed on from home screen Widget
     */
    private void processIntentExtras() {
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            Bundle bundle = intent.getExtras();
            String[] previousRecipe = bundle.getStringArray(Config.INTENT_KEY_WIDGET_RECIPE);
            mPrevRecipeId = (previousRecipe != null) ? Integer.valueOf(previousRecipe[0]) : 0;
        }
    }
}

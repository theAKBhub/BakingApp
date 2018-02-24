package com.example.android.bakingapp.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.fragments.RecipeStepDetailFragment;
import com.example.android.bakingapp.models.Recipe;
import com.example.android.bakingapp.utils.Config;
import java.util.ArrayList;

public class StepActivity extends AppCompatActivity {

    private Recipe mRecipe;
    private int mStepId;
    private int mStepCount;
    private Bundle mStepBundle;
    private ArrayList<Recipe> mSelectedRecipe;
    private FragmentManager mFragmentManager;

    @BindView(R.id.button_previous)
    ImageButton mButtonPrevious;
    @BindView(R.id.button_next)
    ImageButton mButtonNext;
    @BindView(R.id.textview_stepnum)
    TextView mTextViewStepNum;
    @BindString(R.string.display_stepnum)
    String mDisplayStepNum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        ButterKnife.bind(this);

        if (getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            mStepId = extras.getInt(Config.INTENT_KEY_SELECTED_STEP);
            mStepCount = extras.getInt(Config.INTENT_KEY_STEP_COUNT);
        }

        mRecipe = DetailActivity.sRecipe.get(0);
        setTitle(mRecipe.getRecipeName());

        mFragmentManager = getSupportFragmentManager();
        displayStepNum();

        // Create fragment instance only once
        if (savedInstanceState == null) {
            displayStepFragment();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Config.STATE_SELECTED_STEP, mStepId);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            mStepId = savedInstanceState.getInt(Config.STATE_SELECTED_STEP);
            displayStepNum();
        } else {
            displayStepFragment();
        }
    }

    /**
     * OnClick handlers for step navigation buttons
     */
    @OnClick({R.id.button_previous, R.id.button_next})
    public void setViewOnClickEvent(View view) {
        switch (view.getId()) {
            case R.id.button_previous:
                submitPrevious();
                break;

            case R.id.button_next:
                submitNext();
                break;
        }
    }

    /**
     * Method to navigate to previous step of selected recipe
     */
    public void submitPrevious() {
        if (mStepId > 0) {
            mStepId--;
            displayStepNum();
            displayStepFragment();
        }
    }

    /**
     * Method to navigate to next step of selected recipe
     */
    public void submitNext() {
        if (mStepId < (mStepCount - 1)) {
            mStepId++;
            displayStepNum();
            displayStepFragment();
        }
    }

    /**
     * Method to display the current step number
     */
    public void displayStepNum() {
        mTextViewStepNum.setText(String.format(mDisplayStepNum, mStepId, (mStepCount - 1)));
    }

    /**
     * Method to replace fragment with new step details
     */
    public void displayStepFragment() {
        mStepBundle = new Bundle();
        mSelectedRecipe = new ArrayList<>();
        mSelectedRecipe.add(mRecipe);
        mStepBundle.putParcelableArrayList(Config.INTENT_KEY_SELECTED_RECIPE, mSelectedRecipe);
        mStepBundle.putInt(Config.INTENT_KEY_SELECTED_STEP, mStepId);
        mStepBundle.putInt(Config.INTENT_KEY_STEP_COUNT, mStepCount);

        RecipeStepDetailFragment stepDetailFragment = new RecipeStepDetailFragment();

        stepDetailFragment.setArguments(mStepBundle);
        mFragmentManager
                .beginTransaction()
                .replace(R.id.container_recipe_step_detail, stepDetailFragment)
                .commit();

    }
}

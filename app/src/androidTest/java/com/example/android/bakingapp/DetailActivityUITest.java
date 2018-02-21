package com.example.android.bakingapp;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.IsNot.not;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import com.example.android.bakingapp.activities.DetailActivity;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by aditibhattacharya on 21/02/2018.
 */


@RunWith(AndroidJUnit4.class)
public class DetailActivityUITest {

    private final static String STEP_DESC = "Recipe Introduction";
    private final static int STEP_POSITION_WITH_MEDIA = 0;
    private final static int STEP_POSITION_WITHOUT_MEDIA = 1;


    @Rule
    public ActivityTestRule<DetailActivity> mActivityTestRule = new ActivityTestRule<>(DetailActivity.class);


    /**
     * Method to test if clicking on a specified position (0) of Step RecyclerView list shows
     * (1) Step Description as given
     * (2) Media player view is visible and default image view is not visible.
     */
    @Test
    public void testClickStepWithMedia() {

        // Perform click action on Steps RecyclerView list
        onView(withId(R.id.recyclerview_steps))
                .perform(RecyclerViewActions
                        .actionOnItemAtPosition(STEP_POSITION_WITH_MEDIA, click()));

        // Check if step desc textview is displayed
        onView(withId(R.id.textview_step_desc))
                .check(matches(isDisplayed()));

        // Check if media player view is displayed
        onView(withId(R.id.playerview_recipe_video))
                .check(matches(isDisplayed()));

        // Check if default image view overriding the media player is NOT displayed (as this step has a video)
        onView(withId(R.id.imageview_no_media))
                .check(matches(not(isDisplayed())));

    }


}

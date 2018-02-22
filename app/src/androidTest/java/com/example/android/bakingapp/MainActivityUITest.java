package com.example.android.bakingapp;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNot.not;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import com.example.android.bakingapp.activities.MainActivity;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by aditibhattacharya on 20/02/2018.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityUITest {

    private final static String RECIPE_NAME = "Yellow Cake";
    private final static String STEP_NAME = "0)  Recipe Introduction";
    private final static String STEP_DESC = "Recipe Introduction";
    private final static String STEP_NUM_0 = "Step 0 of 12";
    private final static String STEP_NUM_1 = "Step 1 of 12";
    private final static int RECIPE_LIST_SCROLL_POSITION = 2;
    private final static int STEPS_WITH_MEDIA = 0;
    private final static int STEPS_WITHOUT_MEDIA = 1;

    private IdlingResource mIdlingResource;


    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);


    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);
    }


    /**
     * Method to test if correct recipe name is displayed on a specified position of RecyclerView list
     */
    @Test
    public void testRecipeNameAtPosition() {

        // Perform scroll action on Recipe RecyclerView list
        onView(withId(R.id.recyclerview_recipes))
                .perform(RecyclerViewActions
                        .scrollToPosition(RECIPE_LIST_SCROLL_POSITION));

        // Check if recipe name as displayed on the specified position of Recipe RecyclerView list matches the given name
        onView(withText(RECIPE_NAME))
                .check(matches(isDisplayed()));
    }


    /**
     * Method to test if clicking on a given position (ID = 2) of Recipe RecyclerView list
     * displays ingredients and steps. Then test if clicking on a given position (ID = 0) of
     * Step RecyclerView list displays step description, media, and step number.
     */
    @Test
    public void testClickRecipeAtPosition() throws Exception {

        // Perform click action on Recipe RecyclerView list
        onView(withId(R.id.recyclerview_recipes))
                .perform(RecyclerViewActions
                        .actionOnItemAtPosition(RECIPE_LIST_SCROLL_POSITION, click()));

        // Check if ingredients textview is displayed
        onView(withId(R.id.textview_ingredients))
                .check(matches(isDisplayed()));

        // Perform scroll action on Steps RecyclerView list
        onView(withId(R.id.recyclerview_steps))
                .perform(RecyclerViewActions
                        .scrollToPosition(STEPS_WITH_MEDIA));

        // Check if step name as displayed on the specified position of Steps RecyclerView list matches the given name
        onView(withText(STEP_NAME))
                .check(matches(isDisplayed()));

        // Perform click action on steps list (ID = 0)
        onView(withId(R.id.recyclerview_steps))
                .perform(RecyclerViewActions
                        .actionOnItemAtPosition(STEPS_WITH_MEDIA, click()));

        // Check if all views are displayed as expected in StepsActivity (after step item is clicked)
        onView(withId(R.id.button_previous)).check(matches(isDisplayed()));
        onView(withId(R.id.button_next)).check(matches(isDisplayed()));
        onView(withId(R.id.textview_step_desc)).check(matches(isDisplayed()));
        onView(withId(R.id.textview_stepnum)).check(matches(isDisplayed()));
        onView((withId(R.id.textview_step_desc))).check(matches(withText(STEP_DESC)));
        onView((withId(R.id.textview_stepnum))).check(matches(withText(STEP_NUM_0)));
        onView(withId(R.id.playerview_recipe_video)).check(matches(isDisplayed()));
        onView(withId(R.id.imageview_no_media)).check(matches(not(isDisplayed())));
    }


    /**
     * Method to test if clicking on a given position (ID = 2) of Recipe RecyclerView list
     * displays ingredients and steps. Then test if clicking on a given position (ID = 1) of
     * Step RecyclerView list displays step description, default image (as this step is without media), and step number.
     */
    @Test
    public void testClickStepAtPosition() throws Exception {

        // Perform click action on Recipe RecyclerView list
        onView(withId(R.id.recyclerview_recipes))
                .perform(RecyclerViewActions
                        .actionOnItemAtPosition(RECIPE_LIST_SCROLL_POSITION, click()));

        // Perform scroll action on Steps RecyclerView list
        onView(withId(R.id.recyclerview_steps))
                .perform(RecyclerViewActions
                        .scrollToPosition(STEPS_WITHOUT_MEDIA));
        onView(withId(R.id.recyclerview_steps))
                .perform(RecyclerViewActions
                        .actionOnItemAtPosition(STEPS_WITHOUT_MEDIA, click()));

        onView((withId(R.id.textview_stepnum)))
                .check(matches(withText(STEP_NUM_1)));
        onView(withId(R.id.imageview_no_media))
                .check(matches(isDisplayed()));

    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }

}

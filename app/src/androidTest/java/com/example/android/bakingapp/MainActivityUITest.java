package com.example.android.bakingapp;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import com.example.android.bakingapp.activities.MainActivity;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by aditibhattacharya on 20/02/2018.
 */

@RunWith(AndroidJUnit4.class)
public class MainActivityUITest {

    private final static String RECIPE_NAME = "Yellow Cake";
    private final static String STEP_NAME = "0)  Recipe Introduction";
    private final static int RECIPE_LIST_SCROLL_POSITION = 2;
    private final static int STEPS_LIST_SCROLL_POSITION = 0;


    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);


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
     * Method to test if clicking on a specified position (2) of Recipe RecyclerView list opens DetailActivity,
     * Ingredients TextView with specified ID is displayed,
     * and Steps RecyclerView list matches the step name at the specified position (0).
     */
    @Test
    public void testClickRecipeAtPosition() {

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
                        .scrollToPosition(STEPS_LIST_SCROLL_POSITION));

        // Check if step name as displayed on the specified position of Steps RecyclerView list matches the given name
        onView(withText(STEP_NAME))
                .check(matches(isDisplayed()));
    }


}

package com.mytaxi.android_demo;


import android.support.test.espresso.matcher.RootMatchers;
import android.support.test.filters.SdkSuppress;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.mytaxi.android_demo.activities.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

//import androidx.test.uiautomator.UiObjectNotFoundException;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.mytaxi.android_demo.misc.Constants.LOG_TAG;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 15)
public class ExampleInstrumentedTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);


    @Before
    public void setUp() throws Exception {
        //Before Test case execution
    }

    @Test
    public void login() throws InterruptedException {




        onView(withId(R.id.edt_username)).perform(typeText("crazydog335"));
        onView(withId(R.id.edt_password)).perform(typeText("venture"));
        onView(withId(R.id.btn_login)).perform(click());
        Thread.sleep(5000);
        onView(withId(R.id.textSearch)).perform(typeText("sa"));
        Thread.sleep(5000);
        //onView(withText("Sarah Scott")).perform(click());
        onView(withText("Sarah Scott")).inRoot(RootMatchers.isPlatformPopup()).perform(click());

        Thread.sleep(5000);
        onView(withId(R.id.fab)).perform(click());
        Thread.sleep(5000);




    }

    @After
    public void tearDown() throws Exception {
        //After Test case Execution
    }
}

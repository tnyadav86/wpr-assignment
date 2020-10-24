package com.android.wpr.application


import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.android.wpr.application.util.CustomMatcher.atPosition
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    private val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)

    @get:Rule
    val activityRule = ActivityScenarioRule<MainActivity>(intent)

    @Test
    fun feedTitle_Present() {
        Thread.sleep(5000)
        onView(withId(R.id.toolbar)).check(matches(hasDescendant(withText("About Canada"))))
    }

    @Test
    fun allData_Present() {

        Thread.sleep(5000)
        onView(withId(R.id.recycler_view))
            .perform(scrollToPosition<AppRecyclerViewAdapter.ViewHolder>(0))
            .check(matches(atPosition(0, hasDescendant(withText("Beavers")))))

        onView(withId(R.id.recycler_view))
            .perform(scrollToPosition<AppRecyclerViewAdapter.ViewHolder>(12))
            .check(matches(atPosition(12, hasDescendant(withText("Language")))))

    }

    @Test
    fun api_ErrorState() {
        Thread.sleep(5000)
        onView(withId(R.id.error_info)).check(matches(hasDescendant(withText(R.string.api_error))))
    }

    @Test
    fun noDataFound_ErrorState() {
        Thread.sleep(5000)
        onView(withId(R.id.error_info)).check(matches(hasDescendant(withText(R.string.no_data_found))))
    }

    @Test
    fun internetConnection_ErrorState() {
        Thread.sleep(1000)
        onView(withId(R.id.error_info)).check(matches(hasDescendant(withText(R.string.internet_connection_error))))
    }


}
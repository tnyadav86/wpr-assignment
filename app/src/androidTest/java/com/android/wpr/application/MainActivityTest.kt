package com.android.wpr.application


import android.os.SystemClock
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.android.wpr.application.di.server.MockServer
import com.android.wpr.application.util.CustomMatcher
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    private lateinit var mockServer: MockWebServer

    @Before
    fun setUp() {
        mockServer = MockWebServer()
        mockServer.start(8080)
    }

    @After
    fun tearDown() {
        mockServer.shutdown()

    }

    @Test
    fun successCase() {
        mockServer.dispatcher = MockServer.ResponseDispatcher()
        val activityScenario = launch(MainActivity::class.java)
        SystemClock.sleep(2000)
        //  error text - not visible on success response
        onView(withId(R.id.errorInfo)).check(matches(Matchers.not(isDisplayed())))
        // check toolbar has data
        onView(withId(R.id.toolbar)).check(matches(hasDescendant(withText("About Canada"))))
        activityScenario.close()

    }

    @Test
    fun successCase_WithAllData() {
        mockServer.dispatcher = MockServer.ResponseDispatcher()
        val activityScenario = launch(MainActivity::class.java)
        SystemClock.sleep(2000)
        //  error text - not visible on success response
        onView(withId(R.id.errorInfo)).check(matches(Matchers.not(isDisplayed())))
        // check recyclerView firstitem
        onView(withId(R.id.recyclerView))
            .perform(RecyclerViewActions.scrollToPosition<AppRecyclerViewAdapter.ViewHolder>(0))
            .check(matches(CustomMatcher.atPosition(0, hasDescendant(withText("Beavers")))))
        // check recyclerView last item
        onView(withId(R.id.recyclerView))
            .perform(RecyclerViewActions.scrollToPosition<AppRecyclerViewAdapter.ViewHolder>(8))
            .check(matches(CustomMatcher.atPosition(8, hasDescendant(withText("Mounties")))))

        activityScenario.close()

    }

    @Test
    fun failureCase() {
        mockServer.dispatcher = MockServer.ErrorDispatcher()
        val activityScenario = launch(MainActivity::class.java)
        //  failure layout visible
        onView(withId(R.id.errorInfo)).check(matches(isDisplayed()))
        activityScenario.close()
    }
}


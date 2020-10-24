package com.android.wpr.application.util

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.core.internal.deps.guava.base.Preconditions
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher

object CustomMatcher {
    fun atPosition(position: Int, itemMatcher: Matcher<View>): BoundedMatcher<View?, RecyclerView> {
        Preconditions.checkNotNull(itemMatcher)
        return object : BoundedMatcher<View?, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("item found at position $position: ")
                itemMatcher.describeTo(description)
            }

            override fun matchesSafely(view: RecyclerView): Boolean {
                val viewHolder = view.findViewHolderForAdapterPosition(position) ?: return false
                return itemMatcher.matches(viewHolder.itemView)
            }
        }
    }
}
package com.wisal.android.todoapp.testing

import android.app.Activity
import android.view.Gravity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerMatchers.isClosed
import androidx.test.espresso.contrib.DrawerMatchers.isOpen
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.wisal.android.todoapp.testing.data.Task
import com.wisal.android.todoapp.testing.repositories.TasksRepository
import com.wisal.android.todoapp.testing.ui.TasksActivity
import com.wisal.android.todoapp.testing.util.DataBindingIdlingResource
import com.wisal.android.todoapp.testing.util.EspressoIdlingResource
import com.wisal.android.todoapp.testing.util.ServiceLocator
import com.wisal.android.todoapp.testing.util.monitorActivity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

fun <T: Activity> ActivityScenario<T>.getToolbarNavigationContentDescription(): String {
    var description = ""
    onActivity {
        description = it.findViewById<Toolbar>(
         R.id.toolbar
        ).navigationContentDescription as String
    }
    return  description
}

@LargeTest
@RunWith(AndroidJUnit4::class)
class AppNavigationTest {

    private lateinit var repository: TasksRepository

    private val dataBindingIdlingRes = DataBindingIdlingResource()

    @Before
    fun setup() {
        repository = ServiceLocator.provideTasksRepository(
            ApplicationProvider.getApplicationContext()
        )

    }

    @After
    fun clean() {
        ServiceLocator.resetRepository()
    }

    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().register(dataBindingIdlingRes)
    }

    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().unregister(dataBindingIdlingRes)
    }



    @Test
    fun taskDetailScreen_doubleUpButton() = runBlocking {
        val task = Task("1", userId = 1, title = "Up button pressed test",false)
        repository.saveTask(task)

        val activityScenario = ActivityScenario.launch(TasksActivity::class.java)
        dataBindingIdlingRes.monitorActivity(activityScenario)

        onView(withText("Up button pressed test")).perform(click())
        onView(withId(R.id.edit_task_fab)).perform(click())

        onView(withContentDescription(
            activityScenario.getToolbarNavigationContentDescription()
        )).perform(click())

        onView(withId(R.id.title_text)).check(matches(isDisplayed()))

        onView(
            withContentDescription(
                activityScenario.getToolbarNavigationContentDescription()
            )
        ).perform(click())

        onView(withId(R.id.tasks_container_layout)).check(matches(isDisplayed()))



        activityScenario.close()
    }


    @Test
    fun taskDetailScreen_doubleBackButton() = runBlocking {
        val task = Task("1", userId = 1, title = "Back button pressed test",false)
        repository.saveTask(task)

        val activityScenario = ActivityScenario.launch(TasksActivity::class.java)
        dataBindingIdlingRes.monitorActivity(activityScenario)

        onView(withText("Back button pressed test")).perform(click())
        onView(withId(R.id.edit_task_fab)).perform(click())

        Espresso.pressBack()

        onView(withId(R.id.title_text)).check(matches(isDisplayed()))

        Espresso.pressBack()

        onView(withId(R.id.tasks_container_layout)).check(matches(isDisplayed()))


        activityScenario.close()
    }


    @Test
    fun tasksScreen_clickOnHomeScreenIcon_OpenNavigationDrawer() {
        val activityScenario = ActivityScenario.launch(TasksActivity::class.java)
        dataBindingIdlingRes.monitorActivity(activityScenario)

        onView(withId(R.id.drawer_layout))
            .check(matches(isClosed(Gravity.START)))


        onView(
            withContentDescription(
                activityScenario
                    .getToolbarNavigationContentDescription()
            )
        ).perform(click())

        onView(withId(R.id.drawer_layout))
            .check(matches(isOpen(Gravity.START)))


        activityScenario.close()

    }



    @Test
    fun navigateToStatisticsScreen_byNavigationDrawerRoute() {
        val activityScenario = ActivityScenario.launch(TasksActivity::class.java)
        dataBindingIdlingRes.monitorActivity(activityScenario)

        onView(
            withContentDescription(
                activityScenario
                    .getToolbarNavigationContentDescription()
            )
        ).perform(click())

        onView(withId(R.id.drawer_layout))
            .check(matches(isOpen(Gravity.START)))

        onView(withId(R.id.statistics_fragment_dest)).perform(click())

        onView(withId(R.id.statistics_fragment_container))
            .check(matches(isDisplayed()))

    }





}
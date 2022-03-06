package com.wisal.android.todoapp.testing.ui

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.wisal.android.todoapp.testing.R
import com.wisal.android.todoapp.testing.data.Task
import com.wisal.android.todoapp.testing.repositories.TasksRepository
import com.wisal.android.todoapp.testing.util.DataBindingIdlingResource
import com.wisal.android.todoapp.testing.util.EspressoIdlingResource
import com.wisal.android.todoapp.testing.util.ServiceLocator
import com.wisal.android.todoapp.testing.util.monitorActivity
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers.not
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class TasksActivityTest {

    private lateinit var repository: TasksRepository

    private val dataBindingIdlingResource = DataBindingIdlingResource()

    @Before
    fun setUp() {
        repository = ServiceLocator.provideTasksRepository(ApplicationProvider.getApplicationContext())
        runBlocking {
            repository.deleteAllTasks();
        }
    }

    @After
    fun tearDown() {
        ServiceLocator.resetRepository()
    }

    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().register(dataBindingIdlingResource)
    }

    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().unregister(dataBindingIdlingResource)
    }


    @Test
    fun editTask() = runBlocking {
        // Set initial state.
        repository.saveTask(Task("0", 0,"Todo task",false))

        // Start up Tasks screen.
        val activityScenario = ActivityScenario.launch(TasksActivity::class.java)

        dataBindingIdlingResource.monitorActivity(activityScenario)

        onView(withText("Todo task")).perform(click())
        onView(withId(R.id.title_text)).check(matches(withText("Todo task")))
        onView(withId(R.id.complete_checkbox)).check(matches(not(isChecked())))

        onView(withId(R.id.edit_task_fab)).perform(click())
        onView(withId(R.id.add_task_title_edit_text)).perform(replaceText("title"))
        onView(withId(R.id.save_task_fab)).perform(click())

        onView(withText("title")).check(matches(isDisplayed()))
        onView(withText("Todo task")).check(doesNotExist())

        // Make sure the activity is closed before resetting the db:
        activityScenario.close()
    }


    @Test
    fun createTask_deleteThatTask() = runBlocking {

        val  activityScenario = ActivityScenario.launch(TasksActivity::class.java)
        dataBindingIdlingResource.monitorActivity( activityScenario)

        onView(withId(R.id.add_task_fab)).perform(click())

        onView(withId(R.id.add_task_title_edit_text)).
        perform(typeText("test task"), closeSoftKeyboard())
        onView(withId(R.id.save_task_fab)).perform(click())

        onView(withId(R.id.recycler_view))
            .perform(RecyclerViewActions.scrollTo<RecyclerView.ViewHolder>(
                hasDescendant(withText("test task"))
            ))

        onView(withText("test task")).check(matches(isDisplayed()))

        onView(withText("test task")).perform(click())

        onView(withId(R.id.menu_delete)).perform(click())


        onView(withText("test task")).check(doesNotExist())

        activityScenario.close()

    }



}
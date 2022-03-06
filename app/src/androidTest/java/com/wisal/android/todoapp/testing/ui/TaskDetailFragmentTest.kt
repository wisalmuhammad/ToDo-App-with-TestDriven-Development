package com.wisal.android.todoapp.testing.ui

import androidx.annotation.UiThread
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.wisal.android.todoapp.testing.MainCoroutineRule
import com.wisal.android.todoapp.testing.R
import com.wisal.android.todoapp.testing.data.Result
import com.wisal.android.todoapp.testing.data.Task
import com.wisal.android.todoapp.testing.repositories.FakeTasksRepository
import com.wisal.android.todoapp.testing.repositories.TasksRepository
import com.wisal.android.todoapp.testing.util.ServiceLocator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@MediumTest
@RunWith(AndroidJUnit4::class)
class TaskDetailFragmentTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var tasksRepository: FakeTasksRepository

    @Before
    fun setUp() {
        tasksRepository = FakeTasksRepository()
        ServiceLocator.taskRepositories = tasksRepository
    }


    @Test
    fun addNewTask_addNewTaskToDatabase() = mainCoroutineRule.runBlockingTest {
        val newTask = Task(id = "1", userId = 0, title = "Hello AndroidX World",false)
        tasksRepository.addTasks(newTask)
        val task = tasksRepository.getTask(newTask.id)
        assertEquals(newTask.id,(task as Result.Success).data.id)
    }


    @Test
    fun activeTaskDetails_DisplayedInUi() = mainCoroutineRule.runBlockingTest {

        val newTask = Task(id = "2", userId = 0, title = "Hello AndroidX World",false)
        tasksRepository.addTasks(newTask)
        val bundle = TaskDetailFragmentArgs(newTask.id).toBundle()
        launchFragmentInContainer<TaskDetailFragment>(bundle, R.style.Theme_ToDoWithTDD)

        onView(withId(R.id.title_text)).check(matches(isDisplayed()))
        onView(withId(R.id.title_text)).check(matches(withText("Hello AndroidX World")))

        onView(withId(R.id.complete_checkbox)).check(matches(isDisplayed()))
        onView(withId(R.id.complete_checkbox)).check(matches(isNotChecked()))

    }


    @Test
    fun completedTaskDetails_DisplayedInUI() = mainCoroutineRule.runBlockingTest {
        val newTask = Task(id = "2", userId = 0, title = "Hello AndroidX World",true)
        tasksRepository.addTasks(newTask)

        val bundle = TaskDetailFragmentArgs(newTask.id).toBundle()
        launchFragmentInContainer <TaskDetailFragment>(bundle,R.style.Theme_ToDoWithTDD)

        onView(withId(R.id.title_text)).check(matches(isDisplayed()))
        onView(withId(R.id.title_text)).check(matches(withText("Hello AndroidX World")))

        onView(withId(R.id.complete_checkbox)).check(matches(isDisplayed()))
        onView(withId(R.id.complete_checkbox)).check(matches(isChecked()))
    }

    @After
    fun tearDown() = mainCoroutineRule.runBlockingTest {
        ServiceLocator.resetRepository()
    }

}
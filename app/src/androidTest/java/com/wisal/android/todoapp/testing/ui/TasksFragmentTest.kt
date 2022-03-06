package com.wisal.android.todoapp.testing.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.wisal.android.todoapp.testing.MainCoroutineRule
import com.wisal.android.todoapp.testing.R
import com.wisal.android.todoapp.testing.data.Result
import com.wisal.android.todoapp.testing.data.Task
import com.wisal.android.todoapp.testing.getOrAwaitValue
import com.wisal.android.todoapp.testing.repositories.FakeTasksRepository
import com.wisal.android.todoapp.testing.repositories.TasksRepository
import com.wisal.android.todoapp.testing.util.Event
import com.wisal.android.todoapp.testing.util.ServiceLocator
import com.wisal.android.todoapp.testing.viewmodels.TasksViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.Matchers.`is`

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@ExperimentalCoroutinesApi
@MediumTest
@RunWith(AndroidJUnit4::class)
class TasksFragmentTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: FakeTasksRepository
    private lateinit var viewModel: TasksViewModel

    @Before
    fun setUp() {
        repository = FakeTasksRepository()
        viewModel = TasksViewModel(repository)
        ServiceLocator.taskRepositories = repository
    }




    @Test
    fun clickTask_navigateToTaskDetailFragment1() = mainCoroutineRule.runBlockingTest {

        val task1 = Task("0", 0,"title1",false)
        val task2 = Task("1", 2, "title2",false)
        val task3 = Task(id = "2", userId = 0, title = "title3",true)
        val task4 = Task(id = "3", userId = 2, title = "title4",true)

        repository.saveTask(task1)
        repository.saveTask(task2)
        repository.saveTask(task3)
        repository.saveTask(task4)

        val scenario = launchFragmentInContainer<TasksFragment>(Bundle(), R.style.Theme_ToDoWithTDD)

        val navController = mock(NavController::class.java)
        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

        onView(withId(R.id.recycler_view))
            .perform(RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                hasDescendant(withText(task1.title)), click()))

        verify(navController).navigate(
            TasksFragmentDirections.actionTasksFragmentToTaskDetailFragment( taskId = task1.id)
        )


    }


    @Test
    fun clickAddTaskButton_navigateToAddEditFragment() = mainCoroutineRule.runBlockingTest {
        val scenario = launchFragmentInContainer<TasksFragment>(Bundle(),R.style.Theme_ToDoWithTDD)
        val navController = mock(NavController::class.java)
        scenario.onFragment{
            Navigation.setViewNavController(it.view!!,navController)
        }

        onView(withId(R.id.add_task_fab)).perform(click())

        verify(navController).navigate(
            TasksFragmentDirections.actionTasksFragmentToAddEditFragment(
                null,
                getApplicationContext<Context>().getString(R.string.add_task)
            )
        )

    }


    @Test
    fun completeTask_dataAndToastUpdated() = mainCoroutineRule.runBlockingTest {
        val task = Task("0", 0,"title",false)
        repository.addTasks(task)

        viewModel.completeTask(task, true)
        val result = repository.tasksServiceData[task.id]?.completed
        assertThat(result, `is`(true))

        val textId: Event<Int?> =  viewModel.toastText.getOrAwaitValue()
        assertThat(textId, `is`(R.string.task_marked_complete))

    }

    @After
    fun tearDown() = mainCoroutineRule.runBlockingTest {
        ServiceLocator.resetRepository()
    }



}
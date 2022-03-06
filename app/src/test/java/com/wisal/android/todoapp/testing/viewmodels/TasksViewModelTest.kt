package com.wisal.android.todoapp.testing.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.wisal.android.todoapp.testing.data.Task
import com.wisal.android.todoapp.testing.getOrAwaitValue
import com.wisal.android.todoapp.testing.repositories.FakeTasksRepository
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


//@RunWith(AndroidJUnit4::class)
class TasksViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var tasksRepository: FakeTasksRepository
    private lateinit var viewModel: TasksViewModel

    @Before
    fun setUp() {
        tasksRepository = FakeTasksRepository()

        val task1 = Task(id = "22", userId = 1, title = "title1",true)
        val task2 = Task(id = "33", userId = 2, title = "title2",true)
        val task3 = Task(id = "44", userId = 3, title = "title3",true)

        tasksRepository.addTasks(
            task1,
            task2,
            task3
        )

        viewModel = TasksViewModel(
            tasksRepository
        )


    }


    @Test
    fun addNewTask_setsNewTaskEvent() {

        viewModel.setNewTask()

        val value = viewModel.newTaskEvent.getOrAwaitValue()
        assertThat(
            value.getContentIfNotHandled(), (not(nullValue()))
        )

    }

    @After
    fun tearDown() {


    }
}
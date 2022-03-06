package com.wisal.android.todoapp.testing.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.wisal.android.todoapp.testing.data.Result
import com.wisal.android.todoapp.testing.data.Task
import com.wisal.android.todoapp.testing.data.succeeded
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class LocalTasksDataSourceTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var localTasksDataSource: LocalTasksDataSource
    private lateinit var database: ToDoDatabase


    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ToDoDatabase::class.java
        ).allowMainThreadQueries().build()

        localTasksDataSource = LocalTasksDataSource(
            database.tasksDao(),
            dispatcher = Dispatchers.Main
        )

    }

    @Test
   fun saveTaskAndGetTask() = runBlocking{
        val newTask = Task(id = "1", userId = 1, title = "title",false)
        localTasksDataSource.saveTasks(newTask)

        val task = localTasksDataSource.getTask(newTask.id)

        assertThat(task.succeeded, `is`(true))
        task as Result.Success
        assertThat(task.data.title, `is`(newTask.title))
        assertThat(task.data.completed, `is`(false))
    }

    @Test
    fun completeTaskRetrievedTaskIsComplete() = runBlocking {
        val newTask = Task(id = "1", userId = 1, title = "title",false)
        localTasksDataSource.saveTasks(newTask)

        // 2. Mark it as complete.
        localTasksDataSource.completeTask(newTask.id)

        val task = localTasksDataSource.getTask(newTask.id)

        assertThat(task.succeeded, `is`(true))
        task as Result.Success
        assertThat(task.data.title,`is`(newTask.title))
        assertThat(task.data.completed, `is`(true))

    }


    @After
    fun tearDown() {
        database.close()
    }




}
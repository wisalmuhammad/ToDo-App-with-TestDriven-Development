package com.wisal.android.todoapp.testing.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.wisal.android.todoapp.testing.data.Task
import com.wisal.android.todoapp.testing.data.source.local.ToDoDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Collections.copy


@SmallTest
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class TasksDaoTest {


    @get: Rule
    var instantTaskRule = InstantTaskExecutorRule()

    private lateinit var databse: ToDoDatabase


    @Before
    fun setUp() {
        databse = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ToDoDatabase::class.java
        ).build()
    }


    @Test
    fun insertTaskAndGetItById() {
        val newTask = Task(id = "1", userId = 1, title = "title",false)

        databse.tasksDao().insertTask(newTask)

        val task = databse.tasksDao().getTaskById(newTask.id)

        assertThat<Task>(task as Task, not(nullValue()))
        assertThat(task.id,`is`(newTask.id))
        assertThat(task.userId, `is`(newTask.userId))
        assertThat(task.title,`is`(newTask.title))
        assertThat(task.completed,`is`(newTask.completed))
    }

    @Test
    fun updateTaskAndGetItById() {
        val originalTask = Task(id = "1", userId = 1, title = "title",false)

        databse.tasksDao().insertTask(originalTask)

        val updatedTask = Task(id = originalTask.id, userId = originalTask.userId, title = "updated title", completed = true)

        databse.tasksDao().updateTask(updatedTask)

        val task = databse.tasksDao().getTaskById(originalTask.id)

        assertThat(task?.id, `is`(originalTask.id))
        assertThat(task?.title, `is`(updatedTask.title))
        assertThat(task?.completed, `is`(true))

    }


    @After
    fun cleanUp() {
        databse.close()
    }


}
package com.wisal.android.todoapp.testing.util

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import com.wisal.android.todoapp.testing.data.source.local.LocalTasksDataSource
import com.wisal.android.todoapp.testing.data.source.local.ToDoDatabase
import com.wisal.android.todoapp.testing.data.source.remote.RemoteTasksDataSource
import com.wisal.android.todoapp.testing.repositories.TasksRepository
import com.wisal.android.todoapp.testing.repositories.TasksRepositoryImp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

object ServiceLocator {

    @Volatile
    var taskRepositories: TasksRepository? = null
    @VisibleForTesting set

    private var database: ToDoDatabase? = null

    private val lock = Any()

    fun provideTasksRepository(context: Context): TasksRepository {
        synchronized(this) {
            return taskRepositories?: createTasksRepository(context)
        }
    }


    private fun createTasksRepository(context: Context): TasksRepository {
        val repository = TasksRepositoryImp(RemoteTasksDataSource(),createLocalDataSource(context))
        taskRepositories = repository
        return repository
    }


    private fun createLocalDataSource(context: Context): LocalTasksDataSource {
        val database = this.database?: createDataBase(context)
        return LocalTasksDataSource(database.tasksDao())
    }


    private fun createDataBase(context: Context): ToDoDatabase {
        val dataBase = Room.databaseBuilder(
            context.applicationContext,
            ToDoDatabase::class.java,
            "Tasks.db"
        ).build()
        database = dataBase
        return dataBase
    }

    @VisibleForTesting
    fun resetRepository() {
        synchronized(lock) {
            runBlocking {
                taskRepositories?.deleteAllTasks()
            }

            database?.apply {
                clearAllTables()
                close()
            }
            database = null
            taskRepositories = null
        }
    }


}
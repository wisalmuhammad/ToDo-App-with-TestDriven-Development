package com.wisal.android.todoapp.testing.data.source

import androidx.lifecycle.LiveData
import com.wisal.android.todoapp.testing.data.Result
import com.wisal.android.todoapp.testing.data.Task

interface TasksDataSource {

    fun observeTasks(): LiveData<Result<List<Task>>>

    fun observeTaskById(taskId: String): LiveData<Result<Task>>

    suspend fun getTask(taskId: String): Result<Task>

    suspend fun getAllTasks(): Result<List<Task>>

    suspend fun saveTasks(vararg task: Task)

    suspend fun updateTask(task: Task)

    suspend fun completeTask(taskId: String)

    suspend fun clearCompletedTasks()

    suspend fun activateTask(taskId: String)

    suspend fun deleteTaskById(taskId: String)

    suspend fun deleteAllTasks()


}
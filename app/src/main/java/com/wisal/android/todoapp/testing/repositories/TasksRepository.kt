package com.wisal.android.todoapp.testing.repositories

import androidx.lifecycle.LiveData
import com.wisal.android.todoapp.testing.data.Result
import com.wisal.android.todoapp.testing.data.Task

interface TasksRepository {

    fun observeTasks(): LiveData<Result<List<Task>>>

    fun observeTask(id: String): LiveData<Result<Task>>

    suspend fun completeTask(id: String)

    suspend fun completeTask(task: Task)

    suspend fun activateTask(id: String)

    suspend fun activateTask(task: Task)

    suspend fun getTask(taskId: String): Result<Task>

    suspend fun getTasks(): Result<List<Task>>

    suspend fun saveTask(task: Task)

    suspend fun updateTask(task: Task)

    suspend fun clearAllCompletedTasks()

    suspend fun deleteAllTasks()

    suspend fun deleteTask(taskId: String)

    suspend fun fetchAllToDoTasks(isUpdate: Boolean): Result<List<Task>>

}
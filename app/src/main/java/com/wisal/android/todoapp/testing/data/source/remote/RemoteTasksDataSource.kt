package com.wisal.android.todoapp.testing.data.source.remote

import androidx.lifecycle.LiveData
import com.wisal.android.todoapp.testing.data.Result
import com.wisal.android.todoapp.testing.data.Task
import com.wisal.android.todoapp.testing.data.source.TasksDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception


class RemoteTasksDataSource: TasksDataSource {

    private val TAG = "RemoteDataSource"
    private val BASE_URL = "https://jsonplaceholder.typicode.com"

    private val remoteApi: RemoteTasksApi
     get() = Retrofit.Builder()
         .addConverterFactory(GsonConverterFactory.create())
         .baseUrl(BASE_URL)
         .build()
         .create(RemoteTasksApi::class.java)

    override fun observeTasks(): LiveData<Result<List<Task>>> {
        TODO("Not yet implemented")
    }

    override fun observeTaskById(taskId: String): LiveData<Result<Task>> {
        TODO("Not yet implemented")
    }

    override suspend fun getTask(taskId: String): Result<Task> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllTasks(): Result<List<Task>> {
        val response = remoteApi.fetchAllTodoTasks()
        return if(response.isSuccessful) {
            response.body()?.let {
                Result.Success(it)
            }?: run {
                Result.Error(Exception(response.message()))
            }
        } else {
            Result.Error(Exception(response.message()))
        }
    }

    override suspend fun saveTasks(vararg task: Task) {
        TODO("Not yet implemented")
    }

    override suspend fun updateTask(task: Task) {
        TODO("Not yet implemented")
    }

    override suspend fun completeTask(taskId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun clearCompletedTasks() {
        TODO("Not yet implemented")
    }

    override suspend fun activateTask(taskId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTaskById(taskId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllTasks() {
        TODO("Not yet implemented")
    }
}
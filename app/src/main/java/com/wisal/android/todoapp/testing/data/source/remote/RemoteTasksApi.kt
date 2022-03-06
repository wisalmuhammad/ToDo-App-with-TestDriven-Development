package com.wisal.android.todoapp.testing.data.source.remote

import com.wisal.android.todoapp.testing.data.Task
import retrofit2.Response
import retrofit2.http.GET


interface RemoteTasksApi {

    @GET("/todos")
    suspend fun fetchAllTodoTasks(): Response<List<Task>>

}
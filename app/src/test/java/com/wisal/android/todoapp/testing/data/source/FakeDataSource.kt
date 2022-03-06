package com.wisal.android.todoapp.testing.data.source

import androidx.lifecycle.LiveData
import com.wisal.android.todoapp.testing.data.Result
import com.wisal.android.todoapp.testing.data.Task
import java.lang.Exception

class FakeDataSource(var tasks: MutableList<Task>? = mutableListOf()): TasksDataSource {


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
        tasks?.let { return Result.Success(it) }
        return Result.Error(
            Exception("Tasks not found")
        )
    }

    override suspend fun saveTasks(vararg task: Task) {
       tasks?.addAll(task)
    }

    override suspend fun completeTask(taskId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun activateTask(taskId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllTasks() {
        tasks?.clear()
    }
}
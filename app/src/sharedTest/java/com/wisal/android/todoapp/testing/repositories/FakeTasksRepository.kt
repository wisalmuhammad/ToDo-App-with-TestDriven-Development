package com.wisal.android.todoapp.testing.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.room.Update
import com.wisal.android.todoapp.testing.data.Result
import com.wisal.android.todoapp.testing.data.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.lang.Exception

class FakeTasksRepository: TasksRepository {

    var tasksServiceData: LinkedHashMap<String,Task> = LinkedHashMap()
    private val observableTasks: MutableLiveData<Result<List<Task>>> = MutableLiveData()

    private var shouldReturnError: Boolean = false

    fun setReturnError(value: Boolean) {
        shouldReturnError = value
    }

    override fun observeTasks(): LiveData<Result<List<Task>>> {
        return observableTasks
    }

    override fun observeTask(taskId: String): LiveData<Result<Task>> {
        runBlocking { fetchAllToDoTasks(true) }
        return observableTasks.map { tasks ->
            when(tasks) {
                is Result.Loading -> Result.Loading
                is Result.Error -> Result.Error(tasks.exception)
                is Result.Success -> {
                    val task = tasks.data.firstOrNull() { it.id == taskId }
                        ?: return@map Result.Error(Exception("Not found"))
                    Result.Success(task)
                }
            }
        }
    }

    override suspend fun completeTask(id: String) {
        tasksServiceData[id]?.completed = true
    }

    override suspend fun completeTask(task: Task) {
        val compTask = task.copy(completed = true)
        tasksServiceData[task.id] = compTask
        fetchAllToDoTasks(true)
    }

    override suspend fun activateTask(id: String) {
        tasksServiceData[id]?.completed = false
    }

    override suspend fun activateTask(task: Task) {
        val activeTask = task.copy(completed = false)
        tasksServiceData[task.id] = activeTask
        fetchAllToDoTasks(true)
    }

    override suspend fun getTask(taskId: String): Result<Task> {
        if (shouldReturnError) return Result.Error(Exception("Test Exception"))
        tasksServiceData[taskId]?.let {
            return Result.Success(it)
        }
        return Result.Error(Exception("Could not find task"))
    }

    override suspend fun getTasks(): Result<List<Task>> {
        if(shouldReturnError) return Result.Error(Exception("Test Exception"))
        return Result.Success(tasksServiceData.values.toList())
    }

    override suspend fun saveTask(task: Task) {
        tasksServiceData[task.id] = task
    }

    override suspend fun updateTask(task: Task) {
        TODO("Not yet implemented")
    }

    override suspend fun clearAllCompletedTasks() {
        tasksServiceData = tasksServiceData.filterValues {
            !it.completed
        } as LinkedHashMap<String, Task>
    }

    override suspend fun deleteAllTasks() {
        tasksServiceData.clear()
    }

    override suspend fun deleteTask(taskId: String) {
        tasksServiceData.remove(taskId)
        fetchAllToDoTasks(true)
    }

    override suspend fun fetchAllToDoTasks(isUpdate: Boolean): Result<List<Task>> {
        if(shouldReturnError) {
            observableTasks.value = getTasks()
            return Result.Error(Exception("Could not find task"))
        }

        val tasks = getTasks()
        observableTasks.value = tasks
        return tasks
    }

    fun addTasks(vararg tasks: Task) {
        tasks.forEach {
            tasksServiceData[it.id] = it
        }

        runBlocking {
            fetchAllToDoTasks(true)
        }
    }
}
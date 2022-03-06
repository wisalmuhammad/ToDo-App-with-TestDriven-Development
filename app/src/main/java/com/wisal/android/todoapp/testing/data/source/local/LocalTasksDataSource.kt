package com.wisal.android.todoapp.testing.data.source.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.wisal.android.todoapp.testing.data.Result
import com.wisal.android.todoapp.testing.data.Task
import com.wisal.android.todoapp.testing.data.source.TasksDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception


class LocalTasksDataSource(
    private val dao: TasksDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
    ): TasksDataSource {

    private val TAG = "LocalTasksDataSource"

    override fun observeTasks(): LiveData<Result<List<Task>>> {
        return dao.getAllTasks().map {
            Result.Success(it)
        }
    }

    override fun observeTaskById(taskId: String): LiveData<Result<Task>> {
        return dao.observeTaskById(taskId).map {
            Result.Success(it)
        }
    }

    override suspend fun getTask(taskId: String): Result<Task> = withContext(dispatcher) {
        val task = dao.getTaskById(taskId)
        return@withContext task?.let {
            Result.Success(it)
        }?: run {
            Result.Error(Exception("No Task found"))
        }
    }


    override suspend fun getAllTasks(): Result<List<Task>> = withContext(dispatcher) {
        return@withContext try {
            Result.Success(dao.getTasks())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun saveTasks(vararg task: Task) = withContext(dispatcher){
        dao.insertTasks(*task)
    }

    override suspend fun updateTask(task: Task) = withContext(dispatcher){
        dao.updateTask(task)
    }

    override suspend fun completeTask(taskId: String) = withContext(dispatcher) {
        dao.updateCompleted(taskId,true)
    }

    override suspend fun clearCompletedTasks() = withContext<Unit>(dispatcher){
        dao.clearCompletedTasks()
    }

    override suspend fun activateTask(taskId: String) = withContext(dispatcher) {
        dao.updateCompleted(taskId,false)
    }

    override suspend fun deleteTaskById(taskId: String) = withContext<Unit>(dispatcher){
        dao.deleteTaskById(taskId)
    }

    override suspend fun deleteAllTasks() = withContext(dispatcher){
        dao.deleteAllTasks()
    }
}
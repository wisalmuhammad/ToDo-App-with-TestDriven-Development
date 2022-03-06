package com.wisal.android.todoapp.testing.repositories

import androidx.lifecycle.LiveData
import com.wisal.android.todoapp.testing.data.Result
import com.wisal.android.todoapp.testing.data.Task
import com.wisal.android.todoapp.testing.data.source.TasksDataSource
import com.wisal.android.todoapp.testing.util.EspressoIdlingResource.wrapEspressoIdlingResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

class TasksRepositoryImp(
    private val remoteDataSource: TasksDataSource,
    private val localDataSource: TasksDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : TasksRepository {

    private val TAG = "TasksRepository"

    init {
        //Log.d(TAG,"Initialized")
    }


    override fun observeTasks(): LiveData<Result<List<Task>>> {
        wrapEspressoIdlingResource {
            return localDataSource.observeTasks()
        }
    }

    override fun observeTask(id: String): LiveData<Result<Task>> {
        wrapEspressoIdlingResource {
            return localDataSource.observeTaskById(id)
        }
    }

    override suspend fun completeTask(id: String) {
        wrapEspressoIdlingResource {
            localDataSource.completeTask(id)
        }
    }

    override suspend fun completeTask(task: Task) {
        wrapEspressoIdlingResource {
            localDataSource.completeTask(taskId = task.id)
        }
    }

    override suspend fun activateTask(id: String) {
        wrapEspressoIdlingResource {
            localDataSource.activateTask(id)
        }
    }

    override suspend fun activateTask(task: Task) {
        wrapEspressoIdlingResource {
        }
    }

    override suspend fun getTask(taskId: String): Result<Task> {
        wrapEspressoIdlingResource {
            return localDataSource.getTask(taskId)
        }
    }

    override suspend fun getTasks(): Result<List<Task>> {
        wrapEspressoIdlingResource {
            return localDataSource.getAllTasks()
        }
    }

    override suspend fun saveTask(task: Task) {
        wrapEspressoIdlingResource {
            localDataSource.saveTasks(task)
        }
    }

    override suspend fun updateTask(task: Task) {
        wrapEspressoIdlingResource {
            localDataSource.updateTask(task)
        }
    }

    override suspend fun clearAllCompletedTasks() {
        wrapEspressoIdlingResource {
            localDataSource.clearCompletedTasks()
        }
    }

    override suspend fun deleteAllTasks() {
        wrapEspressoIdlingResource {
            localDataSource.deleteAllTasks()
        }
    }

    override suspend fun deleteTask(taskId: String) {
        wrapEspressoIdlingResource {
            localDataSource.deleteTaskById(taskId)
        }
    }

    override suspend fun fetchAllToDoTasks(isUpdate: Boolean): Result<List<Task>> {
        wrapEspressoIdlingResource {
            try {
                if(isUpdate) {
                    updateTaskFromRemote()
                }
            } catch (ex: Exception) {
                return Result.Error(ex)
            }
            return localDataSource.getAllTasks()
        }
    }

    private suspend fun updateTaskFromRemote() {
        wrapEspressoIdlingResource {
            val result = remoteDataSource.getAllTasks()
            if(result is Result.Success) {
                //localDataSource.deleteAllTasks()
                result.data.forEach {
                    localDataSource.saveTasks(it)
                }
            } else if(result is Result.Error) {
                throw result.exception
            }
        }
    }

}
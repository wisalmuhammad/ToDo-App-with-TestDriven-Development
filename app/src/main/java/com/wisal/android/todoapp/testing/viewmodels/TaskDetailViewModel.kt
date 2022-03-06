package com.wisal.android.todoapp.testing.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.wisal.android.todoapp.testing.R
import com.wisal.android.todoapp.testing.data.Result
import com.wisal.android.todoapp.testing.data.Task
import com.wisal.android.todoapp.testing.repositories.TasksRepository
import com.wisal.android.todoapp.testing.repositories.TasksRepositoryImp
import com.wisal.android.todoapp.testing.util.Event
import kotlinx.coroutines.launch


class TaskDetailViewModel(
    private val tasksRepository: TasksRepository
) : ViewModel() {

    private val TAG = "TaskDetailViewModel"

    private val _taskId: MutableLiveData<String> = MutableLiveData()
    private val _task = _taskId.switchMap {
        tasksRepository.observeTask(it).map { res ->
            isolateTask(res)
        }
    }

    val task: LiveData<Task?> = _task
    private val _toastText = MutableLiveData<Event<Int>>()
    val toastText: LiveData<Event<Int>> = _toastText

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading
    private val _editTaskEvent = MutableLiveData<Event<Unit>>()
    val editTaskEvent: LiveData<Event<Unit>> = _editTaskEvent

    private val _deleteTaskEvent = MutableLiveData<Event<Unit>>()
    val deleteTaskEvent = _deleteTaskEvent

    fun loadTaskById(taskId: String) {
        if(dataLoading.value == true || _taskId.value == taskId) return

        _taskId.value = taskId
        Log.d("Test","loading task with id $taskId")
    }

    fun editTask(){
        _editTaskEvent.value = Event(Unit)
    }

    fun deleteTask() = viewModelScope.launch {
        _taskId.value?.let {
            tasksRepository.deleteTask(it)
            _deleteTaskEvent.value = Event(Unit)
        }
    }

    fun setCompleted(completed: Boolean) = viewModelScope.launch {
        val task = _task.value ?: return@launch
        if(completed) {
            tasksRepository.completeTask(task.id)
            _toastText.value = Event(R.string.task_marked_complete)
        }
         else {
            tasksRepository.activateTask(task.id)
            _toastText.value = Event(R.string.task_marked_active)
        }
    }

    private fun isolateTask(result: Result<Task?>): Task? {
        return if(result is Result.Success) {
            result.data
        } else {
            _toastText.value = Event(R.string.loading_tasks_error) // TODO Toast msg need main thread
            null
        }
    }


    @Suppress("UNCHECKED_CAST")
    class TasksDetailViewModelFactory(
        private val tasksRepository: TasksRepository
    ): ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return (TaskDetailViewModel(
                tasksRepository
            ) as T)
        }
    }

}
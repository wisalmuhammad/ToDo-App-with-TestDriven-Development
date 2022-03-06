package com.wisal.android.todoapp.testing.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.wisal.android.todoapp.testing.R
import com.wisal.android.todoapp.testing.data.Result
import com.wisal.android.todoapp.testing.data.Task
import com.wisal.android.todoapp.testing.repositories.TasksRepository
import com.wisal.android.todoapp.testing.repositories.TasksRepositoryImp
import com.wisal.android.todoapp.testing.ui.TasksFilterType
import com.wisal.android.todoapp.testing.util.Event
import kotlinx.coroutines.launch

class TasksViewModel(
    private val tasksRepository: TasksRepository
) : ViewModel() {

    private val TAG = "TasksViewModel"

    private val _forceUpdate = MutableLiveData<Boolean>(false)
    private val _items: LiveData<List<Task>> = _forceUpdate.switchMap { isUpdate ->
        Log.d(TAG," force flag updated $isUpdate")
        if(isUpdate) {
            _dataLoading.value = true
            viewModelScope.launch {
                tasksRepository.fetchAllToDoTasks(isUpdate)
                _dataLoading.value = false
            }
        }

        tasksRepository.observeTasks().switchMap {
            filterTasks(it)
        }
    }

    private fun filterTasks(result: Result<List<Task>>) = liveData {
        emit(
            if(result is Result.Success) {
                val tasksToShow = ArrayList<Task>()
                viewModelScope.launch {
                    result.data.forEach { task ->
                        when(currentFiltering) {
                            TasksFilterType.ALL_TASKS -> {
                                tasksToShow.add(task)
                            }
                            TasksFilterType.COMPLETED_TASKS -> {
                                if (task.completed) {
                                    tasksToShow.add(task)
                                }
                            }
                            TasksFilterType.ACTIVE_TASKS -> {
                                if(task.isActive) {
                                    tasksToShow.add(task)
                                }
                            }
                        }
                    }
                }
                tasksToShow
            } else {
                emptyList()
            }
        )
    }

    val items: LiveData<List<Task>> = _items

    private val _toastText = MutableLiveData<Event<Int>>()
    val toastText: LiveData<Event<Int>> = _toastText

    private val _newTaskEvent: MutableLiveData<Event<Unit>> = MutableLiveData()
    val newTaskEvent: LiveData<Event<Unit>> = _newTaskEvent

    private val _dataLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _openTaskEvent = MutableLiveData<String?>()
    val openTaskEvent: LiveData<String?> = _openTaskEvent

    private var currentFiltering = TasksFilterType.ALL_TASKS


    init {
        loadAllTasks(true)
    }

    fun openTask(taskId: String) {
        _openTaskEvent.value = taskId
        _openTaskEvent.value = null
    }

    fun completeTask(task: Task, completed: Boolean) = viewModelScope.launch {
        if (completed) {
            tasksRepository.completeTask(task.id)
            _toastText.value = Event(R.string.task_marked_complete)
        } else {
            tasksRepository.activateTask(task.id)
            _toastText.value = Event(R.string.task_marked_active)
        }
    }

    fun setNewTask() {
        _newTaskEvent.value = Event(Unit)
    }

    fun loadAllTasks(isUpdate: Boolean) {
        _forceUpdate.value = isUpdate
    }

    fun clearCompletedTasks() = viewModelScope.launch {
        tasksRepository.clearAllCompletedTasks()
        _toastText.value = Event(R.string.completed_tasks_cleared)
    }


    fun setFiltering(type: TasksFilterType) {
        currentFiltering = type
        loadAllTasks(false)
    }

    fun refresh() {
        _forceUpdate.value = true
    }

    @Suppress("UNCHECKED_CAST")
    class TasksViewModelFactory(
        private val tasksRepository: TasksRepository
    ): ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return (TasksViewModel(
                tasksRepository
            ) as T)
        }
    }


}
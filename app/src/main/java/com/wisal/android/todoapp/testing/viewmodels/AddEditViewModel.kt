package com.wisal.android.todoapp.testing.viewmodels

import androidx.lifecycle.*
import com.wisal.android.todoapp.testing.R
import com.wisal.android.todoapp.testing.data.Result
import com.wisal.android.todoapp.testing.data.Task
import com.wisal.android.todoapp.testing.repositories.TasksRepository
import com.wisal.android.todoapp.testing.util.Event
import kotlinx.coroutines.launch

class AddEditViewModel(private val tasksRepository: TasksRepository) : ViewModel() {


    val title = MutableLiveData<String>()
    //val userId = MutableLiveData<Int>()

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _toastText = MutableLiveData<Event<Int>>()
    val toastText: LiveData<Event<Int>> = _toastText

    private val _taskUpdatedEvent = MutableLiveData<Event<Unit>>()
    val taskUpdatedEvent: LiveData<Event<Unit>> = _taskUpdatedEvent

    private var taskId: String? = null
    private var isNewTask: Boolean = false
    private var isDataLoaded = false
    private var taskCompleted = false



    fun start(taskId: String?) {
        if(_dataLoading.value == true) return

        this.taskId = taskId
        if(taskId == null) {
            isNewTask = true
            return
        }
        if(isDataLoaded) return

        isNewTask = false
        _dataLoading.value = true
        viewModelScope.launch {
            tasksRepository.getTask(taskId).let {
                if(it is Result.Success) {
                    onTaskLoaded(it.data)
                } else {
                    onDataNotAvailable()
                }
            }
        }
    }

    fun saveTask() {
        val title = title.value
        if(title == null){
            _toastText.value = Event(R.string.empty_task_message)
            return
        }

        if(isNewTask || taskId == null) {
            createTask(Task(title = title))
        } else {
            val task = Task(id = taskId!!, title = title, completed = taskCompleted)
            updateTask(task)
        }

    }

    private fun createTask(newTask: Task) = viewModelScope.launch {
        tasksRepository.saveTask(newTask)
        _taskUpdatedEvent.value = Event(Unit)
    }

    private fun onDataNotAvailable() {
        _dataLoading.value = false
    }

    private fun onTaskLoaded(task: Task) {
        title.value = task.title
        taskCompleted = task.completed
        _dataLoading.value = false
        isDataLoaded = true
    }

    private fun updateTask(task: Task) {
        if(isNewTask) {
            throw Exception("Update task was called but task is new")
        }

        viewModelScope.launch {
            tasksRepository.updateTask(task)
            _taskUpdatedEvent.value = Event(Unit)
        }

    }

    @Suppress("UNCHECKED_CAST")
    class AddEditTaskViewModelFactory(
        private val tasksRepository: TasksRepository
    ): ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return (AddEditViewModel(
                tasksRepository
            ) as T)
        }
    }

}
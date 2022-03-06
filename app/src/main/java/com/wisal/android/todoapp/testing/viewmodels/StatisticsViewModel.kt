package com.wisal.android.todoapp.testing.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.wisal.android.todoapp.testing.data.Result
import com.wisal.android.todoapp.testing.data.Task
import com.wisal.android.todoapp.testing.repositories.TasksRepository
import com.wisal.android.todoapp.testing.repositories.TasksRepositoryImp
import com.wisal.android.todoapp.testing.statistics.StatsResult
import com.wisal.android.todoapp.testing.statistics.getActiveAndCompletedTasksStats
import kotlinx.coroutines.launch

class StatisticsViewModel(
    private val tasksRepository: TasksRepository
) : ViewModel() {

    private val TAG = "StatisticsViewModel"

    private val tasks: LiveData<Result<List<Task>>> = tasksRepository.observeTasks()

    private val stats: LiveData<StatsResult?> = tasks.map {
        if(it is Result.Success) {
            getActiveAndCompletedTasksStats(
                it.data
            )
        }
         else null
    }

    val empty: LiveData<Boolean> = tasks.map {
        (it as? Result.Success)?.data.isNullOrEmpty()
    }

    val activeTasksPercent = stats.map {
        it?.activeTasksPercent ?: 0f
    }

    val completedTasksPercent: LiveData<Float> = stats.map {
        it?.completedTasksPercent ?: 0f
    }

    fun refreshTasks() {
        viewModelScope.launch {
            tasksRepository.fetchAllToDoTasks(false)
        }
    }

    @Suppress("UNCHECKED_CAST")
    class StatisticsViewModelFactory(
        private val tasksRepository: TasksRepository
    ): ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return (StatisticsViewModel(
                tasksRepository
            ) as T)
        }
    }

}
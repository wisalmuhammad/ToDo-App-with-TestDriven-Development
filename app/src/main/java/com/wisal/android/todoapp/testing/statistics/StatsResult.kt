package com.wisal.android.todoapp.testing.statistics

import com.wisal.android.todoapp.testing.data.Task


data class StatsResult(
    val activeTasksPercent: Float,
    val completedTasksPercent: Float
)


internal fun getActiveAndCompletedTasksStats(
    tasks: List<Task>?
): StatsResult {

    return if(tasks.isNullOrEmpty()) {
        StatsResult(0f,0f)
    } else {
        val totalTasks = tasks.size
        val activeTasks = tasks.count { it.isActive }

        StatsResult(
            activeTasksPercent = 100f * activeTasks / totalTasks,
            completedTasksPercent = 100f * (totalTasks - activeTasks) / totalTasks
        )
    }

}
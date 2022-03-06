package com.wisal.android.todoapp.testing

import android.app.Application
import com.wisal.android.todoapp.testing.repositories.TasksRepository
import com.wisal.android.todoapp.testing.util.ServiceLocator

class BaseApplication: Application() {

    val tasksRepository: TasksRepository
        get() = ServiceLocator.provideTasksRepository(this)

    override fun onCreate() {
        super.onCreate()


    }


}
package com.wisal.android.todoapp.testing.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.wisal.android.todoapp.testing.data.Task


@Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class ToDoDatabase: RoomDatabase() {

   abstract fun tasksDao(): TasksDao

}
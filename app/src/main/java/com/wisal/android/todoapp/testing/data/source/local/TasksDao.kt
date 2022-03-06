package com.wisal.android.todoapp.testing.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.wisal.android.todoapp.testing.data.Task


@Dao
interface TasksDao {

    @Query("SELECT * FROM Tasks")
    fun getAllTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM Tasks WHERE id = :taskId")
    fun getTaskById(taskId: String): Task?

    @Query("SELECT * FROM Tasks")
    fun getTasks(): List<Task>

    @Query("SELECT * FROM Tasks WHERE id = :taskId")
    fun observeTaskById(taskId: String): LiveData<Task>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertTask(task: Task)

    @Query("UPDATE Tasks SET completed = :completed WHERE id = :taskId")
    fun updateCompleted(taskId: String, completed: Boolean)

    @Update
    fun updateTask(task: Task)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertTasks(vararg task: Task)

    @Query("DELETE FROM Tasks WHERE id = :taskId")
    suspend fun deleteTaskById(taskId: String): Int

    @Query("DELETE FROM Tasks")
    fun deleteAllTasks()

    @Query("DELETE FROM Tasks where completed = 1")
    fun clearCompletedTasks(): Int

}
package com.wisal.android.todoapp.testing.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*


@Entity(tableName = "Tasks")
data class Task @JvmOverloads constructor(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "userId")
    var userId: Int = 1,
    @ColumnInfo(name = "title")
    var title: String = "",
    @ColumnInfo(name = "completed")
    var completed: Boolean = false
) {

    val isEmpty
        get() = title.isEmpty()

    val isActive: Boolean
      get() = !(completed)

}

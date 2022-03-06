package com.wisal.android.todoapp.testing.adapters

import android.graphics.Paint
import android.util.Log
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wisal.android.todoapp.testing.data.Task


@BindingAdapter("app:items")
fun setItems(listView: RecyclerView,tasks: List<Task>?) {
    tasks?.let {
        (listView.adapter as TasksAdapter).submitList(tasks)
    }
}

@BindingAdapter("app:completedTask")
fun setStyle(textView: TextView, enabled: Boolean) {
    if (enabled) {
        textView.paintFlags = textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    } else {
        textView.paintFlags = textView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
    }
}
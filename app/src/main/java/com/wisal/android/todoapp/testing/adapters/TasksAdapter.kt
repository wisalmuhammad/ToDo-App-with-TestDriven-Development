package com.wisal.android.todoapp.testing.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wisal.android.todoapp.testing.data.Task
import com.wisal.android.todoapp.testing.databinding.TaskItemBinding
import com.wisal.android.todoapp.testing.viewmodels.TasksViewModel


class DiffTasksCallBack : DiffUtil.ItemCallback<Task>() {

    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem == newItem
    }

}

class TasksAdapter(private val viewModel: TasksViewModel):
        ListAdapter<Task, TasksAdapter.ItemViewHolder>(DiffTasksCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            TaskItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(
            viewModel,getItem(position)
        )
    }


    class ItemViewHolder(private val binding: TaskItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

            fun bind(viewModel: TasksViewModel,item: Task) {
                binding.task = item
                binding.viewmodel = viewModel
                binding.executePendingBindings()
            }
    }
}


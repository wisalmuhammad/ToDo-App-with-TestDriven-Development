package com.wisal.android.todoapp.testing.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.wisal.android.todoapp.testing.BaseApplication
import com.wisal.android.todoapp.testing.R
import com.wisal.android.todoapp.testing.adapters.TasksAdapter
import com.wisal.android.todoapp.testing.databinding.TasksFragmentBinding
import com.wisal.android.todoapp.testing.viewmodels.TasksViewModel
import com.wisal.android.todoapp.testing.repositories.TasksRepositoryImp
import com.wisal.android.todoapp.testing.util.setUpToast


class TasksFragment : Fragment() {

    private val TAG = "TasksFragment"

    private lateinit var binding: TasksFragmentBinding
    private lateinit var listAdapter: TasksAdapter
    private val viewModel by viewModels<TasksViewModel> {
        TasksViewModel.TasksViewModelFactory(
            ((requireContext().applicationContext as BaseApplication).tasksRepository)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TasksFragmentBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            viewmodel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        listAdapter = TasksAdapter(viewModel)
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.adapter = listAdapter
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToast(this,viewModel.toastText)
        binding.addTaskFab.setOnClickListener {
            navigateToNewTask()
        }

        viewModel.newTaskEvent.observe(this, {

        })

        viewModel.openTaskEvent.observe(viewLifecycleOwner, {
            it?.let { id ->
                val action = TasksFragmentDirections.actionTasksFragmentToTaskDetailFragment(id)
                findNavController().navigate(action)
            }
        })
    }

    private fun navigateToNewTask(){
        val action = TasksFragmentDirections.actionTasksFragmentToAddEditFragment(
            null,
            resources.getString(R.string.add_task)
        )
        findNavController().navigate(action)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.tasks_fragment_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
       return when(item.itemId) {
            R.id.all -> {
                viewModel.setFiltering(TasksFilterType.ALL_TASKS)
                true
            }
            R.id.active -> {
                viewModel.setFiltering(TasksFilterType.ACTIVE_TASKS)
                true
            }
            R.id.completed -> {
                viewModel.setFiltering(TasksFilterType.COMPLETED_TASKS)
                true
            }
            R.id.menu_clear -> {
                viewModel.clearCompletedTasks()
                true
            }
            R.id.menu_refresh -> {
                viewModel.loadAllTasks(true)
                true
            } else -> false
        }
    }


}
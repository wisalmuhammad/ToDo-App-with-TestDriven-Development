package com.wisal.android.todoapp.testing.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.wisal.android.todoapp.testing.BaseApplication
import com.wisal.android.todoapp.testing.R
import com.wisal.android.todoapp.testing.databinding.TaskDetailFragmentBinding
import com.wisal.android.todoapp.testing.repositories.TasksRepositoryImp
import com.wisal.android.todoapp.testing.util.EventObserver
import com.wisal.android.todoapp.testing.util.setUpToast
import com.wisal.android.todoapp.testing.viewmodels.TaskDetailViewModel

class TaskDetailFragment : Fragment() {

    private val TAG = "TaskDetailFragment"

    private lateinit var binding: TaskDetailFragmentBinding
    private val viewModel by viewModels<TaskDetailViewModel> {
        TaskDetailViewModel.TasksDetailViewModelFactory(
            ((requireContext().applicationContext as BaseApplication).tasksRepository)
        )
    }
    private val args: TaskDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TaskDetailFragmentBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            viewmodel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadTaskById(args.taskId)

        setUpToast(this,viewModel.toastText)

        viewModel.editTaskEvent.observe(viewLifecycleOwner, EventObserver {
            val action = TaskDetailFragmentDirections
                .actionTaskDetailFragmentToAddEditFragment(
                    args.taskId,
                    resources.getString(R.string.edit_task)
                )
            findNavController().navigate(action)
        })

        viewModel.deleteTaskEvent.observe(viewLifecycleOwner, EventObserver {
            val action = TaskDetailFragmentDirections
                .actionTaskDetailFragmentToTasksFragment(
                    1
                )
            findNavController().navigate(action)
        })

        binding.editTaskFab.setOnClickListener {
            viewModel.editTask()
        }


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.task_detail_fragment_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.menu_delete -> {
                viewModel.deleteTask()
                true
            } else -> false
        }
    }

}
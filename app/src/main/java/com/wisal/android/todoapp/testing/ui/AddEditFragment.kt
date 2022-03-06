package com.wisal.android.todoapp.testing.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.wisal.android.todoapp.testing.BaseApplication
import com.wisal.android.todoapp.testing.databinding.AddEditFragmentBinding
import com.wisal.android.todoapp.testing.util.EventObserver
import com.wisal.android.todoapp.testing.util.setUpToast
import com.wisal.android.todoapp.testing.viewmodels.AddEditViewModel

class AddEditFragment : Fragment() {

    private val TAG = "AddEditFragment"

    private lateinit var binding: AddEditFragmentBinding
    private val args: AddEditFragmentArgs by navArgs()
    private val viewModel by viewModels<AddEditViewModel> {
        AddEditViewModel.AddEditTaskViewModelFactory(
            ((requireContext().applicationContext as BaseApplication).tasksRepository)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AddEditFragmentBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToast(this,viewModel.toastText)
        viewModel.taskUpdatedEvent.observe(viewLifecycleOwner, EventObserver {
            val action = AddEditFragmentDirections.actionAddEditFragmentToTasksFragment(1)
            findNavController().navigate(action)
        })

        viewModel.start(args.taskId)

    }

}
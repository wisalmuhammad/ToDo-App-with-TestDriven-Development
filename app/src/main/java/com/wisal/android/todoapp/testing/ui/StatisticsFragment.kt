package com.wisal.android.todoapp.testing.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.wisal.android.todoapp.testing.BaseApplication
import com.wisal.android.todoapp.testing.R
import com.wisal.android.todoapp.testing.databinding.StatisticsFragmentBinding
import com.wisal.android.todoapp.testing.viewmodels.StatisticsViewModel

class StatisticsFragment : Fragment() {

    private lateinit var binding: StatisticsFragmentBinding
    private val viewModel by viewModels<StatisticsViewModel>{
        StatisticsViewModel.StatisticsViewModelFactory(
            ((requireContext().applicationContext as BaseApplication).tasksRepository)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = StatisticsFragmentBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
        }
        return binding.root
    }


}
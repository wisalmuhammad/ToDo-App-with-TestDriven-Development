package com.wisal.android.todoapp.testing.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.wisal.android.todoapp.testing.MainCoroutineRule
import com.wisal.android.todoapp.testing.getOrAwaitValue
import com.wisal.android.todoapp.testing.repositories.FakeTasksRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
class StatisticsViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Subject under test
    private lateinit var viewModel: StatisticsViewModel

    // Use a fake repository to be injected into the view model.
    private lateinit var tasksRepository: FakeTasksRepository


    @Before
    fun setUp() {
        tasksRepository = FakeTasksRepository()
        viewModel = StatisticsViewModel(tasksRepository)
    }



    @Test
    fun `load statistics when tasks are unavailable,should return empty for display`() {
        tasksRepository.setReturnError(true)
        viewModel.refreshTasks()
        assertThat(viewModel.empty.getOrAwaitValue(),`is`(true))
    }

    @After
    fun tearDown() {
    }
}
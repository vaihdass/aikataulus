package ru.vaihdass.aikataulus.presentation.screen.main

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.flow.MutableSharedFlow
import ru.vaihdass.aikataulus.R
import ru.vaihdass.aikataulus.databinding.FragmentMainBinding
import ru.vaihdass.aikataulus.presentation.base.BaseFragment
import ru.vaihdass.aikataulus.presentation.base.appComponent
import ru.vaihdass.aikataulus.presentation.base.toastShort
import ru.vaihdass.aikataulus.presentation.di.TaskChangedFlow
import javax.inject.Inject

class MainFragment : BaseFragment(R.layout.fragment_main) {
    private val viewBinding by viewBinding(FragmentMainBinding::bind)

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val viewModel: MainViewModel by viewModels { factory }

    @Inject
    @TaskChangedFlow
    lateinit var taskChangedFlow: MutableSharedFlow<Unit>

    override fun onAttach(context: Context) {
        requireContext().appComponent.inject(fragment = this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindViewActions()
        observeData()
    }

    private fun bindViewActions() {
        viewModel.initEventsAdapter { event ->
            val action = MainFragmentDirections
                .actionMainFragmentToEventFragment(event = event)

            findNavController().navigate(action)
        }

        viewModel.initTasksAdapter { task ->
            val action = MainFragmentDirections
                .actionMainFragmentToTaskFragment(task = task)

            findNavController().navigate(action)
        }

        with(viewBinding) {
            rvEvents.adapter = viewModel.eventsAdapter
            rvTasks.adapter = viewModel.tasksAdapter

            ivBtnSettings.setOnClickListener {
                findNavController().navigate(R.id.action_mainFragment_to_settingsFragment)
            }

            btnSchedule.setOnClickListener {
                findNavController().navigate(R.id.action_mainFragment_to_scheduleFragment)
            }

            btnTasks.setOnClickListener {
                findNavController().navigate(R.id.action_mainFragment_to_tasksFragment)
            }

            srlMain.setOnRefreshListener {
                srlMain.isRefreshing = true

                viewModel.fetchEvents()
                viewModel.fetchTasks()

                srlMain.postDelayed({ srlMain.isRefreshing = false }, 3000)
            }

            fabCreateTask.setOnClickListener {
                findNavController().navigate(R.id.action_mainFragment_to_taskCreateFragment)
            }
        }
    }

    private fun observeData() {
        with(viewModel) {
            taskChangedFlow.observe {
                fetchTasks()
            }

            eventsFlow.observe { events ->
                eventsAdapter.setItems(events)
                viewBinding.srlMain.isRefreshing = false
            }

            tasksFlow.observe { tasks ->
                tasksAdapter.setItems(tasks)
                viewBinding.srlMain.isRefreshing = false
            }

            errorFlow.observe {
                it?.let {
                    val errorMessage = it
                    toastShort(errorMessage)
                }
            }
        }
    }
}
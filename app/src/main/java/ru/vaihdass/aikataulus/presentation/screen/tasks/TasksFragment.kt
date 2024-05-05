package ru.vaihdass.aikataulus.presentation.screen.tasks

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.flow.MutableSharedFlow
import ru.vaihdass.aikataulus.R
import ru.vaihdass.aikataulus.databinding.FragmentTasksBinding
import ru.vaihdass.aikataulus.presentation.base.BaseFragment
import ru.vaihdass.aikataulus.presentation.base.appComponent
import ru.vaihdass.aikataulus.presentation.base.toastShort
import ru.vaihdass.aikataulus.presentation.di.TaskChangedFlow
import ru.vaihdass.aikataulus.presentation.helper.TimeFormatHelper
import ru.vaihdass.aikataulus.presentation.recyclerview.adapter.TasksAdapter
import javax.inject.Inject

class TasksFragment : BaseFragment(R.layout.fragment_tasks) {
    private val viewBinding by viewBinding(FragmentTasksBinding::bind)

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val viewModel: TasksViewModel by viewModels { factory }

    @Inject
    @TaskChangedFlow
    lateinit var taskChangedFlow: MutableSharedFlow<Unit>

    @Inject
    lateinit var timeFormatHelper: TimeFormatHelper

    override fun onAttach(context: Context) {
        requireContext().appComponent.inject(fragment = this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindView()
        bindViewActions()
        observeData()
    }

    private fun bindView() {
        with(viewBinding) {
            viewModel.tasksAdapter = TasksAdapter(
                formatDate = { timeFormatHelper.formatDay(it) },
                onItemClicked = { task ->
                    val action = TasksFragmentDirections
                        .actionTasksFragmentToTaskFragment(task = task)

                    findNavController().navigate(action)
                }
            )

            rvTasks.adapter = viewModel.tasksAdapter
        }
    }

    private fun bindViewActions() {
        with(viewBinding) {
            btnRemoveDoneTasks.setOnClickListener {
                viewModel.removeDoneTasks()
            }

            srlTasks.setOnRefreshListener {
                srlTasks.isRefreshing = true

                viewModel.fetchTasks()

                srlTasks.postDelayed({ srlTasks.isRefreshing = false }, 3000)
            }
        }
    }

    private fun observeData() {
        with(viewModel) {
            tasksFlow.observe { tasks ->
                tasksAdapter.setItems(tasks)
                viewBinding.srlTasks.isRefreshing = false
            }

            taskChangedFlow.observe {
                fetchTasks()
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
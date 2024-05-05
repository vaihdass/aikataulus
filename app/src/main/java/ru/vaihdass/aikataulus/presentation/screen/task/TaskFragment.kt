package ru.vaihdass.aikataulus.presentation.screen.task

import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import ru.vaihdass.aikataulus.R
import ru.vaihdass.aikataulus.databinding.FragmentTaskBinding
import ru.vaihdass.aikataulus.presentation.base.BaseFragment
import ru.vaihdass.aikataulus.presentation.base.appComponent
import ru.vaihdass.aikataulus.presentation.base.toastShort
import ru.vaihdass.aikataulus.presentation.di.TaskChangedFlow
import ru.vaihdass.aikataulus.presentation.helper.TimeFormatHelper
import ru.vaihdass.aikataulus.presentation.model.TaskUiModel
import javax.inject.Inject

class TaskFragment : BaseFragment(R.layout.fragment_task) {
    private val viewBinding by viewBinding(FragmentTaskBinding::bind)
    private val args: TaskFragmentArgs by navArgs()

    @Inject
    @TaskChangedFlow
    lateinit var taskChangedFlow: MutableSharedFlow<Unit>

    @Inject
    lateinit var timeFormatHelper: TimeFormatHelper

    @Inject
    lateinit var factory: TaskAssistedViewModel.Factory
    private val viewModel: TaskAssistedViewModel by viewModels {
        TaskAssistedViewModel.provideFactory(
            assistedFactory = factory, task = args.task
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireContext().appComponent.inject(fragment = this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindTask(task = viewModel.task)
        bindViewActions()
        observeData()
    }

    private fun bindViewActions() {
        with(viewBinding) {
            srlTask.setOnRefreshListener {
                srlTask.isRefreshing = true

                viewModel.fetchTask()

                srlTask.postDelayed({ srlTask.isRefreshing = false }, 3000)
            }

            fabRemoveTask.setOnClickListener {
                viewModel.removeTask()
            }

            fabEditTask.setOnClickListener {
                val action =
                    TaskFragmentDirections.actionTaskFragmentToTaskEditFragment(task = viewModel.task)

                findNavController().navigate(action)
            }

            fabDoneTask.setOnClickListener {
                if (viewModel.task.isDone) {
                    viewModel.task.isDone = false

                    viewModel.editTask()

                    fabDoneTask.setImageResource(R.drawable.done)
                    tvSubject.paintFlags =
                        tvSubject.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                } else {
                    viewModel.task.isDone = true

                    viewModel.editTask()

                    fabDoneTask.setImageResource(R.drawable.uncompleted)
                    tvSubject.paintFlags = tvSubject.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG
                }
            }
        }
    }

    private fun bindTask(task: TaskUiModel) {
        with(viewBinding) {
            tvSubject.text = task.subject.trim().lowercase().replaceFirstChar { it.titlecase() }

            if (task.isDone) {
                tvSubject.paintFlags = tvSubject.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                fabDoneTask.setImageResource(R.drawable.uncompleted)
            } else {
                tvSubject.paintFlags = tvSubject.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                fabDoneTask.setImageResource(R.drawable.done)
            }

            tvTask.text = task.task.trim()
            layoutTaskInfo.isVisible = task.task.trim().isEmpty().not()

            if (task.deadline == null) {
                tvDate.text = getString(R.string.without_a_deadline)
            } else {
                task.deadline?.let {
                    tvDate.text = timeFormatHelper.formatDay(it)
                }
            }
        }
    }

    private fun observeData() {
        with(viewModel) {
            taskFlow.observe { task ->
                lifecycleScope.launch {
                    bindTask(task)
                }
            }

            removeSuccessEventFlow.observe {
                lifecycleScope.launch {
                    taskChangedFlow.emit(Unit)
                    findNavController().popBackStack()
                }
            }

            errorFlow.observe {
                val errorMessage = it ?: getString(ru.vaihdass.aikataulus.R.string.unknown_error)
                toastShort(errorMessage)
            }
        }
    }

    companion object
}
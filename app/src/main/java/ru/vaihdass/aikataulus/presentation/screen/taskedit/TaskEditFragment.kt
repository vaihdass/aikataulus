package ru.vaihdass.aikataulus.presentation.screen.taskedit

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import ru.vaihdass.aikataulus.R
import ru.vaihdass.aikataulus.databinding.FragmentTaskEditBinding
import ru.vaihdass.aikataulus.presentation.base.BaseFragment
import ru.vaihdass.aikataulus.presentation.base.appComponent
import ru.vaihdass.aikataulus.presentation.base.toastShort
import ru.vaihdass.aikataulus.presentation.di.TaskChangedFlow
import ru.vaihdass.aikataulus.presentation.helper.TimeFormatHelper
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

class TaskEditFragment : BaseFragment(R.layout.fragment_task_edit) {
    private val viewBinding by viewBinding(FragmentTaskEditBinding::bind)
    private val args: TaskEditFragmentArgs by navArgs()

    @Inject
    @TaskChangedFlow
    lateinit var taskChangedFlow: MutableSharedFlow<Unit>

    @Inject
    lateinit var timeFormatHelper: TimeFormatHelper

    private var deadline: Long? = null

    @Inject
    lateinit var factory: TaskEditAssistedViewModel.Factory
    private val viewModel: TaskEditAssistedViewModel by viewModels {
        TaskEditAssistedViewModel.provideFactory(
            assistedFactory = factory,
            task = args.task
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireContext().appComponent.inject(fragment = this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTask()
        bindViewActions()
        observeFlows()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.apply {
            putString(TASK_SUBJECT, viewBinding.etSubject.text.toString())
            putString(TASK_DESCRIPTION, viewBinding.etTask.text.toString())
            deadline?.let { putLong(TASK_DEADLINE, it) }
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        savedInstanceState?.let { bundle ->
            val subject = bundle.getString(TASK_SUBJECT) ?: viewModel.task.subject
            val task = bundle.getString(TASK_DESCRIPTION) ?: viewModel.task.task

            with(viewBinding) {
                etSubject.setText(subject.trim().lowercase().replaceFirstChar { it.titlecase() })
                etTask.setText(task.trim())
            }

            val deadline = bundle.getLong(TASK_DEADLINE, -1L)
            when (deadline) {
                -1L -> {
                    viewBinding.tvDeadline.text = getString(R.string.deadline_not_chosen)
                }

                else -> {
                    this.deadline = deadline
                    viewBinding.tvDeadline.text = timeFormatHelper.formatDay(Date(deadline))
                }
            }
        }
    }

    private fun initTask() {
        with(viewBinding) {
            val task = viewModel.task

            etSubject.setText(task.subject.trim().lowercase().replaceFirstChar { it.titlecase() })
            etTask.setText(task.task.trim())

            if (task.deadline == null) {
                tvDeadline.text = getString(R.string.deadline_not_chosen)
            }

            task.deadline?.let {
                tvDeadline.text = timeFormatHelper.formatDay(it)
                deadline = it.time
            }
        }
    }

    private fun bindViewActions() {
        with(viewBinding) {
            val datePicker = createDatePicker { dateInMs ->
                deadline = dateInMs
                viewBinding.tvDeadline.text = timeFormatHelper.formatDay(Date(dateInMs))
            }

            btnDeadlineChooser.setOnClickListener {
                datePicker.show(requireActivity().supportFragmentManager, datePicker.toString())
            }

            btnSave.setOnClickListener {
                btnSave.isEnabled = false

                with(viewModel) {
                    task.subject = etSubject.text.toString().trim().lowercase().replaceFirstChar { it.titlecase() }
                    task.task = etTask.text.toString().trim()
                    task.deadline = deadline?.let { date -> Date(date) }

                    editTask()
                }
            }
        }
    }

    private fun createDatePicker(onPositiveClick: (Long) -> Unit): MaterialDatePicker<Long> {
        val calendarStart: Calendar = Calendar.getInstance()
        val calendarEnd: Calendar = Calendar.getInstance()

        calendarStart.set(2022, Calendar.JANUARY, 1)
        calendarEnd.set(2060, Calendar.DECEMBER, 31)

        val constraintsBuilder = CalendarConstraints.Builder()
            .setStart(calendarStart.timeInMillis)
            .setEnd(calendarEnd.timeInMillis)

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(getString(R.string.choose_deadline_date_title))
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .setCalendarConstraints(constraintsBuilder.build())
            .build()

        datePicker.addOnPositiveButtonClickListener { onPositiveClick(it) }

        return datePicker
    }

    private fun observeFlows() {
        with(viewModel) {
            taskEditSuccessEventFlow.observe {
                lifecycleScope.launch {
                    taskChangedFlow.emit(Unit)

                    val navOptions = NavOptions.Builder()
                        .setPopUpTo(R.id.taskFragment, true)
                        .build()

                    findNavController().navigate(
                        R.id.action_taskEditFragment_to_taskFragment,
                        Bundle().apply {
                            putSerializable("task", viewModel.task)
                        },
                        navOptions
                    )
                }
            }

            errorFlow.observe {
                val errorMessage = it ?: getString(ru.vaihdass.aikataulus.R.string.unknown_error)
                toastShort(errorMessage)
            }
        }
    }

    companion object {
        const val TASK_SUBJECT = "TASK_SUBJECT"
        const val TASK_DESCRIPTION = "TASK_DESCRIPTION"
        const val TASK_DEADLINE = "TASK_DEADLINE"
    }
}
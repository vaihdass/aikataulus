package ru.vaihdass.aikataulus.presentation.screen.taskcreate

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import ru.vaihdass.aikataulus.R
import ru.vaihdass.aikataulus.databinding.FragmentTaskCreateBinding
import ru.vaihdass.aikataulus.presentation.base.BaseFragment
import ru.vaihdass.aikataulus.presentation.base.appComponent
import ru.vaihdass.aikataulus.presentation.base.toastShort
import ru.vaihdass.aikataulus.presentation.di.TaskChangedFlow
import ru.vaihdass.aikataulus.presentation.helper.TimeFormatHelper
import ru.vaihdass.aikataulus.presentation.model.TaskUiModel
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

class TaskCreateFragment : BaseFragment(R.layout.fragment_task_create) {
    private val viewBinding by viewBinding(FragmentTaskCreateBinding::bind)

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val viewModel: TaskCreateViewModel by viewModels { factory }

    @Inject
    @TaskChangedFlow
    lateinit var taskChangedFlow: MutableSharedFlow<Unit>

    @Inject
    lateinit var timeFormatHelper: TimeFormatHelper

    private var deadline: Long? = null

    override fun onAttach(context: Context) {
        requireContext().appComponent.inject(fragment = this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindViewActions()
        observeData()
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
            val subject = bundle.getString(TASK_SUBJECT) ?: ""
            val task = bundle.getString(TASK_DESCRIPTION) ?: ""

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

    private fun bindViewActions() {
        with(viewBinding) {
            val datePicker = createDatePicker { dateInMs ->
                deadline = dateInMs
                viewBinding.tvDeadline.text = timeFormatHelper.formatDay(Date(dateInMs))
            }

            btnDeadlineChooser.setOnClickListener {
                datePicker.show(requireActivity().supportFragmentManager, datePicker.toString())
            }

            btnCreate.setOnClickListener {
                btnCreate.isEnabled = false

                val task = TaskUiModel(
                    id = "",
                    subject = etSubject.text.toString().trim().lowercase()
                        .replaceFirstChar { it.titlecase() },
                    task = etTask.text.toString().trim(),
                    deadline = deadline?.let { Date(it) },
                )

                viewModel.createTask(task)
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

    private fun observeData() {
        with(viewModel) {
            taskFlow.observe { task ->
                lifecycleScope.launch {
                    task?.let {
                        taskChangedFlow.emit(Unit)
                        findNavController().popBackStack()
                    }
                }
            }

            errorFlow.observe {
                it?.let {
                    val errorMessage = it
                    toastShort(errorMessage)
                }
            }
        }
    }

    companion object {
        const val TASK_SUBJECT = "TASK_SUBJECT"
        const val TASK_DESCRIPTION = "TASK_DESCRIPTION"
        const val TASK_DEADLINE = "TASK_DEADLINE"
    }
}
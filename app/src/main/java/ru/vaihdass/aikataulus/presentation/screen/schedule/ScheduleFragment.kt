package ru.vaihdass.aikataulus.presentation.screen.schedule

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.WeekDay
import com.kizitonwose.calendar.core.WeekDayPosition
import com.kizitonwose.calendar.core.atStartOfMonth
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.yearMonth
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.ViewContainer
import com.kizitonwose.calendar.view.WeekDayBinder
import ru.vaihdass.aikataulus.R
import ru.vaihdass.aikataulus.databinding.CalendarDayBinding
import ru.vaihdass.aikataulus.databinding.FragmentScheduleBinding
import ru.vaihdass.aikataulus.presentation.base.BaseFragment
import ru.vaihdass.aikataulus.presentation.base.appComponent
import ru.vaihdass.aikataulus.presentation.base.toastShort
import ru.vaihdass.aikataulus.presentation.recyclerview.adapter.TodayEventsAdapter
import ru.vaihdass.aikataulus.presentation.recyclerview.adapter.TodayTasksAdapter
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject

class ScheduleFragment : BaseFragment(R.layout.fragment_schedule) {
    private val viewBinding by viewBinding(FragmentScheduleBinding::bind)

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val viewModel: ScheduleViewModel by viewModels { factory }

    private var selectedDay: LocalDate? = null
    private var isMonthMode = true

    override fun onAttach(context: Context) {
        requireContext().appComponent.inject(fragment = this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initCalendars()
        bindView()
        bindViewActions()
        observeData()
    }

    private fun bindView() {
        with(viewBinding) {
            viewModel.eventsAdapter = TodayEventsAdapter { event ->
                val action = ScheduleFragmentDirections
                    .actionScheduleFragmentToEventFragment(event = event)

                findNavController().navigate(action)
            }

            viewModel.tasksAdapter = TodayTasksAdapter { task ->
                val action = ScheduleFragmentDirections
                    .actionScheduleFragmentToTaskFragment(task = task)

                findNavController().navigate(action)
            }

            rvEvents.adapter = viewModel.eventsAdapter
            rvTasks.adapter = viewModel.tasksAdapter
        }
    }

    private fun bindViewActions() {
        with(viewBinding) {
            ivBtnSchedule.setOnClickListener {
                isMonthMode = !isMonthMode
                onCalendarViewToggled()
            }
        }
    }

    private fun observeData() {
        with(viewModel) {
            eventsFlow.observe { events ->
                eventsAdapter.setItems(events)
            }

            tasksFlow.observe { tasks ->
                tasksAdapter.setItems(tasks)
            }

            errorFlow.observe {
                it?.let {
                    val errorMessage = it
                    toastShort(errorMessage)
                }
            }
        }
    }

    private fun dateClicked(date: LocalDate) {
        with(viewBinding) {
            if (selectedDay == date) return

            val oldDay = selectedDay
            selectedDay = date

            calendarMonth.notifyDateChanged(date)
            calendarWeek.notifyDateChanged(date)

            oldDay?.let {
                calendarMonth.notifyDateChanged(oldDay)
                calendarWeek.notifyDateChanged(oldDay)
            }

            viewModel.eventsAdapter.setItems(emptyList())
            viewModel.tasksAdapter.setItems(emptyList())

            viewModel.fetchEventsByDate(date)
            viewModel.fetchTasksByDate(date)
        }
    }

    private fun onCalendarViewToggled() {
        val monthToWeek = !isMonthMode

        with(viewBinding) {
            // Scroll to the position on the target calendar
            if (monthToWeek) {
                val targetDate = calendarMonth.findFirstVisibleDay()?.date ?: return
                calendarWeek.scrollToWeek(targetDate)
            } else {
                val targetMonth = calendarWeek.findLastVisibleDay()?.date?.yearMonth ?: return
                calendarMonth.scrollToMonth(targetMonth)
            }

            // Calculate old and new height of calendar
            val weekHeight = calendarWeek.height
            val visibleMonthHeight = weekHeight *
                    calendarMonth.findFirstVisibleMonth()?.weekDays.orEmpty().count()

            val oldHeight = if (monthToWeek) visibleMonthHeight else weekHeight
            val newHeight = if (monthToWeek) weekHeight else visibleMonthHeight

            // Animate toggling of the calendar view
            val animator = ValueAnimator.ofInt(oldHeight, newHeight)
            animator.addUpdateListener { anim ->
                calendarMonth.updateLayoutParams {
                    height = anim.animatedValue as Int
                }

                /* Bug is causing the month calendar to not redraw its children
                 with the updated height during animation, this is a workaround.
                */
                calendarMonth.children.forEach { child ->
                    child.requestLayout()
                }
            }

            animator.doOnStart {
                if (!monthToWeek) {
                    calendarWeek.isGone = true
                    calendarMonth.isVisible = true
                }
            }
            animator.doOnEnd {
                if (monthToWeek) {
                    calendarWeek.isVisible = true
                    calendarMonth.isGone = true
                } else {
                    calendarMonth.updateLayoutParams { height =
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    }
                }

                updateTitle()
            }

            animator.duration = 250
            animator.start()
        }
    }

    private fun initCalendars() {
        with(viewBinding) {
            val daysOfWeek = daysOfWeek()

            layoutLegend.root.children.map { it as TextView }.forEachIndexed { index, textView ->
                textView.text = daysOfWeek[index].displayText()
            }

            val currentMonth = YearMonth.now()
            val startMonth = currentMonth.minusMonths(100)
            val endMonth = currentMonth.plusMonths(100)

            setupMonthCalendar(startMonth, endMonth, currentMonth, daysOfWeek)
            setupWeekCalendar(startMonth, endMonth, currentMonth, daysOfWeek)

            updateTitle()
        }
    }

    private fun setupWeekCalendar(
        startMonth: YearMonth,
        endMonth: YearMonth,
        currentMonth: YearMonth,
        daysOfWeek: List<DayOfWeek>,
    ) {
        class WeekDayViewContainer(view: View) : ViewContainer(view) {
            lateinit var day: WeekDay
            val textView = CalendarDayBinding.bind(view).tvDayText

            init {
                view.setOnClickListener {
                    if (day.position == WeekDayPosition.RangeDate) {
                        dateClicked(date = day.date)
                    }
                }
            }
        }

        with(viewBinding) {
            calendarWeek.dayBinder = object : WeekDayBinder<WeekDayViewContainer> {
                override fun create(view: View): WeekDayViewContainer = WeekDayViewContainer(view)
                override fun bind(container: WeekDayViewContainer, data: WeekDay) {
                    container.day = data
                    bindDate(
                        data.date,
                        container.textView,
                        data.position == WeekDayPosition.RangeDate,
                    )
                }
            }

            calendarWeek.weekScrollListener = { updateTitle() }

            calendarWeek.setup(
                startMonth.atStartOfMonth(),
                endMonth.atEndOfMonth(),
                daysOfWeek.first(),
            )

            calendarWeek.scrollToWeek(currentMonth.atStartOfMonth())
        }
    }

    private fun setupMonthCalendar(
        startMonth: YearMonth,
        endMonth: YearMonth,
        currentMonth: YearMonth,
        daysOfWeek: List<DayOfWeek>,
    ) {
        class DayViewContainer(view: View) : ViewContainer(view) {
            lateinit var day: CalendarDay
            val textView = CalendarDayBinding.bind(view).tvDayText

            init {
                view.setOnClickListener {
                    if (day.position == DayPosition.MonthDate) {
                        dateClicked(date = day.date)
                    }
                }
            }
        }

        with(viewBinding) {
            calendarMonth.dayBinder = object : MonthDayBinder<DayViewContainer> {
                override fun create(view: View) = DayViewContainer(view)

                override fun bind(container: DayViewContainer, data: CalendarDay) {
                    container.day = data
                    bindDate(data.date, container.textView, data.position == DayPosition.MonthDate)
                }
            }

            calendarMonth.monthScrollListener = { updateTitle() }

            calendarMonth.setup(
                startMonth,
                endMonth,
                daysOfWeek.first()
            )

            calendarMonth.scrollToMonth(currentMonth)
        }
    }

    private fun bindDate(date: LocalDate, textView: TextView, isSelectable: Boolean) {
        textView.text = date.dayOfMonth.toString()

        if (isSelectable) {
            when {
                selectedDay == date -> {
                    textView.setTextColorRes(R.color.colorCalendarDayTextSelected)
                    textView.setBackgroundResource(R.drawable.bg_calendar_day_selected)
                }

                else -> {
                    textView.setTextColorRes(R.color.colorCalendarDayText)
                    textView.background = null
                }
            }
        } else {
            textView.setTextColorRes(R.color.colorCalendarDayTextOtherMonth)
            textView.background = null
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateTitle() {
        with(viewBinding) {
            if (isMonthMode) {
                val month = calendarMonth.findFirstVisibleMonth()?.yearMonth ?: return

                tvDate.text = month.month.displayText(short = false)
            } else {
                val week = calendarWeek.findFirstVisibleWeek() ?: return

                // Has difference, because dates could belong to 2 other months
                val firstDate = week.days.first().date
                val lastDate = week.days.last().date

                if (firstDate.yearMonth == lastDate.yearMonth) {
                    tvDate.text = firstDate.month.displayText(short = true)
                } else {
                    tvDate.text = firstDate.month.displayText(short = true) + " — " +
                        lastDate.month.displayText(short = true)
                }
            }
        }
    }
}

fun DayOfWeek.displayText(uppercase: Boolean = true): String {
    val locale = Locale.getDefault()
    return getDisplayName(TextStyle.SHORT, locale).let { value ->
        if (uppercase) value.uppercase(locale) else value
    }
}

fun Month.displayText(short: Boolean = true): String {
    val locale = Locale.getDefault()
    val displayName = getDisplayName(TextStyle.FULL_STANDALONE, locale)
    return displayName.replaceFirstChar { if (it.isLowerCase()) it.titlecase(locale) else it.toString() }
}

internal fun Context.getColorCompat(@ColorRes color: Int) = ContextCompat.getColor(this, color)

internal fun TextView.setTextColorRes(@ColorRes color: Int) =
    setTextColor(context.getColorCompat(color))
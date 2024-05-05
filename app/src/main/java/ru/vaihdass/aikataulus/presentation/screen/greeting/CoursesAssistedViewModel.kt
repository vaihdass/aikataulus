package ru.vaihdass.aikataulus.presentation.screen.greeting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import ru.vaihdass.aikataulus.R
import ru.vaihdass.aikataulus.data.ExceptionHandlerDelegate
import ru.vaihdass.aikataulus.data.runCatching
import ru.vaihdass.aikataulus.domain.usecase.GreetingUseCase
import ru.vaihdass.aikataulus.presentation.base.BaseViewModel
import ru.vaihdass.aikataulus.presentation.model.CalendarUiModel
import ru.vaihdass.aikataulus.presentation.model.CourseUiModel
import ru.vaihdass.aikataulus.presentation.model.OrganizationUiModel
import ru.vaihdass.aikataulus.presentation.recyclerview.adapter.CoursesAdapter
import ru.vaihdass.aikataulus.utils.ResManager

class CoursesAssistedViewModel @AssistedInject constructor(
    @Assisted(value = ORGANIZATION_ASSISTED_KEY) private val organization: OrganizationUiModel,
    private val greetingUseCase: GreetingUseCase,
    private val resManager: ResManager,
    private val exceptionHandlerDelegate: ExceptionHandlerDelegate,
) : BaseViewModel() {
    private val EMPTY_CALENDAR = CalendarUiModel(
        id = -1,
        url = "",
        name = resManager.getString(R.string.choose_course_nothing),
        courseId = -1
    )

    private val _calendarsFlow =
        MutableStateFlow<List<Pair<CourseUiModel, List<CalendarUiModel>>>>(emptyList())
    private val _errorFlow = MutableSharedFlow<String?>(1)
    private val _saveCalendarsSuccessEventChannel = Channel<Unit>(Channel.BUFFERED)
    private val selectedCourses: MutableMap<Int, Pair<CourseUiModel, CalendarUiModel>> =
        mutableMapOf()

    private var _coursesAdapter: CoursesAdapter? = null

    val calendarsFlow
        get() = _calendarsFlow.asStateFlow()

    val errorFlow
        get() = _errorFlow.asSharedFlow()

    val saveCalendarsSuccessFlow
        get() = _saveCalendarsSuccessEventChannel.receiveAsFlow()

    val coursesAdapter
        get() = _coursesAdapter
            ?: throw IllegalStateException("Courses adapter should be initialized")

    fun initAdapter(onCalendarClicked: (CourseUiModel, CalendarUiModel) -> Unit) {
        if (_coursesAdapter != null) return

        _coursesAdapter = CoursesAdapter(onItemClicked = onCalendarClicked)
    }

    init {
        fetchCalendars()
    }

    fun choseCalendars() = greetingUseCase.choseCalendars()

    fun fetchCalendars() {
        viewModelScope.launch {
            runCatching(exceptionHandlerDelegate) {
                val calendars = greetingUseCase.getCalendarsGroupingByCourse(organization.id)

                val updatedCalendars = calendars.map { (course, calendar) ->
                    course to listOf(EMPTY_CALENDAR) + calendar
                }

                return@runCatching updatedCalendars
            }.onSuccess { calendars ->
                _calendarsFlow.value = calendars
            }.onFailure { throwable ->
                _errorFlow.emit(throwable.message)
            }
        }
    }

    fun saveSelectedCalendar(course: CourseUiModel, calendar: CalendarUiModel) {
        if (calendar == EMPTY_CALENDAR) {
            selectedCourses.remove(course.id)
            return
        }

        selectedCourses[course.id] = Pair(course, calendar)
    }

    fun clearCalendarsSelected() {
        selectedCourses.clear()
    }

    fun isCalendarsSelected() = selectedCourses.isEmpty()

    fun saveCalendars() {
        viewModelScope.launch {
            runCatching(exceptionHandlerDelegate) {
                val courses = selectedCourses.values.map { it.first }
                val calendars = selectedCourses.values.map { it.second }

                greetingUseCase.saveAikataulusSettings(
                    organization = organization,
                    courses = courses,
                    calendars = calendars,
                )
            }.onSuccess {
                _saveCalendarsSuccessEventChannel.send(Unit)
            }.onFailure { throwable ->
                _errorFlow.emit(throwable.message)
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted(ORGANIZATION_ASSISTED_KEY) organization: OrganizationUiModel,
        ): CoursesAssistedViewModel
    }

    companion object {
        const val ORGANIZATION_ASSISTED_KEY = "ORGANIZATION_ASSISTED_KEY"

        @Suppress("UNCHECKED_CAST")
        fun provideFactory(
            assistedFactory: Factory,
            organization: OrganizationUiModel,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(organization) as T
            }
        }
    }
}
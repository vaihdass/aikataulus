package ru.vaihdass.aikataulus.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.vaihdass.aikataulus.domain.mapper.toDomainModel
import ru.vaihdass.aikataulus.domain.mapper.toDomainModelList
import ru.vaihdass.aikataulus.domain.mapper.toUiModelList
import ru.vaihdass.aikataulus.domain.repository.AikataulusRepository
import ru.vaihdass.aikataulus.presentation.model.CalendarUiModel
import ru.vaihdass.aikataulus.presentation.model.CourseUiModel
import ru.vaihdass.aikataulus.presentation.model.OrganizationUiModel
import javax.inject.Inject

class GreetingUseCase @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val repository: AikataulusRepository,
) {
    fun choseCalendars() = repository.choseCalendars()

    suspend fun getOrganizations(): List<OrganizationUiModel> {
        return withContext(dispatcher) {
            repository.getAllOrganizations().toUiModelList()
        }
    }

    suspend fun getCalendarsGroupingByCourse(organizationId: Int): List<Pair<CourseUiModel, List<CalendarUiModel>>> {
        return withContext(dispatcher) {
            val courses = repository.getCoursesByOrganizationId(organizationId)
                .toUiModelList()
            val calendars = repository.getCalendarsByOrganizationId(organizationId)
                .toUiModelList()
                .groupBy { it.courseId }

            courses.map { course ->
                course to (calendars[course.id] ?: emptyList())
            }
        }
    }

    suspend fun saveAikataulusSettings(
        organization: OrganizationUiModel,
        courses: List<CourseUiModel>,
        calendars: List<CalendarUiModel>,
    ) {
        withContext(dispatcher) {
            repository.clearEntities()
            saveOrganization(organization)
            saveCourses(courses)
            saveCalendars(calendars)
            repository.setChoseCalendars(true)
        }
    }

    private suspend fun saveOrganization(organization: OrganizationUiModel) {
        repository.saveOrganization(organization.toDomainModel())
    }

    private suspend fun saveCourses(courses: List<CourseUiModel>) {
        repository.saveCourses(courses.toDomainModelList())
    }

    private suspend fun saveCalendars(calendars: List<CalendarUiModel>) {
        repository.saveCalendars(calendars.toDomainModelList())
    }
}
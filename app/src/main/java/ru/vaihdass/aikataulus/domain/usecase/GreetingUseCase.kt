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
    suspend fun clearEntities() {
        return withContext(dispatcher) {
            repository.clearEntities()
        }
    }

    suspend fun getOrganizations(): List<OrganizationUiModel> {
        return withContext(dispatcher) {
            repository.getAllOrganizations().toUiModelList()
        }
    }

    suspend fun getCourses(organizationId: Int): List<CourseUiModel> {
        return withContext(dispatcher) {
            repository.getCoursesByOrganizationId(organizationId).toUiModelList()
        }
    }

    suspend fun getCalendars(organizationId: Int): List<CalendarUiModel> {
        return withContext(dispatcher) {
            repository.getCalendarsByOrganizationId(organizationId).toUiModelList()
        }
    }

    suspend fun saveOrganization(organization: OrganizationUiModel) {
        return withContext(dispatcher) {
            repository.saveOrganization(organization.toDomainModel())
        }
    }

    suspend fun saveCourses(courses: List<CourseUiModel>) {
        return withContext(dispatcher) {
            repository.saveCourses(courses.toDomainModelList())
        }
    }

    suspend fun saveCalendars(calendars: List<CalendarUiModel>) {
        return withContext(dispatcher) {
            repository.saveCalendars(calendars.toDomainModelList())
        }
    }
}
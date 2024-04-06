package ru.vaihdass.aikataulus.domain.repository

import ru.vaihdass.aikataulus.domain.model.CalendarDomainModel
import ru.vaihdass.aikataulus.domain.model.CourseDomainModel
import ru.vaihdass.aikataulus.domain.model.OrganizationDomainModel

interface AikataulusRepository {
    suspend fun getAllOrganizations(): List<OrganizationDomainModel>
    suspend fun getCoursesByOrganizationId(organizationId: Int): List<CourseDomainModel>
    suspend fun getCalendarsByOrganizationId(organizationId: Int): List<CalendarDomainModel>

    suspend fun saveOrganization(organization: OrganizationDomainModel)
    suspend fun saveCourses(courses: List<CourseDomainModel>)
    suspend fun saveCalendars(calendars: List<CalendarDomainModel>)
    suspend fun clearEntities()
    suspend fun hasOrganizations(): Boolean
    fun choseCalendars(): Boolean
    fun setChoseCalendars(value: Boolean)
}
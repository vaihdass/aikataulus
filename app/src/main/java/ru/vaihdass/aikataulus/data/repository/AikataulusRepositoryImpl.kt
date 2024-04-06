package ru.vaihdass.aikataulus.data.repository

import ru.vaihdass.aikataulus.base.Constants.PREF_IS_CALENDARS_SELECTED
import ru.vaihdass.aikataulus.data.local.db.dao.CalendarDao
import ru.vaihdass.aikataulus.data.local.db.dao.CourseDao
import ru.vaihdass.aikataulus.data.local.db.dao.OrganizationDao
import ru.vaihdass.aikataulus.data.local.pref.SharedPreferencesManager
import ru.vaihdass.aikataulus.data.mapper.CalendarMapper
import ru.vaihdass.aikataulus.data.mapper.CourseMapper
import ru.vaihdass.aikataulus.data.mapper.OrganizationMapper
import ru.vaihdass.aikataulus.data.remote.api.AikataulusApi
import ru.vaihdass.aikataulus.domain.model.CalendarDomainModel
import ru.vaihdass.aikataulus.domain.model.CourseDomainModel
import ru.vaihdass.aikataulus.domain.model.OrganizationDomainModel
import ru.vaihdass.aikataulus.domain.repository.AikataulusRepository
import javax.inject.Inject

class AikataulusRepositoryImpl @Inject constructor(
    private val aikataulusApi: AikataulusApi,
    private val organizationDao: OrganizationDao,
    private val courseDao: CourseDao,
    private val calendarDao: CalendarDao,
    private val organizationMapper: OrganizationMapper,
    private val courseMapper: CourseMapper,
    private val calendarMapper: CalendarMapper,
    private val preferencesManager: SharedPreferencesManager,
) : AikataulusRepository {
    override suspend fun getAllOrganizations(): List<OrganizationDomainModel> {
        return organizationMapper.map(aikataulusApi.getAllOrganizations())
            ?: throw RuntimeException()
    }

    override suspend fun getCoursesByOrganizationId(organizationId: Int): List<CourseDomainModel> {
        if (organizationId <= 0) throw IllegalArgumentException("Incorrect organization id")

        return courseMapper.map(aikataulusApi.getCoursesByOrganizationId(organizationId))
            ?: throw RuntimeException()
    }

    override suspend fun getCalendarsByOrganizationId(organizationId: Int): List<CalendarDomainModel> {
        if (organizationId <= 0) throw IllegalArgumentException("Incorrect organization id")

        return calendarMapper.map(aikataulusApi.getCalendarsByOrganizationId(organizationId))
            ?: throw RuntimeException()
    }

    override suspend fun saveOrganization(organization: OrganizationDomainModel) {
        organizationDao.insert(organizationMapper.mapToDbEntity(organization))
    }

    override suspend fun saveCourses(courses: List<CourseDomainModel>) {
        courseDao.updateOrInsertAll(courseMapper.mapDomainToDbEntities(courses))
    }

    override suspend fun saveCalendars(calendars: List<CalendarDomainModel>) {
        calendarDao.updateOrInsertAll(calendarMapper.mapDomainToDbEntities(calendars))
    }

    override suspend fun clearEntities() {
        organizationDao.deleteAll()
        courseDao.deleteAll()
        calendarDao.deleteAll()
    }

    override suspend fun hasOrganizations() = organizationDao.hasAny()

    override fun choseCalendars(): Boolean {
        return preferencesManager.getBoolean(PREF_IS_CALENDARS_SELECTED, false)
    }

    override fun setChoseCalendars(value: Boolean) {
        preferencesManager.putBoolean(PREF_IS_CALENDARS_SELECTED, value)
    }
}
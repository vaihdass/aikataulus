package ru.vaihdass.aikataulus.data.remote.api

import retrofit2.http.GET
import retrofit2.http.Query
import ru.vaihdass.aikataulus.data.remote.pojo.aikataulus.Calendar
import ru.vaihdass.aikataulus.data.remote.pojo.aikataulus.Course
import ru.vaihdass.aikataulus.data.remote.pojo.aikataulus.Event
import ru.vaihdass.aikataulus.data.remote.pojo.aikataulus.Organization

interface AikataulusApi {
    @GET("api/v1/events/today")
    suspend fun getTodayEvents(
        @Query(value = "calendars") calendarIds: List<Int>,
    ): List<Event>?

    @GET("api/v1/events/all")
    suspend fun getAllEvents(
        @Query(value = "calendars") calendarIds: List<Int>,
    ): List<Event>?

    @GET("api/v1/events/peek")
    suspend fun peekEvents(
        @Query("calendars") calendarIds: List<Int>,
        @Query("from") fromTimestamp: Long,
        @Query("to") toTimestamp: Long,
    ): List<Event>?

    @GET("api/v1/organization/all")
    suspend fun getAllOrganizations(): List<Organization>?

    @GET("api/v1/course/organization")
    suspend fun getCoursesByOrganizationId(@Query("organization_id") organizationId: Int): List<Course>?

    @GET("api/v1/calendar/organization")
    suspend fun getCalendarsByOrganizationId(@Query("organization_id") organizationId: Int): List<Calendar>?
}
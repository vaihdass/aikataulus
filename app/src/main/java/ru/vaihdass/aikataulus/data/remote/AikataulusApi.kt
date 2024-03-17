package ru.vaihdass.aikataulus.data.remote

import retrofit2.http.GET
import retrofit2.http.Query
import ru.vaihdass.aikataulus.data.remote.pojo.EventsResponse

interface AikataulusApi {
    @GET("api/v1/events/today")
    suspend fun getTodayEvents(
        @Query(value = "calendars") calendarIds: List<Int>,
    ): EventsResponse?

    @GET("api/v1/events/all")
    suspend fun getAllEvents(
        @Query(value = "calendars") calendarIds: List<Int>,
    ): EventsResponse?

    @GET("api/v1/events/peek")
    suspend fun getEvents(
        @Query(value = "calendars") calendarIds: List<Int>,
        @Query(value = "from") from: Long,
        @Query(value = "to") to: Long,
    ): EventsResponse?
}
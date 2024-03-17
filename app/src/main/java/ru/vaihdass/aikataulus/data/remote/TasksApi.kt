package ru.vaihdass.aikataulus.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import ru.vaihdass.aikataulus.data.remote.pojo.Task
import ru.vaihdass.aikataulus.data.remote.pojo.TasksResponse

interface TasksApi {
    @GET("api/v1/tasks/today")
    suspend fun getTodayTasks(): TasksResponse

    @GET("api/v1/tasks/all")
    suspend fun getAllTasks(): TasksResponse

    @POST("api/v1/tasks/create")
    suspend fun createTask(
        @Body task: Task,
    ): Task?

    @PATCH("api/v1/tasks/update")
    suspend fun updateTask(
        @Body task: Task,
    ): Task?
}
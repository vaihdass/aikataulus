package ru.vaihdass.aikataulus.data.remote.api

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import ru.vaihdass.aikataulus.data.remote.pojo.tasks.EmptyOrError
import ru.vaihdass.aikataulus.data.remote.pojo.tasks.ListResponseTask
import ru.vaihdass.aikataulus.data.remote.pojo.tasks.ListResponseTasksList
import ru.vaihdass.aikataulus.data.remote.pojo.tasks.Task
import ru.vaihdass.aikataulus.data.remote.pojo.tasks.TaskList

interface TasksApi {
    @GET("tasks/v1/users/@me/lists")
    suspend fun getAllTaskLists(): ListResponseTasksList

    @DELETE("tasks/v1/users/@me/lists/{taskList}")
    suspend fun removeTaskList(@Path("taskList") taskListId: String): EmptyOrError

    @POST("tasks/v1/users/@me/lists")
    suspend fun insertTaskList(@Body taskList: TaskList): TaskList?

    @POST("tasks/v1/lists/{taskList}/clear")
    suspend fun removeDoneFromTaskList(@Path("taskList") taskListId: String): EmptyOrError

    @POST("tasks/v1/lists/{taskList}/tasks")
    suspend fun insertTask(
        @Path("taskList") taskListId: String,
        @Body task: Task
    ): Task

    @POST("tasks/v1/lists/{taskList}/tasks/{task}")
    suspend fun removeTask(
        @Path("taskList") taskListId: String,
        @Path("task") taskId: String
    ): EmptyOrError

    @PATCH("tasks/v1/lists/{tasklist}/tasks/{task}")
    suspend fun patchTask(
        @Path("taskList") taskListId: String,
        @Path("task") taskId: String,
        @Body task: Task,
    ): Task?

    @GET("tasks/v1/lists/{tasklist}/tasks")
    suspend fun getTasks(
        @Path("taskList") taskListId: String,
        @Query("showCompleted") showCompleted: Boolean = true,
        @Query("showHidden") showHidden: Boolean = true,
        @Query("dueMin") from: String?, // RFC 3339 timestamp -> 2024-01-13T00:00:00.000Z
        @Query("dueMax") to: String?, // RFC 3339 timestamp -> yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
    ): ListResponseTask?

    @GET("tasks/v1/lists/{tasklist}/tasks/{task}")
    suspend fun getTask(
        @Path("taskList") taskListId: String,
        @Path("task") taskId: String,
    ): Task?
}
package ru.vaihdass.aikataulus.data.remote.pojo.tasks

import com.google.gson.annotations.SerializedName
import ru.vaihdass.aikataulus.data.remote.util.TaskStatus
import java.util.Date

data class Task (
    var id: String,
    @SerializedName("title") var subject: String,
    @SerializedName("notes") var task: String?,
    @SerializedName("due") var deadline: Date? = null,
    var status: TaskStatus,
)
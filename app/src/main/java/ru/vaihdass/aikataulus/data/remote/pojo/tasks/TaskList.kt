package ru.vaihdass.aikataulus.data.remote.pojo.tasks

import com.google.gson.annotations.SerializedName
import java.util.Date

data class TaskList (
    var id: String = "",
    @SerializedName("title") var name: String,
    var updated: Date? = null,
)
package ru.vaihdass.aikataulus.domain.model

import ru.vaihdass.aikataulus.data.remote.pojo.TaskStatus
import java.util.Date

data class TaskDomainModel(
    val id: String,
    val subject: String,
    val task: String,
    val description: String,
    val deadline: Date? = null,
    val status: TaskStatus,
)
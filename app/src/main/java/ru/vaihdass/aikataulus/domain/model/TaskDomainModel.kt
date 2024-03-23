package ru.vaihdass.aikataulus.domain.model

import java.util.Date

data class TaskDomainModel(
    var id: String,
    var subject: String,
    var task: String,
    var deadline: Date? = null,
    var isDone: Boolean = false,
)
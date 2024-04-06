package ru.vaihdass.aikataulus.domain.model

import java.util.Date

data class TaskListDomainModel(
    var id: String = "",
    var name: String,
    var updated: Date? = null,
)
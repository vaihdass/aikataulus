package ru.vaihdass.aikataulus.presentation.model

import java.util.Date

data class TaskUiModel (
    var id: String,
    var subject: String,
    var task: String,
    var deadline: Date? = null,
    var isDone: Boolean = false,
)
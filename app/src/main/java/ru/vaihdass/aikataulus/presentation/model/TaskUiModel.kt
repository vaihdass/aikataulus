package ru.vaihdass.aikataulus.presentation.model

import java.util.Date

data class TaskUiModel (
    var id: String,
    var subject: String,
    var task: String,
    var deadline: Date? = null,
    var isDone: Boolean = false,
) {
    override fun toString(): String {
        val deadlineSubstring = if (deadline != null) "(due to ${deadline.toString()}) " else ""
        return "${if (isDone) "Done" else "Not completed"} ${subject.trim()} $deadlineSubstring— $task"
    }
}
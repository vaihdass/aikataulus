package ru.vaihdass.aikataulus.data.remote.pojo

import java.util.Date

class Task (
    val id: String,
    val subject: String,
    val task: String,
    val description: String,
    val deadline: Date? = null,
    val status: TaskStatus,
)
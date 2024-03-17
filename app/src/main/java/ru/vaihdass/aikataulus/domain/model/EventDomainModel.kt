package ru.vaihdass.aikataulus.domain.model

import java.util.Date

data class EventDomainModel(
    val subject: String,
    val location: String? = null,
    val type: String? = null,
    val teacher: String? = null,
    val calendarId: Int,
    val calendarName: String,
    val dateFrom: Date,
    val dateTo: Date,
)
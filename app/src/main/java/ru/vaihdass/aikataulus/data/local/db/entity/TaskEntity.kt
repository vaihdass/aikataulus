package ru.vaihdass.aikataulus.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey val id: String,
    var subject: String,
    var task: String,
    var deadline: Date? = null,
    var isDone: Boolean = false,
    var saved: Boolean = true,
    var deleted: Boolean = false,
    var createdOffline: Boolean = false,
)

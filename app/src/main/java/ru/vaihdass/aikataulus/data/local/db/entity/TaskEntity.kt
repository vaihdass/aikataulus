package ru.vaihdass.aikataulus.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import ru.vaihdass.aikataulus.data.local.db.entity.converter.DateConverter
import ru.vaihdass.aikataulus.data.remote.pojo.TaskStatus
import java.util.Date

@TypeConverters(DateConverter::class)
@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey val id: String,
    val subject: String,
    val task: String,
    val description: String,
    val deadline: Date? = null,
    val status: TaskStatus,
    val saved: Boolean,
)

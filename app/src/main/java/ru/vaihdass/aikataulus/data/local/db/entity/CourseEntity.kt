package ru.vaihdass.aikataulus.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "courses")
data class CourseEntity(
    @PrimaryKey val id: Int,
    var name: String,
    val organizationId: Int,
)
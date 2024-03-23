package ru.vaihdass.aikataulus.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "organizations")
data class OrganizationEntity (
    @PrimaryKey val id: Int,
    var name: String,
)
package ru.vaihdass.aikataulus.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.vaihdass.aikataulus.data.local.db.entity.OrganizationEntity

@Dao
interface OrganizationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(organization: OrganizationEntity)

    @Query("SELECT * FROM organizations LIMIT 1")
    fun getAny(): OrganizationEntity?

    @Query("SELECT * FROM organizations")
    fun getAll(): List<OrganizationEntity>

    @Query("DELETE FROM organizations")
    fun deleteAll()
}
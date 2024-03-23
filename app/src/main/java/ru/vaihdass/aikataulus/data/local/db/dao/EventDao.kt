package ru.vaihdass.aikataulus.data.local.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.vaihdass.aikataulus.data.local.db.entity.EventEntity

@Dao
interface EventDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun add(event: EventEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun update(event: EventEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateAll(event: List<EventEntity>)

    @Delete
    fun delete(event: EventEntity)

    @Delete
    fun delete(event: List<EventEntity>)

    @Query("DELETE FROM events WHERE date_from >= :from AND date_to <= :to")
    suspend fun deleteAllFromTo(from: Long, to: Long)

    @Query("DELETE FROM events")
    fun deleteAll()

    @Query("SELECT * FROM events")
    fun getAll(): List<EventEntity>

    @Query("SELECT * FROM events WHERE date_from >= :from AND date_to <= :to")
    suspend fun getAllFromTo(from: Long, to: Long): List<EventEntity>
}
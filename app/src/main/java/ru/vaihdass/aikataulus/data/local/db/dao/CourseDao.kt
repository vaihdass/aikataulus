package ru.vaihdass.aikataulus.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.vaihdass.aikataulus.data.local.db.entity.CourseEntity

@Dao
interface CourseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateOrInsertAll(entities: List<CourseEntity>)

    @Query("DELETE FROM courses WHERE id = :id")
    fun deleteById(id: Int)

    @Query("DELETE FROM courses")
    fun deleteAll()

    @Query("SELECT * FROM courses")
    fun getAll(): List<CourseEntity>

    @Query("SELECT id FROM courses")
    fun getAllId(): List<Int>
}
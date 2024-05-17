package ru.vaihdass.aikataulus.data.local.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.vaihdass.aikataulus.base.Constants
import ru.vaihdass.aikataulus.data.local.db.AikataulusDatabase
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideAikataulusDatabase(ctx: Context): AikataulusDatabase {
        return Room.databaseBuilder(ctx, AikataulusDatabase::class.java, Constants.DB_FILENAME)
            .addMigrations(
                // TODO: Add migrations if needed
            )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideEventDao(db: AikataulusDatabase) = db.eventDao

    @Provides
    fun provideTaskDao(db: AikataulusDatabase) = db.taskDao

    @Provides
    fun provideOrganizationDao(db: AikataulusDatabase) = db.organizationDao

    @Provides
    fun provideCourseDao(db: AikataulusDatabase) = db.courseDao

    @Provides
    fun provideCalendarDao(db: AikataulusDatabase) = db.calendarDao
}
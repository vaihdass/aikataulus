package ru.vaihdass.aikataulus.data.local

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
            .build()
    }

    /*
    TODO: Provide dao for tasks and events
    @Provides
    fun provideSomeDao(db: AikataulusDatabase) = db.someDao*/
}
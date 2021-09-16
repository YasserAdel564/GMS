package com.gms.app.di

import android.content.Context
import androidx.room.Room
import com.gms.app.data.storage.local.db.AppDB
import com.gms.app.data.storage.local.db.AppDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton


@InstallIn(ApplicationComponent::class)
@Module
object DatabaseModule {
    @Provides
    fun provideChannelDao(appDatabase: AppDB): AppDao {
        return appDatabase.dao()
    }
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDB {
        return Room.databaseBuilder(
            appContext,
            AppDB::class.java,
            "GMSDB"
        ).allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }
}
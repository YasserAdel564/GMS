package com.gms.app.data.storage.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import com.gms.app.data.storage.remote.model.auth.CountryModel
import com.gms.app.data.storage.remote.model.auth.GenderModel
import com.gms.app.data.storage.remote.model.auth.NationalityModel
import com.gms.app.di.DatabaseModule.provideAppDatabase

@Database(
    entities = [GenderModel::class ,CountryModel::class ,NationalityModel::class],
    version = 1,
    exportSchema = false
)
abstract class AppDB : RoomDatabase() {
    abstract fun dao(): AppDao

    companion object {
        @Volatile
        private var instance: AppDB? = null
        private val LOCK = Any()
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: provideAppDatabase(context).also { instance = it }
        }
    }
}
package com.dmitryy.notesapp

import android.app.Application
import androidx.room.Room
import com.dmitryy.notesapp.data.AppDatabase

class NotesApplication : Application() {
    lateinit var database: AppDatabase

    override fun onCreate() {
        super.onCreate()
        val MIGRATION_1_2 = object : androidx.room.migration.Migration(1, 2) {
            override fun migrate(database: androidx.sqlite.db.SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE notes ADD COLUMN isDeleted INTEGER NOT NULL DEFAULT 0")
            }
        }

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "notes-database"
        )
        .addMigrations(MIGRATION_1_2)
        .build()
    }
}

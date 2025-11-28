package com.dmitryy.notesapp

import android.app.Application
import androidx.room.Room
import com.dmitryy.notesapp.data.AppDatabase

class NotesApplication : Application() {
    lateinit var database: AppDatabase

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "notes-database"
        ).build()
    }
}

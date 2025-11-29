package com.dmitryy.notesapp.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Note::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}

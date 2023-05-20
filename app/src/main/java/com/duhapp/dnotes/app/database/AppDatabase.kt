package com.duhapp.dnotes.app.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CategoryEntity::class, NoteEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao

    abstract fun noteDao(): NoteDao
}

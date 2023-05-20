package com.duhapp.dnotes.app.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Upsert

@Dao
interface NoteDao {

    @Insert
    suspend fun insert(noteEntity: NoteEntity): Long

    @Upsert
    suspend fun update(noteEntity: NoteEntity)
}

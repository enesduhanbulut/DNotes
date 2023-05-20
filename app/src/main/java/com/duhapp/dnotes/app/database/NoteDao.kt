package com.duhapp.dnotes.app.database

import androidx.room.Dao
import androidx.room.Upsert

@Dao
interface NoteDao {

    @Upsert
    suspend fun upsert(noteEntity: NoteEntity): Long
}

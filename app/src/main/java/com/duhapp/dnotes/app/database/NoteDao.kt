package com.duhapp.dnotes.app.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert

@Dao
interface NoteDao {
    @Insert
    suspend fun insert(noteEntity: NoteEntity): Long

    @Upsert
    suspend fun update(noteEntity: NoteEntity)

    @Query("SELECT * FROM NoteEntity WHERE category_id = :categoryId")
    suspend fun getNoteByCategoryId(categoryId: Int): List<NoteEntity>

    @Query("DELETE FROM NoteEntity WHERE id IN (:notes)")
    suspend fun deleteNotes(notes: List<Int>)

    @Update
    fun updateNotes(map: List<NoteEntity>)
}

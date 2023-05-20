package com.duhapp.dnotes.app.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo("title") val title: String,
    @ColumnInfo(name = "details") val details: String,
    @ColumnInfo(name = "category_id") val categoryId: Int,
)

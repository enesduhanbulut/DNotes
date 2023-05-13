package com.duhapp.dnotes.app.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CategoryEntity(
    @ColumnInfo(name = "title") val name: String,
    @ColumnInfo(name = "message") val message: String,
    @ColumnInfo(name = "emoji") val iconId: String,
    @ColumnInfo(name = "color_id") val colorId: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

}
package com.duhapp.dnotes.app.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("SELECT * FROM CategoryEntity")
    fun getAll(): Flow<List<CategoryEntity>>

    @Insert
    suspend fun insert(categoryEntity: CategoryEntity): Long

    @Update
    suspend fun update(categoryEntity: CategoryEntity)

    @Delete
    suspend fun delete(categoryEntity: CategoryEntity)

    @Query("DELETE FROM CategoryEntity WHERE id = :id")
    suspend fun deleteCategoryWithId(id: Int)

    @Update
    fun updateCategory(category: CategoryEntity)

    @Query("SELECT * FROM CategoryEntity")
    suspend fun getCategories(): List<CategoryEntity>

    @Query("SELECT * FROM CategoryEntity WHERE id = :id")
    suspend fun getById(id: Int): CategoryEntity?
}
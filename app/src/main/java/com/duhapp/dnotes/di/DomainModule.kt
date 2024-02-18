package com.duhapp.dnotes.di

import android.content.Context
import androidx.room.Room
import com.duhapp.dnotes.app.database.AppDatabase
import com.duhapp.dnotes.app.database.CategoryDao
import com.duhapp.dnotes.app.database.NoteDao
import com.duhapp.dnotes.features.add_or_update_category.data.CategoryRepository
import com.duhapp.dnotes.features.add_or_update_category.data.CategoryRepositoryImpl
import com.duhapp.dnotes.features.note.data.NoteRepository
import com.duhapp.dnotes.features.note.data.NoteRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DomainModule {
    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java, "app_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideCategoryRepository(categoryDao: CategoryDao, noteDao: NoteDao): CategoryRepository {
        return CategoryRepositoryImpl(categoryDao, noteDao, Dispatchers.IO)
    }

    @Provides
    @Singleton
    fun provideNoteRepository(noteDao: NoteDao): NoteRepository {
        return NoteRepositoryImpl(noteDao, Dispatchers.IO)
    }
}

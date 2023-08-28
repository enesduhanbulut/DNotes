package com.duhapp.dnotes.di

import com.duhapp.dnotes.app.database.CategoryDao
import com.duhapp.dnotes.app.database.NoteDao
import com.duhapp.dnotes.features.add_or_update_category.data.CategoryRepository
import com.duhapp.dnotes.features.add_or_update_category.data.CategoryRepositoryImpl
import com.duhapp.dnotes.features.note.data.NoteRepository
import com.duhapp.dnotes.features.note.data.NoteRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DomainModule {
    @Provides
    @Singleton
    fun provideCategoryRepository(categoryDao: CategoryDao, noteDao: NoteDao): CategoryRepository {
        return CategoryRepositoryImpl(categoryDao, noteDao, Dispatchers.IO)
    }

    @Provides
    fun provideNoteRepository(noteDao: NoteDao): NoteRepository {
        return NoteRepositoryImpl(noteDao, Dispatchers.IO)
    }
}

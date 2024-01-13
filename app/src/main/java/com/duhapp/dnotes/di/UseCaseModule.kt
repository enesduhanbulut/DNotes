package com.duhapp.dnotes.di

import com.duhapp.dnotes.features.add_or_update_category.data.CategoryRepository
import com.duhapp.dnotes.features.add_or_update_category.domain.DeleteCategory
import com.duhapp.dnotes.features.add_or_update_category.domain.FetchHomeData
import com.duhapp.dnotes.features.add_or_update_category.domain.UpsertCategory
import com.duhapp.dnotes.features.add_or_update_category.ui.CategoryUIModel
import com.duhapp.dnotes.features.all_notes.domain.DeleteNote
import com.duhapp.dnotes.features.all_notes.domain.GetNotesByCategoryId
import com.duhapp.dnotes.features.all_notes.domain.UpdateNotes
import com.duhapp.dnotes.features.manage_category.domain.CreateDefaultCategory
import com.duhapp.dnotes.features.manage_category.domain.GetCategories
import com.duhapp.dnotes.features.manage_category.domain.InsertCategory
import com.duhapp.dnotes.features.manage_category.domain.UndoCategory
import com.duhapp.dnotes.features.note.data.NoteRepository
import com.duhapp.dnotes.features.note.domain.GetDefaultCategory
import com.duhapp.dnotes.features.note.domain.UpsertNote
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
object UseCaseModule {
    @Provides
    fun provideDeleteCategory(
        categoryRepository: CategoryRepository,
        noteRepository: NoteRepository,
        defaultCategory: CategoryUIModel
    ): DeleteCategory {
        return DeleteCategory(categoryRepository, noteRepository, defaultCategory)
    }

    @Provides
    fun provideUndoCategory(
        categoryRepository: CategoryRepository
    ): UndoCategory {
        return UndoCategory(categoryRepository)
    }

    @Provides
    fun provideUpsertCategory(categoryRepository: CategoryRepository): UpsertCategory {
        return UpsertCategory(categoryRepository)
    }

    @Provides
    fun provideGetCategories(categoryRepository: CategoryRepository): GetCategories {
        return GetCategories(categoryRepository)
    }

    @Provides
    fun provideUpsertNote(noteRepository: NoteRepository): UpsertNote {
        return UpsertNote(noteRepository)
    }

    @Provides
    fun provideFetchHomeData(
        noteRepository: NoteRepository,
        categoryRepository: CategoryRepository
    ): FetchHomeData {
        return FetchHomeData(noteRepository, categoryRepository)
    }

    @Provides
    fun provideCreateDefaultCategory(
        categoryRepository: CategoryRepository,
        categoryUIModel: CategoryUIModel
    ): CreateDefaultCategory {
        return CreateDefaultCategory(categoryRepository, categoryUIModel)
    }

    @Provides
    fun provideInsertCategory(
        categoryRepository: CategoryRepository
    ): InsertCategory {
        return InsertCategory(categoryRepository)
    }

    @Provides
    fun provideGetDefaultCategory(
        categoryRepository: CategoryRepository,
        categoryUIModel: CategoryUIModel
    ): GetDefaultCategory {
        return GetDefaultCategory(categoryRepository, categoryUIModel)
    }

    @Provides
    fun provideGetNotesByCategoryId(
        noteRepository: NoteRepository,
        categoryRepository: CategoryRepository
    ): GetNotesByCategoryId {
        return GetNotesByCategoryId(noteRepository, categoryRepository)
    }

    @Provides
    fun provideDeleteNote(
        noteRepository: NoteRepository
    ): DeleteNote {
        return DeleteNote(noteRepository)
    }

    @Provides
    fun provideUpdateNotes(
        noteRepository: NoteRepository
    ): UpdateNotes {
        return UpdateNotes(noteRepository)
    }
}

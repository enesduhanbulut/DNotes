package com.duhapp.dnotes.di

import com.duhapp.dnotes.features.add_or_update_category.data.CategoryRepository
import com.duhapp.dnotes.features.add_or_update_category.domain.DeleteCategory
import com.duhapp.dnotes.features.add_or_update_category.domain.FetchHomeData
import com.duhapp.dnotes.features.add_or_update_category.domain.UpsertCategory
import com.duhapp.dnotes.features.add_or_update_category.ui.CategoryUIModel
import com.duhapp.dnotes.features.manage_category.domain.CreateDefaultCategory
import com.duhapp.dnotes.features.manage_category.domain.GetCategories
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
        categoryUIModel: CategoryUIModel
    ): DeleteCategory {
        return DeleteCategory(categoryRepository, categoryUIModel)
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
    fun provideGetDefaultCategory(
        categoryRepository: CategoryRepository,
        categoryUIModel: CategoryUIModel
    ): GetDefaultCategory {
        return GetDefaultCategory(categoryRepository, categoryUIModel)
    }
}

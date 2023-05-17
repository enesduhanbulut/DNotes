package com.duhapp.dnotes.di

import com.duhapp.dnotes.features.add_or_update_category.data.CategoryRepository
import com.duhapp.dnotes.features.add_or_update_category.domain.DeleteCategory
import com.duhapp.dnotes.features.add_or_update_category.domain.UpsertCategory
import com.duhapp.dnotes.features.select_category.domain.GetCategories
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
object UseCaseModule {
    @Provides
    fun provideDeleteCategory(categoryRepository: CategoryRepository): DeleteCategory {
        return DeleteCategory(categoryRepository)
    }

    @Provides
    fun provideUpsertCategory(categoryRepository: CategoryRepository): UpsertCategory {
        return UpsertCategory(categoryRepository)
    }

    @Provides
    fun provideGetCategories(categoryRepository: CategoryRepository): GetCategories {
        return GetCategories(categoryRepository)
    }

}
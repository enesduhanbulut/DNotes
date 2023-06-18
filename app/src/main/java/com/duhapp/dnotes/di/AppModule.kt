package com.duhapp.dnotes.di

import android.content.Context
import androidx.room.Room
import com.duhapp.dnotes.NoteColor
import com.duhapp.dnotes.R
import com.duhapp.dnotes.app.database.AppDatabase
import com.duhapp.dnotes.features.add_or_update_category.ui.CategoryUIModel
import com.duhapp.dnotes.features.add_or_update_category.ui.ColorItemUIModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java, "app_database"
        ).build()
    }

    @Singleton
    @Provides
    fun provideDefaultCategory(@ApplicationContext context: Context): CategoryUIModel {
        val defaultCategoryName = context.getString(R.string.default_category_name)
        val defaultCategoryEmoji = context.getString(R.string.default_category_emoji)
        val defaultCategoryDescription = context.getString(R.string.default_category_description)
        val defaultCategoryColor = NoteColor.BLUE
        return CategoryUIModel(
            id = 1,
            name = defaultCategoryName,
            emoji = defaultCategoryEmoji,
            description = defaultCategoryDescription,
            color = ColorItemUIModel(false, defaultCategoryColor),
            isDefault = true
        )
    }
}
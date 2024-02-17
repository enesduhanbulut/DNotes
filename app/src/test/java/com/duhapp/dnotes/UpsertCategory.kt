package com.duhapp.dnotes

import com.duhapp.dnotes.features.add_or_update_category.data.CategoryRepository
import com.duhapp.dnotes.features.add_or_update_category.domain.UpsertCategory
import com.duhapp.dnotes.features.add_or_update_category.ui.CategoryUIModel
import com.duhapp.dnotes.features.add_or_update_category.ui.ColorItemUIModel
import com.duhapp.dnotes.features.base.domain.CustomException
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock

class UpsertCategory {

    private val validCategoryModel = CategoryUIModel(
        id = -1,
        name = "name",
        emoji = "emoji",
        description = "description",
        color = ColorItemUIModel(
            color = NoteColor.BLUE,
        )
    )

    private val updatedCategoryModel = CategoryUIModel(
        id = 1,
        name = "name",
        emoji = "emoji",
        description = "description",
        color = ColorItemUIModel(
            color = NoteColor.BLUE,
        )
    )

    private val invalidCategoryModel = CategoryUIModel(
        id = -1,
        name = "",
        emoji = "emoji",
        description = "description",
        color = ColorItemUIModel(
            color = NoteColor.BLUE,
        )
    )


    @Test
    fun `insert category`() = runBlocking {
        val categoryRepository = mock(CategoryRepository::class.java)
        val upsertCategory = UpsertCategory(categoryRepository)
        runBlocking {
            upsertCategory.invoke(validCategoryModel)
        }
        Mockito.verify(categoryRepository).insert(validCategoryModel)
    }

    @Test
    fun `update category`() = runBlocking {
        val categoryRepository = mock(CategoryRepository::class.java)
        val upsertCategory = UpsertCategory(categoryRepository)
        runBlocking {
            upsertCategory.invoke(updatedCategoryModel)
        }
        Mockito.verify(categoryRepository).updateCategory(updatedCategoryModel)
    }

    @Test(expected = CustomException.InvalidInputException::class)
    fun `insert invalid category`() = runBlocking {
        val categoryRepository = mock(CategoryRepository::class.java)
        val upsertCategory = UpsertCategory(categoryRepository)
        runBlocking {
            upsertCategory.invoke(invalidCategoryModel)
        }
        Mockito.verify(categoryRepository).insert(invalidCategoryModel)
    }
}
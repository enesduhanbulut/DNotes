package com.duhapp.dnotes.features.add_or_update_category.data

import com.duhapp.dnotes.NoteColor
import com.duhapp.dnotes.app.database.CategoryDao
import com.duhapp.dnotes.app.database.CategoryEntity
import com.duhapp.dnotes.features.add_or_update_category.ui.CategoryUIModel
import com.duhapp.dnotes.features.add_or_update_category.ui.ColorItemUIModel
import com.duhapp.dnotes.features.base.data.BaseRepository
import kotlinx.coroutines.CoroutineDispatcher

class CategoryRepositoryImpl(
    private val dao: CategoryDao,
    dispatcher: CoroutineDispatcher
) : CategoryRepository, BaseRepository(dispatcher) {
    override suspend fun deleteCategory(categoryUIModel: CategoryUIModel) {
        runOnIO {
            dao.deleteCategoryWithId(categoryUIModel.id)
        }
    }

    override suspend fun insert(categoryUIModel: CategoryUIModel) {
        val category = CategoryEntity(
            name = categoryUIModel.name,
            message = categoryUIModel.description,
            emoji = categoryUIModel.emoji,
            colorId = categoryUIModel.color.color.ordinal,
            isDefault = categoryUIModel.isDefault
        )
        runOnIO { dao.insert(category) }
    }

    override suspend fun getById(id: Int): CategoryUIModel? = runOnIO {
        dao.getById(id)?.let { categoryEntity ->
            CategoryUIModel(
                id = categoryEntity.id,
                name = categoryEntity.name,
                emoji = categoryEntity.emoji,
                description = categoryEntity.message,
                color = ColorItemUIModel(
                    color = NoteColor.fromOrdinal(categoryEntity.colorId)
                ),
                isDefault = categoryEntity.isDefault
            )
        }
    }

    override suspend fun updateCategory(categoryUIModel: CategoryUIModel) {
        val category = CategoryEntity(
            name = categoryUIModel.name,
            message = categoryUIModel.description,
            emoji = categoryUIModel.emoji,
            colorId = categoryUIModel.color.color.ordinal,
            isDefault = categoryUIModel.isDefault
        )
        category.id = categoryUIModel.id
        runOnIO { dao.update(category) }
    }

    override suspend fun getCategories(): List<CategoryUIModel> = runOnIO {
        dao.getCategories().map { categoryEntity ->
            CategoryUIModel(
                id = categoryEntity.id,
                name = categoryEntity.name,
                emoji = categoryEntity.emoji,
                description = categoryEntity.message,
                color = ColorItemUIModel(
                    color = NoteColor.fromOrdinal(categoryEntity.colorId)
                ),
                isDefault = categoryEntity.isDefault
            )
        }
    }


}
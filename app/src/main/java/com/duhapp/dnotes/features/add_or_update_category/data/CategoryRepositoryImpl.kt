package com.duhapp.dnotes.features.add_or_update_category.data

import com.duhapp.dnotes.app.database.CategoryDao
import com.duhapp.dnotes.app.database.CategoryEntity
import com.duhapp.dnotes.features.add_or_update_category.ui.CategoryUIModel
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
            categoryUIModel.name,
            categoryUIModel.description,
            categoryUIModel.emoji,
            categoryUIModel.colorId
        )
        runOnIO { dao.insert(category) }
    }

    override suspend fun updateCategory(categoryUIModel: CategoryUIModel) {
        val category = CategoryEntity(
            categoryUIModel.name,
            categoryUIModel.description,
            categoryUIModel.emoji,
            categoryUIModel.colorId
        )
        category.id = categoryUIModel.id
        runOnIO { dao.update(category) }
    }

    override suspend fun getCategories(): List<CategoryUIModel> = runOnIO {
        dao.getCategories().map { categoryEntity ->
            CategoryUIModel(
                categoryEntity.id,
                categoryEntity.name,
                categoryEntity.emoji,
                categoryEntity.message,
                categoryEntity.colorId
            )
        }
    }


}
package com.duhapp.dnotes.features.add_or_update_category.data

import com.duhapp.dnotes.app.database.CategoryDao
import com.duhapp.dnotes.app.database.CategoryEntity
import com.duhapp.dnotes.features.add_or_update_category.ui.CategoryUIModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CategoryRepositoryImpl(
    private val dao: CategoryDao
) : CategoryRepository {
    override suspend fun deleteCategory(categoryUIModel: CategoryUIModel) {
        dao.deleteCategoryWithId(categoryUIModel.id)
    }

    override suspend fun insert(categoryUIModel: CategoryUIModel) {
        val category = CategoryEntity(
            categoryUIModel.name,
            categoryUIModel.description,
            categoryUIModel.emoji,
            categoryUIModel.colorId
        )
        dao.insert(category)
    }

    override suspend fun updateCategory(categoryUIModel: CategoryUIModel) {
        val category = CategoryEntity(
            categoryUIModel.name,
            categoryUIModel.description,
            categoryUIModel.emoji,
            categoryUIModel.colorId
        )
        category.id = categoryUIModel.id
        dao.update(category)
    }

    override fun getCategories(): Flow<List<CategoryUIModel>> {
        return dao.getCategories()
            .map {
                it.map { categoryEntity ->
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


}
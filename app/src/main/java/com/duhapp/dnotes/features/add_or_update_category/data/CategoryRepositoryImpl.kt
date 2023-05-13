package com.duhapp.dnotes.features.add_or_update_category.data

import com.duhapp.dnotes.app.database.CategoryDao
import com.duhapp.dnotes.app.database.CategoryEntity
import com.duhapp.dnotes.features.add_or_update_category.ui.CategoryUIModel

class CategoryRepositoryImpl(
    private val dao: CategoryDao
) : CategoryRepository {
    override suspend fun deleteCategory(categoryUIModel: CategoryUIModel) {
        dao.deleteCategoryWithId(categoryUIModel.id)
    }

    override suspend fun insert(categoryUIModel: CategoryUIModel) {
        val category = CategoryEntity(
            categoryUIModel.name,
            categoryUIModel.descripton,
            categoryUIModel.iconId,
            categoryUIModel.colorId
        )
        dao.insert(category)
    }

    override suspend fun updateCategory(categoryUIModel: CategoryUIModel) {
        val category = CategoryEntity(
            categoryUIModel.name,
            categoryUIModel.descripton,
            categoryUIModel.iconId,
            categoryUIModel.colorId
        )
        category.id = categoryUIModel.id
        dao.update(category)
    }


}
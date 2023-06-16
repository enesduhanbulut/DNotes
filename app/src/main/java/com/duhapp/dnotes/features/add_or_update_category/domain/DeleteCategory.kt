package com.duhapp.dnotes.features.add_or_update_category.domain

import com.duhapp.dnotes.features.add_or_update_category.data.CategoryRepository
import com.duhapp.dnotes.features.add_or_update_category.ui.CategoryUIModel
import java.lang.Exception

class DeleteCategory(
    private val categoryRepository: CategoryRepository,
    private val defaultCategoryModel: CategoryUIModel
) {
    suspend operator fun invoke(categoryUIModel: CategoryUIModel) {
        categoryRepository.getById(defaultCategoryModel.id).let { defaultCategory ->
            if (defaultCategory!!.id == categoryUIModel.id) {
                throw Exception("Cannot delete default category")
            } else {
                categoryRepository.deleteCategory(categoryUIModel)
            }
        }
    }
}
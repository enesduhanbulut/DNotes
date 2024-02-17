package com.duhapp.dnotes.features.add_or_update_category.domain

import com.duhapp.dnotes.R
import com.duhapp.dnotes.features.add_or_update_category.data.CategoryRepository
import com.duhapp.dnotes.features.add_or_update_category.ui.CategoryUIModel
import com.duhapp.dnotes.features.base.domain.CustomException
import com.duhapp.dnotes.features.base.domain.CustomExceptionData

class UpsertCategory(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(categoryUIModel: CategoryUIModel) {
        if (categoryUIModel.name.isEmpty()) {
            throw CustomException.InvalidInputException(
                CustomExceptionData(
                    title = R.string.Invalid_Input,
                    message = R.string.Category_Name_Cannot_Be_Empty,
                    code = 0
                )
            )
        }

        if (categoryUIModel.id == -1) {
            categoryRepository.insert(categoryUIModel)
        } else {
            categoryRepository.updateCategory(categoryUIModel)
        }
    }
}
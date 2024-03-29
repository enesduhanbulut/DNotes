package com.duhapp.dnotes.features.add_or_update_category.domain

import com.duhapp.dnotes.R
import com.duhapp.dnotes.features.add_or_update_category.data.CategoryRepository
import com.duhapp.dnotes.features.add_or_update_category.ui.CategoryUIModel
import com.duhapp.dnotes.features.base.domain.CustomException
import com.duhapp.dnotes.features.base.domain.CustomExceptionCode
import com.duhapp.dnotes.features.base.domain.CustomExceptionData


class UpsertCategory(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(categoryUIModel: CategoryUIModel) {
        if (categoryUIModel.name.isEmpty() && categoryUIModel.description.isEmpty()) {
            throw CustomException.NotValidParametersException(
                CustomExceptionData(
                    title = R.string.Not_Valid_Parameters,
                    message = R.string.Category_Name_And_Description_Could_Not_Be_Empty,
                    code = CustomExceptionCode.NOT_VALID_PARAMETERS_EXCEPTION.code
                )
            )
        }

        if (categoryUIModel.name.length > 180) {
            throw CustomException.NotValidParametersException(
                CustomExceptionData(
                    title = R.string.Not_Valid_Parameters,
                    message = R.string.Category_Name_Could_Not_Be_More_Than_20_Characters,
                    code = CustomExceptionCode.NOT_VALID_PARAMETERS_EXCEPTION.code
                )
            )
        }

        if (!categoryUIModel.emoji.isEmoji()) {
            categoryUIModel.emoji = ""
        }

        if (categoryUIModel.id == -1) {
            categoryRepository.insert(categoryUIModel)
        } else {
            categoryRepository.updateCategory(categoryUIModel)
        }
    }
}



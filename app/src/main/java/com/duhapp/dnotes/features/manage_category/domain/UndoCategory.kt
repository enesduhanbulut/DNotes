package com.duhapp.dnotes.features.manage_category.domain

import com.duhapp.dnotes.features.add_or_update_category.data.CategoryRepository

class UndoCategory(
    private val categoryRepository: CategoryRepository
) {
    suspend fun invoke() {
        categoryRepository.undo()
    }
}
package com.duhapp.dnotes.features.add_or_update_category.domain

import com.duhapp.dnotes.features.add_or_update_category.data.CategoryRepository
import com.duhapp.dnotes.features.home.home_screen_category.ui.HomeCategoryUIModel
import com.duhapp.dnotes.features.note.data.NoteRepository

class FetchHomeData(
    private val noteRepository: NoteRepository,
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(): List<HomeCategoryUIModel> {
        val homeCategoryUIModels: MutableList<HomeCategoryUIModel> = mutableListOf()
        categoryRepository.getCategories().forEach { category ->
            val notes = noteRepository.getNoteByCategory(category)
            if (notes.isNotEmpty()) {
                homeCategoryUIModels.add(
                    HomeCategoryUIModel(
                        id = category.id,
                        title = category.name,
                        noteList = notes
                    )
                )
            }
        }
        return homeCategoryUIModels
    }
}
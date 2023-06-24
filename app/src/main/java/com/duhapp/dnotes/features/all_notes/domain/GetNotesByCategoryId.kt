package com.duhapp.dnotes.features.all_notes.domain

import com.duhapp.dnotes.features.add_or_update_category.data.CategoryRepository
import com.duhapp.dnotes.features.home.home_screen_category.ui.BaseNoteUIModel
import com.duhapp.dnotes.features.note.data.NoteRepository

class GetNotesByCategoryId(
    private val noteRepository: NoteRepository, private val categoryRepository: CategoryRepository
) {
    suspend fun invoke(categoryId: Int): List<BaseNoteUIModel> {
        val category = categoryRepository.getById(categoryId)
        return noteRepository.getNoteByCategory(category ?: return emptyList())
    }
}
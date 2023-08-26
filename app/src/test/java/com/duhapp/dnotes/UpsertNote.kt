package com.duhapp.dnotes

import com.duhapp.dnotes.features.add_or_update_category.ui.CategoryUIModel
import com.duhapp.dnotes.features.add_or_update_category.ui.ColorItemUIModel
import com.duhapp.dnotes.features.base.domain.CustomException
import com.duhapp.dnotes.features.home.home_screen_category.ui.BaseNoteUIModel
import com.duhapp.dnotes.features.home.home_screen_category.ui.BasicNoteUIModel
import com.duhapp.dnotes.features.note.data.NoteRepository
import com.duhapp.dnotes.features.note.domain.UpsertNote
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock


class UpsertNote {

    private val validBasicNoteModel: BaseNoteUIModel = BasicNoteUIModel(
        id = -1,
        title = "title",
        body = "message",
        isCompletable = false,
        isCompleted = false,
        isPinned = false,
        category = CategoryUIModel(
            id = 1,
            name = "name",
            emoji = "emoji",
            description = "description",
            color = ColorItemUIModel(
                color = NoteColor.BLUE,
            )
        ),
        color = 1,
    )

    private val invalidTitledNoteModel: BaseNoteUIModel = BasicNoteUIModel(
        id = -1,
        title = "",
        body = "message",
        isCompletable = false,
        isCompleted = false,
        isPinned = false,
        category = CategoryUIModel(
            id = 1,
            name = "name",
            emoji = "emoji",
            description = "description",
            color = ColorItemUIModel(
                color = NoteColor.BLUE,
            )
        ),
        color = 1,
    )

    private val updateNoteModel = BasicNoteUIModel(
        id = 1,
        title = "title",
        body = "message",
        isCompletable = false,
        isCompleted = false,
        isPinned = false,
        category = CategoryUIModel(
            id = 1,
            name = "name",
            emoji = "emoji",
            description = "description",
            color = ColorItemUIModel(
                color = NoteColor.BLUE,
            )
        ),
        color = 1,
    )


    @Test
    fun inserting_valid_note() = runBlocking {
        val mockedNoteRepository: NoteRepository = mock(
            NoteRepository::class.java
        )
        val returnId = 5
        Mockito.`when`(mockedNoteRepository.insert(validBasicNoteModel)).thenReturn(returnId)
        val upsertNote = UpsertNote(
            mockedNoteRepository
        )
        val result = upsertNote.invoke(validBasicNoteModel)
        if (validBasicNoteModel.id == -1) {
            assert(result.id != validBasicNoteModel.id)
            assert(result.id == returnId)
        }
        else
            assert(result.id == validBasicNoteModel.id)
    }

    @Test
    fun inserting_invalid_note(): Unit = runBlocking {
        val mockedNoteRepository: NoteRepository = mock(
            NoteRepository::class.java
        )
        Mockito.`when`(mockedNoteRepository.insert(invalidTitledNoteModel)).thenReturn(5)
        val upsertNote = UpsertNote(
            mockedNoteRepository
        )
        try {
            upsertNote.invoke(invalidTitledNoteModel)
        } catch (e: CustomException.InvalidInputException) {
            assert(e.data.title == R.string.Invalid_Input)
            assert(e.data.message == R.string.Note_Title_Cannot_Be_Empty)
            assert(e.data.code == 0)
        }
    }

    @Test
    fun `update_note_if_its_id_is_not_-1`(): Unit = runBlocking {
        val mockedNoteRepository: NoteRepository = mock(
            NoteRepository::class.java
        )
        val upsertNote = UpsertNote(
            mockedNoteRepository
        )
        val result = upsertNote.invoke(updateNoteModel)
        assert(result.id == updateNoteModel.id)
    }
}
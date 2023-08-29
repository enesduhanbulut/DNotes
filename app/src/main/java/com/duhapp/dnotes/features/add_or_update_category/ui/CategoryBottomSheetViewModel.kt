package com.duhapp.dnotes.features.add_or_update_category.ui

import android.os.Parcelable
import android.util.Log
import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.viewModelScope
import com.duhapp.dnotes.NoteColor
import com.duhapp.dnotes.R
import com.duhapp.dnotes.features.add_or_update_category.domain.UpsertCategory
import com.duhapp.dnotes.features.base.domain.CustomException
import com.duhapp.dnotes.features.base.domain.CustomExceptionData
import com.duhapp.dnotes.features.base.domain.asCustomException
import com.duhapp.dnotes.features.base.ui.BottomSheetEvent
import com.duhapp.dnotes.features.base.ui.BottomSheetState
import com.duhapp.dnotes.features.base.ui.BottomSheetViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

@HiltViewModel
class CategoryBottomSheetViewModel @Inject constructor(
    private val upsertCategory: UpsertCategory
) :
    BottomSheetViewModel<CategoryUIEvent, CategoryBottomSheetUIState>() {
    val TAG = "CategoryBottomSheetViewModel"
    fun onPositiveButtonClicked() {
        withStateValue {
            viewModelScope.launch {
                try {
                    upsertCategory.invoke(it.categoryUIModel)
                } catch (e: Exception) {
                    setSuccessState(
                        it.copy(
                            error = e.asCustomException()
                        )
                    )
                }
            }
            it
        }
        setEvent(CategoryUIEvent.Upserted)
    }

    fun setViewWithBundle(categoryUIState: CategoryUIModel, categoryShowType: CategoryShowType) {
        setSuccessState(
            CategoryBottomSheetUIState(
                NoteColor.values().toList().map {
                    ColorItemUIModel(
                        isSelected = it == categoryUIState.color.color,
                        color = it
                    )
                }, categoryUIState, categoryShowType
            )
        )
    }

    fun onEmojiClicked() {
        setEvent(CategoryUIEvent.ShowEmojiDialog)
    }

    fun setEmoji(emoji: String) {
        setEvent(CategoryUIEvent.DismissedEmojiDialog)
        setSuccessState(
            withStateValue {
                it.apply {
                    it.categoryUIModel.emoji = emoji
                }
            }
        )
    }

    fun onDismissed() {
        setEvent(CategoryUIEvent.Dismissed)
    }

    fun onColorSelected(itemUIModel: ColorItemUIModel) {
        val list = uiState.value?.colors?.subList(0, uiState.value?.colors!!.size)
        list?.forEach {
            it.isSelected = it.color == itemUIModel.color
        }
        setSuccessState(
            withStateValue {
                it.apply {
                    it.colors = emptyList()
                    it.categoryUIModel.color = itemUIModel.apply {
                        isSelected = true
                    }
                }
            }
        )
        setSuccessState(
            withStateValue {
                it.apply { it.colors = list!! }
            }
        )
    }
}

data class CategoryBottomSheetUIState(
    var colors: List<ColorItemUIModel>,
    val categoryUIModel: CategoryUIModel,
    val categoryShowType: CategoryShowType,
    val error: CustomException? = null
) : BottomSheetState {
    @StringRes
    var title: Int = 0

    @StringRes
    var message: Int = 0

    @StringRes
    var positiveButtonText: Int = 0

    @StringRes
    var negativeButtonText: Int = 0
    var negativeButtonVisibility = View.VISIBLE

    init {
        when (categoryShowType) {
            CategoryShowType.Add -> {
                title = R.string.New_Category
                message = R.string.New_Category_Message
                positiveButtonText = R.string.Add
                negativeButtonText = R.string.blank
                negativeButtonVisibility = View.GONE
            }

            CategoryShowType.Edit -> {
                title = R.string.Edit_Category
                message = R.string.Edit_Category_Message
                positiveButtonText = R.string.Confirm
                negativeButtonText = R.string.Delete
            }
        }
    }

}

sealed interface CategoryUIEvent : BottomSheetEvent {
    data class ColorSelected(val list: List<ColorItemUIModel>) : CategoryUIEvent
    object Upserted : CategoryUIEvent
    object Canceled : CategoryUIEvent
    object Dismissed : CategoryUIEvent
    object ShowEmojiDialog : CategoryUIEvent
    object DismissedEmojiDialog : CategoryUIEvent
}

@Parcelize
enum class CategoryShowType : Parcelable {
    Add, Edit
}
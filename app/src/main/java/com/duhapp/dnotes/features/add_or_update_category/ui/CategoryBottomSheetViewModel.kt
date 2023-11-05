package com.duhapp.dnotes.features.add_or_update_category.ui

import android.os.Parcelable
import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.viewModelScope
import com.ahk.annotation.GenerateSealedGetters
import com.duhapp.dnotes.NoteColor
import com.duhapp.dnotes.R
import com.duhapp.dnotes.features.add_or_update_category.domain.UpsertCategory
import com.duhapp.dnotes.features.base.domain.CustomException
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
) : BottomSheetViewModel<CategoryUIEvent, CategoryBottomSheetUIState, CategoryBottomSheetUIStateFunctions>() {
    val TAG = "CategoryBottomSheetViewModel"

    fun onPositiveButtonClicked() {
        withStateValue {
            viewModelScope.launch {
                try {
                    upsertCategory.invoke(functions.getSuccessCategoryUIModel(uiState.value))
                } catch (e: Exception) {
                    setState(
                        CategoryBottomSheetUIState.Error(
                            e.asCustomException()
                        )
                    )
                }
            }
            it
        }
        setEvent(CategoryUIEvent.Upserted)
    }

    fun setViewWithBundle(categoryUIState: CategoryUIModel, categoryShowType: CategoryShowType) {
        setState(
            CategoryBottomSheetUIState.Success(
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
        setState(
            CategoryBottomSheetUIState.Success(
                colors = functions.getSuccessColors(uiState.value),
                categoryUIModel = functions.getSuccessCategoryUIModel(uiState.value).apply {
                    this.emoji = emoji
                },
                categoryShowType = functions.getSuccessCategoryShowType(uiState.value)
            )
        )
    }

    fun onDismissed() {
        setEvent(CategoryUIEvent.Dismissed)
    }

    fun onColorSelected(itemUIModel: ColorItemUIModel) {
        val list = functions.getSuccessColors(uiState.value).subList(0, functions.getSuccessColors(uiState.value).size)
        list?.forEach {
            it.isSelected = it.color == itemUIModel.color
        }
        setState(
            CategoryBottomSheetUIState.Success(
                colors = emptyList(),
                categoryUIModel = functions.getSuccessCategoryUIModel(uiState.value).apply {
                    this.color = itemUIModel.apply {
                        isSelected = true
                    }
                },
                categoryShowType = functions.getSuccessCategoryShowType(uiState.value)
            )
        )
        setState(
            CategoryBottomSheetUIState.Success(
                colors = list,
                categoryUIModel = functions.getSuccessCategoryUIModel(uiState.value),
                categoryShowType = functions.getSuccessCategoryShowType(uiState.value)
            )
        )
    }
}

@GenerateSealedGetters
sealed interface CategoryBottomSheetUIState : BottomSheetState {
    data class Success(
        var colors: List<ColorItemUIModel>,
        val categoryUIModel: CategoryUIModel,
        val categoryShowType: CategoryShowType,
    ): CategoryBottomSheetUIState {
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
    data class Error(val error: CustomException) : CategoryBottomSheetUIState
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
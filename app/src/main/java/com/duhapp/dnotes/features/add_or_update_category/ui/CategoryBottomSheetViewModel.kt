package com.duhapp.dnotes.features.add_or_update_category.ui

import android.os.Parcelable
import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.viewModelScope
import com.duhapp.dnotes.NoteColor
import com.duhapp.dnotes.R
import com.duhapp.dnotes.features.add_or_update_category.domain.UpsertCategory
import com.duhapp.dnotes.features.add_or_update_category.domain.isEmoji
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
) :
    BottomSheetViewModel<CategoryUIEvent, CategoryBottomSheetUIState>() {
    fun onPositiveButtonClicked() {
        setState(
            withStateValue {
                var state = it
                if (it.isSuccess()) {
                    viewModelScope.launch {
                        try {
                            upsertCategory.invoke(it.getSuccessCategory()!!)
                            setEvent(CategoryUIEvent.Upserted)
                        } catch (e: Exception) {
                            state = CategoryBottomSheetUIState.Error(
                                e.asCustomException(
                                    message = R.string.Save_Category_Failed
                                )
                            )
                        }
                    }
                }
                state
            }
        )
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
            withStateValue {
                it.apply {
                    if (it.isSuccess()) {
                        it.getSuccessCategory()?.emoji = emoji
                    }
                }
            }
        )
    }

    fun onDismissed() {
        setEvent(CategoryUIEvent.Dismissed)
    }

    fun onColorSelected(itemUIModel: ColorItemUIModel) {
        val list = uiState.value?.getSuccessColors()
        list?.map {
            it.isSelected = it.color == itemUIModel.color
        }
        setState(
            withStateValue {
                return@withStateValue if (!it.isSuccess())
                    it
                else
                    CategoryBottomSheetUIState.Success(
                        colors = list!!,
                        categoryUIModel = it.getSuccessCategory()!!.copy(
                            color = itemUIModel.apply {
                                isSelected = true
                            }
                        ),
                        it.getSuccessCategoryShowType()!!
                    )
            }
        )
    }


}

sealed interface CategoryBottomSheetUIState : BottomSheetState {
    data class Success(
        var colors: List<ColorItemUIModel>,
        var categoryUIModel: CategoryUIModel,
        val categoryShowType: CategoryShowType,
    ) : CategoryBottomSheetUIState {
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

    data class Error(
        val customException: CustomException
    ) : CategoryBottomSheetUIState

    fun isSuccess(): Boolean = this is Success

    fun isError(): Boolean = this is Error

    fun getSuccessColors(): List<ColorItemUIModel>? {
        return if (isSuccess()) {
            (this as Success).colors
        } else {
            null
        }
    }

    fun getSuccessCategory(): CategoryUIModel? {
        return if (isSuccess()) {
            (this as Success).categoryUIModel
        } else {
            null
        }
    }

    fun getSuccessCategoryShowType(): CategoryShowType? {
        return if (isSuccess()) {
            (this as Success).categoryShowType
        } else {
            null
        }
    }

    fun getErrorCustomException(): CustomException? {
        return if (isError()) {
            (this as Error).customException
        } else {
            null
        }
    }

    fun getSuccessPositiveButtonText(): Int? {
        return if (isSuccess()) {
            (this as Success).positiveButtonText
        } else {
            null
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
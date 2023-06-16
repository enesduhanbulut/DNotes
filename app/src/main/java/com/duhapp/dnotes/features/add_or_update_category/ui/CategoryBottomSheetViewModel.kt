package com.duhapp.dnotes.features.add_or_update_category.ui

import android.os.Parcelable
import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.viewModelScope
import com.duhapp.dnotes.R
import com.duhapp.dnotes.features.add_or_update_category.domain.UpsertCategory
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
        viewModelScope.launch {
            upsertCategory.invoke(mutableUIState.value!!.categoryUIModel)
            setEvent(CategoryUIEvent.Upserted)
        }
    }

    fun setViewWithBundle(categoryUIState: CategoryUIModel, categoryShowType: CategoryShowType) {
        setState(
            CategoryBottomSheetUIState(categoryUIState, categoryShowType)
        )
    }

    fun onEmojiClicked() {
        setEvent(CategoryUIEvent.ShowEmojiDialog)
    }

    fun setEmoji(emoji: String) {
        setEvent(CategoryUIEvent.DismissedEmojiDialog)
        setState(
            mutableUIState.value!!.copy(
                categoryUIModel = mutableUIState.value!!.categoryUIModel.copy(
                    emoji = emoji
                )
            )
        )
    }

    fun onDismissed() {
        setEvent(CategoryUIEvent.Dismissed)
    }
}

data class CategoryBottomSheetUIState(
    var categoryUIModel: CategoryUIModel,
    var categoryShowType: CategoryShowType,
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
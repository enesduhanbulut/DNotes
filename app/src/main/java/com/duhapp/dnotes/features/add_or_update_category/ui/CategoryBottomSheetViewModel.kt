package com.duhapp.dnotes.features.add_or_update_category.ui

import android.os.Parcelable
import android.view.View
import androidx.annotation.StringRes
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
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

@HiltViewModel
class CategoryBottomSheetViewModel @Inject constructor(
    private val upsertCategory: UpsertCategory
) : BottomSheetViewModel<CategoryUIEvent, CategoryBottomSheetUIState>() {
    var lastSuccessState: CategoryBottomSheetUIState.Success? = null
    override fun setState(state: CategoryBottomSheetUIState) {
        if (state is CategoryBottomSheetUIState.Success) {
            lastSuccessState = state
        }
        super.setState(state)
    }

    fun onSaveButtonClick() {
        val state = uiState.value
        if (state == null) {
            setTemporaryState(
                state = CategoryBottomSheetUIState.Error(
                    CustomException.UnknownException(
                        CustomExceptionData(
                            title = R.string.Save_Category_Failed,
                            message = R.string.Category_Could_Not_Be_Saved,
                            code = R.string.Unknown_Error
                        )
                    )
                ),
                oldState = lastSuccessState,
                delay = 50
            )
        } else {
            val categoryVal: CategoryUIModel? = if (state.isSuccess())
                state.getSuccessCategory()
            else
                lastSuccessState!!.categoryUIModel

            run {
                if (categoryVal != null)
                    try {
                        upsertCategory.invoke(categoryVal)
                        setEvent(CategoryUIEvent.Upserted)
                    } catch (e: Exception) {
                        setTemporaryState(
                            state = CategoryBottomSheetUIState.Error(
                                e.asCustomException(
                                    message = R.string.Save_Category_Failed
                                )
                            ),
                            oldState = lastSuccessState,
                            delay = 50
                        )
                    }
                else {
                    setTemporaryState(
                        state = CategoryBottomSheetUIState.Error(
                            CustomException.UnknownException(
                                CustomExceptionData(
                                    title = R.string.Save_Category_Failed,
                                    message = R.string.Category_Could_Not_Be_Saved,
                                    code = R.string.Unknown_Error
                                )
                            )
                        ),
                        oldState = lastSuccessState,
                        delay = 50
                    )
                }
            }
        }
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
        val state = uiState.value
        if (state != null) {
            if (!state.isSuccess()) {
                return
            }

            var list = state.getSuccessColors() ?: lastSuccessState?.colors ?: return
            list.map {
                it.newCopy(isSelected = it.color.ordinal == itemUIModel.color.ordinal)
            }.also { list = it }
            var category = state.getSuccessCategory()
            category = category?.apply {
                color = itemUIModel
            } ?: return
            setState(
                CategoryBottomSheetUIState.Success(
                    colors = list,
                    categoryUIModel = category,
                    state.getSuccessCategoryShowType()!!
                )
            )
        }
    }
}

data class FormData(
    @StringRes
    var title: Int = 0,
    var message: Int = 0,
    @StringRes
    var positiveButtonText: Int = 0,
    @StringRes
    var negativeButtonText: Int = 0,
    var negativeButtonVisibility: Int = View.VISIBLE
)

sealed interface CategoryBottomSheetUIState : BottomSheetState {

    data class Success(
        var colors: List<ColorItemUIModel>,
        var categoryUIModel: CategoryUIModel,
        val categoryShowType: CategoryShowType = CategoryShowType.Add,
        var formData: FormData = FormData(
            title = R.string.Add_Category,
            message = R.string.New_Category_Message,
            positiveButtonText = R.string.Add,
            negativeButtonText = R.string.Delete,
            negativeButtonVisibility = View.VISIBLE
        )
    ) : CategoryBottomSheetUIState

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

    fun setSuccessCategoryName(text: String) {
        if (isSuccess()) {
            (this as Success).categoryUIModel.name = text
        }
    }

    fun setSuccessCategoryDescription(text: String) {
        if (isSuccess()) {
            (this as Success).categoryUIModel.description = text
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
            (this as Success).formData.positiveButtonText
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
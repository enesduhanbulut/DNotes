package com.duhapp.dnotes.features.generic.ui

import android.os.Parcelable
import com.duhapp.dnotes.features.base.ui.BottomSheetEvent
import com.duhapp.dnotes.features.base.ui.BottomSheetState
import com.duhapp.dnotes.features.base.ui.BottomSheetViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

@HiltViewModel
class ShowMessageBottomSheetViewModel @Inject constructor() :
    BottomSheetViewModel<ShowMessageUIEvent, ShowMessageBottomSheetUIState>() {
    fun setViewWithBundle(sheetUIState: ShowMessageUIModel, buttonStyle: ButtonStyle) {
        setSuccessState(
            ShowMessageBottomSheetUIState(
                sheetUIState, buttonStyle
            )
        )
    }
}

data class ShowMessageBottomSheetUIState(
    val uiModel: ShowMessageUIModel,
    val buttonStyle: ButtonStyle = ButtonStyle.Both,
) : BottomSheetState

sealed interface ShowMessageUIEvent : BottomSheetEvent {
    object Cancel : ShowMessageUIEvent
}

@Parcelize
enum class ButtonStyle : Parcelable {
    Positive, Negative, Both
}
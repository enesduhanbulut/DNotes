package com.duhapp.dnotes.features.generic.ui

import android.os.Parcelable
import com.ahk.annotation.GenerateSealedGetters
import com.duhapp.dnotes.features.base.ui.BottomSheetEvent
import com.duhapp.dnotes.features.base.ui.BottomSheetState
import com.duhapp.dnotes.features.base.ui.BottomSheetViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

@HiltViewModel
class ShowMessageBottomSheetViewModel @Inject constructor() :
    BottomSheetViewModel<ShowMessageUIEvent, ShowMessageBottomSheetUIState, ShowMessageBottomSheetUIStateFunctions>() {
    fun setViewWithBundle(sheetUIState: ShowMessageUIModel, buttonStyle: ButtonStyle) {
        setState(
            ShowMessageBottomSheetUIState.Success(
                sheetUIState, buttonStyle
            )
        )
    }
}


@GenerateSealedGetters
sealed interface ShowMessageBottomSheetUIState : BottomSheetState {
    data class Success(
        val uiModel: ShowMessageUIModel,
        val buttonStyle: ButtonStyle = ButtonStyle.Both,
    ): ShowMessageBottomSheetUIState
}

sealed interface ShowMessageUIEvent : BottomSheetEvent {
    object Cancel : ShowMessageUIEvent
}

@Parcelize
enum class ButtonStyle : Parcelable {
    Positive, Negative, Both
}
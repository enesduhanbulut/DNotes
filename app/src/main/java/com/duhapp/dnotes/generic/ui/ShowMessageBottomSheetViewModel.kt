package com.duhapp.dnotes.generic.ui

import android.os.Bundle
import androidx.annotation.DrawableRes
import com.duhapp.dnotes.base.ui.BottomSheetEvent
import com.duhapp.dnotes.base.ui.BottomSheetState
import com.duhapp.dnotes.base.ui.BottomSheetViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ShowMessageBottomSheetViewModel @Inject constructor() :
    BottomSheetViewModel<ShowMessageBottomSheetUIEvent, ShowMessageBottomSheetUIState>() {
    fun setViewWithBundle(arguments: Bundle?) {
        arguments?.let {
            val image = it.getInt("image")
            val title = it.getString("title")
            val message = it.getString("message")
            val positiveButtonText = it.getString("positiveButtonText")
            val negativeButtonText = it.getString("negativeButtonText")
            val buttonStyle = it.getString("buttonStyle")
            mutableBottomSheetUIState.value = ShowMessageBottomSheetUIState(
                image = image,
                title = title ?: "",
                message = message ?: "",
                positiveButtonText = positiveButtonText ?: "Confirm",
                negativeButtonText = negativeButtonText ?: "Cancel"
            )
        }
    }
}

data class ShowMessageBottomSheetUIState(
    @DrawableRes val image: Int,
    val title: String,
    val message: String,
    var positiveButtonText: String = "Confirm",
    var negativeButtonText: String = "Cancel",
    var buttonStyle: ButtonStyle = ButtonStyle.Both,
) : BottomSheetState

sealed interface ShowMessageBottomSheetUIEvent : BottomSheetEvent {
    object Cancel : ShowMessageBottomSheetUIEvent
}

enum class ButtonStyle {
    Positive, Negative, Both
}
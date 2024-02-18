package com.duhapp.dnotes.ui.custom_views

import android.view.View
import com.duhapp.dnotes.R
import com.duhapp.dnotes.features.base.ui.DialogFragmentState
import com.duhapp.dnotes.features.base.ui.FragmentUIEvent
import com.duhapp.dnotes.features.base.ui.FragmentUIState
import com.duhapp.dnotes.features.base.ui.FragmentViewModel

class ErrorDialogViewModel : FragmentViewModel<ErrorDialogUIEvent, ErrorItemUIState>() {
    fun setDialogState(dialogState: DialogFragmentState) {
        when (dialogState) {
            is DialogFragmentState.OptionDialog -> {
                setState(
                    ErrorItemUIState(
                        title = dialogState.title,
                        message = dialogState.message,
                        positiveButtonText = dialogState.positiveButtonText,
                        positiveButtonAction = dialogState.positiveButtonAction,
                        positiveButtonVisibility = View.VISIBLE,
                        negativeButtonText = dialogState.negativeButtonText,
                        negativeButtonAction = dialogState.negativeButtonAction,
                        negativeButtonVisibility = View.VISIBLE,
                    )
                )
            }

            is DialogFragmentState.OneButtonDialog -> {
                setState(
                    ErrorItemUIState(
                        title = dialogState.title,
                        message = dialogState.message,
                        okButtonText = dialogState.okButtonText,
                        okButtonAction = dialogState.okButtonAction,
                        okButtonVisibility = View.VISIBLE,
                    )
                )
            }

            is DialogFragmentState.InformativeDialog -> {
                setState(
                    ErrorItemUIState(
                        title = dialogState.title,
                        message = dialogState.message,
                    )
                )
            }
        }
    }

    fun positiveButtonClicked() {
        withStateValue {
            setEvent(ErrorDialogUIEvent.DismissDialog)
            it.positiveButtonAction.invoke()
            it
        }
    }

    fun negativeButtonClicked() {
        withStateValue {
            setEvent(ErrorDialogUIEvent.DismissDialog)
            it.negativeButtonAction.invoke()
            it
        }
    }

    fun okButtonClicked() {
        withStateValue {
            setEvent(ErrorDialogUIEvent.DismissDialog)
            it.okButtonAction.invoke()
            it
        }
    }
}

interface ErrorDialogUIEvent : FragmentUIEvent {
    object DismissDialog : ErrorDialogUIEvent
}

data class ErrorItemUIState(
    val title: Int = R.string.Unknown_Error,
    val message: Int = R.string.Unknown_Error_Message,
    val positiveButtonText: Int = R.string.Positive_Button_Text,
    val positiveButtonAction: () -> Unit = {},
    val positiveButtonVisibility: Int = View.GONE,
    val negativeButtonText: Int = R.string.Negative_Button_Text,
    val negativeButtonAction: () -> Unit = {},
    val negativeButtonVisibility: Int = View.GONE,
    val okButtonText: Int = R.string.Ok_Button_Text,
    val okButtonAction: () -> Unit = {},
    val okButtonVisibility: Int = View.GONE,
) : FragmentUIState

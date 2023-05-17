package com.duhapp.dnotes.features.base.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class BottomSheetViewModel<UE : BottomSheetEvent, US : BottomSheetState> : ViewModel() {
    protected val mUIEvent = MutableStateFlow<UE?>(null)
    val uiEvent: StateFlow<UE?> = mUIEvent
    protected val mUIState = MutableStateFlow<US?>(null)
    val uiState: StateFlow<US?> = mUIState
    fun setEvent(event: UE) {
        if (mUIEvent.value == event) {
            mUIEvent.value = null
        }
        mUIEvent.value = event
    }

    fun setState(state: US) {
        if (mUIState.value == state) {
            mUIState.value = null
        }
        mUIState.value = state
    }
}

interface BottomSheetEvent : FragmentUIEvent
interface BottomSheetState : FragmentUIState
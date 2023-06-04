package com.duhapp.dnotes.features.base.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class BottomSheetViewModel<UE : BottomSheetEvent, US : BottomSheetState> : ViewModel() {
    protected val mUIEvent = MutableSharedFlow<UE?>()
    val uiEvent: SharedFlow<UE?> = mUIEvent
    protected val mUIState = MutableStateFlow<US?>(null)
    val uiState: StateFlow<US?> = mUIState
    fun setEvent(event: UE) =
        viewModelScope.launch { mUIEvent.emit(event) }

    fun setState(state: US) {
        if (mUIState.value == state) {
            mUIState.value = null
        }
        mUIState.value = state
    }
}

interface BottomSheetEvent : FragmentUIEvent
interface BottomSheetState : FragmentUIState

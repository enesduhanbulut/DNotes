package com.duhapp.dnotes.features.base.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class BottomSheetViewModel<UE : BottomSheetEvent, US : BottomSheetState> : ViewModel() {
    protected val mUIEvent = MutableSharedFlow<UE>(0)
    val uiEvent: SharedFlow<UE> = mUIEvent.asSharedFlow()
    protected val mUIState = MutableStateFlow<US?>(null)
    val uiState: StateFlow<US?> = mUIState.asStateFlow()
    fun setEvent(event: UE) =
        viewModelScope.launch {
            mUIEvent.emit(event)
        }

    fun setState(state: US) {
        if (mUIState.value == state) {
            mUIState.value = null
        }
        mUIState.value = state
    }

    protected fun runInIO(block: suspend () -> Unit) {
        viewModelScope.launch {
            block()
        }
    }
}

interface BottomSheetEvent : FragmentUIEvent
interface BottomSheetState : FragmentUIState

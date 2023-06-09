package com.duhapp.dnotes.features.base.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<UE : BaseUIEvent, US : BaseUIState> : ViewModel() {
    protected val mutableUIEvent = MutableSharedFlow<UE>()
    val uiEvent = mutableUIEvent.asSharedFlow()
    protected val mutableUIState = MutableStateFlow<US?>(null)
    val uiState = mutableUIState.asStateFlow()

    fun setEvent(event: UE) = viewModelScope.launch {
        mutableUIEvent.emit(event)
    }

    fun setState(state: US) {
        mutableUIState.value = state
    }
}

interface BaseUIState : Cloneable {
    fun newCopy(): BaseUIState {
        return super.clone() as BaseUIState
    }

}

interface BaseUIEvent


package com.duhapp.dnotes.features.base.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<UE : FragmentUIEvent, US : FragmentUIState> : ViewModel() {
    protected val mutableUIEvent = MutableSharedFlow<UE?>()
    val uiEvent = mutableUIEvent as SharedFlow<UE?>
    protected val mutableUIState = MutableStateFlow<US?>(null)
    val uiState = mutableUIState as StateFlow<US?>

    fun setEvent(event: UE) = viewModelScope.launch { mutableUIEvent.emit(event) }
}

interface FragmentUIState

interface FragmentUIEvent


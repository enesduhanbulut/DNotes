package com.duhapp.dnotes.features.base.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Suppress("Unchecked_cast")
abstract class BaseViewModel<UE : FragmentUIEvent, US : FragmentUIState> : ViewModel() {
    protected val mutableUIEvent = MutableStateFlow<UE?>(null)
    val uiEvent = mutableUIEvent as StateFlow<UE?>
    protected val mutableUIState = MutableStateFlow<US?>(null)
    val uiState = mutableUIState as StateFlow<US?>

    fun setEvent(event: UE) {
        if (mutableUIEvent.value == event) {
            mutableUIEvent.value = null
        }
        mutableUIEvent.value = event
    }
}

interface FragmentUIState

interface FragmentUIEvent


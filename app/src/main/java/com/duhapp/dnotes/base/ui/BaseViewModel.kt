package com.duhapp.dnotes.base.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

open class BaseViewModel<UE : BaseUIEvent, US : BaseUIState> : ViewModel() {
    protected val mutableUIEvent = MutableStateFlow<UE?>(null)
    val uiEvent = mutableUIEvent as StateFlow<UE?>
    protected val mutableUIState = MutableStateFlow<US?>(null)
    val uiState = mutableUIState as StateFlow<US?>


}

abstract class BaseUIState

abstract class BaseUIEvent


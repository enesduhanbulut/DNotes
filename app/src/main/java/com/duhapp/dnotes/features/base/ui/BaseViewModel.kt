package com.duhapp.dnotes.features.base.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<UE : BaseUIEvent, US : BaseUIState> : ViewModel() {
    private val mutableUIEvent = MutableSharedFlow<UE>()
    val uiEvent = mutableUIEvent.asSharedFlow()
    private val mutableUIState = MutableStateFlow<US?>(null)
    val uiState: StateFlow<US?> = mutableUIState
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    open fun setEvent(event: UE) = viewModelScope.launch {
        mutableUIEvent.emit(event)
    }

    open fun setState(state: US) {
        if (mutableUIState.value == state) {
            mutableUIState.value = null
        }
        mutableUIState.update { state }
    }

    fun setTemporaryState(state: US, oldState: US?, delay: Long) {
        setState(state)
        if (oldState == null) {
            return
        }
        run {
            kotlinx.coroutines.delay(delay)
            setState(oldState)
        }
    }

    fun setStateAndRunMethodAfterDelay(state: US, delay: Long, block: () -> Unit) {
        setState(state)
        run {
            kotlinx.coroutines.delay(delay)
            block()
        }
    }

    protected fun withStateValue(block: (US) -> US): US {
        val state = mutableUIState.value
        state?.let {
            return block(it)
        }
        throw IllegalStateException("State is null")
    }

    fun run(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch {
            block()
        }
    }
}

interface BaseUIState : Cloneable {
    fun newCopy(): BaseUIState {
        return super.clone() as BaseUIState
    }

}

interface BaseUIEvent


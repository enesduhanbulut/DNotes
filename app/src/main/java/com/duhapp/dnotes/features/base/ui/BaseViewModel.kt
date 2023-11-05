package com.duhapp.dnotes.features.base.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duhapp.dnotes.FunctionProvider
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.reflect.Type

abstract class BaseViewModel<UE : BaseUIEvent, US : BaseUIState, F : BaseStateFunctions>() :
    ViewModel() {
    private val mutableUIEvent = MutableSharedFlow<UE>()
    val uiEvent = mutableUIEvent.asSharedFlow()
    private val mutableUIState = MutableStateFlow<US?>(null)
    val uiState: StateFlow<US?> = mutableUIState
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val type: Type = object: TypeToken<F>() {}.type
    open val functions : F = FunctionProvider.getFunction(type) as F


    fun setEvent(event: UE) = viewModelScope.launch {
        mutableUIEvent.emit(event)
    }

    fun setState(state: US) {
        if (mutableUIState.value == state) {
            mutableUIState.value = null
        }
        mutableUIState.update { state }
    }

    protected fun withStateValue(block: (US) -> US): US {
        val state = mutableUIState.value
        state?.let {
            return block(it)
        }
        throw IllegalStateException("State is null")
    }
}

interface BaseUIState : Cloneable {
    fun newCopy(): BaseUIState {
        return super.clone() as BaseUIState
    }

}

interface BaseUIEvent


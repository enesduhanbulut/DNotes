package com.duhapp.dnotes.features.base.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duhapp.dnotes.features.base.domain.CustomException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<UE : BaseUIEvent, US : BaseUIState> : ViewModel() {
    private val mutableUIEvent = MutableSharedFlow<UE>()
    val uiEvent = mutableUIEvent.asSharedFlow()
    private val mutableUIState = MutableStateFlow<Result<US>?>(null)
    val uiState: StateFlow<US?> = mutableUIState
        .map { it?.getOrNull() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    fun setEvent(event: UE) = viewModelScope.launch {
        mutableUIEvent.emit(event)
    }

    fun setFailureState(customException: CustomException) {
        mutableUIState.value = Result.failure(customException)
    }

    fun setSuccessState(state: US) {
        if (mutableUIState.value?.getOrNull() == state) {
            mutableUIState.value = null
        }
        mutableUIState.update {
            Result.success(state)
        }
    }

    protected fun withStateValue(block: (US) -> US): US {
        val state = mutableUIState.value?.getOrNull()
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


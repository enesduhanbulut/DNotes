package com.duhapp.dnotes.features.base.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class BottomSheetViewModel<UE : BottomSheetEvent, US : BottomSheetState> : ViewModel() {
    protected val mutableBottomSheetUIEvent = MutableStateFlow<UE?>(null)
    val uiEvent: StateFlow<UE?> = mutableBottomSheetUIEvent
    protected val mutableBottomSheetUIState = MutableStateFlow<US?>(null)
    val uiState: StateFlow<US?> = mutableBottomSheetUIState
}

interface BottomSheetEvent : FragmentUIEvent
interface BottomSheetState : FragmentUIState
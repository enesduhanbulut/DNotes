package com.duhapp.dnotes.features.base.ui

abstract class BottomSheetViewModel<UE : BottomSheetEvent, US : BottomSheetState> :
    BaseViewModel<UE, US>()

interface BottomSheetEvent : BaseUIEvent
interface BottomSheetState : BaseUIState

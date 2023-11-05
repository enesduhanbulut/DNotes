package com.duhapp.dnotes.features.base.ui

abstract class BottomSheetViewModel<UE : BottomSheetEvent, US : BottomSheetState, F: BaseStateFunctions> :
    BaseViewModel<UE, US, F>()

interface BottomSheetEvent : BaseUIEvent
interface BottomSheetState : BaseUIState

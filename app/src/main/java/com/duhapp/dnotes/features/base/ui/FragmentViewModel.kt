package com.duhapp.dnotes.features.base.ui

abstract class FragmentViewModel<UE : FragmentUIEvent, US : FragmentUIState, F : BaseStateFunctions> :
    BaseViewModel<UE, US, F>()

interface FragmentUIState : BaseUIState

interface FragmentUIEvent : BaseUIEvent

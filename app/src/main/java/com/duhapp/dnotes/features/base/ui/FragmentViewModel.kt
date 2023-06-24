package com.duhapp.dnotes.features.base.ui

abstract class FragmentViewModel<UE : FragmentUIEvent, US : FragmentUIState> :
    BaseViewModel<UE, US>()

interface FragmentUIState : BaseUIState

interface FragmentUIEvent : BaseUIEvent

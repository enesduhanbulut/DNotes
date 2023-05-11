package com.duhapp.dnotes

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor() : ViewModel() {
    private val mutableMainActivityUIEvent = MutableStateFlow<MainActivityUIEvent?>(null)
    val mainActivityUIEvent = mutableMainActivityUIEvent as StateFlow<MainActivityUIEvent?>
}

interface MainActivityUIEvent

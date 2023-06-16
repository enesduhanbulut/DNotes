package com.duhapp.dnotes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duhapp.dnotes.features.manage_category.domain.CreateDefaultCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val createDefaultCategory: CreateDefaultCategory
) : ViewModel() {
    private val mutableMainActivityUIEvent = MutableStateFlow<MainActivityUIEvent?>(null)
    val mainActivityUIEvent = mutableMainActivityUIEvent as StateFlow<MainActivityUIEvent?>

    fun initializeDefaultDataModels() {
        viewModelScope.launch {
            createDefaultCategory.invoke()
        }
    }

}

interface MainActivityUIEvent

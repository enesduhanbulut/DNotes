package com.duhapp.dnotes.home_category.ui

import android.os.Bundle
import com.duhapp.dnotes.generic.ui.ShowMessageBottomSheet
import com.duhapp.dnotes.generic.ui.ShowMessageBottomSheetUIState

object BottomSheetProvider {
    fun provideShowMessage(
        showMessageBottomSheetUIState: ShowMessageBottomSheetUIState
    ): Pair<ShowMessageBottomSheet, Bundle> {
        val bottomSheet = ShowMessageBottomSheet()
        val bundle = with(Bundle()) {
            putString("title", showMessageBottomSheetUIState.title)
            putString("message", showMessageBottomSheetUIState.message)
            bottomSheet.arguments = this
            this
        }
        return Pair(bottomSheet, bundle)
    }
}
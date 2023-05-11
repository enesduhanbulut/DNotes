package com.duhapp.dnotes.generic.ui

import androidx.fragment.app.viewModels
import com.duhapp.dnotes.R
import com.duhapp.dnotes.base.ui.BaseBottomSheet
import com.duhapp.dnotes.base.ui.BottomSheetEvent
import com.duhapp.dnotes.databinding.FragmentShowMessageBottomSheetBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class ShowMessageBottomSheet :
    BaseBottomSheet<ShowMessageBottomSheetUIEvent, ShowMessageBottomSheetUIState,
            ShowMessageBottomSheetViewModel, FragmentShowMessageBottomSheetBinding>() {
    private val showMessageBottomSheetViewModel: ShowMessageBottomSheetViewModel by viewModels()
    override val layoutId: Int
        get() = R.layout.fragment_show_message_bottom_sheet
    override val fragmentTag: String
        get() = "MessageBottomSheet"


    override fun provideViewModel(): ShowMessageBottomSheetViewModel {
        return showMessageBottomSheetViewModel
    }

    override fun setBindingViewModel() {
        binding.viewModel = viewModel
    }

    override fun handleUIEvent(it: BottomSheetEvent) {
        when (it) {
            ShowMessageBottomSheetUIEvent.Cancel -> {
                dismiss()
            }
        }
    }

    override fun initView(binding: FragmentShowMessageBottomSheetBinding) {
        viewModel.setViewWithBundle(arguments)
    }

}

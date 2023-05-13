package com.duhapp.dnotes.features.generic.ui

import androidx.fragment.app.viewModels
import com.duhapp.dnotes.R
import com.duhapp.dnotes.databinding.FragmentShowMessageBottomSheetBinding
import com.duhapp.dnotes.features.base.ui.BaseBottomSheet
import com.duhapp.dnotes.features.generic.domain.getNotNullParcelableWithKey
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

    override fun handleUIEvent(it: ShowMessageBottomSheetUIEvent) {
        when (it) {
            ShowMessageBottomSheetUIEvent.Cancel -> {
                dismiss()
            }
        }
    }

    override fun initView(binding: FragmentShowMessageBottomSheetBinding) {
        arguments.let {
            val sheetUIState = it!!.getNotNullParcelableWithKey<ShowMessageUIModel>("sheetUIState")
            val buttonStyle = it.getNotNullParcelableWithKey<ButtonStyle>("buttonStyle")
            viewModel.setViewWithBundle(sheetUIState, buttonStyle)
        }
    }

}

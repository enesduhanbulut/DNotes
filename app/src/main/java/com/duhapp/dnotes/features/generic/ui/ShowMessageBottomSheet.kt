package com.duhapp.dnotes.features.generic.ui

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.duhapp.dnotes.R
import com.duhapp.dnotes.databinding.FragmentShowMessageBottomSheetBinding
import com.duhapp.dnotes.features.base.ui.BaseBottomSheet
import com.duhapp.dnotes.features.generic.domain.getNotNullParcelableWithKey
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class ShowMessageBottomSheet :
    BaseBottomSheet<FragmentShowMessageBottomSheetBinding,
            ShowMessageUIEvent, ShowMessageBottomSheetUIState,
            ShowMessageBottomSheetViewModel>() {
    override val viewModel: ShowMessageBottomSheetViewModel by viewModels()
    override val layoutId = R.layout.fragment_show_message_bottom_sheet
    override val titleId = R.string.New_Category_Message
    override val fragmentTag = "MessageBottomSheet"


    override fun setBindingViewModel() {
        mBinding!!.viewModel = viewModel
    }

    override fun handleArgs(args: Bundle) {
        TODO("Not yet implemented")
    }

    override fun handleUIState(it: ShowMessageBottomSheetUIState) {
        TODO("Not yet implemented")
    }

    override fun handleUIEvent(it: ShowMessageUIEvent) {
        when (it) {
            ShowMessageUIEvent.Cancel -> {
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

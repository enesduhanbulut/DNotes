package com.duhapp.dnotes.ui.custom_views

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.duhapp.dnotes.R
import com.duhapp.dnotes.databinding.ErrorDialogBinding
import com.duhapp.dnotes.features.base.ui.BaseDialogFragment
import timber.log.Timber

class ErrorDialog: BaseDialogFragment<ErrorDialogBinding, ErrorDialogUIEvent, ErrorItemUIState, ErrorDialogViewModel>() {
    override val layoutId: Int
        get() = R.layout.error_dialog
    override val titleId: Int
        get() = R.string.Common_Error_Title
    override val viewModel: ErrorDialogViewModel by viewModels()
    override val fragmentTag: String
        get() = "ErrorDialog"

    override fun setBindingViewModel() {
        mBinding!!.viewModel = viewModel
    }

    override fun handleArgs(args: Bundle) {
        val dialogState = ErrorDialogArgs.fromBundle(args).dialogState
        viewModel.setDialogState(dialogState)
    }

    override fun handleUIState(it: ErrorItemUIState) {
        Timber.d("handleUIState")
    }

    override fun handleUIEvent(it: ErrorDialogUIEvent) {
        Timber.d("handleUIEvent")
        when (it) {
            is ErrorDialogUIEvent.DismissDialog -> {
                findNavController().popBackStack()
            }
        }
    }

    override fun initView(binding: ErrorDialogBinding) {
        Timber.d("initView")
    }
}
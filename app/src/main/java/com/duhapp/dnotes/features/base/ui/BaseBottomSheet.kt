package com.duhapp.dnotes.features.base.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch

abstract class BaseBottomSheet<BUE : BottomSheetEvent, BUS : BottomSheetState, VM : BottomSheetViewModel<BUE, BUS>, DB : ViewDataBinding> :
    BottomSheetDialogFragment() {
    abstract val layoutId: Int
    abstract val fragmentTag: String
    protected lateinit var binding: DB
    protected lateinit var viewModel: VM
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        viewModel = provideViewModel()
        binding.lifecycleOwner = viewLifecycleOwner
        setBindingViewModel()
        observeUIEvent()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(binding)
    }

    private fun observeUIEvent() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiEvent.collect {
                    if (it != null)
                        handleUIEvent(it)
                }
            }
        }
    }

    abstract fun initView(binding: DB)
    abstract fun provideViewModel(): VM
    abstract fun setBindingViewModel()
    abstract fun handleUIEvent(it: BUE)
}


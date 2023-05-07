package com.duhapp.dnotes.base.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch

abstract class BaseFragment
<
        DB : ViewDataBinding,
        UE : BaseUIEvent,
        US : BaseUIState,
        VM : BaseViewModel<UE, US>,
        > : Fragment() {

    protected lateinit var binding: DB
    protected lateinit var viewModel: VM

    @get:LayoutRes
    abstract val layoutId: Int

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        initView(binding)
        viewModel = provideViewModel()
        binding.lifecycleOwner = viewLifecycleOwner
        setBindingViewModel()
        observeUIEvent()
        return binding.root
    }

    private fun observeUIEvent() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiEvent.collect {
                    if (it != null) {
                        handleUIEvent(it)
                    }
                }
            }
        }
    }

    abstract fun initView(binding: DB)
    abstract fun provideViewModel(): VM
    abstract fun setBindingViewModel()
    abstract fun handleUIEvent(it: UE)

    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
    }

}
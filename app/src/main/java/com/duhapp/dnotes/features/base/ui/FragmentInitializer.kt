package com.duhapp.dnotes.features.base.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

interface FragmentInitializer<DB : ViewDataBinding, UE : BaseUIEvent, US : BaseUIState, VM : BaseViewModel<UE, US>> {
    var observeJobs: MutableList<Job>

    @get:LayoutRes
    val layoutId: Int

    @get:StringRes
    val titleId: Int
    val viewModel: VM
    val fragmentTag: String

    var mBinding: DB?
    fun initView(binding: DB)
    fun setBindingViewModel()
    fun handleUIEvent(it: UE)
    fun handleUIState(it: US)
    fun handleArgs(args: Bundle)

    fun onOnCreateViewTask(
        viewLifecycleOwner: LifecycleOwner, args: Bundle?,
        inflater: LayoutInflater, container: ViewGroup?
    ): DB {
        if (args != null) {
            handleArgs(args)
        }
        mBinding = DataBindingUtil.inflate(inflater, layoutId, container, false) as DB
        mBinding!!.lifecycleOwner = viewLifecycleOwner
        setBindingViewModel()
        observeUIEvent(viewLifecycleOwner)
        observeUIState(viewLifecycleOwner)
        initView(mBinding!!)
        return mBinding!!
    }

    fun observeUIEvent(lifecycleOwner: LifecycleOwner) {
        observeJobs.add(
            lifecycleOwner.lifecycleScope.launch {
                viewModel.uiEvent.collect {
                    handleUIEvent(it)
                }
            },
        )
    }

    fun observeUIState(lifecycleOwner: LifecycleOwner) {
        observeJobs.add(
            lifecycleOwner.lifecycleScope.launch {
                viewModel.uiState.collect {
                    if (it != null) {
                        handleUIState(it)
                    }
                }
            },
        )
    }

    fun onDeAttachTask() {
        observeJobs.forEach {
            it.cancel()
        }
        mBinding!!.unbind()
        mBinding!!.lifecycleOwner = null
        mBinding = null
    }

}
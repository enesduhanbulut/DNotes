package com.duhapp.dnotes.features.base.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.duhapp.dnotes.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

abstract class BaseBottomSheet<BUE : BottomSheetEvent, BUS : BottomSheetState, VM : BottomSheetViewModel<BUE, BUS>, DB : ViewDataBinding> :
    BottomSheetDialogFragment() {
    abstract val layoutId: Int
    abstract val fragmentTag: String
    protected lateinit var binding: DB
    protected lateinit var viewModel: VM
    protected var observeJobs: MutableList<Job> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        viewModel = provideViewModel()
        handleArgs(requireArguments())
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        setBindingViewModel()
        observeUIEvent()
        return binding.root
    }

    protected open fun handleArgs(args: Bundle) {
        // override this method if you want to do something before binding
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(binding)
    }

    private fun observeUIEvent() {
        observeJobs.add(
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.uiEvent.collect {
                    if (it != null) {
                        handleUIEvent(it)
                    }
                }
            },
        )
    }

    fun observeUIState() {
        observeJobs.add(
            lifecycleScope.launch {
                viewModel.uiState.collect {
                    if (it != null)
                        handleUIState(it)
                }
            },
        )
    }

    open fun handleUIState(it: BUS) {
        // no-op
    }

    abstract fun initView(binding: DB)
    abstract fun provideViewModel(): VM
    abstract fun setBindingViewModel()
    abstract fun handleUIEvent(it: BUE)
}

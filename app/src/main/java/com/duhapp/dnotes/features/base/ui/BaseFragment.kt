package com.duhapp.dnotes.features.base.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.duhapp.dnotes.MainActivity
import com.duhapp.dnotes.features.generic.ui.ShowMessageBottomSheetViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch

abstract class BaseFragment<
        DB : ViewDataBinding,
        UE : FragmentUIEvent,
        US : FragmentUIState,
        VM : BaseViewModel<UE, US>,
        > : Fragment() {

    protected lateinit var binding: DB
    protected lateinit var viewModel: VM

    @get:LayoutRes
    abstract val layoutId: Int

    @get:StringRes
    abstract val titleId: Int
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        viewModel = provideViewModel()
        setBindingViewModel()
        binding.lifecycleOwner = viewLifecycleOwner
        initView(binding)
        with(requireActivity()) {
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    setAppBarTitle(titleId)
                }
            }
        }
        observeUIEvent()
        return binding.root
    }

    protected fun setAppBarTitle(@StringRes titleId: Int) {
        (requireActivity() as MainActivity).setAppBarTitle(titleId)
    }

    protected fun setAppBarVisibility(visibility: Int) {
        (requireActivity() as MainActivity).setAppBarVisibility(visibility)
    }

    protected fun setBottomNavigationVisibility(visibility: Int) {
        (requireActivity() as MainActivity).setBottomAppBarVisibility(visibility)
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

    fun observeUIState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    if (it != null) {
                        handleUIState(it)
                    }
                }
            }
        }
    }

    protected open fun handleUIState(it: US) {
        // override this method if you want to handle UIState
    }

    abstract fun initView(binding: DB)
    abstract fun provideViewModel(): VM
    abstract fun setBindingViewModel()
    abstract fun handleUIEvent(it: UE)
    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
    }

    fun showBottomSheetFragment(
        pair: Pair<BottomSheetDialogFragment, Bundle>,
        showMessageBottomSheetViewModel: BottomSheetViewModel<*, *> = ShowMessageBottomSheetViewModel(),
    ): BottomSheetViewModel<*, *> {
        val fragment = pair.first
        val bundle = pair.second
        fragment.arguments = bundle
        fragment.show(
            childFragmentManager,
            fragment.tag,
        )
        return showMessageBottomSheetViewModel
    }

}
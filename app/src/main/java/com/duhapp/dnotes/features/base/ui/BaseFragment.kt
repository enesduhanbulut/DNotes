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
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

abstract class BaseFragment<
        DB : ViewDataBinding,
        UE : FragmentUIEvent,
        US : FragmentUIState,
        VM : FragmentViewModel<UE, US, F>,
        F : BaseStateFunctions
        > : Fragment() {

    protected var observeJobs: MutableList<Job> = mutableListOf()
    private var mBinding: DB? = null
    protected lateinit var binding: DB
    protected lateinit var viewModel: VM
    protected lateinit var functions: F

    @get:LayoutRes
    abstract val layoutId: Int

    @get:StringRes
    abstract val titleId: Int
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        viewModel = provideViewModel()
        beforeBinding()
        mBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        binding = mBinding!!
        setBindingViewModel()
        mBinding!!.lifecycleOwner = viewLifecycleOwner
        initView(mBinding!!)
        observeJobs.add(
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    setAppBarTitle(titleId)
                }
            },
        )
        observeUIEvent()
        observeUIState()
        return mBinding!!.root
    }

    protected open fun beforeBinding() {
        // override this method if you want to do something before binding
    }

    fun setFabVisibility(visible: Int) {
        (requireActivity() as MainActivity).setFabVisibility(titleId)

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
        observeJobs.add(
            lifecycleScope.launch {
                viewModel.uiEvent.collect {
                    if (it != null) {
                        handleUIEvent(it)
                    }
                }
            },
        )
    }

    protected fun observeUIState() {
        observeJobs.add(
            lifecycleScope.launch {
                viewModel.uiState.collect {
                    if (it != null) {
                        handleUIState(it)
                    }
                }
            },
        )
    }

    protected open fun handleUIState(it: US) {
        // override this method if you want to handle UIState
    }

    abstract fun initView(binding: DB)
    abstract fun provideViewModel(): VM
    abstract fun setBindingViewModel()
    abstract fun handleUIEvent(it: UE)
    override fun onDestroyView() {
        observeJobs.forEach {
            it.cancel()
        }
        mBinding!!.unbind()
        mBinding!!.lifecycleOwner = null
        mBinding = null
        super.onDestroyView()
    }
}

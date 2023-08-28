package com.duhapp.dnotes.features.base.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import kotlinx.coroutines.Job

abstract class BaseFragment<
        DB : ViewDataBinding,
        UE : FragmentUIEvent,
        US : FragmentUIState,
        VM : FragmentViewModel<UE, US>,
        > : Fragment(), FragmentInitializer<DB, UE, US, VM> {
    override var observeJobs: MutableList<Job> = mutableListOf()
    override var mBinding: DB? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        mBinding = onOnCreateViewTask(viewLifecycleOwner, arguments, inflater, container)
        return mBinding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        onDeAttachTask()
    }
}

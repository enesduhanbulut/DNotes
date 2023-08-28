package com.duhapp.dnotes.features.base.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.duhapp.dnotes.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.Job

abstract class BaseBottomSheet<DB : ViewDataBinding, BUE : BottomSheetEvent, BUS : BottomSheetState, VM : BottomSheetViewModel<BUE, BUS>> :
    BottomSheetDialogFragment(), FragmentInitializer<DB, BUE, BUS, VM> {
    override var observeJobs: MutableList<Job> = mutableListOf()
    override var mBinding: DB? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        mBinding = onOnCreateViewTask(viewLifecycleOwner, arguments, inflater, container)
        return mBinding!!.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }

    override fun onDetach() {
        super.onDetach()
        onDeAttachTask()
    }
}

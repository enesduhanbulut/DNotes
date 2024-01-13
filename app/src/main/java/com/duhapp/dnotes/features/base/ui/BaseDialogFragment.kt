package com.duhapp.dnotes.features.base.ui

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.vanniktech.ui.Parcelize
import kotlinx.coroutines.Job

abstract class BaseDialogFragment<
        DB : ViewDataBinding,
        UE : FragmentUIEvent,
        US : FragmentUIState,
        VM : FragmentViewModel<UE, US>,
        >(
    override var observeJobs: MutableList<Job> = mutableListOf(),
    override var mBinding: DB? = null,
) : DialogFragment(), FragmentInitializer<DB, UE, US, VM> {
    var dialogFragmentState: DialogFragmentState? = null
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
        onDestroyViewTask()
    }
}


sealed class DialogFragmentState(
    @StringRes open val title: Int,
    @StringRes open val message: Int,
) : Parcelable {
    @Parcelize
    data class InformativeDialog(
        @StringRes override val title: Int,
        @StringRes override val message: Int,
    ) : DialogFragmentState(title = title, message = message)

    @Parcelize
    data class OptionDialog(
        @StringRes override val title: Int,
        @StringRes override val message: Int,
        @StringRes val positiveButtonText: Int,
        val positiveButtonAction: () -> Unit,
        @StringRes val negativeButtonText: Int,
        val negativeButtonAction: () -> Unit,
    ) : DialogFragmentState(title = title, message = message)

    @Parcelize
    data class OneButtonDialog(
        @StringRes override val title: Int,
        @StringRes override val message: Int,
        @StringRes val okButtonText: Int,
        val okButtonAction: () -> Unit,
    ) : DialogFragmentState(title = title, message = message)
}


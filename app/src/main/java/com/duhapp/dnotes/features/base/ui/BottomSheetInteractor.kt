package com.duhapp.dnotes.features.base.ui

import android.os.Bundle
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

fun <T : BottomSheetEvent, BS : BaseBottomSheet<*, *, *, *>> BaseFragment<*, *, *, *>.showSnippedBottomSheet(
    containerId: Int,
    fragment: Class<BS>,
    bundle: Bundle? = null,
    activityViewModel: BottomSheetViewModel<T, *>,
    collector: (T) -> Unit,
    unsubscribeEvent: BottomSheetEvent
) {
    showSnippedBottomSheet(
        containerId,
        fragment,
        bundle,
        activityViewModel,
        null,
        collector,
        unsubscribeEvent
    )
}

fun <T : BottomSheetEvent, BS : BaseBottomSheet<*, *, *, *>> BaseFragment<*, *, *, *>.showSnippedBottomSheet(
    containerId: Int,
    fragment: Class<BS>,
    bundle: Bundle? = null,
    activityViewModel: BottomSheetViewModel<T, *>,
    singleEventCollector: (T) -> Unit
) {
    showSnippedBottomSheet(
        containerId,
        fragment,
        bundle,
        activityViewModel,
        singleEventCollector,
        null, null
    )
}

private fun <T : BottomSheetEvent, BS : BaseBottomSheet<*, *, *, *>> BaseFragment<*, *, *, *>.showSnippedBottomSheet(
    containerId: Int,
    fragment: Class<BS>,
    bundle: Bundle? = null,
    activityViewModel: BottomSheetViewModel<T, *>,
    singleEventCollector: ((T) -> Unit)?,
    collector: ((T) -> Unit)?,
    unsubscribeEvent: BottomSheetEvent? = null,
) {
    var job: Job? = null
    job = lifecycleScope.launch {
        if (singleEventCollector != null)
            activityViewModel.uiEvent.collectLatest {
                singleEventCollector.invoke(it)
                job?.cancel()
            }
        else {
            activityViewModel.uiEvent.collect {
                collector?.invoke(it)
                if (it.javaClass == unsubscribeEvent?.javaClass) {
                    job?.cancel()
                }
            }
        }
    }
    parentFragmentManager.beginTransaction().add(
        containerId,
        fragment,
        bundle,
        "bottomSheet"
    ).commit()
}


fun <BOE : BottomSheetEvent, BOS : BottomSheetState, BS : BaseBottomSheet<*, *, *, *>> BaseFragment<*, *, *, *>.showSnippedBottomSheet(
    containerId: Int,
    fragment: Class<BS>,
    bundle: Bundle? = null,
    activityViewModel: BottomSheetViewModel<BOE, BOS>,
    eventCollector: ((BOE) -> Unit),
    unsubscribeEvent: BottomSheetEvent,
    stateCollector: ((BOS) -> Unit),
    unsubscribeState: BOS? = null,
) {
    showSnippedBottomSheet(
        containerId,
        fragment,
        bundle,
        activityViewModel,
        null,
        eventCollector,
        unsubscribeEvent,
        stateCollector,
        unsubscribeState
    )
}

private fun <BOE : BottomSheetEvent, BOS : BottomSheetState, BS : BaseBottomSheet<*, *, *, *>> BaseFragment<*, *, *, *>.showSnippedBottomSheet(
    containerId: Int,
    fragment: Class<BS>,
    bundle: Bundle? = null,
    activityViewModel: BottomSheetViewModel<BOE, BOS>,
    singleEventCollector: ((BOE) -> Unit)?,
    eventCollector: ((BOE) -> Unit)?,
    unsubscribeEvent: BottomSheetEvent? = null,
    stateCollector: ((BOS) -> Unit)?,
    unsubscribeState: BottomSheetState? = null,
) {
    var job1: Job? = null
    job1 = viewLifecycleOwner.lifecycleScope.launch {
        if (stateCollector != null)
            activityViewModel.uiState.collect {
                it?.let {
                    stateCollector.invoke(it)
                    if (it.javaClass == unsubscribeState?.javaClass) {
                        job1?.cancel()
                    }
                }
            }
    }
    var job2: Job? = null
    job2 = viewLifecycleOwner.lifecycleScope.launch {
        if (singleEventCollector != null)
            activityViewModel.uiEvent.collectLatest {
                singleEventCollector.invoke(it)
                job2?.cancel()
                parentFragmentManager.findFragmentByTag("bottomSheet")?.let { fragment ->
                    parentFragmentManager.beginTransaction().remove(fragment).commit()
                }
            }
        else {
            activityViewModel.uiEvent.collect {
                eventCollector?.invoke(it)
                if (it.javaClass == unsubscribeEvent?.javaClass) {
                    job2?.cancel()
                    parentFragmentManager.findFragmentByTag("bottomSheet")?.let {
                        parentFragmentManager.beginTransaction().remove(it).commit()
                    }
                }
            }
        }
    }
    viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
        override fun onDestroy(owner: LifecycleOwner) {
            super.onDestroy(owner)
            job1.cancel()
            job2.cancel()
        }
    })

    parentFragmentManager.beginTransaction().add(
        containerId,
        fragment,
        bundle,
        "bottomSheet"
    ).commit()
}

fun <T : BottomSheetEvent> BaseFragment<*, *, *, *>.showBottomSheet(
    fragment: BaseBottomSheet<*, *, *, *>,
    bundle: Bundle? = null,
    activityViewModel: BottomSheetViewModel<T, *>,
    collector: (T) -> Unit,
    unsubscribeEvent: BottomSheetEvent
) {
    showBottomSheet(fragment, bundle, activityViewModel, null, collector, unsubscribeEvent)
}

fun <T : BottomSheetEvent> BaseFragment<*, *, *, *>.showBottomSheet(
    fragment: BaseBottomSheet<*, *, *, *>,
    bundle: Bundle? = null,
    activityViewModel: BottomSheetViewModel<T, *>,
    singleEventCollector: (T) -> Unit
) {
    showBottomSheet(
        fragment,
        bundle,
        activityViewModel,
        singleEventCollector,
        null, null
    )
}

private fun <T : BottomSheetEvent> BaseFragment<*, *, *, *>.showBottomSheet(
    fragment: BaseBottomSheet<*, *, *, *>,
    bundle: Bundle? = null,
    activityViewModel: BottomSheetViewModel<T, *>,
    singleEventCollector: ((T) -> Unit)?,
    collector: ((T) -> Unit)?,
    unsubscribeEvent: BottomSheetEvent? = null,
) {
    var job: Job? = null
    job = lifecycleScope.launch {
        if (singleEventCollector != null)
            activityViewModel.uiEvent.collectLatest {
                singleEventCollector.invoke(it)
                job?.cancel()
            }
        else {
            activityViewModel.uiEvent.collect {
                collector?.invoke(it)
                if (it.javaClass == unsubscribeEvent?.javaClass) {
                    job?.cancel()
                }
            }
        }
    }
    viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
        override fun onDestroy(owner: LifecycleOwner) {
            super.onDestroy(owner)
            job.cancel()
        }
    }
    )
    fragment.arguments = bundle
    fragment.show(
        childFragmentManager,
        fragment.tag,
    )
}
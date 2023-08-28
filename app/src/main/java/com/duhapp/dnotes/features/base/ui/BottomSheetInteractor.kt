package com.duhapp.dnotes.features.base.ui

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.duhan.satelliteinfo.features.base.presentation.BaseBottomSheet
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
    fragment.arguments = bundle
    fragment.show(
        childFragmentManager,
        fragment.tag,
    )
}
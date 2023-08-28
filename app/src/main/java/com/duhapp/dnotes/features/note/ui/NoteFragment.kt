package com.duhapp.dnotes.features.note.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.duhapp.dnotes.R
import com.duhapp.dnotes.databinding.FragmentNoteBinding
import com.duhapp.dnotes.features.base.ui.BaseFragment
import com.duhapp.dnotes.features.base.ui.showSnippedBottomSheet
import com.duhapp.dnotes.features.select_category.ui.SelectCategoryFragment
import com.duhapp.dnotes.features.select_category.ui.SelectCategoryFragmentArgs
import com.duhapp.dnotes.features.select_category.ui.SelectCategoryUIEvent
import com.duhapp.dnotes.features.select_category.ui.SelectCategoryViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteFragment :
    BaseFragment<FragmentNoteBinding, NoteUIEvent, NoteUIState, NoteViewModel>() {
    private var isCategoryShown = false
    override val layoutId = R.layout.fragment_note
    override val titleId = R.string.Note_Fragment
    override val viewModel: NoteViewModel by viewModels()
    override val fragmentTag = "NoteFragment"
    private val categoryViewModel: SelectCategoryViewModel by activityViewModels()
    override fun initView(binding: FragmentNoteBinding) {
        viewModel.initState(NoteFragmentArgs.fromBundle(requireArguments()))
        initBottomSheet()
    }

    override fun setBindingViewModel() {
        mBinding!!.viewModel = viewModel
    }

    override fun handleArgs(args: Bundle) {
        viewModel.initState(NoteFragmentArgs.fromBundle(args))
    }

    private fun initBottomSheet() {
        with(mBinding!!.bottomSheetCategory) {
            val bottomSheetBehavior = BottomSheetBehavior.from(this)
            val bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                        categoryViewModel.onCollapse()
                    }
                    if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                        categoryViewModel.onExpand()
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    // Do something for slide offset.
                }
            }
            bottomSheetBehavior.addBottomSheetCallback(bottomSheetCallback)
            setOnClickListener {
                if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                } else if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }
            }
        }
    }


    private fun handleBottomSheetEvent(event: SelectCategoryUIEvent) {
        when (event) {
            is SelectCategoryUIEvent.OnCategorySelected -> {
                viewModel.onCategorySelected(event.category)
            }

            else -> {}
        }
    }

    override fun handleUIState(it: NoteUIState) {
        if (!isCategoryShown && it.baseNoteUIModel.category.id != -1) {
            isCategoryShown = true
            showSnippedBottomSheet(
                mBinding!!.bottomSheetContainer.id,
                SelectCategoryFragment::class.java,
                SelectCategoryFragmentArgs(it.baseNoteUIModel.category).toBundle(),
                activityViewModel = categoryViewModel,
                {
                    handleBottomSheetEvent(it)
                },
                SelectCategoryUIEvent.Dismiss
            )
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.save()
    }

    override fun handleUIEvent(it: NoteUIEvent) {
        when (it) {
            else -> {}
        }
    }
}

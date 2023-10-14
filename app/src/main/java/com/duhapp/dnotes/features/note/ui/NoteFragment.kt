package com.duhapp.dnotes.features.note.ui

import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.duhapp.dnotes.R
import com.duhapp.dnotes.databinding.FragmentNoteBinding
import com.duhapp.dnotes.features.base.ui.BaseFragment
import com.duhapp.dnotes.features.base.ui.showSnippedBottomSheet
import com.duhapp.dnotes.features.home.home_screen_category.ui.BaseNoteUIModel
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
    override val layoutId: Int
        get() = R.layout.fragment_note

    override val titleId: Int
        get() = R.string.Note_Fragment

    private val noteViewModel: NoteViewModel by viewModels()
    private val categoryViewModel: SelectCategoryViewModel by activityViewModels()
    override fun initView(binding: FragmentNoteBinding) {
        viewModel.initState(NoteFragmentArgs.fromBundle(requireArguments()))
        initBottomSheet()
    }

    private fun initBottomSheet() {
        with(binding.bottomSheetCategory) {
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

    override fun provideViewModel(): NoteViewModel = noteViewModel

    override fun setBindingViewModel() {
        binding.viewModel = viewModel
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
        super.handleUIState(it)
        val noteUIModel: BaseNoteUIModel? = NoteUIStateFunctions.getSuccessStateData(it)?.baseNoteUIModel
        if (!isCategoryShown && noteUIModel != null && noteUIModel.category.id != -1) {
            isCategoryShown = true
            showSnippedBottomSheet(
                binding.bottomSheetContainer.id,
                SelectCategoryFragment::class.java,
                SelectCategoryFragmentArgs(noteUIModel.category).toBundle(),
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

    override fun handleUIEvent(it: NoteUIEvent) {}
}

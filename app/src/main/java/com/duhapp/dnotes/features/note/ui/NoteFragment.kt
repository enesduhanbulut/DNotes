package com.duhapp.dnotes.features.note.ui

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
        if (!isCategoryShown && it.baseNoteUIModel.category.id != -1) {
            isCategoryShown = true
            showSnippedBottomSheet(
                binding.bottomSheetContainer.id,
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

    override fun handleUIEvent(it: NoteUIEvent) {
        when (it) {
            else -> {}
        }
    }
}

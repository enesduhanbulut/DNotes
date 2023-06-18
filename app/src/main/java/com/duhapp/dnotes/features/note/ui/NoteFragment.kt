package com.duhapp.dnotes.features.note.ui

import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.duhapp.dnotes.R
import com.duhapp.dnotes.databinding.FragmentNoteBinding
import com.duhapp.dnotes.features.base.ui.BaseFragment
import com.duhapp.dnotes.features.select_category.ui.SelectCategoryFragment
import com.duhapp.dnotes.features.select_category.ui.SelectCategoryUIEvent
import com.duhapp.dnotes.features.select_category.ui.SelectCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteFragment :
    BaseFragment<FragmentNoteBinding, NoteUIEvent, NoteUIState, NoteViewModel>() {

    override val layoutId: Int
        get() = R.layout.fragment_note

    override val titleId: Int
        get() = R.string.Note_Fragment

    private val noteViewModel: NoteViewModel by viewModels()
    private val categoryViewModel: SelectCategoryViewModel by activityViewModels()
    override fun initView(binding: FragmentNoteBinding) {
        observeUIState()
        viewModel.initState()
    }

    override fun provideViewModel(): NoteViewModel = noteViewModel

    override fun setBindingViewModel() {
        binding.viewModel = viewModel
    }

    override fun handleUIEvent(it: NoteUIEvent) {
        when (it) {
            is NoteUIEvent.NavigateSelectCategory -> {
                showBottomSheet(
                    SelectCategoryFragment(),
                    null,
                    categoryViewModel,
                ) { event ->
                    when (event) {
                        is SelectCategoryUIEvent.OnCategorySelected -> {
                            viewModel.onCategorySelected(event.category)
                        }

                        else -> {}
                    }
                }
            }
            else -> {}
        }
    }
}

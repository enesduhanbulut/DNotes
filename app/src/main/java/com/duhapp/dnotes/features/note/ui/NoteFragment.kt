package com.duhapp.dnotes.features.note.ui

import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.duhapp.dnotes.R
import com.duhapp.dnotes.databinding.FragmentNoteBinding
import com.duhapp.dnotes.features.base.ui.BaseFragment
import com.duhapp.dnotes.features.select_category.ui.SelectCategoryFragment
import com.duhapp.dnotes.features.select_category.ui.SelectCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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
        viewLifecycleOwner.lifecycleScope.launch {
            categoryViewModel.uiEvent.collect {
                Toast.makeText(requireContext(), "Category Selected", Toast.LENGTH_SHORT).show()
            }
        }
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
                    categoryViewModel
                ) {
                    Toast.makeText(requireContext(), "Category Selected", Toast.LENGTH_SHORT).show()
                }
            }

            else -> {}
        }
    }
}

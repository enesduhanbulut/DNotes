package com.duhapp.dnotes.features.note.ui

import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.duhapp.dnotes.R
import com.duhapp.dnotes.databinding.FragmentNoteBinding
import com.duhapp.dnotes.features.base.ui.BaseFragment
import com.duhapp.dnotes.features.manage_category.ui.ManageCategoryUIEvent
import com.duhapp.dnotes.features.select_category.ui.SelectCategoryUIEvent
import com.duhapp.dnotes.features.select_category.ui.SelectCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class NoteFragment :
    BaseFragment<FragmentNoteBinding, NoteUIEvent, NoteUIState, NoteViewModel>() {

    override val layoutId: Int
        get() = R.layout.fragment_note

    override val titleId: Int
        get() = R.string.Note_Fragment

    private val noteViewModel: NoteViewModel by viewModels()
    private val categoryViewModel: SelectCategoryViewModel by viewModels()

    override fun initView(binding: FragmentNoteBinding) {
        observeUIState()
        categoryViewModel.uiEvent.flowWithLifecycle(lifecycle)
            .onEach {
                when (it) {
                    is SelectCategoryUIEvent.OnCategorySelected -> {
                        viewModel.onCategorySelected(it.category)
                    }

                    else -> {}
                }
            }
            .launchIn(lifecycleScope)
    }

    override fun provideViewModel(): NoteViewModel = noteViewModel

    override fun setBindingViewModel() {
        binding.viewModel = viewModel
    }

    override fun handleUIEvent(it: NoteUIEvent) {
        when (it) {
            is NoteUIEvent.NavigateSelectCategory -> {
                categoryViewModel.setEvent(SelectCategoryUIEvent.Idle)
                findNavController().navigate(
                    NoteFragmentDirections.actionNoteFragmentToSelectCategoryFragment(),
                )
            }

            else -> {}
        }
    }
}

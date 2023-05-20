package com.duhapp.dnotes.features.note.ui

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.duhapp.dnotes.R
import com.duhapp.dnotes.databinding.FragmentNoteBinding
import com.duhapp.dnotes.features.base.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteFragment :
    BaseFragment<FragmentNoteBinding, NoteUIEvent, NoteUIState, NoteViewModel>() {

    override val layoutId: Int
        get() = R.layout.fragment_note

    override val titleId: Int
        get() = R.string.note_fragment

    private val noteViewModel: NoteViewModel by viewModels()
    private val navArgs by navArgs<NoteFragmentArgs>()

    override fun initView(binding: FragmentNoteBinding) {
        observeUIState()
        viewModel.initUIState(navArgs.category)
    }

    override fun provideViewModel(): NoteViewModel = noteViewModel

    override fun setBindingViewModel() {
        binding.viewModel = viewModel
    }

    override fun handleUIEvent(it: NoteUIEvent) {}
}

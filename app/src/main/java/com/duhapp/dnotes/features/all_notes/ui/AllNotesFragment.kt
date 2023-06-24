package com.duhapp.dnotes.features.all_notes.ui

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.duhapp.dnotes.R
import com.duhapp.dnotes.databinding.FragmentAllNotesBinding
import com.duhapp.dnotes.features.all_notes.NoteItemListAdapter
import com.duhapp.dnotes.features.base.ui.BaseFragment
import com.duhapp.dnotes.features.base.ui.BaseListAdapter
import com.duhapp.dnotes.ui.custom_views.AutoColumnGridLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllNotesFragment :
    BaseFragment<FragmentAllNotesBinding, AllNotesEvent, AllNotesState, AllNotesViewModel>() {
    override val layoutId: Int
        get() = R.layout.fragment_all_notes
    override val titleId: Int
        get() = R.string.default_title_all_notes
    lateinit var adapter: NoteItemListAdapter
    private val allNotesViewModel: AllNotesViewModel by viewModels()

    override fun initView(binding: FragmentAllNotesBinding) {
        arguments?.let {
            val args = AllNotesFragmentArgs.fromBundle(it)
            allNotesViewModel.initiate(args.categoryId)
        }
        binding.lifecycleOwner = viewLifecycleOwner
        binding.adapter = NoteItemListAdapter().apply {
            onItemClickListener = BaseListAdapter.OnItemClickListener { noteUIModel, _ ->
                viewModel.onNoteClick(noteUIModel)
            }
        }
        val widthOfItem = context?.resources?.getDimension(R.dimen.note_list_item_width)
        binding.notes.layoutManager = AutoColumnGridLayout(widthOfItem!!.toInt(), requireContext(), 2,GridLayoutManager.VERTICAL, false)
        adapter = binding.adapter!!
    }

    override fun provideViewModel(): AllNotesViewModel {
        return allNotesViewModel
    }

    override fun setBindingViewModel() {
        binding.viewModel = viewModel
    }

    override fun handleUIEvent(it: AllNotesEvent) {
        when (it) {
            is AllNotesEvent.OnNoteClicked -> {
                val action =
                    AllNotesFragmentDirections.actionAllNotesFragmentToNavigationNote(it.noteUIModel)
                findNavController().navigate(action)
            }
            else -> {}
        }
    }

    override fun handleUIState(it: AllNotesState) {
        super.handleUIState(it)
        if (it.notes.isEmpty()) {
            return
        }
        adapter.setItems(it.notes)
    }
}
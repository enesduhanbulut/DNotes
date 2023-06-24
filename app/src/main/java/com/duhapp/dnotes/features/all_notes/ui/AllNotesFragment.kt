package com.duhapp.dnotes.features.all_notes.ui

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.duhapp.dnotes.R
import com.duhapp.dnotes.databinding.FragmentAllNotesBinding
import com.duhapp.dnotes.databinding.LayoutBasicNoteListItemBinding
import com.duhapp.dnotes.features.base.ui.BaseFragment
import com.duhapp.dnotes.features.base.ui.BaseListAdapter
import com.duhapp.dnotes.features.home.home_screen_category.ui.BaseNoteUIModel
import com.duhapp.dnotes.features.home.home_screen_category.ui.BasicNoteUIModel
import com.duhapp.dnotes.ui.custom_views.AutoColumnGridLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllNotesFragment :
    BaseFragment<FragmentAllNotesBinding, AllNotesEvent, AllNotesState, AllNotesViewModel>() {
    override val layoutId: Int
        get() = R.layout.fragment_all_notes
    override val titleId: Int
        get() = R.string.default_title_all_notes
    lateinit var adapter: BaseListAdapter<BaseNoteUIModel, LayoutBasicNoteListItemBinding>
    private val allNotesViewModel: AllNotesViewModel by viewModels()

    override fun initView(binding: FragmentAllNotesBinding) {
        arguments?.let {
            val args = AllNotesFragmentArgs.fromBundle(it)
            allNotesViewModel.initiate(args.categoryId)
        }
        binding.lifecycleOwner = viewLifecycleOwner
        adapter = object : BaseListAdapter<BaseNoteUIModel, LayoutBasicNoteListItemBinding>() {
            override fun getLayoutId(): Int {
                return R.layout.layout_basic_note_list_item
            }

            override fun setUIState(
                binding: LayoutBasicNoteListItemBinding,
                item: BaseNoteUIModel
            ) {
                binding.item = item as BasicNoteUIModel
            }
        }
        adapter.onItemClickListener = BaseListAdapter.OnItemClickListener { noteUIModel, _ ->
            viewModel.onNoteClick(noteUIModel)
        }
        binding.adapter = adapter
        val widthOfItem = context?.resources?.getDimension(R.dimen.note_list_item_width)
        binding.notes.layoutManager = AutoColumnGridLayout(
            widthOfItem!!.toInt(),
            requireContext(),
            2,
            GridLayoutManager.VERTICAL,
            false
        )
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
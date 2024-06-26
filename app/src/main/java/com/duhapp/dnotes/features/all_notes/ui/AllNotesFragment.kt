package com.duhapp.dnotes.features.all_notes.ui

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.duhapp.dnotes.R
import com.duhapp.dnotes.databinding.FragmentAllNotesBinding
import com.duhapp.dnotes.databinding.LayoutBasicNoteListItemBinding
import com.duhapp.dnotes.features.base.ui.BaseFragment
import com.duhapp.dnotes.features.base.ui.BaseListAdapter
import com.duhapp.dnotes.features.base.ui.MenuContainer
import com.duhapp.dnotes.features.base.ui.setup
import com.duhapp.dnotes.features.base.ui.showBottomSheet
import com.duhapp.dnotes.features.generic.ui.SpaceModel
import com.duhapp.dnotes.features.home.home_screen_category.ui.BaseNoteUIModel
import com.duhapp.dnotes.features.home.home_screen_category.ui.BasicNoteUIModel
import com.duhapp.dnotes.features.select_category.ui.SelectCategoryFragment
import com.duhapp.dnotes.features.select_category.ui.SelectCategoryFragmentArgs
import com.duhapp.dnotes.features.select_category.ui.SelectCategoryUIEvent
import com.duhapp.dnotes.features.select_category.ui.SelectCategoryViewModel
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class AllNotesFragment :
    BaseFragment<FragmentAllNotesBinding, AllNotesEvent, AllNotesState, AllNotesViewModel>() {
    lateinit var adapter: BaseListAdapter<BaseNoteUIModel, LayoutBasicNoteListItemBinding>
    override val layoutId = R.layout.fragment_all_notes
    override val titleId = R.string.default_title_all_notes
    override val viewModel: AllNotesViewModel by viewModels()
    override val fragmentTag = "AllNotesFragment"
    private val selectCategoryViewModel: SelectCategoryViewModel by activityViewModels()
    private lateinit var menuContainer: MenuContainer
    private val TAG = "AllNotesFragment"
    override fun initView(binding: FragmentAllNotesBinding) {
        initAdapter()
        menuContainer = MenuContainer(
            mBinding!!.selectActionMenu.id,
            R.menu.note_item_general_menu,
            menuItemClickListener = {
                when (it.itemId) {
                    R.id.delete_notes -> {
                        viewModel.deleteSelectedNotes()
                        true
                    }

                    R.id.move_notes -> {
                        viewModel.moveSelectedNotes()
                        true
                    }

                    else -> {
                        false
                    }
                }
            }
        )
        menuContainer.defineMenu(mBinding!!.root)
    }

    private fun initAdapter() {
        val spaceModel = SpaceModel(16)

        val layout = FlexboxLayoutManager(context)
        layout.justifyContent = JustifyContent.CENTER
        layout.alignItems = AlignItems.FLEX_START
        layout.flexDirection = FlexDirection.ROW
        layout.flexWrap = FlexWrap.WRAP

        adapter = object : BaseListAdapter<BaseNoteUIModel, LayoutBasicNoteListItemBinding>() {
            override fun getLayoutId(): Int {
                return R.layout.layout_basic_note_list_item
            }

            override fun setUIState(
                binding: LayoutBasicNoteListItemBinding,
                item: BaseNoteUIModel
            ) {
                binding.item = (item as BasicNoteUIModel)
                binding.hasSelectMode = true
            }

            override fun initializeMenu(binding: LayoutBasicNoteListItemBinding): MenuContainer {
                return MenuContainer(
                    binding.selectActionMenu.id,
                    R.menu.single_note_item_menu
                ) { menuItem ->
                    val baseNoteItem: BaseNoteUIModel = binding.item as BaseNoteUIModel
                    when (menuItem.itemId) {
                        R.id.delete_note -> {
                            viewModel.onDeleteNoteClick(baseNoteItem)
                            true
                        }

                        R.id.edit_note -> {
                            viewModel.onEditNoteClick(baseNoteItem)
                            true
                        }

                        R.id.select_note -> {
                            viewModel.enableSelectionModeAndSelectANote(baseNoteItem)
                            true
                        }

                        R.id.move_note -> {
                            viewModel.onMoveNoteClick(baseNoteItem)
                            true
                        }

                        else -> {
                            false
                        }
                    }
                }
            }
        }
        adapter.onItemClickListener = BaseListAdapter.OnItemClickListener { noteUIModel, _ ->
            viewModel.onNoteClick(noteUIModel)
        }
        adapter.onItemLongClickListener =
            BaseListAdapter.OnItemLongClickListener { noteUIModel, _ ->
                viewModel.enableSelectionModeAndSelectANote(noteUIModel)
            }
        mBinding!!.notes.setup(
            adapter,
            layoutManager = layout,
            spaceModel = spaceModel
        )
    }

    override fun setBindingViewModel() {
        mBinding!!.viewModel = viewModel
    }

    override fun handleArgs(args: Bundle) {
        val arguments = AllNotesFragmentArgs.fromBundle(args)
        viewModel.initiate(arguments.categoryId)
    }

    override fun handleUIEvent(it: AllNotesEvent) {
        when (it) {
            is AllNotesEvent.OnEditNoteEvent -> {
                val action =
                    AllNotesFragmentDirections.actionAllNotesFragmentToNavigationNote(it.noteUIModel)
                findNavController().navigate(action)
            }

            is AllNotesEvent.OnMoveAnotherCategoryEvent -> {
                val fragment = SelectCategoryFragment()

                showBottomSheet(
                    fragment = fragment,
                    SelectCategoryFragmentArgs(viewModel.uiState.value!!.getSuccessCategory()).toBundle(),
                    selectCategoryViewModel
                ) { uiEvent ->
                    when (uiEvent) {
                        is SelectCategoryUIEvent.OnCategorySelected -> {
                            viewModel.onMoveActionTriggered(it.noteUIModel, uiEvent.category)
                        }

                        else -> Timber.d(TAG, "Unhandled event: $uiEvent")
                    }
                }

            }
            is AllNotesEvent.CloseBottomSheet -> {
                selectCategoryViewModel.dismissFragment()
            }
            is AllNotesEvent.NavigateToHome -> {
                findNavController().popBackStack()
            }

            else -> Timber.d( "Unhandled event: $it")
        }
    }

    override fun handleUIState(it: AllNotesState) {
        if (it is AllNotesState.Success) {
            adapter.setItems(it.notes)
        }
    }

    override fun onDestroy() {
        menuContainer.destroyMenu()
        super.onDestroy()
    }
}
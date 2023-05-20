package com.duhapp.dnotes.features.select_category.ui

import android.view.View
import android.widget.PopupMenu
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.duhapp.dnotes.R
import com.duhapp.dnotes.databinding.CategoryListItemBinding
import com.duhapp.dnotes.databinding.FragmentSelectCategoryBinding
import com.duhapp.dnotes.features.add_or_update_category.ui.CategoryShowType
import com.duhapp.dnotes.features.add_or_update_category.ui.CategoryUIModel
import com.duhapp.dnotes.features.base.ui.BaseFragment
import com.duhapp.dnotes.features.base.ui.BaseListAdapter
import com.duhapp.dnotes.features.generic.ui.SpaceModel
import com.duhapp.dnotes.features.generic.ui.SpacingItemDecorator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectCategoryFragment :
    BaseFragment<
        FragmentSelectCategoryBinding, SelectCategoryUIEvent,
        SelectCategoryUIState, SelectCategoryViewModel,>() {
    override val layoutId: Int
        get() = R.layout.fragment_select_category
    override val titleId: Int
        get() = R.string.Select_Category

    private lateinit var adapter: BaseListAdapter<CategoryUIModel, CategoryListItemBinding>
    private val selectCategoryViewModel: SelectCategoryViewModel by viewModels()
    override fun initView(binding: FragmentSelectCategoryBinding) {
        binding.categories.addItemDecoration(
            SpacingItemDecorator(
                SpaceModel(
                    16,
                    16,
                    16,
                    16,
                ),
            ),
        )
        observeUIState()
    }

    override fun provideViewModel(): SelectCategoryViewModel {
        return selectCategoryViewModel
    }

    override fun setBindingViewModel() {
        binding.viewModel = viewModel
        initAdapter()
    }

    private fun initAdapter() {
        val menuClickListener = MenuClickListener { categoryUIModel, view ->
            handleMenuClick(categoryUIModel, view)
        }
        adapter = object : BaseListAdapter<CategoryUIModel, CategoryListItemBinding>() {
            override fun getLayoutId(): Int {
                return R.layout.category_list_item
            }

            override fun setUIState(
                binding: CategoryListItemBinding,
                item: CategoryUIModel,
            ) {
                binding.uiModel = item
                binding.menuClickListener = menuClickListener
            }
        }
        adapter.onItemClickListener =
            BaseListAdapter.OnItemClickListener(viewModel::handleCategorySelect)
        binding.categories.adapter = adapter
    }

    private fun handleMenuClick(categoryUIModel: CategoryUIModel, view: View) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.menuInflater.inflate(R.menu.category_item_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.edit -> {
                    navigateToCategoryBottomSheet(categoryUIModel, CategoryShowType.Edit)
                    true
                }

                R.id.delete -> {
                    viewModel.handleDeleteCategory(categoryUIModel)
                    true
                }

                else -> false
            }
        }
        // Showing the popup menu
        // Showing the popup menu
        popupMenu.show()
    }

    private fun navigateToCategoryBottomSheet(
        categoryUIModel: CategoryUIModel,
        showType: CategoryShowType,
    ) {
        findNavController().navigate(
            SelectCategoryFragmentDirections.actionSelectCategoryFragmentToCategoryBottomSheet(
                categoryUIModel,
                showType,
            ),
        )
    }

    override fun handleUIEvent(it: SelectCategoryUIEvent) {
        when (it) {
            is SelectCategoryUIEvent.NavigateAddCategory -> {
                findNavController().navigate(
                    SelectCategoryFragmentDirections.actionSelectCategoryFragmentToCategoryBottomSheet(
                        CategoryUIModel(
                            -1,
                            "",
                            String(Character.toChars(0x1F4DD)),
                            "",
                            R.color.primary_color,
                        ),
                        CategoryShowType.Add,
                    ),
                )
            }

            is SelectCategoryUIEvent.OnCategorySelected -> {
                this.findNavController().navigate(
                    SelectCategoryFragmentDirections.actionSelectCategoryFragmentToNoteFragment(
                        it.category,
                    ),
                )
            }

            else -> {}
        }
    }

    override fun handleUIState(it: SelectCategoryUIState) {
        super.handleUIState(it)
        adapter.setItems(it.categoryList)
    }

    fun interface MenuClickListener {
        fun onMenuClick(categoryUIModel: CategoryUIModel, view: View)
    }
}

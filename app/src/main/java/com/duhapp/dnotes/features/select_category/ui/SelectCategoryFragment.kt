package com.duhapp.dnotes.features.select_category.ui

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.duhapp.dnotes.R
import com.duhapp.dnotes.databinding.CategoryListItemBinding
import com.duhapp.dnotes.databinding.FragmentSelectCategoryBinding
import com.duhapp.dnotes.features.add_or_update_category.ui.CategoryUIModel
import com.duhapp.dnotes.features.base.ui.BaseBottomSheet
import com.duhapp.dnotes.features.base.ui.BaseListAdapter
import com.duhapp.dnotes.features.generic.ui.SpaceModel
import com.duhapp.dnotes.features.generic.ui.SpacingItemDecorator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectCategoryFragment :
    BaseBottomSheet<SelectCategoryUIEvent, SelectCategoryUIState, SelectCategoryViewModel, FragmentSelectCategoryBinding>() {

    private val selectCategoryViewModel: SelectCategoryViewModel by viewModels()
    private lateinit var adapter: BaseListAdapter<CategoryUIModel, CategoryListItemBinding>

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
    }

    private fun initAdapter() {
        adapter = object : BaseListAdapter<CategoryUIModel, CategoryListItemBinding>() {
            override fun getLayoutId(): Int {
                return R.layout.category_list_item
            }

            override fun setUIState(
                binding: CategoryListItemBinding,
                item: CategoryUIModel,
            ) {
                binding.uiModel = item
            }
        }
        adapter.onItemClickListener =
            BaseListAdapter.OnItemClickListener { categoryUIModel, _ ->
                viewModel.handleCategorySelect(categoryUIModel)
            }
        binding.categories.adapter = adapter
    }

    override val layoutId = R.layout.fragment_select_category
    override val fragmentTag = "SelectCategoryFragment"

    override fun provideViewModel(): SelectCategoryViewModel {
        return selectCategoryViewModel
    }

    override fun setBindingViewModel() {
        binding.viewModel = viewModel
        initAdapter()
    }

    override fun handleUIEvent(event: SelectCategoryUIEvent) {
        when (event) {
            is SelectCategoryUIEvent.OnCategorySelected -> {
                findNavController().previousBackStackEntry?.savedStateHandle?.set(
                    "category",
                    event.category,
                )
                findNavController().popBackStack()
            }
        }
    }
}

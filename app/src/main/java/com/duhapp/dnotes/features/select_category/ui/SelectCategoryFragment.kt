package com.duhapp.dnotes.features.select_category.ui

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.webkit.internal.ApiFeature
import com.duhapp.dnotes.R
import com.duhapp.dnotes.databinding.CategorySelectListItemBinding
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
    override val layoutId = R.layout.fragment_select_category
    override val fragmentTag = "SelectCategoryFragment"
    private val selectCategoryViewModel: SelectCategoryViewModel by activityViewModels()
    private lateinit var adapter: BaseListAdapter<CategoryUIModel, CategorySelectListItemBinding>

    override fun handleArgs(args: Bundle) {
        viewModel.initState(
            SelectCategoryFragmentArgs.fromBundle(args).selectedCategory
                ?: CategoryUIModel()
        )
    }

    override fun initView(binding: FragmentSelectCategoryBinding) {
        observeUIState()
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
        adapter = object : BaseListAdapter<CategoryUIModel, CategorySelectListItemBinding>() {
            override fun getLayoutId(): Int {
                return R.layout.category_select_list_item
            }

            override fun setUIState(binding: CategorySelectListItemBinding, item: CategoryUIModel) {
                binding.uiModel = item
            }
        }
        adapter.onItemClickListener =
            BaseListAdapter.OnItemClickListener { categoryUIModel, _ ->
                viewModel.handleCategorySelect(categoryUIModel)
            }
        binding.categories.adapter = adapter
    }

    override fun provideViewModel(): SelectCategoryViewModel {
        return selectCategoryViewModel
    }

    override fun setBindingViewModel() {
        binding.viewModel = viewModel
        initAdapter()
    }

    override fun handleUIEvent(event: SelectCategoryUIEvent) {}

    override fun handleUIState(it: SelectCategoryUIState) {
        super.handleUIState(it)
        SelectCategoryUIStateFunctions.getSuccessStateData(it)?.let {
            adapter.setItems(it.categories)
        } ?: adapter.setItems(emptyList())
    }
}

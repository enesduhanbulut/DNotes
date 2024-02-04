package com.duhapp.dnotes.features.select_category.ui

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import com.duhapp.dnotes.R
import com.duhapp.dnotes.databinding.CategorySelectListItemBinding
import com.duhapp.dnotes.databinding.FragmentSelectCategoryBinding
import com.duhapp.dnotes.features.add_or_update_category.ui.CategoryUIModel
import com.duhapp.dnotes.features.base.ui.BaseBottomSheet
import com.duhapp.dnotes.features.base.ui.BaseListAdapter
import com.duhapp.dnotes.features.base.ui.setup
import com.duhapp.dnotes.features.generic.ui.SpaceModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class SelectCategoryFragment :
    BaseBottomSheet<FragmentSelectCategoryBinding, SelectCategoryUIEvent, SelectCategoryUIState, SelectCategoryViewModel>() {
    override val layoutId = R.layout.fragment_select_category
    override val titleId = R.string.Select_Category
    override val fragmentTag = "SelectCategoryFragment"
    override val viewModel: SelectCategoryViewModel by activityViewModels()
    private lateinit var adapter: BaseListAdapter<CategoryUIModel, CategorySelectListItemBinding>

    override fun handleArgs(args: Bundle) {
        viewModel.initState(
            SelectCategoryFragmentArgs.fromBundle(args).selectedCategory
                ?: CategoryUIModel()
        )
    }

    override fun initView(binding: FragmentSelectCategoryBinding) {
        initAdapter()
        binding.categories.setup(
            adapter = adapter,
            spaceModel = SpaceModel(
                resources.getDimensionPixelSize(R.dimen.tiny_margin),
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
        mBinding!!.categories.adapter = adapter
    }

    override fun setBindingViewModel() {
        mBinding!!.viewModel = viewModel
    }

    override fun handleUIEvent(it: SelectCategoryUIEvent) {
        when (it) {
            is SelectCategoryUIEvent.Dismiss -> {
                dismiss()
            }

            else -> {
                Timber.d("Unhandled event: $it")
            }
        }
    }

    override fun handleUIState(it: SelectCategoryUIState) {
        adapter.setItems(it.getSuccessCategories() ?: emptyList())
    }

    override fun onDestroy() {
        super.onDestroy()
        adapter.onItemClickListener = null
    }
}

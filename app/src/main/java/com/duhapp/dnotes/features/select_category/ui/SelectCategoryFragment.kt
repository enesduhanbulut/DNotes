package com.duhapp.dnotes.features.select_category.ui

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.duhapp.dnotes.R
import com.duhapp.dnotes.databinding.FragmentSelectCategoryBinding
import com.duhapp.dnotes.features.add_or_update_category.ui.CategoryShowType
import com.duhapp.dnotes.features.add_or_update_category.ui.CategoryUIModel
import com.duhapp.dnotes.features.base.ui.BaseFragment
import com.duhapp.dnotes.features.generic.ui.SpaceModel
import com.duhapp.dnotes.features.generic.ui.SpacingItemDecorator

class SelectCategoryFragment :
    BaseFragment<
            FragmentSelectCategoryBinding, SelectCategoryUIEvent,
            SelectCategoryUIState, SelectCategoryViewModel>() {
    override val layoutId: Int
        get() = R.layout.fragment_select_category
    override val titleId: Int
        get() = R.string.Select_Category
    private val selectCategoryViewModel: SelectCategoryViewModel by viewModels()
    override fun initView(binding: FragmentSelectCategoryBinding) {
        binding.categories.addItemDecoration(
            SpacingItemDecorator(
                SpaceModel(
                    16,
                    16,
                    16,
                    16
                )
            )
        )
    }

    override fun provideViewModel(): SelectCategoryViewModel {
        return selectCategoryViewModel
    }

    override fun setBindingViewModel() {
        binding.viewModel = viewModel
    }

    override fun handleUIEvent(it: SelectCategoryUIEvent) {
        when (it) {
            is SelectCategoryUIEvent.NavigateAddCategory -> {
                findNavController().navigate(
                    SelectCategoryFragmentDirections.actionSelectCategoryFragmentToCategoryBottomSheet(
                        CategoryUIModel(
                            -1,
                            "",
                            "",
                        ), CategoryShowType.Add
                    )
                )
            }

            else -> {}
        }
    }
}
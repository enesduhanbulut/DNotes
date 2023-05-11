package com.duhapp.dnotes.select_category.ui

import androidx.fragment.app.viewModels
import com.duhapp.dnotes.R
import com.duhapp.dnotes.base.ui.BaseFragment
import com.duhapp.dnotes.databinding.FragmentSelectCategoryBinding
import com.duhapp.dnotes.generic.ui.SpaceModel

class SelectCategoryFragment :
    BaseFragment<
            FragmentSelectCategoryBinding, SelectCategoryUIEvent,
            SelectCategoryUIState, SelectCategoryViewModel>() {
    override val layoutId: Int
        get() = R.layout.fragment_select_category
    private val selectCategoryViewModel: SelectCategoryViewModel by viewModels()
    override fun initView(binding: FragmentSelectCategoryBinding) {
        binding.categories.addItemDecoration(
            com.duhapp.dnotes.generic.ui.SpacingItemDecorator(
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

    override fun handleUIEvent(it: SelectCategoryUIEvent) {}
}
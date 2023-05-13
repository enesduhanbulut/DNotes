package com.duhapp.dnotes.features.add_or_update_category.ui

import androidx.fragment.app.viewModels
import com.duhapp.dnotes.R
import com.duhapp.dnotes.databinding.FragmentCategoryBottomSheetBinding
import com.duhapp.dnotes.features.base.ui.BaseBottomSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryBottomSheet : BaseBottomSheet<
        CategoryBottomSheetUIEvent,
        CategoryBottomSheetUIState,
        CategoryBottomSheetViewModel,
        FragmentCategoryBottomSheetBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_category_bottom_sheet
    override val fragmentTag: String
        get() = "CategoryBottomSheet"
    private val categoryBottomSheetViewModel: CategoryBottomSheetViewModel by viewModels()

    override fun initView(binding: FragmentCategoryBottomSheetBinding) {
        arguments.let { it ->
            CategoryBottomSheetArgs.fromBundle(it!!).let {
                viewModel.setViewWithBundle(it.uiModel, it.categoryShowType)
            }
        }
    }

    override fun provideViewModel(): CategoryBottomSheetViewModel {
        return categoryBottomSheetViewModel
    }

    override fun setBindingViewModel() {
        binding.viewModel = viewModel
    }

    override fun handleUIEvent(it: CategoryBottomSheetUIEvent) {
        when (it) {
            is CategoryBottomSheetUIEvent.Canceled -> {
                dismiss()
            }

            is CategoryBottomSheetUIEvent.Inserted -> {
                dismiss()
            }

            else -> {}
        }
    }

}
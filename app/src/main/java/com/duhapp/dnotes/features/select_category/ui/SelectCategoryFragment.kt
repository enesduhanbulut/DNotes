package com.duhapp.dnotes.features.select_category.ui

import android.widget.Toast
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
        SelectCategoryUIState, SelectCategoryViewModel,
        >() {
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
        adapter.onItemClickListener = BaseListAdapter.OnItemClickListener(viewModel::handleCategorySelect)
        binding.categories.adapter = adapter
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
                            "Test",
                            R.color.primary_color,
                        ),
                        CategoryShowType.Add,
                    ),
                )
            }

            is SelectCategoryUIEvent.OnCategorySelected -> {
                Toast.makeText(context, it.category.toString(), Toast.LENGTH_LONG).show()
            }
            else -> {}
        }
    }

    override fun handleUIState(it: SelectCategoryUIState) {
        super.handleUIState(it)
        adapter.setItems(it.categoryList)
    }
}

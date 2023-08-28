package com.duhapp.dnotes.features.manage_category.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.duhapp.dnotes.R
import com.duhapp.dnotes.databinding.CategoryListItemBinding
import com.duhapp.dnotes.databinding.FragmentManageCategoryBinding
import com.duhapp.dnotes.features.add_or_update_category.ui.CategoryBottomSheet
import com.duhapp.dnotes.features.add_or_update_category.ui.CategoryBottomSheetArgs
import com.duhapp.dnotes.features.add_or_update_category.ui.CategoryBottomSheetViewModel
import com.duhapp.dnotes.features.add_or_update_category.ui.CategoryShowType
import com.duhapp.dnotes.features.add_or_update_category.ui.CategoryUIEvent
import com.duhapp.dnotes.features.add_or_update_category.ui.CategoryUIModel
import com.duhapp.dnotes.features.base.ui.BaseFragment
import com.duhapp.dnotes.features.base.ui.BaseListAdapter
import com.duhapp.dnotes.features.base.ui.SwipeDirection
import com.duhapp.dnotes.features.base.ui.addSwipeListener
import com.duhapp.dnotes.features.base.ui.showBottomSheet
import com.duhapp.dnotes.features.generic.ui.SpaceModel
import com.duhapp.dnotes.features.generic.ui.SpacingItemDecorator
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ManageCategoryFragment :
    BaseFragment<
            FragmentManageCategoryBinding, ManageCategoryUIEvent,
            ManageCategoryUIState, ManageCategoryViewModel,
            >() {
    override val layoutId: Int
        get() = R.layout.fragment_manage_category
    override val titleId: Int
        get() = R.string.Select_Category
    override val viewModel: ManageCategoryViewModel by viewModels()
    override val fragmentTag = "ManageCategoryFragment"

    private lateinit var adapter: BaseListAdapter<CategoryUIModel, CategoryListItemBinding>
    private val categoryBottomSheetViewModel: CategoryBottomSheetViewModel by activityViewModels()
    override fun initView(binding: FragmentManageCategoryBinding) {
        binding.categories.addItemDecoration(
            SpacingItemDecorator(
                SpaceModel(
                    bottomSpace = 24
                ),
            ),
        )
    }

    override fun setBindingViewModel() {
        mBinding?.viewModel = viewModel
        initAdapter()
    }

    override fun handleArgs(args: Bundle) {
        TODO("Not yet implemented")
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
            BaseListAdapter.OnItemClickListener(viewModel::handleCategorySelect)
        // TODO There is a warning at resources.getDrawable, Use ResourcesCompat.getDrawable()
        mBinding!!.categories.addSwipeListener(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.baseline_delete_24,
                requireActivity().theme
            )!!,
            ColorDrawable(Color.RED),
            {},
            { position ->
                viewModel.handleDeleteCategory(adapter.items[position])
            },
            SwipeDirection.LEFT,
        )
        mBinding!!.categories.adapter = adapter
    }

    override fun handleUIEvent(it: ManageCategoryUIEvent) {
        when (it) {
            is ManageCategoryUIEvent.NavigateAddCategory -> {
                showCategoryScreen(CategoryUIModel(), CategoryShowType.Add)
            }

            is ManageCategoryUIEvent.OnCategorySelected -> {
                showCategoryScreen(it.category.copy(), CategoryShowType.Edit)
            }

            is ManageCategoryUIEvent.OnCategoryDeleted -> {
                Snackbar.make(
                    mBinding!!.root,
                    R.string.Item_deleted_do_you_want_to_undo,
                    Snackbar.LENGTH_LONG,
                ).setAction(R.string.Undo) { _ ->
                    viewModel.onUndoDelete()
                }.show()
            }

            else -> {
            }
        }
    }

    private fun showCategoryScreen(categoryUIModel: CategoryUIModel, showType: CategoryShowType) {
        showBottomSheet(
            CategoryBottomSheet(),
            activityViewModel = categoryBottomSheetViewModel,
            bundle = CategoryBottomSheetArgs(
                categoryUIModel,
                showType,
            ).toBundle(), collector = {
                handleBottomSheetResponse(it)
            }, unsubscribeEvent = CategoryUIEvent.Dismissed
        )
    }

    private fun handleBottomSheetResponse(it: CategoryUIEvent) {
        when (it) {
            is CategoryUIEvent.Upserted -> {
                viewModel.onCategoryUpserted()
            }

            else -> {
            }
        }
    }

    override fun handleUIState(it: ManageCategoryUIState) {
        if (it.categoryList.isEmpty()) {
            return
        }
        adapter.setItems(it.categoryList)
    }
}

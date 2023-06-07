package com.duhapp.dnotes.features.manage_category.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.Toast
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

    private lateinit var adapter: BaseListAdapter<CategoryUIModel, CategoryListItemBinding>
    private val manageCategoryViewModel: ManageCategoryViewModel by viewModels()
    private val categoryBottomSheetViewModel: CategoryBottomSheetViewModel by activityViewModels()
    override fun initView(binding: FragmentManageCategoryBinding) {
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
        setAppBarVisibility(View.GONE)
    }

    override fun provideViewModel(): ManageCategoryViewModel {
        return manageCategoryViewModel
    }

    override fun setBindingViewModel() {
        binding.viewModel = viewModel
        initAdapter()
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
        binding.categories.addSwipeListener(
            resources.getDrawable(R.drawable.baseline_lightbulb_24, requireActivity().theme),
            ColorDrawable(Color.RED), {
                Toast.makeText(requireContext(), "move", Toast.LENGTH_SHORT)
                    .show()
            },
            {
                //show a undo snackbar
                Snackbar.make(
                    binding.root,
                    R.string.Item_deleted_do_you_want_to_undo,
                    Snackbar.LENGTH_LONG
                ).setAction(R.string.Confirm) {
                    Toast.makeText(requireContext(), "undo", Toast.LENGTH_SHORT).show()
                }.show()

            }, SwipeDirection.LEFT
        )
        binding.categories.adapter = adapter
    }

    override fun handleUIEvent(it: ManageCategoryUIEvent) {
        when (it) {
            is ManageCategoryUIEvent.NavigateAddCategory -> {
                showBottomSheet(CategoryUIModel(), CategoryShowType.Add)
            }

            is ManageCategoryUIEvent.OnCategorySelected -> {
                showBottomSheet(it.category, CategoryShowType.Edit)
            }

            else -> {}
        }
    }

    private fun showBottomSheet(categoryUIModel: CategoryUIModel, showType: CategoryShowType) {
        showBottomSheet(
            CategoryBottomSheet(),
            activityViewModel = categoryBottomSheetViewModel,
            bundle = CategoryBottomSheetArgs(
                categoryUIModel,
                showType,
            ).toBundle(), singleEventCollector = {
                handleBottomSheetResponse(it)
            })
    }

    private fun handleBottomSheetResponse(it: CategoryUIEvent) {
        when (it) {
            is CategoryUIEvent.Inserted -> {
                viewModel.onCategoryAdded()
            }

            is CategoryUIEvent.Updated -> {
                viewModel.onCategoryUpdated()
            }

            else -> {}
        }
    }

    override fun handleUIState(it: ManageCategoryUIState) {
        super.handleUIState(it)
        if (it.categoryList.isEmpty()) {
            return
        }
        adapter.setItems(it.categoryList)
    }

    fun interface MenuClickListener {
        fun onMenuClick(categoryUIModel: CategoryUIModel, view: View)
    }
}

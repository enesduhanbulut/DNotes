package com.duhapp.dnotes.features.add_or_update_category.ui

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import com.duhan.satelliteinfo.features.base.presentation.BaseBottomSheet
import com.duhapp.dnotes.R
import com.duhapp.dnotes.databinding.FragmentCategoryBottomSheetBinding
import com.duhapp.dnotes.databinding.LayoutColorSelectorItemBinding
import com.duhapp.dnotes.features.base.ui.BaseListAdapter
import com.duhapp.dnotes.features.generic.ui.SpaceModel
import com.duhapp.dnotes.features.generic.ui.SpacingItemDecorator
import com.vanniktech.emoji.EmojiPopup
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryBottomSheet : BaseBottomSheet<
        FragmentCategoryBottomSheetBinding,
        CategoryUIEvent,
        CategoryBottomSheetUIState,
        CategoryBottomSheetViewModel>() {
    private lateinit var emojiPopup: EmojiPopup
    override val layoutId = R.layout.fragment_category_bottom_sheet
    override val titleId = R.string.Add_Category
    override val fragmentTag = "CategoryBottomSheet"
    override val viewModel: CategoryBottomSheetViewModel by activityViewModels()
    private lateinit var adapter: BaseListAdapter<ColorItemUIModel, LayoutColorSelectorItemBinding>

    override fun initView(binding: FragmentCategoryBottomSheetBinding) {
        arguments.let { it ->
            CategoryBottomSheetArgs.fromBundle(it!!).let {
                viewModel.setViewWithBundle(it.uiModel.copy(), it.categoryShowType)
            }
        }

        val onClickListener =
            BaseListAdapter.OnItemClickListener<ColorItemUIModel> { item, position ->
                viewModel.onColorSelected(item)
            }
        adapter = object :
            BaseListAdapter<ColorItemUIModel, LayoutColorSelectorItemBinding>() {
            override fun getLayoutId() = R.layout.layout_color_selector_item

            override fun setUIState(
                binding: LayoutColorSelectorItemBinding,
                item: ColorItemUIModel
            ) {
                binding.uiModel = item
            }

        }
        adapter.onItemClickListener = onClickListener
        mBinding!!.colorSelector.addItemDecoration(
            SpacingItemDecorator(
                SpaceModel(rightSpace = 32)
            ),
        )
        mBinding!!.adapter = adapter
    }

    override fun setBindingViewModel() {
        mBinding!!.viewModel = viewModel
    }

    override fun handleArgs(args: Bundle) {
        CategoryBottomSheetArgs.fromBundle(args).let {
            viewModel.setViewWithBundle(it.uiModel.copy(), it.categoryShowType)
        }
    }

    override fun handleUIState(it: CategoryBottomSheetUIState) {

    }

    override fun handleUIEvent(it: CategoryUIEvent) {
        when (it) {
            is CategoryUIEvent.Canceled, CategoryUIEvent.Upserted -> {
                dismiss()
            }

            is CategoryUIEvent.ColorSelected -> {

            }

            is CategoryUIEvent.ShowEmojiDialog -> {
                emojiPopup = EmojiPopup(
                    requireView(), onEmojiClickListener = {
                        mBinding!!.nameTextInputLayout.requestFocus()
                        viewModel.setEmoji(it.unicode)
                    }, editText = mBinding!!.categoryIcon
                )
                emojiPopup.toggle()
            }

            is CategoryUIEvent.DismissedEmojiDialog -> {
                emojiPopup.let {
                    if (it.isShowing) {
                        it.dismiss()
                    }
                }
            }

            else -> {}
        }
    }

    override fun dismiss() {
        super.dismiss()
        viewModel.onDismissed()
    }

}
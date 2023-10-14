package com.duhapp.dnotes.features.add_or_update_category.ui

import androidx.fragment.app.activityViewModels
import com.duhapp.dnotes.R
import com.duhapp.dnotes.databinding.FragmentCategoryBottomSheetBinding
import com.duhapp.dnotes.databinding.LayoutColorSelectorItemBinding
import com.duhapp.dnotes.features.base.ui.BaseBottomSheet
import com.duhapp.dnotes.features.base.ui.BaseListAdapter
import com.duhapp.dnotes.features.generic.ui.SpaceModel
import com.duhapp.dnotes.features.generic.ui.SpacingItemDecorator
import com.vanniktech.emoji.EmojiPopup
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryBottomSheet : BaseBottomSheet<
        CategoryUIEvent,
        CategoryBottomSheetUIState,
        CategoryBottomSheetViewModel,
        FragmentCategoryBottomSheetBinding>() {
    private lateinit var emojiPopup: EmojiPopup
    override val layoutId: Int
        get() = R.layout.fragment_category_bottom_sheet
    override val fragmentTag: String
        get() = "CategoryBottomSheet"
    private val categoryBottomSheetViewModel: CategoryBottomSheetViewModel by activityViewModels()
    private lateinit var adapter: BaseListAdapter<ColorItemUIModel, LayoutColorSelectorItemBinding>

    override fun initView(binding: FragmentCategoryBottomSheetBinding) {
        arguments.let { it ->
            CategoryBottomSheetArgs.fromBundle(it!!).let {
                viewModel.setViewWithBundle(it.uiModel.copy(), it.categoryShowType)
            }
        }

        val onClickListener =
            BaseListAdapter.OnItemClickListener<ColorItemUIModel> { item, _ ->
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
        binding.colorSelector.addItemDecoration(
            SpacingItemDecorator(
                SpaceModel(rightSpace = 32)
            ),
        )
        binding.adapter = adapter
    }

    override fun provideViewModel(): CategoryBottomSheetViewModel {
        return categoryBottomSheetViewModel
    }

    override fun setBindingViewModel() {
        binding.viewModel = viewModel
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
                        binding.nameTextInputLayout.requestFocus()
                        categoryBottomSheetViewModel.setEmoji(it.unicode)
                    }, editText = binding.categoryIcon
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
        categoryBottomSheetViewModel.onDismissed()
    }

}
package com.duhapp.dnotes.features.add_or_update_category.ui

import androidx.fragment.app.activityViewModels
import com.duhapp.dnotes.NoteColor
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

    override fun initView(binding: FragmentCategoryBottomSheetBinding) {
        arguments.let { it ->
            CategoryBottomSheetArgs.fromBundle(it!!).let {
                viewModel.setViewWithBundle(it.uiModel, it.categoryShowType)
            }
        }
        val adapter = object :
            BaseListAdapter<ColorItemUIModel, LayoutColorSelectorItemBinding>() {
            override fun getLayoutId() = R.layout.layout_color_selector_item

            override fun setUIState(
                binding: LayoutColorSelectorItemBinding,
                item: ColorItemUIModel
            ) {
                binding.uiModel = item
            }

        }
        binding.colorSelector.addItemDecoration(
            SpacingItemDecorator(
                SpaceModel(rightSpace = 32)
            ),
        )
        binding.colorSelector.adapter = adapter
        val items = NoteColor.values().map {
            ColorItemUIModel(
                it
            )
        }
        adapter.setItems(items)
    }

    override fun provideViewModel(): CategoryBottomSheetViewModel {
        return categoryBottomSheetViewModel
    }

    override fun setBindingViewModel() {
        binding.viewModel = viewModel
    }

    override fun handleUIEvent(it: CategoryUIEvent) {
        when (it) {
            is CategoryUIEvent.Canceled -> {
                dismiss()
            }

            is CategoryUIEvent.Inserted -> {
                dismiss()
            }

            is CategoryUIEvent.ShowEmojiDialog -> {
                emojiPopup = EmojiPopup(
                    requireView(), onEmojiClickListener = {
                        binding.categoryIcon.text.clear()
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

}
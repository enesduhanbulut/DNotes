package com.duhapp.dnotes.features.add_or_update_category.ui

import androidx.fragment.app.viewModels
import com.duhapp.dnotes.R
import com.duhapp.dnotes.databinding.FragmentCategoryBottomSheetBinding
import com.duhapp.dnotes.features.base.ui.BaseBottomSheet
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

            is CategoryUIEvent.Dialog -> {
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
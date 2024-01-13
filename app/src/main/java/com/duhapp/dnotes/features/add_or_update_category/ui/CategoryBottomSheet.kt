package com.duhapp.dnotes.features.add_or_update_category.ui

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import com.duhapp.dnotes.R
import com.duhapp.dnotes.databinding.FragmentCategoryBottomSheetBinding
import com.duhapp.dnotes.databinding.LayoutColorSelectorItemBinding
import com.duhapp.dnotes.features.add_or_update_category.domain.isEmoji
import com.duhapp.dnotes.features.base.ui.BaseBottomSheet
import com.duhapp.dnotes.features.base.ui.BaseListAdapter
import com.duhapp.dnotes.features.generic.ui.SpaceModel
import com.duhapp.dnotes.features.generic.ui.SpacingItemDecorator
import com.google.android.material.snackbar.Snackbar
import com.vanniktech.emoji.EmojiPopup
import com.vanniktech.emoji.search.NoSearchEmoji
import com.vanniktech.emoji.variant.NoVariantEmoji
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class CategoryBottomSheet : BaseBottomSheet<
        FragmentCategoryBottomSheetBinding,
        CategoryUIEvent,
        CategoryBottomSheetUIState,
        CategoryBottomSheetViewModel>() {
    var emojiPopup: EmojiPopup? = null
    override val layoutId = R.layout.fragment_category_bottom_sheet
    override val titleId = R.string.Add_Category
    override val fragmentTag = "CategoryBottomSheet"
    override val viewModel: CategoryBottomSheetViewModel by activityViewModels()
    private lateinit var adapter: BaseListAdapter<ColorItemUIModel, LayoutColorSelectorItemBinding>
    private val tag = "CategoryBottomSheet"
    override fun initView(binding: FragmentCategoryBottomSheetBinding) {
        val onClickListener =
            BaseListAdapter.OnItemClickListener<ColorItemUIModel> { item, _ ->
                viewModel.onColorSelected(item)
            }
        adapter = object : BaseListAdapter<ColorItemUIModel, LayoutColorSelectorItemBinding>() {
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
        when (it) {
            is CategoryBottomSheetUIState.Error -> {
                Snackbar.make(
                    requireView().rootView,
                    it.getErrorCustomException()?.data?.message ?: R.string.Unknown_Error,
                    Snackbar.LENGTH_LONG
                ).show()
            }
            else -> {
                Timber.d(tag, "Unhandled state: $it")
            }
        }
    }

    override fun handleUIEvent(it: CategoryUIEvent) {
        when (it) {
            is CategoryUIEvent.Canceled, CategoryUIEvent.Upserted -> {
                dismiss()
            }

            is CategoryUIEvent.ColorSelected -> {
                Timber.d(tag, "ColorSelected: $it")
            }

            is CategoryUIEvent.ShowEmojiDialog -> {
                if (emojiPopup == null) {
                    emojiPopup = EmojiPopup(
                        requireView(),
                        searchEmoji = NoSearchEmoji,
                        variantEmoji = NoVariantEmoji,
                        onEmojiClickListener = {
                            mBinding!!.nameTextInputLayout.requestFocus()
                            if (it.isEmoji())
                                viewModel.setEmoji(it.unicode)
                            else {
                                viewModel.setEmoji("")
                                Snackbar.make(
                                    requireView(),
                                    R.string.Emoji_Is_Not_Valid,
                                    Snackbar.LENGTH_LONG
                                ).show()
                            }
                        }, editText = mBinding!!.categoryIcon
                    )

                }
                if (emojiPopup!!.isShowing) {
                    emojiPopup!!.dismiss()
                    emojiPopup = null
                    mBinding!!.nameTextInputLayout.requestFocus()
                } else {
                    emojiPopup!!.toggle()
                }
            }

            is CategoryUIEvent.DismissedEmojiDialog -> {
                emojiPopup.let {
                    it?.dismiss()
                    emojiPopup = null
                }
            }

            else -> {
                Timber.d(tag, "Unhandled event: $it")
            }
        }
    }

    override fun dismiss() {
        super.dismiss()
        viewModel.onDismissed()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDismissed()
        adapter.onItemClickListener = null
        emojiPopup.let {
            it?.dismiss()
            emojiPopup = null
        }
    }
}
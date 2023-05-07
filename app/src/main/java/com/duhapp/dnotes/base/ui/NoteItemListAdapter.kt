package com.duhapp.dnotes.base.ui

import com.duhapp.dnotes.base.ui.BaseListAdapter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.duhapp.dnotes.R
import com.duhapp.dnotes.databinding.LayoutBasicNoteListItemBinding
import com.duhapp.dnotes.home_category.ui.BaseNoteUIModel
import com.duhapp.dnotes.home_category.ui.BasicNoteUIModel

class NoteItemListAdapter : BaseListAdapter<BaseNoteUIModel, ViewDataBinding>() {
    override fun getLayoutId() = -1
    override fun setUIState(binding: ViewDataBinding, item: BaseNoteUIModel) {
        when (binding) {
            is LayoutBasicNoteListItemBinding -> {
                binding.item = item as BasicNoteUIModel
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        lateinit var viewHolder: RecyclerView.ViewHolder
        val binding: ViewDataBinding
        when (viewType) {
            0 -> {
                binding = DataBindingUtil.inflate<LayoutBasicNoteListItemBinding>(
                    LayoutInflater.from(parent.context), R.layout.layout_basic_note_list_item, parent, false
                )
                viewHolder = onCreateViewHolder(binding)
            }

            else -> {
                throw IllegalArgumentException("Unknown type")
            }
        }

        binding.root.setOnClickListener {
            val position = viewHolder.adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                onItemClickListener?.onItemClick(itemList[position], position)
            }
        }
        return viewHolder
    }
}
package com.duhapp.dnotes.features.base.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class BaseListAdapter<M : BaseListItem, DB : ViewDataBinding> :
    RecyclerView.Adapter<BaseListAdapter<M, DB>.BaseViewHolder>() {

    protected var itemList: List<M> = emptyList()
    val items: List<M>
        get() = itemList
    var onItemClickListener: OnItemClickListener<M>? = null

    abstract fun getLayoutId(): Int
    abstract fun setUIState(binding: DB, item: M)

    protected fun onCreateViewHolder(binding: DB): BaseViewHolder {
        return BaseViewHolder(binding)
    }

    private fun onBindViewHolder(holder: BaseViewHolder, item: M) {
        holder.bind(item)
    }

    fun setItems(items: List<M>) {
        // TODO: Use DiffUtil to calculate the difference between the old and new list

        /*val diffResult = DiffUtil.calculateDiff(DiffCallback(itemList, items.toList()))
        diffResult.dispatchUpdatesTo(this)
        */
        notifyDataSetChanged()
        itemList = items.toList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context), getLayoutId(), parent, false
        )
        val viewHolder = onCreateViewHolder(binding as DB)
        binding.root.setOnClickListener {
            val position = viewHolder.adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                onItemClickListener?.onItemClick(itemList[position], position)
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val item = itemList[position]
        onBindViewHolder(holder, item)
    }

    open fun initializeMenu(binding: DB): MenuContainer? {
        return null
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class BaseViewHolder(private val binding: DB) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: M) {
            val menuContainer = initializeMenu(binding)
            if (menuContainer != null) {
                val view = binding.root.findViewById<View>(menuContainer.viewId)
                view.setOnClickListener {
                    with(
                        PopupMenu(
                            binding.root.context,
                            binding.root
                        )
                    ) {
                        setOnMenuItemClickListener(menuContainer.menuItemClickListener)
                        inflate(menuContainer.menu)
                        show()
                    }
                }
            }
            setUIState(binding, item)
        }
    }

    fun interface OnItemClickListener<T> {
        fun onItemClick(item: T, position: Int)
    }

    private class DiffCallback<T>(private val oldList: List<T>, private val newList: List<T>) :
        DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return areItemsSame(oldItem, newItem)
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return areContentsSame(oldItem, newItem)
        }

        private fun areItemsSame(oldItem: T, newItem: T): Boolean {
            // Implement your logic to determine if two items represent the same object
            return oldItem === newItem
        }

        private fun areContentsSame(oldItem: T, newItem: T): Boolean {
            // Implement your logic to determine if the item content has changed
            return oldItem == newItem
        }
    }
}

data class MenuContainer(
    val viewId: Int,
    val menu: Int,
    val menuItemClickListener: PopupMenu.OnMenuItemClickListener
)

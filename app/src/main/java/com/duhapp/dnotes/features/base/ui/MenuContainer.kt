package com.duhapp.dnotes.features.base.ui

import android.view.View
import androidx.appcompat.widget.PopupMenu

data class MenuContainer(
    val viewId: Int, val menu: Int, val menuItemClickListener: PopupMenu.OnMenuItemClickListener
) {
    private lateinit var popupMenu: PopupMenu
    private lateinit var triggerView: View
    fun defineMenu(root: View) {
        triggerView = root.findViewById(viewId)
        popupMenu = PopupMenu(root.context, triggerView)
        triggerView.setOnClickListener {
            with(
                popupMenu
            ) {
                menu.clear()
                setOnMenuItemClickListener(this@MenuContainer.menuItemClickListener)
                inflate(this@MenuContainer.menu)
                show()
            }
        }
    }

    fun destroyMenu() {
        popupMenu.menu.clear()
        popupMenu.dismiss()
        triggerView.setOnClickListener(null)
    }
}
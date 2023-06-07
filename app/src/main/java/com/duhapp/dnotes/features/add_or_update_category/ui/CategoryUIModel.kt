package com.duhapp.dnotes.features.add_or_update_category.ui

import android.os.Parcelable
import androidx.annotation.ColorRes
import com.duhapp.dnotes.R
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoryUIModel(
    val id: Int = -1,
    var name: String = "",
    var emoji: String = "",
    var description: String = "",
    @ColorRes var colorId: Int = R.color.primary_color,
) : Parcelable

package com.duhapp.dnotes.features.add_or_update_category.ui

import android.os.Parcelable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.duhapp.dnotes.R
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoryUIModel(
    val id: Int,
    var name: String,
    var descripton: String,
    @DrawableRes var iconId: Int = R.drawable.baseline_lightbulb_24,
    @ColorRes var colorId: Int = R.color.primary_color
) : Parcelable
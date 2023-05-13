package com.duhapp.dnotes.features.generic.ui

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShowMessageUIModel(
    @DrawableRes val image: Int,
    var title: String,
    var message: String,
    @StringRes var positiveButtonText: Int,
    @StringRes var negativeButtonText: Int,
) : Parcelable
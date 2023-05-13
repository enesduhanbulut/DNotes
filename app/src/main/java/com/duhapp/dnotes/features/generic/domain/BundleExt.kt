package com.duhapp.dnotes.features.generic.domain

import android.os.Build
import android.os.Bundle
import android.os.Parcelable

inline fun <reified T : Parcelable> Bundle.getNotNullParcelableWithKey(key: String): T {
    if (!this.containsKey(key)) {
        throw IllegalArgumentException("Bundle does not contain key $key")
    }
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelable("showMessageUIState", T::class.java)
            ?: throw IllegalArgumentException("Bundle does not contain key $key")
    } else {
        getParcelable<T>("showMessageUIState")
            ?: throw IllegalArgumentException("Bundle does not contain key $key")
    }

}
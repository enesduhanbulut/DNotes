package com.duhapp.dnotes.ui.custom_views

import androidx.databinding.BindingAdapter
import com.duhapp.dnotes.features.base.domain.CustomException

@BindingAdapter("setErrorContent")
fun setError(errorItemLayout: ErrorItemLayout, error: CustomException?) {
    if (error == null)
        return
    val data = error.data

    val context = errorItemLayout.context
    errorItemLayout.binding.errorCode.text = data.code.toString()
    errorItemLayout.binding.errorTitle.text = context.getText(data.title)
    errorItemLayout.binding.errorMessage.text = context.getText(data.message)
    errorItemLayout.binding.errorIcon.setImageResource(data.image)
}
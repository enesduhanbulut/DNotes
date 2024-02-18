package com.duhapp.dnotes.ui.custom_views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.duhapp.dnotes.databinding.ErrorItemBinding

class ErrorItemLayout : ConstraintLayout {
    var binding: ErrorItemBinding

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        binding = ErrorItemBinding.inflate(LayoutInflater.from(context), this, true)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        binding = ErrorItemBinding.inflate(LayoutInflater.from(context), this, true)
    }

    constructor(context: Context) : super(context) {
        binding = ErrorItemBinding.inflate(LayoutInflater.from(context), this, true)
    }
}

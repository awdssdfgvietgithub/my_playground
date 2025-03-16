package com.example.test.features.recycler_view_with_complex_item.utils

import android.view.View
import android.view.ViewGroup

fun View.setMarginTop(marginTop: Int) {
    val menuLayoutParams = this.layoutParams as ViewGroup.MarginLayoutParams
    menuLayoutParams.setMargins(0, marginTop, 0, 0)
    this.layoutParams = menuLayoutParams
}

fun View.setMarginBottom(marginBottom: Int) {
    val menuLayoutParams = this.layoutParams as ViewGroup.MarginLayoutParams
    menuLayoutParams.setMargins(0, 0, 0, marginBottom)
    this.layoutParams = menuLayoutParams
}
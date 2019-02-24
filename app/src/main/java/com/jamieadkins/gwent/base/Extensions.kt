package com.jamieadkins.gwent.base

import android.content.Context
import android.util.DisplayMetrics

fun Context.convertDpToPixel(dp: Float): Float {
    val metrics = resources.displayMetrics
    val px = dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    return px
}
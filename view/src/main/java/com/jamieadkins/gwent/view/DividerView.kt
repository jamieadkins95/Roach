package com.jamieadkins.gwent.view

import android.content.Context
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.airbnb.epoxy.ModelProp

import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.TextProp
import kotterknife.bindView

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class DividerView : LinearLayout {

    private val tvTitle by bindView<TextView>(R.id.title)
    private val colorView by bindView<View>(R.id.color_view)

    constructor(context: Context) : super(context) { inflateView() }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) { inflateView() }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) { inflateView() }

    private fun inflateView() {
        orientation = VERTICAL
        gravity = Gravity.CENTER
        LayoutInflater.from(context).inflate(R.layout.view_google_now_subheader, this, true)
    }

    @TextProp
    fun setTitle(text: CharSequence) {
        tvTitle.text = text
    }

    @ModelProp
    fun setColor(@ColorRes color: Int) {
        colorView.setBackgroundColor(ContextCompat.getColor(context, color))
    }
}
package com.jamieadkins.gwent.view.card

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.TextProp
import com.jamieadkins.gwent.view.R
import kotterknife.bindView

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class HeaderView : ConstraintLayout {

    constructor(context: Context) : super(context) { inflateView() }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) { inflateView() }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) { inflateView() }

    private val tvTitle by bindView<TextView>(R.id.header_primary_text)

    private fun inflateView() {
        LayoutInflater.from(context).inflate(R.layout.view_header, this, true)
    }

    @TextProp
    fun setTitle(text: CharSequence) {
        tvTitle.text = text
    }
}
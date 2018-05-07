package com.jamieadkins.gwent.card.list

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.TextProp
import com.jamieadkins.gwent.R
import kotterknife.bindView

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class SubHeaderView @JvmOverloads constructor(context: Context,
                                              attrs: AttributeSet? = null,
                                              defStyleAttr: Int = 0): ConstraintLayout(context, attrs, defStyleAttr) {

    private val tvTitle by bindView<TextView>(R.id.subheader_text)

    init {
        LayoutInflater.from(context).inflate(R.layout.view_subheader, this, true)
    }

    @TextProp
    fun setTitle(text: CharSequence) {
        tvTitle.text = text
    }
}
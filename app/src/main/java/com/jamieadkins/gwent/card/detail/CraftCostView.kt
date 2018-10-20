package com.jamieadkins.gwent.card.detail

import android.content.Context
import androidx.cardview.widget.CardView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.airbnb.epoxy.ModelProp

import com.airbnb.epoxy.ModelView
import com.jamieadkins.gwent.R
import kotterknife.bindView

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class CraftCostView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : LinearLayout(context, attrs, defStyleAttr) {

    private val tvText by bindView<TextView>(R.id.text)

    init {
        LayoutInflater.from(context).inflate(R.layout.view_craft, this, true)
    }

    @ModelProp
    fun setValue(value: Int) {
        tvText.text = value.toString()
    }
}
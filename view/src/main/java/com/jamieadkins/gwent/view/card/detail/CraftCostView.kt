package com.jamieadkins.gwent.view.card.detail

import android.content.Context
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import com.airbnb.epoxy.ModelProp

import com.airbnb.epoxy.ModelView
import com.jamieadkins.gwent.view.R
import kotterknife.bindView

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class CraftCostView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : CardView(context, attrs, defStyleAttr) {

    private val tvText by bindView<TextView>(R.id.text)

    init {
        LayoutInflater.from(context).inflate(R.layout.view_craft, this, true)
    }

    @ModelProp
    fun setValue(value: Int) {
        tvText.text = value.toString()
    }
}
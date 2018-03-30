package com.jamieadkins.gwent.view.card

import android.content.Context
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import com.airbnb.epoxy.ModelProp

import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.TextProp
import com.bumptech.glide.Glide
import com.jamieadkins.gwent.view.R
import kotterknife.bindView

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class GwentCardView : CardView {

    private val tvName by bindView<TextView>(R.id.name)
    private val tvTooltip by bindView<TextView>(R.id.tooltip)
    private val tvCategories by bindView<TextView>(R.id.categories)
    private val imgCard by bindView<ImageView>(R.id.card_image)

    constructor(context: Context) : super(context) { inflateView() }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) { inflateView() }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) { inflateView() }

    private fun inflateView() {
        LayoutInflater.from(context).inflate(R.layout.view_gwent_card, this, true)
    }

    @TextProp
    fun setCardName(text: CharSequence) {
        tvName.text = text
    }

    @TextProp
    fun setCardTooltip(text: CharSequence) {
        tvTooltip.text = text
    }

    @ModelProp
    fun setCardCategories(categories: List<String>) {
        var text = ""
        categories.forEachIndexed { index, category ->
            text += category
            if (index != categories.size - 1) {
                text += ", "
            }
        }
        tvCategories.text = text
    }

    @ModelProp
    fun setCardImage(imageUrl: String?) {
        imageUrl?.let {
            Glide.with(context)
                    .load(imageUrl)
                    .into(imgCard)
        }
    }
}
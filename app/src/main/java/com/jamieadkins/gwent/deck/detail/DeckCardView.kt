package com.jamieadkins.gwent.deck.detail

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.cardview.widget.CardView
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.TextProp
import com.jamieadkins.gwent.R
import kotlinx.android.synthetic.main.view_deck_card.view.*

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class DeckCardView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : CardView(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_deck_card, this, true)
    }

    @CallbackProp
    fun setClickListener(listener: View.OnClickListener?) {
        setOnClickListener(listener)
    }

    @CallbackProp
    fun setLongClickListener(listener: View.OnLongClickListener?) {
        setOnLongClickListener(listener)
    }

    @TextProp
    fun setCardName(text: CharSequence) {
        name.text = text
    }

    @TextProp
    fun setCardTooltip(text: CharSequence) {
        tooltip.text = text
    }

    @ModelProp
    fun setProvisionCost(provisions: Int) {
        provisionCost.text = provisions.toString()
    }

    @ModelProp
    fun setCount(countInDeck: Int) {
        count.text = "x$countInDeck"
    }

    @ModelProp
    fun setStrength(cardStrength: Int) {
        if (cardStrength > 0) {
            strength.visibility = View.VISIBLE
            strength.text = cardStrength.toString()
        } else {
            strength.visibility = View.INVISIBLE
        }
    }
}
package com.jamieadkins.gwent.deck.detail

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.epoxy.AfterPropsSet
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.TextProp
import com.jamieadkins.gwent.R
import kotlinx.android.synthetic.main.view_deck_header.view.*

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class DeckHeaderView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_deck_header, this, true)
    }

    @TextProp
    fun setDeckName(text: CharSequence) {
        name.text = text
    }

    @ModelProp
    fun setCardCount(count: Int) {
        cardCount.text = resources.getString(R.string.cards_in_deck, count, 25)
    }

    @AfterPropsSet
    fun updateProvisionCost() {
        deckProvisionCost.text = "$provisionCost/$provisionAllowance"
    }

    @JvmField @ModelProp var provisionCost: Int = 0
    @JvmField @ModelProp var provisionAllowance: Int = 0
}
package com.jamieadkins.gwent.card.detail

import android.content.res.Resources
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.jamieadkins.gwent.R
import com.jamieadkins.gwent.domain.card.model.GwentCard
import java.lang.IllegalArgumentException

class CardDetailsAdapter(
    private val resources: Resources,
    private val card: GwentCard,
    private val relatedCards: List<GwentCard>
) : PagerAdapter() {

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = when (position) {
            0 -> CardImageView(container.context).apply { setCardImage(card.cardArt.medium, card.faction)}
            1 -> CardDetailsView(container.context).apply { showCardDetails(card, relatedCards) }
            else -> throw IllegalArgumentException("Invalid position $position")
        }
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, view: Any) {
        (view as? View)?.let(container::removeView)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> resources.getString(R.string.card_image)
            1 -> resources.getString(R.string.card_info)
            else -> throw IllegalArgumentException("Invalid position $position")
        }
    }

    override fun getCount(): Int = 2
}
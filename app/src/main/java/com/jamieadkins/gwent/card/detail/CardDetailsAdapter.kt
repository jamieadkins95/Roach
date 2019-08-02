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

    private val hasCardArt = card.cardArt.medium != null

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = when (position) {
            0 -> {
                if (hasCardArt) {
                    CardImageView(container.context).apply { setCardImage(card.cardArt.medium)}
                } else {
                    CardDetailsView(container.context).apply { showCardDetails(card, relatedCards) }
                }
            }
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
            0 -> if (hasCardArt) resources.getString(R.string.card_image) else resources.getString(R.string.card_info)
            1 -> resources.getString(R.string.card_info)
            else -> throw IllegalArgumentException("Invalid position $position")
        }
    }

    override fun getCount(): Int = if (card.cardArt.medium != null) 2 else 1
}
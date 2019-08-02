package com.jamieadkins.gwent.card.detail

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import java.lang.IllegalArgumentException

class CardDetailsAdapter(
    private val cardId: String,
    fragmentManager: FragmentManager
) : FragmentStatePagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> CardDetailsFragment.withCardId(cardId)
            1 -> CardDetailsFragment.withCardId(cardId)
            else -> throw IllegalArgumentException()
        }
    }

    override fun getCount(): Int = 2
}
package com.jamieadkins.gwent.filter

import android.view.View
import android.widget.CheckBox
import android.widget.TextView

import com.jamieadkins.commonutils.ui.BaseViewHolder
import com.jamieadkins.commonutils.ui.RecyclerViewItem
import com.jamieadkins.gwent.R
import com.jamieadkins.gwent.model.CardColour
import com.jamieadkins.gwent.model.GwentFaction
import com.jamieadkins.gwent.model.Loyalty
import com.jamieadkins.gwent.model.Rarity

/**
 * Holds much more detail about a card.
 */

class FilterViewHolder<F>(view: View, private val mListener: FilterBottomSheetDialogFragment.FilterUiListener) : BaseViewHolder(view), View.OnClickListener {
    private var mFilter: FilterableItem<F>? = null
    private val mFilterName: TextView
    private val mCheckbox: CheckBox

    init {
        mFilterName = view.findViewById<View>(R.id.name) as TextView
        mCheckbox = view.findViewById<View>(R.id.checkbox) as CheckBox
    }

    override fun bindItem(item: RecyclerViewItem) {
        super.bindItem(item)
        mFilter = item as FilterableItem<F>

        // Initially set to provided data.
        mCheckbox.isChecked = mFilter!!.isChecked
        mCheckbox.setOnClickListener(this)

        view.setOnClickListener { v ->
            mCheckbox.isChecked = !mCheckbox.isChecked
            this@FilterViewHolder.onClick(v)
        }

        mFilter?.filterable?.let {
            when (it) {
                is Rarity -> mFilterName.text = GwentStringHelper.getRarityString(mFilterName.context, it)
                is GwentFaction -> mFilterName.text = GwentStringHelper.getFactionString(mFilterName.context, it)
                is CardColour -> mFilterName.text = GwentStringHelper.getColourString(mFilterName.context, it)
                is Loyalty -> mFilterName.text = GwentStringHelper.getLoyaltyString(mFilterName.context, it)
            }
        }
    }

    override fun onClick(v: View) {
        mFilter?.filterable?.let {
            when (it) {
                is Rarity -> mListener.onRarityFilterChanged(it, mCheckbox.isChecked)
                is GwentFaction -> mListener.onFactionFilterChanged(it, mCheckbox.isChecked)
                is CardColour -> mListener.onColourFilterChanged(it, mCheckbox.isChecked)
                is Loyalty -> mListener.onLoyaltyFilterChanged(it, mCheckbox.isChecked)
            }
        }
    }
}

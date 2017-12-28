package com.jamieadkins.gwent.filter

import android.view.View
import android.widget.CheckBox
import android.widget.TextView

import com.jamieadkins.commonutils.ui.BaseViewHolder
import com.jamieadkins.commonutils.ui.RecyclerViewItem
import com.jamieadkins.gwent.R

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

        mFilterName.text = mFilter?.filterable.toString()
    }

    override fun onClick(v: View) {
        when (mFilter?.filterable) {
            /*is Rarity -> mListener.onRarityFilterChanged(it, mCheckbox.isChecked)
            is GwentFaction -> mListener.onFactionFilterChanged(it, mCheckbox.isChecked)
            is CardColour -> mListener.onColourFilterChanged(it, mCheckbox.isChecked)
            is Loyalty -> mListener.onLoyaltyFilterChanged(it, mCheckbox.isChecked)*/
        }
    }
}

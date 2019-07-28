package com.jamieadkins.gwent.tracker

import com.jamieadkins.gwent.R
import com.jamieadkins.gwent.domain.GwentFaction
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.view_faction_picker.*

data class FactionPickerItem(
    private val onFactionSelected: (faction: GwentFaction) -> Unit
) : Item() {

    override fun getLayout(): Int = R.layout.view_faction_picker

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.groupFaction.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.faction_nr -> onFactionSelected.invoke(GwentFaction.NORTHERN_REALMS)
                R.id.faction_mon -> onFactionSelected.invoke(GwentFaction.MONSTER)
                R.id.faction_sc -> onFactionSelected.invoke(GwentFaction.SCOIATAEL)
                R.id.faction_sk -> onFactionSelected.invoke(GwentFaction.SKELLIGE)
                R.id.faction_ng -> onFactionSelected.invoke(GwentFaction.NILFGAARD)
                R.id.faction_sy -> onFactionSelected.invoke(GwentFaction.SYNDICATE)
            }
        }
    }
}
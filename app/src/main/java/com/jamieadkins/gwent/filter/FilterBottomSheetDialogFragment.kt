package com.jamieadkins.gwent.filter

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jamieadkins.gwent.R
import com.jamieadkins.gwent.base.DaggerSupportDialogFragment
import com.jamieadkins.gwent.domain.GwentFaction
import com.jamieadkins.gwent.domain.card.model.GwentCardColour
import com.jamieadkins.gwent.domain.card.model.GwentCardRarity
import kotlinx.android.synthetic.main.fragment_filter.*

class FilterBottomSheetDialogFragment : DaggerSupportDialogFragment(), FilterContract.View {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), this.theme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnApply.setOnClickListener {

        }
    }

    override fun showFactions(factions: Map<GwentFaction, Boolean>) {
        factions.entries.forEach {
            val view = when (it.key) {
                GwentFaction.NORTHERN_REALMS -> filter_faction_nr
                GwentFaction.MONSTER -> filter_faction_mon
                GwentFaction.SKELLIGE -> filter_faction_sk
                GwentFaction.SCOIATAEL -> filter_faction_sc
                GwentFaction.NILFGAARD -> filter_faction_ng
                GwentFaction.NEUTRAL -> filter_faction_ne
            }
            view.isChecked = it.value
        }
    }

    override fun showTiers(tiers: Map<GwentCardColour, Boolean>) {
        tiers.entries.forEach {
            val view = when (it.key) {
                GwentCardColour.BRONZE -> filter_color_bronze
                GwentCardColour.GOLD -> filter_color_gold
                GwentCardColour.LEADER -> filter_color_leader
            }
            view.isChecked = it.value
        }
    }

    override fun showRarities(rarities: Map<GwentCardRarity, Boolean>) {
        rarities.entries.forEach {
            val view = when (it.key) {
                GwentCardRarity.COMMON -> filter_rarity_common
                GwentCardRarity.RARE -> filter_rarity_rare
                GwentCardRarity.EPIC -> filter_rarity_epic
                GwentCardRarity.LEGENDARY -> filter_rarity_legendary
            }
            view.isChecked = it.value
        }
    }

    override fun showProvisions(provisions: Map<Int, Boolean>) {

    }
}

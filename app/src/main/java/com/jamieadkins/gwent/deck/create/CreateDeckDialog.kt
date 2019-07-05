package com.jamieadkins.gwent.deck.create

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jamieadkins.gwent.R
import com.jamieadkins.gwent.base.DaggerSupportDialogFragment
import com.jamieadkins.gwent.deck.detail.DeckDetailsActivity
import com.jamieadkins.gwent.domain.GwentFaction
import kotlinx.android.synthetic.main.fragment_create_deck.*
import timber.log.Timber
import javax.inject.Inject

class CreateDeckDialog : DaggerSupportDialogFragment(), CreateDeckContract.View {

    @Inject lateinit var presenter: CreateDeckContract.Presenter

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), this.theme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_create_deck, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnCreate.setOnClickListener {
            val faction = when (groupFaction.checkedChipId) {
                R.id.faction_nr -> GwentFaction.NORTHERN_REALMS
                R.id.faction_mon -> GwentFaction.MONSTER
                R.id.faction_sk -> GwentFaction.SKELLIGE
                R.id.faction_sc -> GwentFaction.SCOIATAEL
                R.id.faction_ng -> GwentFaction.NILFGAARD
                R.id.faction_sy -> GwentFaction.SYNDICATE
                else -> GwentFaction.NORTHERN_REALMS
            }
            presenter.createDeck(inputName.text?.toString() ?: "", faction)
        }
    }

    override fun showDeckDetails(deckId: String) {
        startActivity(DeckDetailsActivity.getIntent(requireContext(), deckId))
    }

    override fun close() {
        this.dismiss()
    }
}
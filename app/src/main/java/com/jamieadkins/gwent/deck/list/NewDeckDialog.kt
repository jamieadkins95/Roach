package com.jamieadkins.gwent.deck.list

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner

import com.jamieadkins.gwent.R
import com.jamieadkins.gwent.bus.NewDeckBundle
import com.jamieadkins.gwent.bus.NewDeckRequest
import com.jamieadkins.gwent.bus.RxBus
import com.jamieadkins.gwent.data.Faction

/**
 * Prompts user for new deck name and faction.
 */

class NewDeckDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        val rootView = inflater.inflate(R.layout.dialog_new_deck, null)

        val factionSpinner = rootView.findViewById<Spinner>(R.id.factions) as Spinner
        val adapter = ArrayAdapter.createFromResource(activity,
                R.array.factions_array, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        factionSpinner.adapter = adapter

        builder.setView(rootView)
                .setTitle(R.string.new_deck)
                .setPositiveButton(R.string.create) { dialog, id ->
                    val deckName = rootView.findViewById<EditText>(R.id.deck_name) as EditText
                    val faction = rootView.findViewById<Spinner>(R.id.factions) as Spinner
                    RxBus.post(NewDeckRequest(NewDeckBundle(deckName.text.toString(),
                            Faction.ALL_FACTIONS[faction.selectedItemPosition].id)))
                }
                .setNegativeButton(android.R.string.cancel, null)
        return builder.create()
    }
}

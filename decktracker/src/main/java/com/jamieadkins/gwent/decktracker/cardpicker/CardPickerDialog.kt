package com.jamieadkins.gwent.decktracker.cardpicker

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jamieadkins.gwent.base.DaggerSupportDialogFragment
import com.jamieadkins.gwent.base.VerticalSpaceItemDecoration
import com.jamieadkins.gwent.base.convertDpToPixel
import com.jamieadkins.gwent.card.data.FactionMapper
import com.jamieadkins.gwent.decktracker.R
import com.jamieadkins.gwent.domain.GwentFaction
import com.jamieadkins.gwent.domain.card.model.GwentCard
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.fragment_card_picker.*
import javax.inject.Inject

class CardPickerDialog : DaggerSupportDialogFragment(), CardPickerContract.View {

    @Inject lateinit var presenter: CardPickerContract.Presenter
    @Inject lateinit var factionMapper: FactionMapper
    private val adapter = GroupAdapter<ViewHolder>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), this.theme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_card_picker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(recyclerView.context)
        recyclerView.layoutManager = layoutManager
        val dividerItemDecoration = VerticalSpaceItemDecoration(requireContext().convertDpToPixel(8f).toInt())
        recyclerView.addItemDecoration(dividerItemDecoration)
        recyclerView.adapter = adapter
        adapter.setOnItemClickListener { item, _ ->
            when (item) {
                is SearchResultItem -> presenter.onCardPicked(item.card.id)
            }
        }

        val faction = arguments?.getString(KEY_FACTION)?.let(factionMapper::map) ?: GwentFaction.NEUTRAL

        inputName.setOnEditorActionListener { v, actionId, event ->
            if (actionId ==  EditorInfo.IME_ACTION_SEARCH) {
                presenter.search(v.text?.toString() ?: "", faction)
            }
            false
        }

        btnSearch.setOnClickListener {
            presenter.search(inputName.text?.toString() ?: "", faction)
        }
    }

    override fun showCards(cards: List<GwentCard>) {
        adapter.update(cards.map { SearchResultItem(it) })
    }

    override fun close() {
        this.dismiss()
    }

    companion object {

        fun withFaction(factionId: String): CardPickerDialog {
            val bundle = Bundle()
            bundle.putString(KEY_FACTION, factionId)
            return CardPickerDialog().apply { arguments = bundle }
        }

        const val KEY_FACTION = "faction"
    }
}
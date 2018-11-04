package com.jamieadkins.gwent.deck.detail.leader

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jamieadkins.gwent.R
import com.jamieadkins.gwent.base.DaggerSupportDialogFragment
import com.jamieadkins.gwent.card.list.VerticalSpaceItemDecoration
import com.jamieadkins.gwent.domain.GwentFaction
import com.jamieadkins.gwent.domain.card.model.GwentCard
import kotlinx.android.synthetic.main.fragment_create_deck.*
import kotlinx.android.synthetic.main.fragment_deck_list.*
import timber.log.Timber
import javax.inject.Inject

class LeaderPickerDialog : DaggerSupportDialogFragment(), LeaderPickerContract.View {

    @Inject lateinit var presenter: LeaderPickerContract.Presenter

    private lateinit var controller: LeaderPickerController

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), this.theme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_leader_picker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(recyclerView.context)
        recyclerView.layoutManager = layoutManager
        val dividerItemDecoration = VerticalSpaceItemDecoration(resources.getDimensionPixelSize(R.dimen.divider_spacing))
        recyclerView.addItemDecoration(dividerItemDecoration)
        controller = LeaderPickerController(resources)
        recyclerView.adapter = controller.adapter

        presenter.setDeckId(arguments?.getString(KEY_DECK_ID) ?: throw IllegalArgumentException("No Deck id"))
        presenter.onAttach()
    }

    override fun onDestroyView() {
        presenter.onDetach()
        super.onDestroyView()
    }

    override fun showLeaders(cards: List<GwentCard>) {
        controller.setData(cards)
    }

    override fun close() {
        dismiss()
    }

    companion object {

        private const val KEY_DECK_ID = "DeckId"

        fun newInstance(deckId: String): LeaderPickerDialog {
            val dialog = LeaderPickerDialog()
            dialog.arguments = Bundle().apply {
                putString(KEY_DECK_ID, deckId)
            }
            return dialog
        }
    }
}
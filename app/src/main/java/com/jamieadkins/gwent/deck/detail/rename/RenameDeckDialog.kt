package com.jamieadkins.gwent.deck.detail.rename

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
import com.jamieadkins.gwent.domain.card.model.GwentCard
import kotlinx.android.synthetic.main.fragment_deck_list.*
import kotlinx.android.synthetic.main.fragment_rename_deck.*
import javax.inject.Inject

class RenameDeckDialog : DaggerSupportDialogFragment(), RenameDeckContract.View {

    @Inject lateinit var presenter: RenameDeckContract.Presenter

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), this.theme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_rename_deck, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.setDeckId(arguments?.getString(KEY_DECK_ID) ?: throw IllegalArgumentException("No Deck id"))
        presenter.onAttach()

        btnRename.setOnClickListener {
            presenter.renameDeck(inputName.text?.toString() ?: "")
        }
    }

    override fun onDestroyView() {
        presenter.onDetach()
        super.onDestroyView()
    }

    override fun close() {
        dismiss()
    }

    companion object {

        private const val KEY_DECK_ID = "DeckId"

        fun newInstance(deckId: String): RenameDeckDialog {
            val dialog = RenameDeckDialog()
            dialog.arguments = Bundle().apply {
                putString(KEY_DECK_ID, deckId)
            }
            return dialog
        }
    }
}
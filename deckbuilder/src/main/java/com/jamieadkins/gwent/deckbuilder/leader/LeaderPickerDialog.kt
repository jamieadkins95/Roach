package com.jamieadkins.gwent.deckbuilder.leader

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jamieadkins.gwent.base.DaggerSupportDialogFragment
import com.jamieadkins.gwent.bus.LeaderPickerEvent
import com.jamieadkins.gwent.bus.RxBus
import com.jamieadkins.gwent.card.list.GwentCardItem
import com.jamieadkins.gwent.base.items.SubHeaderItem
import com.jamieadkins.gwent.base.VerticalSpaceItemDecoration
import com.jamieadkins.gwent.deckbuilder.R
import com.jamieadkins.gwent.domain.card.model.GwentCard
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.fragment_leader_picker.*
import javax.inject.Inject

class LeaderPickerDialog : DaggerSupportDialogFragment(), LeaderPickerContract.View {

    @Inject lateinit var presenter: LeaderPickerContract.Presenter

    private val adapter = GroupAdapter<ViewHolder>()

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
        val dividerItemDecoration = VerticalSpaceItemDecoration(resources.getDimensionPixelSize(com.jamieadkins.gwent.R.dimen.divider_spacing))
        recyclerView.addItemDecoration(dividerItemDecoration)
        recyclerView.adapter = adapter
        adapter.setOnItemClickListener { item, _ ->
            when (item) {
                is GwentCardItem -> RxBus.post(LeaderPickerEvent(item.card.id))
            }
        }

        presenter.setDeckId(arguments?.getString(KEY_DECK_ID) ?: throw IllegalArgumentException("No Deck id"))
        presenter.onAttach()
    }

    override fun onDestroyView() {
        presenter.onDetach()
        super.onDestroyView()
    }

    override fun showLeaders(cards: List<GwentCard>) {
        adapter.update(listOf(SubHeaderItem(R.string.change_leader)) + cards.map { GwentCardItem(it) })
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
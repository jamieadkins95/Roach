package com.jamieadkins.gwent.card.detail

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.jamieadkins.gwent.R
import com.jamieadkins.gwent.base.CardResourceHelper
import com.jamieadkins.gwent.domain.card.model.GwentCard
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_card_details.*
import javax.inject.Inject

class CardDetailsFragment : DaggerFragment(), DetailContract.View {

    lateinit var cardId: String
    @Inject lateinit var presenter: DetailContract.Presenter

    companion object {
        private const val KEY_ID = "cardId"

        fun withCardId(cardId: String): CardDetailsFragment {
            return CardDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_ID, cardId)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        cardId = savedInstanceState?.getString(KEY_ID) ?: arguments?.getString(KEY_ID) ?: throw Exception("Card id not found.")
        super.onCreate(savedInstanceState)

        presenter.setCardId(cardId)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_card_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? AppCompatActivity)?.apply {
            setSupportActionBar(toolbar)
        }
        toolbar.setTitleTextAppearance(requireContext(), R.style.GwentTextAppearance)

        refreshLayout.setColorSchemeResources(R.color.gwentAccent)
        refreshLayout.isEnabled = false

        tabLayout.setupWithViewPager(viewPager)

        presenter.onAttach()
    }

    override fun onDestroyView() {
        presenter.onDetach()
        super.onDestroyView()
    }

    override fun showScreen(cardDetailsScreenData: CardDetailsScreenData) {
        val card = cardDetailsScreenData.card
        showCard(card)
        viewPager.adapter = CardDetailsAdapter(resources, card, cardDetailsScreenData.relatedCards)
    }

    private fun showCard(card: GwentCard) {
        (activity as? AppCompatActivity)?.title = card.name

        val colour = CardResourceHelper.getColorForFaction(resources, card.faction)
        toolbar.setBackgroundColor(colour)
        tabLayout.setBackgroundColor(colour)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity?.window?.statusBarColor = CardResourceHelper.getDarkColorForFaction(resources, card.faction)
        }
    }

    override fun showLoadingIndicator() {
        refreshLayout.isRefreshing = true
    }

    override fun hideLoadingIndicator() {
        refreshLayout.isRefreshing = false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(KEY_ID, cardId)
        super.onSaveInstanceState(outState)
    }
}
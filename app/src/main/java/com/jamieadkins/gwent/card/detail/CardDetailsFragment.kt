package com.jamieadkins.gwent.card.detail

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jamieadkins.commonutils.mvp2.BasePresenter
import com.jamieadkins.commonutils.mvp3.MvpFragment
import com.jamieadkins.gwent.Injection
import com.jamieadkins.gwent.R
import com.jamieadkins.gwent.core.CardDatabaseResult
import com.jamieadkins.gwent.core.GwentCard
import com.jamieadkins.gwent.view.card.CardDatabaseController
import com.jamieadkins.gwent.view.card.VerticalSpaceItemDecoration
import kotterknife.bindView

class CardDetailsFragment : MvpFragment<DetailContract.View>(), DetailContract.View {

    lateinit var cardId: String
    private lateinit var controller: CardDatabaseController

    private val recyclerView by bindView<RecyclerView>(R.id.recycler_view)

    companion object {
        const val KEY_ID = "cardId"
    }

    override fun setupPresenter(): BasePresenter<DetailContract.View> {
        return DetailPresenter(cardId, Injection.provideCardRepository(), Injection.provideSchedulerProvider())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        cardId = savedInstanceState?.getString(KEY_ID) ?: arguments?.getString(KEY_ID) ?: throw Exception("Card id not found.")
        super.onCreate(savedInstanceState)
        controller = CardDatabaseController(resources)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_card_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(recyclerView.context)
        recyclerView.layoutManager = layoutManager
        val dividerItemDecoration = VerticalSpaceItemDecoration(convertDpToPixel(8f, requireContext()).toInt())
        recyclerView.addItemDecoration(dividerItemDecoration)
        recyclerView.adapter = controller.adapter
    }

    override fun showCard(card: GwentCard) {
        controller.setData(CardDatabaseResult(listOf(card)))
    }

    override fun setLoadingIndicator(loading: Boolean) {

    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(KEY_ID, cardId)
        super.onSaveInstanceState(outState)
    }
}
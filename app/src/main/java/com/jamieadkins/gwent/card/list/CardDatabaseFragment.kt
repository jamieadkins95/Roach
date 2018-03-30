package com.jamieadkins.gwent.card.list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jamieadkins.commonutils.mvp2.BaseListView
import com.jamieadkins.commonutils.mvp2.MvpFragment

import com.jamieadkins.gwent.Injection
import com.jamieadkins.gwent.R
import com.jamieadkins.gwent.bus.RefreshEvent
import com.jamieadkins.gwent.bus.RxBus
import com.jamieadkins.gwent.bus.SnackbarBundle
import com.jamieadkins.gwent.bus.SnackbarRequest
import com.jamieadkins.gwent.core.GwentCard
import com.jamieadkins.gwent.update.UpdateActivity
import com.jamieadkins.gwent.view.card.CardDatabaseController
import kotterknife.bindView
import com.jamieadkins.gwent.view.card.VerticalSpaceItemDecoration
import android.util.DisplayMetrics


class CardDatabaseFragment :
        MvpFragment<CardDatabaseContract.View>(),
        CardDatabaseContract.View,
        SwipeRefreshLayout.OnRefreshListener,
        BaseListView {

    val emptyView by bindView<View>(R.id.empty)
    val recyclerView by bindView<RecyclerView>(R.id.recycler_view)
    val refreshLayout by bindView<SwipeRefreshLayout>(R.id.refreshContainer)
    val controller = CardDatabaseController()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.title = getString(R.string.card_database)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_card_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        refreshLayout.setColorSchemeResources(R.color.gwentAccent)
        refreshLayout.setOnRefreshListener(this)

        val layoutManager = LinearLayoutManager(recyclerView.context)
        recyclerView.layoutManager = layoutManager
        context?.let {
            val dividerItemDecoration = VerticalSpaceItemDecoration(convertDpToPixel(8f, it).toInt())
            recyclerView.addItemDecoration(dividerItemDecoration)
        }
        recyclerView.adapter = controller.adapter
    }

    fun convertDpToPixel(dp: Float, context: Context): Float {
        val resources = context.resources
        val metrics = resources.displayMetrics
        val px = dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
        return px
    }

    override fun setupPresenter() {
        presenter = CardsPresenter(
                Injection.provideSchedulerProvider(),
                Injection.provideCardRepository(),
                Injection.provideUpdateRepository())
    }

    override fun setLoadingIndicator(loading: Boolean) {
        refreshLayout.isRefreshing = loading
    }

    override fun showEmptyView() {
        emptyView.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
    }

    override fun showCards(cards: MutableList<GwentCard>) {
        if (cards.isNotEmpty()) {
            controller.setData(cards.toList())
            emptyView.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        } else {
            showEmptyView()
        }
    }

    override fun showGenericErrorMessage() {
        RxBus.post(SnackbarRequest(SnackbarBundle(getString(R.string.general_error), Snackbar.LENGTH_LONG)))
    }

    override fun onRefresh() {
        RxBus.post(RefreshEvent())
    }

    override fun showUpdateAvailable() {
        val message = getString(R.string.update_available)
        val actionMessage = getString(R.string.update)
        val action = View.OnClickListener {
            val i = Intent(activity, UpdateActivity::class.java)
            startActivity(i)
        }
        RxBus.post(SnackbarRequest(SnackbarBundle(message, actionMessage, action, Snackbar.LENGTH_INDEFINITE)))
    }
}

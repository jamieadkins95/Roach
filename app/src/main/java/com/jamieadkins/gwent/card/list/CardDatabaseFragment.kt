package com.jamieadkins.gwent.card.list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.view.MenuItemCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import com.jamieadkins.commonutils.mvp2.BaseListView
import com.jamieadkins.commonutils.mvp3.MvpFragment

import com.jamieadkins.gwent.Injection
import com.jamieadkins.gwent.R
import com.jamieadkins.gwent.bus.RefreshEvent
import com.jamieadkins.gwent.bus.RxBus
import com.jamieadkins.gwent.core.GwentCard
import com.jamieadkins.gwent.update.UpdateActivity
import com.jamieadkins.gwent.view.card.CardDatabaseController
import kotterknife.bindView
import com.jamieadkins.gwent.view.card.VerticalSpaceItemDecoration
import android.util.DisplayMetrics
import android.view.*
import com.jamieadkins.commonutils.mvp2.BasePresenter
import com.jamieadkins.gwent.base.PresenterFactory
import com.jamieadkins.gwent.bus.ResetFiltersEvent
import com.jamieadkins.gwent.filter.FilterBottomSheetDialogFragment
import com.jamieadkins.gwent.filter.FilterType

class CardDatabaseFragment :
        MvpFragment<CardDatabaseContract.View>(),
        CardDatabaseContract.View,
        SwipeRefreshLayout.OnRefreshListener,
        BaseListView {

    private val screenKey = javaClass.name

    private val emptyView by bindView<View>(R.id.empty)
    private val recyclerView by bindView<RecyclerView>(R.id.recycler_view)
    private val refreshLayout by bindView<SwipeRefreshLayout>(R.id.refreshContainer)
    private val controller = CardDatabaseController()
    private lateinit var cardsPresenter: CardsPresenter

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

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.search, menu)

        val searchMenuItem = menu?.findItem(R.id.action_search)
        val searchView = MenuItemCompat.getActionView(searchMenuItem) as SearchView
        searchView.queryHint = getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                cardsPresenter.search(query)
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                return false
            }
        })

        searchView.setOnCloseListener {
            cardsPresenter.clearSearch()
            false
        }

        inflater?.inflate(R.menu.card_filters, menu)
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

    override fun setupPresenter(): BasePresenter<CardDatabaseContract.View> {
        val newPresenter = PresenterFactory.getPresenter(
                javaClass.simpleName,
                { CardsPresenter(
                        Injection.provideSchedulerProvider(),
                        Injection.provideCardRepository(),
                        Injection.provideUpdateRepository(),
                        Injection.provideFilterRepository(screenKey))
                })
        cardsPresenter = newPresenter as CardsPresenter
        return newPresenter
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.filter_faction, R.id.filter_rarity, R.id.filter_colour -> {
                val filterType = when (item.itemId) {
                    R.id.filter_faction -> FilterType.FACTION
                    R.id.filter_rarity -> FilterType.RARITY
                    R.id.filter_colour -> FilterType.COLOUR
                    else -> throw Exception("Filter not supported")
                }
                val dialog = FilterBottomSheetDialogFragment.newInstance(filterType, screenKey)
                dialog.show(activity?.supportFragmentManager, dialog.javaClass.simpleName)
                true
            }
            R.id.filter_reset -> {
                RxBus.post(ResetFiltersEvent())
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
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

        view?.let {
            val snackbar = Snackbar.make(it, message, Snackbar.LENGTH_INDEFINITE)
            snackbar.setAction(actionMessage, action)
            snackbar.show()
        }

    }
}

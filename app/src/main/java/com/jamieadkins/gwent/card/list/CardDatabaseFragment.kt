package com.jamieadkins.gwent.card.list

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.jamieadkins.gwent.base.ScrollView
import com.jamieadkins.gwent.R
import com.jamieadkins.gwent.base.FeatureNavigator
import com.jamieadkins.gwent.base.VerticalSpaceItemDecoration
import com.jamieadkins.gwent.base.convertDpToPixel
import com.jamieadkins.gwent.base.items.HeaderItem
import com.jamieadkins.gwent.base.items.NoticeItem
import com.jamieadkins.gwent.bus.GwentCardClickEvent
import com.jamieadkins.gwent.bus.RxBus
import com.jamieadkins.gwent.domain.card.screen.CardDatabaseScreenModel
import com.jamieadkins.gwent.filter.FilterBottomSheetDialogFragment
import com.jamieadkins.gwent.update.UpdateService
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.appbar_layout.*
import kotlinx.android.synthetic.main.fragment_card_list.*
import javax.inject.Inject
import android.net.Uri

class CardDatabaseFragment :
    DaggerFragment(),
    CardDatabaseContract.View,
    SwipeRefreshLayout.OnRefreshListener,
    ScrollView {

    @Inject
    lateinit var presenter: CardDatabaseContract.Presenter

    private val adapter = GroupAdapter<ViewHolder>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_card_list, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.search, menu)

        val searchMenuItem = menu?.findItem(R.id.action_search)
        val searchView = searchMenuItem?.actionView as? SearchView
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                presenter.search(query)
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                return false
            }
        })

        searchView?.setOnCloseListener {
            presenter.clearSearch()
            false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as? AppCompatActivity)?.apply {
            setSupportActionBar(toolbar)
            title = getString(R.string.card_database)
        }

        toolbar.setTitleTextAppearance(requireContext(), R.style.GwentTextAppearance)

        refreshContainer.setColorSchemeResources(R.color.gwentAccent)
        refreshContainer.setOnRefreshListener(this)

        val layoutManager = LinearLayoutManager(recycler_view.context)
        recycler_view.layoutManager = layoutManager
        val dividerItemDecoration = VerticalSpaceItemDecoration(requireContext().convertDpToPixel(8f).toInt())
        recycler_view.addItemDecoration(dividerItemDecoration)
        recycler_view.adapter = adapter
        recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (layoutManager.findLastVisibleItemPosition() - layoutManager.findFirstVisibleItemPosition() + 1 < adapter.itemCount) {
                    buttonReturnToTop.visibility = if (dy < 0) View.VISIBLE else View.GONE
                }
                if (layoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
                    buttonReturnToTop.visibility = View.GONE
                }
            }
        })

        buttonReturnToTop.setOnClickListener {
            recycler_view.scrollToPosition(0)
        }

        adapter.setOnItemClickListener { item, _ ->
            when (item) {
                is GwentCardItem -> RxBus.post(GwentCardClickEvent(item.card.id))
            }
        }

        presenter.onAttach()
    }

    override fun onDestroyView() {
        presenter.onDetach()
        super.onDestroyView()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_filter -> {
                val dialog = FilterBottomSheetDialogFragment()
                dialog.show(activity?.supportFragmentManager, dialog.javaClass.simpleName)
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun showLoadingIndicator(loading: Boolean) {
        refreshContainer.isRefreshing = loading
    }

    override fun showData(data: CardDatabaseScreenModel) {
        // Diffing here is too expensive.
        adapter.clear()

        val searchResults: List<Item> = if (data.searchQuery.isNotEmpty()) {
            listOf(
                HeaderItem(
                    R.string.search_results, resources.getString(R.string.results_found, data.cards.size, data.searchQuery)
                )
            )
        } else {
            emptyList()
        }

        val instantAppNotice = if (data.showInstantAppNotice) listOf(InstantAppNoticeItem(::onInstallClicked)) else emptyList()

        adapter.update(
            instantAppNotice +
            data.notices.map { NoticeItem(it.title) } +
            searchResults +
            data.cards.map { GwentCardItem(it) }
        )

        recycler_view.visibility = View.VISIBLE
    }

    override fun openUpdateScreen() {
        val intent = Intent(requireContext(), UpdateService::class.java)
        activity?.startService(intent)
    }

    override fun onRefresh() {
        presenter.onRefresh()
    }

    override fun scrollToTop() {
        recycler_view.smoothScrollToPosition(0)
    }

    override fun showCardDetails(cardId: String) {
        activity?.let { FeatureNavigator(it).openCardDetails(cardId) }
    }

    private fun onInstallClicked() {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://play.google.com/store/apps/details?id=com.jamieadkins.gwent")
        )
        context?.startActivity(intent)
    }
}

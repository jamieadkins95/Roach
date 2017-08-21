package com.jamieadkins.gwent.base;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.preference.BuildConfig;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.jamieadkins.commonutils.mvp2.BaseListView;
import com.jamieadkins.commonutils.mvp2.BaseView;
import com.jamieadkins.commonutils.mvp2.MvpFragment;
import com.jamieadkins.commonutils.ui.RecyclerViewItem;
import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.card.CardFilter;
import com.jamieadkins.gwent.card.CardFilterListener;
import com.jamieadkins.gwent.data.Faction;
import com.jamieadkins.gwent.data.Filterable;
import com.jamieadkins.gwent.data.Rarity;
import com.jamieadkins.gwent.data.Type;
import com.jamieadkins.gwent.data.interactor.RxDatabaseEvent;
import com.jamieadkins.gwent.filter.FilterBottomSheetDialogFragment;
import com.jamieadkins.gwent.filter.FilterableItem;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;

/**
 * UI fragment that shows a list of the users decks.
 */
public abstract class BaseFragment<V> extends MvpFragment<V>
        implements SwipeRefreshLayout.OnRefreshListener,
        FilterBottomSheetDialogFragment.FilterUiListener, BaseListView {
    private static final String STATE_CARD_FILTER = "com.jamieadkins.gwent.card.filter";
    public static final String TAG_FILTER_MENU = "com.jamieadkins.gwent.filter.menu";

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mRefreshContainer;
    private GwentRecyclerViewAdapter mAdapter;

    private FilterBottomSheetDialogFragment mFilterMenu;
    public BaseFilterPresenter<V> filterPresenter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filterPresenter = (BaseFilterPresenter) getPresenter();

        if (savedInstanceState != null) {
            filterPresenter.cardFilter = (CardFilter) savedInstanceState.get(STATE_CARD_FILTER);
        } else {
            filterPresenter.cardFilter = initialiseCardFilter();
        }
    }

    public CardFilter initialiseCardFilter() {
        CardFilter filter = new CardFilter();
        filter.setCurrentFilterAsBase();
        return filter;
    }

    public void setupViews(View rootView) {
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        setupRecyclerView(mRecyclerView);

        mRefreshContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.refreshContainer);
        mRefreshContainer.setColorSchemeResources(R.color.gwentAccent);
        mRefreshContainer.setOnRefreshListener(this);
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        final LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (Build.VERSION.SDK_INT >= 17) {
                    if (getActivity().isDestroyed()) {
                        return;
                    }
                }

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Glide.with(getActivity()).resumeRequests();
                } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    Glide.with(getActivity()).pauseRequests();
                }
            }
        });
        mAdapter = onBuildRecyclerView();
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void setLoadingIndicator(boolean loading) {
        mRefreshContainer.setRefreshing(loading);
    }

    @Override
    public void showItem(RecyclerViewItem item) {
        mAdapter.addItem(item);
    }

    public GwentRecyclerViewAdapter onBuildRecyclerView() {
        return new GwentRecyclerViewAdapter.Builder()
                .build();
    }

    public GwentRecyclerViewAdapter getRecyclerViewAdapter() {
        return mAdapter;
    }

    @Override
    public void onRefresh() {
        getPresenter().onRefresh();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mFilterMenu != null) {
            mFilterMenu.dismiss();
        }
        outState.putParcelable(STATE_CARD_FILTER, filterPresenter.cardFilter);
        super.onSaveInstanceState(outState);
    }

    public void setupFilterMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search, menu);

        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
        searchView.setQueryHint(getString(R.string.search_hint));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterPresenter.updateSearchQuery(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                filterPresenter.updateSearchQuery(null);
                return false;
            }
        });

        if (filterPresenter.cardFilter.getSearchQuery() != null) {
            searchView.setQuery(filterPresenter.cardFilter.getSearchQuery(), false);
        }

        inflater.inflate(R.menu.card_filters, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        List<FilterableItem> filterableItems = new ArrayList<>();
        String filteringOn;
        Filterable[] filterItems;
        switch (item.getItemId()) {
            case R.id.filter_reset:
                filterPresenter.clearFilters();
                return true;
            case R.id.filter_faction:
                filteringOn = getString(R.string.faction);
                filterItems = Faction.ALL_FACTIONS;
                break;
            case R.id.filter_rarity:
                filteringOn = getString(R.string.rarity);
                filterItems = Rarity.ALL_RARITIES;
                break;
            case R.id.filter_type:
                filteringOn = getString(R.string.type);
                filterItems = Type.ALL_TYPES;
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        for (Filterable filterable : filterItems) {
            filterableItems.add(new FilterableItem(
                    filterable.getId(),
                    getString(filterable.getName()),
                    filterPresenter.cardFilter.get(filterable.getId())));
        }

        showFilterMenu(filteringOn, filterableItems);
        return true;
    }

    public void showFilterMenu(String filteringOn, List<FilterableItem> items) {
        mFilterMenu = FilterBottomSheetDialogFragment
                .newInstance(filteringOn, items, this);
        mFilterMenu.show(getChildFragmentManager(), TAG_FILTER_MENU);
    }

    @Override
    public void onFilterChanged(String key, boolean checked) {
        filterPresenter.updateFilter(key, checked);
    }

    @Override
    public void onFilterDismissed(boolean filtersChanged) {
        if (filtersChanged) {
            filterPresenter.onCardFilterUpdated();
        }
    }
}

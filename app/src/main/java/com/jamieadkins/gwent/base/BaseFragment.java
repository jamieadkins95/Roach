package com.jamieadkins.gwent.base;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.graphics.drawable.VectorDrawableCompat;
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
import com.jamieadkins.gwent.bus.RxBus;
import com.jamieadkins.gwent.bus.SnackbarBundle;
import com.jamieadkins.gwent.bus.SnackbarRequest;
import com.jamieadkins.gwent.card.CardFilter;
import com.jamieadkins.gwent.card.CardFilterListener;
import com.jamieadkins.gwent.data.Faction;
import com.jamieadkins.gwent.data.Filterable;
import com.jamieadkins.gwent.data.Loyalty;
import com.jamieadkins.gwent.data.Position;
import com.jamieadkins.gwent.data.Rarity;
import com.jamieadkins.gwent.data.Type;
import com.jamieadkins.gwent.data.interactor.RxDatabaseEvent;
import com.jamieadkins.gwent.filter.FilterBottomSheetDialogFragment;
import com.jamieadkins.gwent.filter.FilterableItem;
import com.trello.rxlifecycle2.components.support.RxFragment;

import org.jetbrains.annotations.NotNull;

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
    private static final String STATE_CARD_QUERY = "com.jamieadkins.gwent.card.query";
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
            filterPresenter.updateSearchQuery(savedInstanceState.getString(STATE_CARD_QUERY, null));
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
        mAdapter = onBuildRecyclerView();
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void setLoadingIndicator(final boolean loading) {
        mRefreshContainer.post(new Runnable() {
            @Override
            public void run() {
                mRefreshContainer.setRefreshing(loading);
            }
        });
    }

    @Override
    public void showGenericErrorMessage() {
        RxBus.INSTANCE.post(new SnackbarRequest(new SnackbarBundle(getString(R.string.general_error), Snackbar.LENGTH_LONG)));
    }

    @Override
    public void showItems(@NotNull List<? extends RecyclerViewItem> items) {
        mAdapter.setItems(items);
    }

    @Override
    public void showEmptyView() {
        RxBus.INSTANCE.post(new SnackbarRequest(new SnackbarBundle(getString(R.string.no_results), Snackbar.LENGTH_LONG)));
    }

    public GwentRecyclerViewAdapter onBuildRecyclerView() {
        return new GwentRecyclerViewAdapter.Builder()
                .build();
    }

    public GwentRecyclerViewAdapter getRecyclerViewAdapter() {
        return mAdapter;
    }

    public SwipeRefreshLayout getRefreshLayout() {
        return mRefreshContainer;
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
        outState.putString(STATE_CARD_QUERY, filterPresenter.getSearchQuery());
        super.onSaveInstanceState(outState);
    }

    public void setupFilterMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search, menu);

        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchMenuItem.getActionView();
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
            case R.id.filter_positions:
                filteringOn = getString(R.string.position);
                filterItems = Position.ALL_POSITIONS;
                break;
            case R.id.filter_loyalty:
                filteringOn = getString(R.string.loyalty);
                filterItems = Loyalty.ALL_LOYALTIES;
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

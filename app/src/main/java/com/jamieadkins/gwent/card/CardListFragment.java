package com.jamieadkins.gwent.card;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.data.CardDetails;

/**
 * UI fragment that shows a list of the users decks.
 */

public class CardListFragment extends Fragment implements CardsContract.View {
    private CardsContract.Presenter mCardsPresenter;
    private RecyclerView mCardListView;
    private SwipeRefreshLayout mRefreshContainer;
    private CardRecyclerViewAdapter mViewAdapter;

    public CardListFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewAdapter = new CardRecyclerViewAdapter(CardRecyclerViewAdapter.Detail.LARGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_card_list, container, false);

        mCardListView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        setupRecyclerView(mCardListView);

        mRefreshContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.refreshContainer);
        mRefreshContainer.setColorSchemeResources(R.color.gwentAccent);

        return rootView;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(mViewAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        mCardsPresenter.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        mViewAdapter.clear();
        mCardsPresenter.stop();
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        mRefreshContainer.setRefreshing(active);

        // We don't want user's trying to refresh, so only enable the view when we are loading.
        mRefreshContainer.setEnabled(active);
    }

    @Override
    public void showCard(CardDetails deck) {
        mViewAdapter.addItem(deck);
    }

    @Override
    public void setPresenter(CardsContract.Presenter presenter) {
        mCardsPresenter = presenter;
    }
}

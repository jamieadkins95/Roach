package com.jamieadkins.gwent.deck.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jamieadkins.commonutils.ui.GoogleNowSubHeader;
import com.jamieadkins.commonutils.ui.RecyclerViewItem;
import com.jamieadkins.commonutils.ui.SubHeader;
import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.base.BaseFragment;
import com.jamieadkins.gwent.data.deck.Deck;
import com.jamieadkins.gwent.model.GwentFaction;
import com.jamieadkins.gwent.model.GwentCard;
import com.jamieadkins.gwent.model.deck.GwentDeckCard;
import com.jamieadkins.gwent.view.DeckController;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * UI fragment that shows a list of the users decks.
 */

public abstract class BaseDeckDetailFragment<T extends DeckDetailsContract.DeckDetailsView> extends BaseFragment<T>
        implements DeckDetailsContract.DeckDetailsView {
    private static final int LEADER_INDEX = 0;
    protected String mDeckId;
    protected Deck mDeck;
    protected GwentFaction mFaction;

    private DeckController deckController = new DeckController();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mDeckId = savedInstanceState.getString(DeckDetailActivity.EXTRA_DECK_ID);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(getLayoutId(), container, false);
        setupViews(rootView);

        getRefreshLayout().setEnabled(false);
        return rootView;
    }

    @Override
    public void setupViews(View rootView) {
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        final LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(mRecyclerView.getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(deckController.getAdapter());
        deckController.setData(new ArrayList<String>());

        mRefreshContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.refreshContainer);
        mRefreshContainer.setColorSchemeResources(R.color.gwentAccent);
        mRefreshContainer.setOnRefreshListener(this);
    }

    protected abstract int getLayoutId();

    @Override
    public void showCardsInDeck(@NotNull List<GwentDeckCard> cards) {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(DeckDetailActivity.EXTRA_DECK_ID, mDeckId);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onLeaderChanged(GwentCard newLeader) {
    }
}

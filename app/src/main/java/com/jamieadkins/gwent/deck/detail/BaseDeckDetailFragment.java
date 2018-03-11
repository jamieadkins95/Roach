package com.jamieadkins.gwent.deck.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
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

import org.jetbrains.annotations.NotNull;

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

    private Map<String, SubHeader> mRowHeaders = new HashMap<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mDeckId = savedInstanceState.getString(DeckDetailActivity.EXTRA_DECK_ID);
            mFaction = (GwentFaction) savedInstanceState.getSerializable(DeckDetailActivity.EXTRA_FACTION_ID);
        }
        super.onCreate(savedInstanceState);
        mRowHeaders.put(getString(R.string.leader), new GoogleNowSubHeader(getString(R.string.leader), R.color.gold));
        mRowHeaders.put(getString(R.string.gold), new GoogleNowSubHeader(getString(R.string.gold), R.color.gold));
        mRowHeaders.put(getString(R.string.silver), new GoogleNowSubHeader(getString(R.string.silver), R.color.silver));
        mRowHeaders.put(getString(R.string.bronze), new GoogleNowSubHeader(getString(R.string.bronze), R.color.bronze));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(getLayoutId(), container, false);
        setupViews(rootView);

        getRecyclerViewAdapter().addItem(LEADER_INDEX, mRowHeaders.get(getString(R.string.leader)));
        getRecyclerViewAdapter().addItem(1, mRowHeaders.get(getString(R.string.gold)));
        getRecyclerViewAdapter().addItem(2, mRowHeaders.get(getString(R.string.silver)));
        getRecyclerViewAdapter().addItem(3, mRowHeaders.get(getString(R.string.bronze)));

        getRefreshLayout().setEnabled(false);
        return rootView;
    }

    protected abstract int getLayoutId();

    @Override
    public void showCardsInDeck(@NotNull List<GwentDeckCard> cards) {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(DeckDetailActivity.EXTRA_DECK_ID, mDeckId);
        outState.putSerializable(DeckDetailActivity.EXTRA_FACTION_ID, mFaction);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onLeaderChanged(GwentCard newLeader) {
        int leaderIndex = getRecyclerViewAdapter().getItems()
                .indexOf(mRowHeaders.get(getString(R.string.leader))) + 1;
        RecyclerViewItem oldLeader = getRecyclerViewAdapter().getItems().get(leaderIndex);
        if (oldLeader instanceof GwentCard && !oldLeader.equals(newLeader)) {
            getRecyclerViewAdapter().removeItemAt(leaderIndex);
        }
        getRecyclerViewAdapter().addItem(leaderIndex, newLeader);
    }
}

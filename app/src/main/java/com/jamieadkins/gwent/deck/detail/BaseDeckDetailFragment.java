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
import com.jamieadkins.gwent.card.detail.DetailActivity;
import com.jamieadkins.gwent.data.CardDetails;
import com.jamieadkins.gwent.data.Deck;
import com.jamieadkins.gwent.data.Type;
import com.jamieadkins.gwent.data.interactor.RxDatabaseEvent;
import com.jamieadkins.gwent.deck.list.DecksContract;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * UI fragment that shows a list of the users decks.
 */

public abstract class BaseDeckDetailFragment extends BaseFragment
        implements DecksContract.View {
    private static final int LEADER_INDEX = 0;
    protected DecksContract.Presenter mDecksPresenter;
    protected String mDeckId;
    protected Deck mDeck;
    private String mPatch;
    protected String mFactionId;

    private Map<String, SubHeader> mRowHeaders = new HashMap<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mDeckId = savedInstanceState.getString(DeckDetailActivity.EXTRA_DECK_ID);
            mPatch = savedInstanceState.getString(DetailActivity.EXTRA_PATCH);
            mFactionId = savedInstanceState.getString(DeckDetailActivity.EXTRA_FACTION_ID);
        }
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
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        onLoadData();
    }

    @Override
    public void onLoadData() {
        super.onLoadData();
        mDecksPresenter.getDeck(mDeckId, false)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver());
    }

    @Override
    public void onCardFilterUpdated() {
        // Do nothing.
    }

    @Override
    public void onDataEvent(RxDatabaseEvent<? extends RecyclerViewItem> data) {
        super.onDataEvent(data);
        onDeckLoaded((Deck) data.getValue());
    }

    protected abstract int getLayoutId();

    protected void onDeckLoaded(Deck deck) {
        mDeck = deck;
        getActivity().setTitle(mDeck.getName());

        // Add the sub headers.
        if (getRecyclerViewAdapter().isAnItemAt(LEADER_INDEX)) {
            getRecyclerViewAdapter().replaceItem(LEADER_INDEX, mDeck.getLeader());
        } else {
            getRecyclerViewAdapter().addItem(LEADER_INDEX, mDeck.getLeader());
        }
        getRecyclerViewAdapter().addItem(1, mRowHeaders.get(getString(R.string.gold)));
        getRecyclerViewAdapter().addItem(2, mRowHeaders.get(getString(R.string.silver)));
        getRecyclerViewAdapter().addItem(3, mRowHeaders.get(getString(R.string.bronze)));

        for (String cardId : mDeck.getCards().keySet()) {
            CardDetails card = mDeck.getCards().get(cardId);
            if (mDeck.getCardCount().get(cardId) > 0) {
                switch (card.getType()) {
                    case Type.BRONZE_ID:
                        getRecyclerViewAdapter().addItem(card);
                        break;
                    case Type.SILVER_ID:
                        int bronzeIndex = getRecyclerViewAdapter().getItems().indexOf(mRowHeaders.get(getString(R.string.bronze)));
                        getRecyclerViewAdapter().addItem(bronzeIndex, card);
                        break;
                    case Type.GOLD_ID:
                        int silverIndex = getRecyclerViewAdapter().getItems().indexOf(mRowHeaders.get(getString(R.string.silver)));
                        getRecyclerViewAdapter().addItem(silverIndex, card);
                        break;
                }

            } else {
                // Remove the card from the list.
                getRecyclerViewAdapter().removeItem(card);
            }
        }

        setLoadingIndicator(false);
    }

    @Override
    public void onStop() {
        super.onStop();
        mDecksPresenter.stop();
    }

    @Override
    public void setPresenter(DecksContract.Presenter presenter) {
        mDecksPresenter = presenter;
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        setLoading(active);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(DeckDetailActivity.EXTRA_DECK_ID, mDeckId);
        outState.putString(DetailActivity.EXTRA_PATCH, mPatch);
        outState.putString(DeckDetailActivity.EXTRA_FACTION_ID, mFactionId);
        super.onSaveInstanceState(outState);
    }
}

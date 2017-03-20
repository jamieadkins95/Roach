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
import com.jamieadkins.gwent.base.BaseCompletableObserver;
import com.jamieadkins.gwent.base.BaseFragment;
import com.jamieadkins.gwent.base.BaseObserver;
import com.jamieadkins.gwent.base.BaseSingleObserver;
import com.jamieadkins.gwent.base.GwentRecyclerViewAdapter;
import com.jamieadkins.gwent.card.detail.DetailActivity;
import com.jamieadkins.gwent.data.CardDetails;
import com.jamieadkins.gwent.data.Deck;
import com.jamieadkins.gwent.data.Type;
import com.jamieadkins.gwent.data.interactor.RxDatabaseEvent;
import com.jamieadkins.gwent.deck.list.DecksContract;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
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

    private BaseObserver<RxDatabaseEvent<Integer>> mCardCountObserver
            = new BaseObserver<RxDatabaseEvent<Integer>>() {
        @Override
        public void onNext(RxDatabaseEvent<Integer> value) {
            onDeckCardCountsChanged(value);
        }

        @Override
        public void onComplete() {

        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mDeckId = savedInstanceState.getString(DeckDetailActivity.EXTRA_DECK_ID);
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
        getRecyclerViewAdapter().addItem(LEADER_INDEX, mRowHeaders.get(getString(R.string.leader)));
        getRecyclerViewAdapter().addItem(1, mRowHeaders.get(getString(R.string.gold)));
        getRecyclerViewAdapter().addItem(2, mRowHeaders.get(getString(R.string.silver)));
        getRecyclerViewAdapter().addItem(3, mRowHeaders.get(getString(R.string.bronze)));

        mDecksPresenter.getDeck(mDeckId, false)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver());
        mDecksPresenter.subscribeToCardCountUpdates(mDeckId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mCardCountObserver);
    }

    @Override
    public void onCardFilterUpdated() {
        // Do nothing.
    }

    @Override
    public void onDataEvent(RxDatabaseEvent<? extends RecyclerViewItem> data) {
        onDeckLoaded((Deck) data.getValue());
    }

    protected abstract int getLayoutId();

    protected void onDeckLoaded(final Deck deck) {
        getActivity().setTitle(deck.getName());
        boolean getLeader = false;
        if (mDeck == null) {
            getLeader = true;
        } else {
            getLeader = !mDeck.getLeaderId().equals(deck.getLeaderId());
        }

        if (getLeader) {
            mDecksPresenter.getCard(deck.getLeaderId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseSingleObserver<RxDatabaseEvent<CardDetails>>() {
                        @Override
                        public void onSuccess(RxDatabaseEvent<CardDetails> value) {
                            int gold = getRecyclerViewAdapter().getItems()
                                    .indexOf(mRowHeaders.get(getString(R.string.gold)));
                            if (getRecyclerViewAdapter().getItemAt(gold - 1).getItemType()
                                    == GwentRecyclerViewAdapter.TYPE_CARD_LEADER) {
                                getRecyclerViewAdapter().replaceItem(gold - 1, value.getValue());
                            } else {
                                getRecyclerViewAdapter().addItem(gold, value.getValue());
                            }
                        }
                    });
        }

        mDeck = deck;
    }

    protected void onDeckCardCountsChanged(RxDatabaseEvent<Integer> data) {
        if (data.getEventType() != RxDatabaseEvent.EventType.COMPLETE) {
            getRecyclerViewAdapter().updateCardCount(data.getKey(), data.getValue());
        }

        switch (data.getEventType()) {
            case ADDED:
                mDecksPresenter.getCard(data.getKey())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new BaseSingleObserver<RxDatabaseEvent<CardDetails>>() {
                            @Override
                            public void onSuccess(RxDatabaseEvent<CardDetails> value) {
                                CardDetails card = value.getValue();
                                switch (card.getType()) {
                                    case Type.BRONZE_ID:
                                        getRecyclerViewAdapter().addItem(card);
                                        break;
                                    case Type.SILVER_ID:
                                        int bronzeIndex = getRecyclerViewAdapter().getItems()
                                                .indexOf(mRowHeaders.get(getString(R.string.bronze)));
                                        getRecyclerViewAdapter().addItem(bronzeIndex, card);
                                        break;
                                    case Type.GOLD_ID:
                                        int silverIndex = getRecyclerViewAdapter().getItems()
                                                .indexOf(mRowHeaders.get(getString(R.string.silver)));
                                        getRecyclerViewAdapter().addItem(silverIndex, card);
                                        break;
                                }
                            }
                        });
                break;
            case REMOVED:
                getRecyclerViewAdapter().removeCard(data.getKey());
                break;
            case COMPLETE:
                setLoadingIndicator(false);
                break;
        }
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
        outState.putString(DeckDetailActivity.EXTRA_FACTION_ID, mFactionId);
        super.onSaveInstanceState(outState);
    }
}

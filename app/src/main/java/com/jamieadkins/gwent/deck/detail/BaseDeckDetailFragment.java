package com.jamieadkins.gwent.deck.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jamieadkins.commonutils.ui.Header;
import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.base.BaseFragment;
import com.jamieadkins.gwent.base.BaseObserver;
import com.jamieadkins.gwent.card.CardFilter;
import com.jamieadkins.gwent.card.CardFilterProvider;
import com.jamieadkins.gwent.card.list.CardRecyclerViewAdapter;
import com.jamieadkins.gwent.data.CardDetails;
import com.jamieadkins.gwent.data.Deck;
import com.jamieadkins.gwent.data.Position;
import com.jamieadkins.gwent.data.Type;
import com.jamieadkins.gwent.data.interactor.RxDatabaseEvent;
import com.jamieadkins.gwent.deck.list.DecksContract;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * UI fragment that shows a list of the users decks.
 */

public abstract class BaseDeckDetailFragment extends BaseFragment<CardDetails> implements DecksContract.View {
    protected DecksContract.Presenter mDecksPresenter;
    protected String mDeckId;
    protected Deck mDeck;
    protected Observer<RxDatabaseEvent<Deck>> mObserver =
            new BaseObserver<RxDatabaseEvent<Deck>>() {
                @Override
                public void onNext(RxDatabaseEvent<Deck> value) {
                    onDeckLoaded(value.getValue());
                }

                @Override
                public void onComplete() {

                }
            };

    private Map<String, Header> mRowHeaders = new HashMap<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRecyclerViewAdapter(new CardRecyclerViewAdapter(CardRecyclerViewAdapter.Detail.LARGE));

        if (savedInstanceState != null) {
            mDeckId = savedInstanceState.getString(DeckDetailActivity.EXTRA_DECK_ID);

        }
        mRowHeaders.put(getString(R.string.leader), new Header(getString(R.string.leader), null));
        mRowHeaders.put(getString(R.string.gold_units), new Header(getString(R.string.gold_units), null));
        mRowHeaders.put(getString(R.string.silver_units), new Header(getString(R.string.silver_units), null));
        mRowHeaders.put(getString(R.string.bronze_units), new Header(getString(R.string.bronze_units), null));
        mRowHeaders.put(getString(R.string.event_cards), new Header(getString(R.string.event_cards), null));
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

    protected abstract int getLayoutId();

    protected void onDeckLoaded(Deck deck) {
        mDeck = deck;
        getActivity().setTitle(mDeck.getName());

        // Add the sub headers.
        getRecyclerViewAdapter().addItem(0, mRowHeaders.get(getString(R.string.leader)));
        getRecyclerViewAdapter().addItem(1, mDeck.getLeader());
        getRecyclerViewAdapter().addItem(2, mRowHeaders.get(getString(R.string.gold_units)));
        getRecyclerViewAdapter().addItem(3, mRowHeaders.get(getString(R.string.silver_units)));
        getRecyclerViewAdapter().addItem(4, mRowHeaders.get(getString(R.string.bronze_units)));
        getRecyclerViewAdapter().addItem(5, mRowHeaders.get(getString(R.string.event_cards)));

        for (String cardId : mDeck.getCards().keySet()) {
            CardDetails card = mDeck.getCards().get(cardId);
            if (mDeck.getCardCount().get(cardId) > 0) {
                if (card.getLane().contains(Position.EVENT)) {
                    getRecyclerViewAdapter().addItem(card);
                } else {
                    switch (card.getType()) {
                        case Type.BRONZE:
                            int eventIndex = getRecyclerViewAdapter().getItems().indexOf(mRowHeaders.get(getString(R.string.event_cards)));
                            getRecyclerViewAdapter().addItem(eventIndex, card);
                            break;
                        case Type.SILVER:
                            int bronzeIndex = getRecyclerViewAdapter().getItems().indexOf(mRowHeaders.get(getString(R.string.bronze_units)));
                            getRecyclerViewAdapter().addItem(bronzeIndex, card);
                            break;
                        case Type.GOLD:
                            int silverIndex = getRecyclerViewAdapter().getItems().indexOf(mRowHeaders.get(getString(R.string.silver_units)));
                            getRecyclerViewAdapter().addItem(silverIndex, card);
                            break;
                    }
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
        super.onSaveInstanceState(outState);
    }
}

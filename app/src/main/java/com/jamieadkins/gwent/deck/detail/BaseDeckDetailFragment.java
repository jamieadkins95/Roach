package com.jamieadkins.gwent.deck.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jamieadkins.gwent.base.BaseSingleObserver;
import com.jamieadkins.gwent.card.list.BaseCardListFragment;
import com.jamieadkins.gwent.card.list.CardRecyclerViewAdapter;
import com.jamieadkins.gwent.data.Deck;
import com.jamieadkins.gwent.data.interactor.RxDatabaseEvent;
import com.jamieadkins.gwent.deck.list.DecksContract;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

/**
 * UI fragment that shows a list of the users decks.
 */

public abstract class BaseDeckDetailFragment extends BaseCardListFragment implements DecksContract.View {
    protected DecksContract.Presenter mDecksPresenter;
    protected String mDeckId;
    protected Deck mDeck;
    protected SingleObserver<RxDatabaseEvent<Deck>> mObserver =
            new BaseSingleObserver<RxDatabaseEvent<Deck>>() {
                @Override
                public void onSuccess(RxDatabaseEvent<Deck> value) {
                    onDeckLoaded(value.getValue());
                }
            };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRecyclerViewAdapter(new CardRecyclerViewAdapter(CardRecyclerViewAdapter.Detail.LARGE));

        if (savedInstanceState != null) {
            mDeckId = savedInstanceState.getString(DeckDetailActivity.EXTRA_DECK_ID);
        }
    }

    protected void onDeckLoaded(Deck deck) {
        mDeck = deck;
        getActivity().setTitle(mDeck.getName());

        getRecyclerViewAdapter().clear();
        for (String cardId : mDeck.getCards().keySet()) {
            if (mDeck.getCardCount().get(cardId) > 0) {
                getRecyclerViewAdapter().addItem(mDeck.getCards().get(cardId));
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
        setCardsPresenter(presenter);
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

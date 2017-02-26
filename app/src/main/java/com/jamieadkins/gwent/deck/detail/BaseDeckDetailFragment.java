package com.jamieadkins.gwent.deck.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jamieadkins.gwent.base.BaseFragment;
import com.jamieadkins.gwent.base.BaseObserver;
import com.jamieadkins.gwent.card.CardFilter;
import com.jamieadkins.gwent.card.CardFilterProvider;
import com.jamieadkins.gwent.card.list.CardRecyclerViewAdapter;
import com.jamieadkins.gwent.data.CardDetails;
import com.jamieadkins.gwent.data.Deck;
import com.jamieadkins.gwent.data.interactor.RxDatabaseEvent;
import com.jamieadkins.gwent.deck.list.DecksContract;

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRecyclerViewAdapter(new CardRecyclerViewAdapter(CardRecyclerViewAdapter.Detail.LARGE));

        if (savedInstanceState != null) {
            mDeckId = savedInstanceState.getString(DeckDetailActivity.EXTRA_DECK_ID);
        }
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

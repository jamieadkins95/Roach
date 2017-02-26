package com.jamieadkins.gwent.deck.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.view.View;

import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.card.CardFilter;
import com.jamieadkins.gwent.card.list.BaseCardListFragment;
import com.jamieadkins.gwent.card.list.CardRecyclerViewAdapter;
import com.jamieadkins.gwent.data.Deck;
import com.jamieadkins.gwent.data.interactor.RxDatabaseEvent;
import com.jamieadkins.gwent.deck.list.DecksContract;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * UI fragment that shows a list of the users decks.
 */

public abstract class BaseDeckDetailFragment extends BaseCardListFragment implements DecksContract.View {
    protected DecksContract.Presenter mDecksPresenter;
    protected String mDeckId;
    protected Observer<RxDatabaseEvent<Deck>> mObserver = new Observer<RxDatabaseEvent<Deck>>() {
        @Override
        public void onSubscribe(Disposable d) {

        }

        @Override
        public void onNext(RxDatabaseEvent<Deck> value) {
            getActivity().setTitle(value.getValue().getName());

            getRecyclerViewAdapter().clear();
            for (String cardId : value.getValue().getCards().keySet()) {
                getRecyclerViewAdapter().addItem(value.getValue().getCards().get(cardId));
            }
        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onComplete() {

        }
    };

    public BaseDeckDetailFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRecyclerViewAdapter(new CardRecyclerViewAdapter(CardRecyclerViewAdapter.Detail.LARGE));

        if (savedInstanceState != null) {
            mDeckId = savedInstanceState.getString(DeckDetailActivity.EXTRA_DECK_ID);
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

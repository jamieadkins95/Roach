package com.jamieadkins.gwent.deck.detail.user;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jamieadkins.commonutils.mvp.PresenterFactory;
import com.jamieadkins.gwent.InteractorContainer;
import com.jamieadkins.gwent.InteractorContainers;
import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.base.BaseObserver;
import com.jamieadkins.gwent.base.GwentRecyclerViewAdapter;
import com.jamieadkins.gwent.card.CardFilterProvider;
import com.jamieadkins.gwent.card.list.BaseCardListFragment;
import com.jamieadkins.gwent.card.list.CardListFragment;
import com.jamieadkins.gwent.card.list.CardsContract;
import com.jamieadkins.gwent.data.CardDetails;
import com.jamieadkins.gwent.data.Deck;
import com.jamieadkins.gwent.data.Faction;
import com.jamieadkins.gwent.data.Filterable;
import com.jamieadkins.gwent.data.interactor.RxDatabaseEvent;
import com.jamieadkins.gwent.deck.detail.DeckDetailActivity;
import com.jamieadkins.gwent.deck.list.DecksContract;
import com.jamieadkins.gwent.deck.list.DecksPresenter;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by jamiea on 28/02/17.
 */

public class UserDeckCardListFragment extends BaseCardListFragment<DecksContract.Presenter>
        implements DecksContract.View {

    DecksContract.Presenter mDecksPresenter;
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

    DeckDetailCardViewHolder.DeckDetailButtonListener mButtonListener =
            new DeckDetailCardViewHolder.DeckDetailButtonListener() {
                @Override
                public void addCard(CardDetails card) {
                    mDecksPresenter.addCardToDeck(mDeck, card);
                }

                @Override
                public void removeCard(CardDetails card) {
                    mDecksPresenter.removeCardFromDeck(mDeck, card);
                }
            };

    public static UserDeckCardListFragment newInstance(String deckId) {
        UserDeckCardListFragment fragment = new UserDeckCardListFragment();
        fragment.mDeckId = deckId;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mDeckId = savedInstanceState.getString(DeckDetailActivity.EXTRA_DECK_ID);
        }
        createPresenter();
    }

    @Override
    public void onLoadData() {
        super.onLoadData();
        mDecksPresenter.getDeck(mDeckId, false)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver);
    }

    @Override
    public void onStop() {
        super.onStop();
        mDecksPresenter.stop();
    }

    @Override
    public DecksContract.Presenter createPresenter() {
        InteractorContainer interactorContainer = InteractorContainers.getFromApp(getActivity());
        mDecksPresenter = new DecksPresenter(
                this,
                interactorContainer.getDecksInteractor(),
                interactorContainer.getCardsInteractor(),
                interactorContainer.getPatchInteractor());

        return mDecksPresenter;
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        setLoading(true);
    }

    @Override
    public GwentRecyclerViewAdapter onBuildRecyclerView() {
        return new GwentRecyclerViewAdapter.Builder()
                .withControls(GwentRecyclerViewAdapter.Controls.DECK)
                .withDeckButtonListener(mButtonListener)
                .build();
    }

    protected void onDeckLoaded(Deck deck) {
        mDeck = deck;
        getRecyclerViewAdapter().setDeck(deck);

        for (Filterable faction : Faction.ALL_FACTIONS) {
            if (!faction.getId().equals(deck.getFactionId())) {
                ((CardFilterProvider) getActivity()).getCardFilter().put(faction.getId(), false);
            }
        }

        ((CardFilterProvider) getActivity()).getCardFilter().put(Faction.NEUTRAL.getId(), true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(DeckDetailActivity.EXTRA_DECK_ID, mDeckId);
        super.onSaveInstanceState(outState);
    }
}

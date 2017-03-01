package com.jamieadkins.gwent.deck.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jamieadkins.commonutils.mvp.PresenterFactory;
import com.jamieadkins.commonutils.ui.RecyclerViewItem;
import com.jamieadkins.commonutils.ui.SubHeader;
import com.jamieadkins.gwent.InteractorContainer;
import com.jamieadkins.gwent.InteractorContainers;
import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.base.BaseFragment;
import com.jamieadkins.gwent.data.CardDetails;
import com.jamieadkins.gwent.data.Deck;
import com.jamieadkins.gwent.data.Position;
import com.jamieadkins.gwent.data.Type;
import com.jamieadkins.gwent.data.interactor.RxDatabaseEvent;
import com.jamieadkins.gwent.deck.list.DecksContract;
import com.jamieadkins.gwent.deck.list.DecksPresenter;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * UI fragment that shows a list of the users decks.
 */

public abstract class BaseDeckDetailFragment extends BaseFragment
        implements DecksContract.View, PresenterFactory<DecksContract.Presenter> {
    protected DecksContract.Presenter mDecksPresenter;
    protected String mDeckId;
    protected Deck mDeck;

    private Map<String, SubHeader> mRowHeaders = new HashMap<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mDeckId = savedInstanceState.getString(DeckDetailActivity.EXTRA_DECK_ID);

        }
        mRowHeaders.put(getString(R.string.leader), new SubHeader(getString(R.string.leader)));
        mRowHeaders.put(getString(R.string.gold_units), new SubHeader(getString(R.string.gold_units)));
        mRowHeaders.put(getString(R.string.silver_units), new SubHeader(getString(R.string.silver_units)));
        mRowHeaders.put(getString(R.string.bronze_units), new SubHeader(getString(R.string.bronze_units)));
        mRowHeaders.put(getString(R.string.event_cards), new SubHeader(getString(R.string.event_cards)));

        mDecksPresenter = createPresenter();
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
    public void onDataEvent(RxDatabaseEvent<? extends RecyclerViewItem> data) {
        super.onDataEvent(data);
        onDeckLoaded((Deck) data.getValue());
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
                        case Type.BRONZE_ID:
                            int eventIndex = getRecyclerViewAdapter().getItems().indexOf(mRowHeaders.get(getString(R.string.event_cards)));
                            getRecyclerViewAdapter().addItem(eventIndex, card);
                            break;
                        case Type.SILVER_ID:
                            int bronzeIndex = getRecyclerViewAdapter().getItems().indexOf(mRowHeaders.get(getString(R.string.bronze_units)));
                            getRecyclerViewAdapter().addItem(bronzeIndex, card);
                            break;
                        case Type.GOLD_ID:
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
    public DecksContract.Presenter createPresenter() {
        InteractorContainer interactorContainer = InteractorContainers.getFromApp(getActivity());
        return new DecksPresenter(
                this,
                interactorContainer.getDecksInteractor(),
                interactorContainer.getCardsInteractor(),
                interactorContainer.getPatchInteractor());
    }

    @Override
    public void onStop() {
        super.onStop();
        mDecksPresenter.stop();
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

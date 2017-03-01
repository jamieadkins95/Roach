package com.jamieadkins.gwent.deck.detail.user;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.card.CardFilterProvider;
import com.jamieadkins.gwent.data.CardDetails;
import com.jamieadkins.gwent.data.Deck;
import com.jamieadkins.gwent.data.Faction;
import com.jamieadkins.gwent.data.Filterable;
import com.jamieadkins.gwent.deck.detail.BaseDeckDetailFragment;
import com.jamieadkins.gwent.deck.list.DecksContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * UI fragment that shows a list of the users decks.
 */

public class UserDeckDetailFragment extends BaseDeckDetailFragment
        implements DecksContract.View {
    DeckDetailRecyclerViewAdapter mDeckRecyclerViewAdapter;
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

    public UserDeckDetailFragment() {
    }

    public static UserDeckDetailFragment newInstance(String deckId) {
        UserDeckDetailFragment fragment = new UserDeckDetailFragment();
        fragment.mDeckId = deckId;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDeckRecyclerViewAdapter = new DeckDetailRecyclerViewAdapter(mButtonListener);
        setRecyclerViewAdapter(mDeckRecyclerViewAdapter);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_card_list;
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
    protected void onDeckLoaded(Deck deck) {
        super.onDeckLoaded(deck);
        mDeckRecyclerViewAdapter.setCardCounts(deck.getCardCount());

        for (Filterable faction : Faction.ALL_FACTIONS) {
            if (!faction.getId().equals(deck.getFactionId())) {
                ((CardFilterProvider) getActivity()).getCardFilter().put(faction.getId(), false);
            }
        }
    }
}
